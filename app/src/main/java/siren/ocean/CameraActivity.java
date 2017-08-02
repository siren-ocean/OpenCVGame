package siren.ocean;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class CameraActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2, View.OnClickListener {

    private static final Scalar FACE_RECT_COLOR = new Scalar(0, 255, 0, 255);
    private CameraBridgeViewBase mCameraView;
    private OpenCVHelper mOpenCVHelper;
    private Mat mRgba, mGray, mTmp, mSepiaKernel;
    private float mRelativeFaceSize = 0.2f;
    private int mAbsoluteFaceSize = 0;

    private final int DETECTION = 0;
    private final int ASHING = 1;
    private final int CANNY = 2;
    private final int LUV = 3;
    private int mCameraStatus = DETECTION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        mCameraView = (CameraBridgeViewBase) findViewById(R.id.camera_view);
        mCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {

        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                    initDependencies();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 從配置文件加载lbpcascade_frontalface.xml 創建人臉識別器
     */
    private void initDependencies() {
        try {
            InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
            File cascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
            FileOutputStream os = new FileOutputStream(cascadeFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();

            mOpenCVHelper = new OpenCVHelper(cascadeFile.getAbsolutePath(), 0);
        } catch (Exception e) {
            Log.e("initDependencies", "Error loading cascade", e);
        }

        // 启动 CameraView
        mCameraView.enableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mGray = new Mat();
        mRgba = new Mat();
        mTmp = new Mat();

        mSepiaKernel = new Mat(4, 4, CvType.CV_32F);
        mSepiaKernel.put(0, 0, /* R */0.189f, 0.769f, 0.393f, 0f);
        mSepiaKernel.put(1, 0, /* G */0.168f, 0.686f, 0.349f, 0f);
        mSepiaKernel.put(2, 0, /* B */0.131f, 0.534f, 0.272f, 0f);
        mSepiaKernel.put(3, 0, /* A */0.000f, 0.000f, 0.000f, 1f);
    }

    @Override
    public void onCameraViewStopped() {
        mGray.release();
        mRgba.release();
        mTmp.release();
        mSepiaKernel.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();

        setCameraStatus();
        return mRgba;
    }

    private void setCameraStatus() {
        switch (mCameraStatus) {
            case DETECTION:
                //人脸检测
                if (mAbsoluteFaceSize == 0) {
                    int height = mGray.rows();
                    if (Math.round(height * mRelativeFaceSize) > 0) {
                        mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
                    }
                    mOpenCVHelper.setMinFaceSize(mAbsoluteFaceSize);
                }

                MatOfRect faces = new MatOfRect();
                mOpenCVHelper.detect(mGray, faces);

                Rect[] facesArray = faces.toArray();
                for (int i = 0; i < facesArray.length; i++)
                    Imgproc.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(), FACE_RECT_COLOR, 3);
                break;
            case ASHING:
                //灰化处理
                Imgproc.cvtColor(mGray, mRgba, Imgproc.COLOR_GRAY2RGBA, 4);
                break;
            case CANNY:
                //Canny边缘检测
                Imgproc.Canny(mGray, mTmp, 80, 100);
                Imgproc.cvtColor(mTmp, mRgba, Imgproc.COLOR_GRAY2RGBA, 4);
                break;
            case LUV:
                Size sizeRgba = mRgba.size();
                int rows = (int) sizeRgba.height;
                int cols = (int) sizeRgba.width;

                int left = cols / 8;
                int top = rows / 8;

                int width = cols * 3 / 4;
                int height = rows * 3 / 4;

                Mat rgbaInnerWindow = mRgba.submat(top, top + height, left, left + width);
                Core.transform(rgbaInnerWindow, rgbaInnerWindow, mSepiaKernel);
                rgbaInnerWindow.release();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_detection:
                mCameraStatus = DETECTION;
                break;
            case R.id.rb_ashing:
                mCameraStatus = ASHING;
                break;
            case R.id.rb_canny:
                mCameraStatus = CANNY;
                break;
            case R.id.rb_luv:
                mCameraStatus = LUV;
                break;
        }
    }
}
