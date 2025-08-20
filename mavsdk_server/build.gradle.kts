import java.util.Properties
import java.io.FileInputStream
import java.io.IOException
import java.net.URI

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jreleaser)
    `maven-publish`
}

// Load file "keystore.properties" where we keep our keys
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()

try {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
} catch (ignored: IOException) {
    if (project.hasProperty("centralUsername")) keystoreProperties["centralUsername"] = property("centralUsername")
    if (project.hasProperty("centralPassword")) keystoreProperties["centralPassword"] = property("centralPassword")
    if (project.hasProperty("gpgPass")) keystoreProperties["gpgPass"] = property("gpgPass")
}

allprojects {
    // We fetch the mavsdk_server binary that corresponds to the mavsdk-java
    // version. Say we set this package to be 3.6.0-2-SNAPSHOT, it means that it
    // corresponds to mavsdk_server 3.6.0.
    val mavsdk_server_release = if (!project.hasProperty("VERSION")) {
        "v3.10.0"
    } else {
        val versionString = project.property("VERSION").toString()
        val regex = Regex("v?(\\d+\\.\\d+\\.\\d+)")
        val version = regex.find(versionString)?.groupValues?.get(1)
        "v$version"
    }

    tasks {
        register<Copy>("extractMavsdkServer") {
            val tmpDir = File(layout.buildDirectory.get().asFile, "tmp")
            tmpDir.mkdirs()

            fun extractForArch(arch: String) {
                val archiveName = "mavsdk_server_android-${arch}_${mavsdk_server_release}.tar"
                val archiveFile = File(tmpDir, archiveName)
                val archiveUrl = "https://github.com/mavlink/MAVSDK/releases/download/${mavsdk_server_release}/mavsdk_server_android-$arch.tar"
                val destDir = "${project.projectDir.getAbsolutePath()}/src/main/prebuiltLibs"

                inputs.file(archiveFile)
                outputs.dir(destDir)

                if (!archiveFile.exists()) {
                    project.logger.warn("Downloading ${archiveFile.getName()} into ${archiveFile.getAbsolutePath()}...")
                    URI(archiveUrl).toURL().openStream().use { input ->
                        archiveFile.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                } else {
                    project.logger.warn("Archive already exists! Skipping download.")
                }

                project.logger.warn("Extracting $archiveFile into $destDir")

                from(tarTree(archiveFile)) {
                    duplicatesStrategy = DuplicatesStrategy.INCLUDE
                }
                eachFile {
                    if (path.endsWith(".so")) {
                        filePermissions {
                            group.execute = true
                            user.execute = true
                            other.execute = true
                        }
                    }
                }
                into(destDir)
                project.logger.warn("Should be extracted for arch $arch")
            }

            extractForArch("arm64")
            extractForArch("arm")
            extractForArch("x86")
            extractForArch("x86_64")
        }
    }

    tasks.named("preBuild") {
        dependsOn("extractMavsdkServer")
    }
}

android {
    namespace = "io.mavsdk.mavsdkserver"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        group = "io.mavsdk"

        // The version must be of the form "X.Y.Z-b[-SNAPSHOT]", where "X.Y.Z"
        // is the MAVSDK-C++ version, "b" is the build number of this
        // MAVSDK-Java package and "SNAPSHOT" optionally sets it as a SNAPSHOT.
        version =
            if (project.hasProperty("VERSION")) project.property("VERSION").toString()
            else "3.10.0-SNAPSHOT"

        ndk {
            abiFilters += listOf("arm64-v8a", "armeabi-v7a", "x86", "x86_64")
        }
    }

    externalNativeBuild {
        cmake {
            path = File("src/main/cpp/CMakeLists.txt")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    ndkVersion = "28.1.13356709"
}


if (keystoreProperties.containsKey("centralUsername") && keystoreProperties.containsKey("centralPassword")) {
    afterEvaluate {
        publishing {
            publications {
                create<MavenPublication>("release") {
                    from(components["release"])

                    pom {
                        name = "MAVSDK-Server"
                        packaging = "aar"
                        description = "MAVSDK server for Android."
                        url = "https://github.com/mavlink/MAVSDK-Java"

                        scm {
                            connection = "scm:git:https://github.com/mavlink/MAVSDK-Java"
                            developerConnection = "scm:git:https://github.com/mavlink/MAVSDK-Java"
                            url = "https://github.com/mavlink/MAVSDK-Java"
                        }

                        licenses {
                            license {
                                name = "BSD 3-Clause"
                                url = "https://opensource.org/licenses/BSD-3-Clause"
                            }
                        }

                        developers {
                            developer {
                                id = "jonasvautherin"
                                name = "Jonas Vautherin"
                                email = "dev@jonas.vautherin.ch"
                            }
                            developer {
                                id = "julianoes"
                                name = "Julian Oes"
                                email = "julian@oes.ch"
                            }
                        }
                    }
                }
            }

            repositories {
                maven {
                   url = uri(layout.buildDirectory.dir("target/staging-deploy"))
                }
            }
        }
    }

    jreleaser {
        signing {
            setActive("ALWAYS")
            armored.set(true)
            setMode("COMMAND")
            keystoreProperties["gpgPass"]?.let {
                passphrase.set(it as String)
            }

            command {
                keyName.set("CF3FF35732A465F680A89BC25B01A8023597C84B")
            }
        }
        deploy {
            release {
                github {
                    skipRelease = true
                    skipTag = true
                }
            }
            maven {
                mavenCentral {
                    create("sonatype") {
                        verifyPom = false
                        setActive("RELEASE")
                        username = keystoreProperties["centralUsername"] as String
                        password = keystoreProperties["centralPassword"] as String
                        url = "https://central.sonatype.com/api/v1/publisher"
                        stagingRepository("build/target/staging-deploy")
                    }
                }
                nexus2 {
                    create("snapshot-deploy") {
                        verifyPom = false
                        setActive("SNAPSHOT")
                        snapshotUrl.set("https://central.sonatype.com/repository/maven-snapshots")
                        url = "https://central.sonatype.com/repository/maven-snapshots"
                        applyMavenCentralRules = true
                        snapshotSupported = true
                        username = keystoreProperties["centralUsername"] as String
                        password = keystoreProperties["centralPassword"] as String
                        stagingRepository("build/target/staging-deploy")
                    }
                }
            }
        }
    }
}

