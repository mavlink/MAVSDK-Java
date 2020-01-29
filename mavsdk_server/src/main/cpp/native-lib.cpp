#include <android/log.h>
#include <jni.h>
#include <backend_api.h>

#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,"MAVSDK-Server",__VA_ARGS__)

extern "C"
{
    JNIEXPORT void JNICALL
    Java_io_mavsdk_mavsdkserver_MavsdkServer_run(JNIEnv* env, jobject thiz, jstring system_address, jint mavsdk_server_port)
    {
        const char* native_connection_url = env->GetStringUTFChars(system_address, 0);

        LOGD("Running mavsdk_server with connection url: %s", native_connection_url);
        runBackend(native_connection_url, mavsdk_server_port, nullptr, nullptr);
        //runBackend("serial:///dev/bus/usb/001/002", nullptr, nullptr);
    }
};
