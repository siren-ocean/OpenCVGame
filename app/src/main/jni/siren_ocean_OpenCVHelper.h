/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class siren_ocean_OpenCVHelper */

#ifndef _Included_siren_ocean_OpenCVHelper
#define _Included_siren_ocean_OpenCVHelper
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     siren_ocean_OpenCVHelper
 * Method:    nativeCreateObject
 * Signature: (Ljava/lang/String;I)J
 */
JNIEXPORT jlong JNICALL Java_siren_ocean_OpenCVHelper_nativeCreateObject
  (JNIEnv *, jclass, jstring, jint);

/*
 * Class:     siren_ocean_OpenCVHelper
 * Method:    nativeDestroyObject
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_siren_ocean_OpenCVHelper_nativeDestroyObject
  (JNIEnv *, jclass, jlong);

/*
 * Class:     siren_ocean_OpenCVHelper
 * Method:    nativeStart
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_siren_ocean_OpenCVHelper_nativeStart
  (JNIEnv *, jclass, jlong);

/*
 * Class:     siren_ocean_OpenCVHelper
 * Method:    nativeStop
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_siren_ocean_OpenCVHelper_nativeStop
  (JNIEnv *, jclass, jlong);

/*
 * Class:     siren_ocean_OpenCVHelper
 * Method:    nativeSetFaceSize
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_siren_ocean_OpenCVHelper_nativeSetFaceSize
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     siren_ocean_OpenCVHelper
 * Method:    nativeDetect
 * Signature: (JJJ)V
 */
JNIEXPORT void JNICALL Java_siren_ocean_OpenCVHelper_nativeDetect
  (JNIEnv *, jclass, jlong, jlong, jlong);

#ifdef __cplusplus
}
#endif
#endif
