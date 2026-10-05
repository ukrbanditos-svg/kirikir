cmake_minimum_required(VERSION 3.22.1)
project(kirikir_runtime)

add_library(kirikir_runtime SHARED native_bridge.cpp)

find_library(log-lib log)
target_link_libraries(kirikir_runtime ${log-lib})

target_compile_features(kirikir_runtime PRIVATE cxx_std_17)
