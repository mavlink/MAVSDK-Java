#include <android/log.h>
#include <mavsdk_server_api.h>
#include <future>
#include <jni.h>
#include <thread>

#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,"MAVSDK-Server",__VA_ARGS__)

extern "C"
{
    void onServerStarted(void* port_promise) {
        auto* promise = static_cast<std::promise<void>*>(port_promise);
        promise->set_value();
    }

    JNIEXPORT jlong JNICALL
    Java_io_mavsdk_mavsdkserver_MavsdkServer_runNative(JNIEnv* env, jobject thiz, jstring system_address, jint mavsdk_server_port)
    {
        const char* native_connection_url = env->GetStringUTFChars(system_address, 0);

        LOGD("Running mavsdk_server with connection url: %s", native_connection_url);
        auto mavsdk_server = mavsdk_server_run(native_connection_url, mavsdk_server_port);
        auto server_port = mavsdk_server_get_port(mavsdk_server);
        LOGD("mavsdk_server is now running, listening on port %d", server_port);

        return reinterpret_cast<jlong>(mavsdk_server);
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
};
