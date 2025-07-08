#include <android/log.h>
#include <mavsdk_server_api.h>
#include <future>
#include <jni.h>
#include <thread>

#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,"MAVSDK-Server",__VA_ARGS__)

extern "C" char* mavsdk_temp_path;

extern "C"
{
    JNIEXPORT jlong JNICALL
    Java_io_mavsdk_mavsdkserver_MavsdkServer_initNative(JNIEnv *env, jobject thiz) {
        MavsdkServer* mavsdk_server;
        mavsdk_server_init(&mavsdk_server);
        return reinterpret_cast<jlong>(mavsdk_server);
    }

    JNIEXPORT jboolean JNICALL
    Java_io_mavsdk_mavsdkserver_MavsdkServer_runNative(JNIEnv* env, jobject thiz, jlong mavsdkServerHandle, jstring system_address, jint mavsdk_server_port)
    {
        const char* native_connection_url = env->GetStringUTFChars(system_address, nullptr);
        auto mavsdk_server = reinterpret_cast<MavsdkServer*>(mavsdkServerHandle);

        LOGD("Running mavsdk_server with connection url: %s", native_connection_url);
        auto result = mavsdk_server_run(mavsdk_server, native_connection_url, mavsdk_server_port);
        if (result != 0) {
            LOGD("Failed to start mavsdk_server: %d", result);
            return false;
        }

        auto server_port = mavsdk_server_get_port(mavsdk_server);
        LOGD("mavsdk_server is now running, listening on port %d", server_port);
        return true;
    }

    JNIEXPORT jboolean JNICALL
    Java_io_mavsdk_mavsdkserver_MavsdkServer_runNativeWithMavIds(JNIEnv* env, jobject thiz, jlong mavsdkServerHandle, jstring system_address, jint mavsdk_server_port, jint system_id, jint component_id)
    {
        const char* native_connection_url = env->GetStringUTFChars(system_address, nullptr);
        auto mavsdk_server = reinterpret_cast<MavsdkServer*>(mavsdkServerHandle);

        LOGD("Running mavsdk_server with connection url: %s", native_connection_url);
        auto result = mavsdk_server_run_with_mavlink_ids(mavsdk_server, native_connection_url, mavsdk_server_port, system_id, component_id);
        if (result != 0) {
            LOGD("Failed to start mavsdk_server: %d", result);
            return false;
        }
        auto server_port = mavsdk_server_get_port(mavsdk_server);
        LOGD("mavsdk_server is now running, listening on port %d", server_port);
        return true;
    }

    JNIEXPORT jint JNICALL
    Java_io_mavsdk_mavsdkserver_MavsdkServer_getPort(JNIEnv* env, jobject thiz, jlong mavsdkServerHandle)
    {
        auto mavsdk_server = reinterpret_cast<MavsdkServer*>(mavsdkServerHandle);
        return mavsdk_server_get_port(mavsdk_server);
    }

    JNIEXPORT void JNICALL
    Java_io_mavsdk_mavsdkserver_MavsdkServer_attach(JNIEnv* env, jobject thiz, jlong mavsdkServerHandle)
    {
        auto mavsdk_server = reinterpret_cast<MavsdkServer*>(mavsdkServerHandle);
        mavsdk_server_attach(mavsdk_server);
    }

    JNIEXPORT void JNICALL
    Java_io_mavsdk_mavsdkserver_MavsdkServer_stop(JNIEnv* env, jobject thiz, jlong mavsdkServerHandle)
    {
        auto mavsdk_server = reinterpret_cast<MavsdkServer*>(mavsdkServerHandle);
        mavsdk_server_stop(mavsdk_server);
    }

    JNIEXPORT void JNICALL
    Java_io_mavsdk_mavsdkserver_MavsdkServer_destroy(JNIEnv* env, jobject thiz, jlong mavsdkServerHandle)
    {
        auto mavsdk_server = reinterpret_cast<MavsdkServer*>(mavsdkServerHandle);
        mavsdk_server_destroy(mavsdk_server);
    }

    JNIEXPORT void JNICALL
    Java_io_mavsdk_mavsdkserver_MavsdkServer_setTempDirectory(JNIEnv *env, jobject thiz, jstring temp_dir)
    {

        const char* temp_c_str = env->GetStringUTFChars(temp_dir, 0);

        static char our_copy[256];
        strncpy(our_copy, temp_c_str, sizeof(our_copy));
        mavsdk_temp_path = our_copy;

        env->ReleaseStringUTFChars(temp_dir, temp_c_str);
    }
};
