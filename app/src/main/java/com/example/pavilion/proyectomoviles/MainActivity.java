package com.example.pavilion.proyectomoviles;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.SurfaceView;


import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;


public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    private static String TAG="MainActivity";
    JavaCameraView mOpenCvCameraView;
    Mat mRgba,imgGray,imgCanny;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    mOpenCvCameraView.enableView();
                    break;
                }
                default:
                {
                    super.onManagerConnected(status);
                    break;
                }
            }
        }
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOpenCvCameraView = (JavaCameraView) findViewById(R.id.camera_view);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        disableCamera();

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (OpenCVLoader.initDebug()) {
            Log.d(TAG, "OpenCV Loaded Successfully");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);

        }
        else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, mLoaderCallback);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        disableCamera();
    }

    public void disableCamera() {
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height,width, CvType.CV_8UC4);
        imgCanny = new Mat(height,width, CvType.CV_8UC1);
        imgGray = new Mat(height,width, CvType.CV_8UC1);
    }

    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba=inputFrame.rgba();
        Imgproc.cvtColor(mRgba,imgGray,Imgproc.COLOR_RGB2GRAY);
        Imgproc.Canny(imgGray,imgCanny,50,150);
        return imgCanny;
    }


}
