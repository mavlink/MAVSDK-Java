#include <android/log.h>
#include <mavsdk_server_api.h>
#include <future>
#include <jni.h>
#include <thread>

#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,"MAVSDK-Server",__VA_ARGS__)

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
        const char* native_connection_url = env->GetStringUTFChars(system_address, 0);
        auto mavsdk_server = reinterpret_cast<MavsdkServer*>(mavsdkServerHandle);

        LOGD("Running mavsdk_server with connection url: %s", native_connection_url);
        if (!mavsdk_server_run(mavsdk_server, native_connection_url, mavsdk_server_port)) {
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
};
