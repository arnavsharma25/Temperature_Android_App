#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jfloatArray JNICALL
Java_com_example_temperatureapplication_MainActivity_fahrenheit(JNIEnv *env, jobject obj, jfloatArray celsius ) {
    jsize len = env->GetArrayLength(celsius);
    int i, sum = 0;
    jfloatArray fahtemp=env->NewFloatArray(len);
    jfloat *arr = env->GetFloatArrayElements(celsius, NULL);
    jfloat *newarr=env->GetFloatArrayElements(fahtemp,NULL);

    for (i=0; i<len; i++) {
        newarr[i] = arr[i] * 1.8 + 32;
    }

    env->SetFloatArrayRegion(fahtemp,0,len,newarr);
    return fahtemp;

}
