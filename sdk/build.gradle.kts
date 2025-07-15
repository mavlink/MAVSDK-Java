import java.util.Properties
import java.io.FileInputStream
import java.io.IOException

plugins {
    alias(libs.plugins.jreleaser)
    alias(libs.plugins.protobuf)
    `java-library`
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

group = "io.mavsdk"

// The version must be of the form "X.Y.Z-b[-SNAPSHOT]", where "X.Y.Z"
// is the MAVSDK-C++ version, "b" is the build number of this
// MAVSDK-Java package and "SNAPSHOT" optionally sets it as a SNAPSHOT.
// For instance, if the version is 3.6.0-2, it should be built with the same
// version of the proto files as MAVSDK-C++ v3.6.0.
version =
    if (project.hasProperty("VERSION")) project.property("VERSION").toString()
    else "3.7.1-SNAPSHOT"

val grpcVersion = "1.61.1"

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.2"
    }

    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${grpcVersion}"
        }
    }

    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                getByName("java") {
                    option("lite")
                }
            }
            task.plugins {
                create("grpc") {
                    option("lite")
                }

                create("mavsdk") {
                    option("file_ext=java")
                    option("template_path=templates/")
                }
            }
        }
    }
}

tasks.withType<Jar>().configureEach {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }

    withJavadocJar()
    withSourcesJar()
}

if (keystoreProperties.containsKey("centralUsername") && keystoreProperties.containsKey("centralPassword")) {
    afterEvaluate {
        publishing {
            publications {
                create<MavenPublication>("java") {
                    from(components["java"])

                    pom {
                        name = "MAVSDK-Java"
                        packaging = "jar"
                        description = "MAVSDK client for Java."
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

dependencies {
    protobuf(files("proto/protos/"))

    compileOnly("io.grpc:grpc-protobuf:${grpcVersion}")

    implementation("io.grpc:grpc-okhttp:${grpcVersion}")
    implementation("io.grpc:grpc-protobuf-lite:${grpcVersion}")
    implementation("io.grpc:grpc-stub:${grpcVersion}")
    implementation("org.slf4j:slf4j-api:2.0.12")
    api("io.reactivex.rxjava2:rxjava:2.2.21")

    compileOnly("javax.annotation:javax.annotation-api:1.3.2")

    testImplementation("io.grpc:grpc-testing:${grpcVersion}")
    testImplementation("junit:junit:4.12")
    testImplementation("org.mockito:mockito-core:2.13.0")
}
