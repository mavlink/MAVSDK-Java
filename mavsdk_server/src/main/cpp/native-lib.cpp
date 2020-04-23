#include <android/log.h>
#include <backend_api.h>
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
        std::promise<int> port_promise;
        auto port_future = port_promise.get_future();

        auto mavsdk_server = runBackend(
                native_connection_url,
                mavsdk_server_port,
                onServerStarted,
                &port_promise);
        auto server_port = port_future.get();
        LOGD("mavsdk_server is now running, listening on port %d", server_port);

        return reinterpret_cast<jlong>(mavsdk_server);
    }

    JNIEXPORT jint JNICALL
    Java_io_mavsdk_mavsdkserver_MavsdkServer_getPort(JNIEnv* env, jobject thiz, jlong mavsdkServerHandle)
    {
        auto mavsdk_server = reinterpret_cast<MavsdkBackend*>(mavsdkServerHandle);
        return getPort(mavsdk_server);
    }

    JNIEXPORT void JNICALL
    Java_io_mavsdk_mavsdkserver_MavsdkServer_attach(JNIEnv* env, jobject thiz, jlong mavsdkServerHandle)
    {
        auto mavsdk_server = reinterpret_cast<MavsdkBackend*>(mavsdkServerHandle);
        attach(mavsdk_server);
    }

    JNIEXPORT void JNICALL
    Java_io_mavsdk_mavsdkserver_MavsdkServer_stop(JNIEnv* env, jobject thiz, jlong mavsdkServerHandle)
    {
        auto mavsdk_server = reinterpret_cast<MavsdkBackend*>(mavsdkServerHandle);
        stopBackend(mavsdk_server);
    }
};
