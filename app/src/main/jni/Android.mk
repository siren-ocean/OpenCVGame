LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

OPENCV_CAMERA_MODULES:=on
OPENCV_INSTALL_MODULES:=on
OPENCV_LIB_TYPE:=SHARED

include $(LOCAL_PATH)/native/jni/OpenCV.mk

LOCAL_SRC_FILES  := siren_ocean_OpenCVHelper.cpp
LOCAL_C_INCLUDES += $(LOCAL_PATH)
LOCAL_LDLIBS     += -llog -ldl

LOCAL_MODULE     := OpenCVHelper

include $(BUILD_SHARED_LIBRARY)
