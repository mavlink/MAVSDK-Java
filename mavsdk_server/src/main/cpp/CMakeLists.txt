cmake_minimum_required(VERSION 3.4.1)

add_library(mavsdk_server SHARED IMPORTED)
set_target_properties(mavsdk_server
                      PROPERTIES IMPORTED_LOCATION
                      "${CMAKE_CURRENT_SOURCE_DIR}/../jniLibs/${ANDROID_ABI}/libmavsdk_server.so")

include_directories("${CMAKE_CURRENT_SOURCE_DIR}/../jniLibs/include")

add_library(native_lib SHARED native-lib.cpp)
target_link_libraries(native_lib mavsdk_server log)
