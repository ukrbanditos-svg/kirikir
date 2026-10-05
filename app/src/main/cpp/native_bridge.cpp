#include <jni.h>
#include <filesystem>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_kirikir_player_runtime_NativeKirikiriBridge_nativeVersion(
        JNIEnv* env, jobject) {
    return env->NewStringUTF("native-bootstrap-0.1");
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_kirikir_player_runtime_NativeKirikiriBridge_nativeProbeGame(
        JNIEnv* env, jobject, jstring path) {
    const char* raw = env->GetStringUTFChars(path, nullptr);
    std::filesystem::path root(raw);
    env->ReleaseStringUTFChars(path, raw);

    if (!std::filesystem::exists(root)) return -1;
    for (const auto& entry : std::filesystem::directory_iterator(root)) {
        if (entry.is_regular_file() && entry.path().extension() == ".xp3") return 1;
    }
    return 0;
}
