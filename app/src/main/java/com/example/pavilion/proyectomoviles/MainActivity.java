package com.example.pavilion.proyectomoviles;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.hardware.camera2.CameraCharacteristics;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.pavilion.proyectomoviles.OpcvCamaraV;

public class MainActivity extends Activity implements CvCameraViewListener2, OnTouchListener {
    private static final String TAG = "OCVSample::Activity";
    private boolean mIsColorSelected = false;
    boolean hammer, justOneTouch;
    private int cantidadDeCoincidencias;
    private Scalar mBlobColorRgba;
    private Scalar mBlobColorHsv;
    private Scalar colorSelectedByUser;
    private IntensityDetector mDetector;
    private Mat mRgba;
    private List<MatOfPoint> contours;
    private Mat mSpectrum;
    private org.opencv.core.Size SPECTRUM_SIZE;
    private Scalar CONTOUR_COLOR;
    private OpcvCamaraV mOpenCvCameraView;
    private List<Size> mResolutionList;
    private MenuItem[] mEffectMenuItems;
    private SubMenu mColorEffectsMenu;
    private MenuItem[] mResolutionMenuItems;
    private List<Scalar> scalarList;
    private SubMenu mResolutionMenu;
    FloatingActionButton btnCamara, btnReanudarFoto;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                    mOpenCvCameraView.setOnTouchListener(MainActivity.this);
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    public MainActivity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);

        mOpenCvCameraView = (OpcvCamaraV) findViewById(R.id.camera_view);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

        hammer=false;
        cantidadDeCoincidencias = 0;
        btnCamara = (FloatingActionButton) findViewById(R.id.btnTomaFoto);
        btnReanudarFoto = (FloatingActionButton) findViewById(R.id.btnReanudarFoto);
        mOpenCvCameraView.setCvCameraViewListener(this);
        btnReanudarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hammer=false;
            }
        });
        btnCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hammer=true;
                mIsColorSelected = false;
            }
        });
        scalarList = new ArrayList<Scalar>();
        mBlobColorHsv = new Scalar(60,255,120,0);
        scalarList.add(mBlobColorHsv);
        mBlobColorHsv = new Scalar(156,252,92,0);
        //setTargetColorScalar();
        scalarList.add(mBlobColorHsv);
        Toast.makeText(getBaseContext(),scalarList.toString(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mDetector = new IntensityDetector();
        mSpectrum = new Mat();
        mBlobColorRgba = new Scalar(255);
        mBlobColorHsv = new Scalar(255);
        SPECTRUM_SIZE = new org.opencv.core.Size(200, 64);
        CONTOUR_COLOR = new Scalar(255,0,0,255);
    }
    @Override
    public void onCameraViewStopped() {
    }
    @Override
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {

        if (hammer){
            if (!mIsColorSelected) {
                if (colorSelectedByUser()) {
                    mIsColorSelected = true;
                    Toast.makeText(getBaseContext(),"Hay coincidencias!",Toast.LENGTH_SHORT).show();
                }
            }

            return mRgba;
        }
        mRgba=inputFrame.rgba();
        return mRgba ;
    }
    private void setTargetColorScalar() {
        //mBlobColorHsv = new Scalar(0,0,0,0);
        mDetector.setHsvColor(mBlobColorHsv);

    }

    private boolean colorSelectedByUser() {
        //mBlobColorHsv = new Scalar(60,255,120,0);
        int posicion = 0;
        while (posicion < scalarList.size()) {
            mDetector.setHsvColor(scalarList.get(posicion));
            Log.e(TAG, "*****************************************************//*////////////////////////////////////********************************************************************" );

            mDetector.process(mRgba);
            contours = mDetector.getContours();
            Log.e(TAG, "Contours count: " + contours.size());

            if (contours.size() != 0) {
                setTargetColorScalar();
                Log.e(TAG, "PERRAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-----------------------AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + contours.size());
                Imgproc.drawContours(mRgba, contours, -1, CONTOUR_COLOR);

                Mat spectrumLabel = mRgba.submat(4, 4 + mSpectrum.rows(), 70, 70 + mSpectrum.cols());
                mSpectrum.copyTo(spectrumLabel);
                return true;
            }

            posicion ++;
        }
        return false;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int cols = mRgba.cols();
        int rows = mRgba.rows();

        int xOffset = (mOpenCvCameraView.getWidth() - cols) / 2;
        int yOffset = (mOpenCvCameraView.getHeight() - rows) / 2;

        int x = (int)event.getX() - xOffset;
        int y = (int)event.getY() - yOffset;

        Log.i(TAG, "Touch image coordinates: (" + x + ", " + y + ")");

        if ((x < 0) || (y < 0) || (x > cols) || (y > rows)) return false;

        Rect touchedRect = new Rect();

        touchedRect.x = (x>4) ? x-4 : 0;
        touchedRect.y = (y>4) ? y-4 : 0;

        touchedRect.width = (x+4 < cols) ? x + 4 - touchedRect.x : cols - touchedRect.x;
        touchedRect.height = (y+4 < rows) ? y + 4 - touchedRect.y : rows - touchedRect.y;

        Mat touchedRegionRgba = mRgba.submat(touchedRect);

        Mat touchedRegionHsv = new Mat();
        Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2HSV_FULL);

        // Calculate average color of touched region
        mBlobColorHsv = Core.sumElems(touchedRegionHsv);
        int pointCount = touchedRect.width*touchedRect.height;
        for (int i = 0; i < mBlobColorHsv.val.length; i++)
            mBlobColorHsv.val[i] /= pointCount;


        //mBlobColorHsv = new Scalar(0,0,0,0);

        //mBlobColorHsv = new Scalar(60,255,120,0);

        mBlobColorRgba = convertFromScalarToHsv(mBlobColorHsv);


        Log.i(TAG, "Touched rgba color: (" + mBlobColorRgba.val[0] + ", " + mBlobColorRgba.val[1] +
                ", " + mBlobColorRgba.val[2] + ", " + mBlobColorRgba.val[3] + ")");

        mDetector.setHsvColor(mBlobColorHsv);

        //Imgproc.resize(mDetector.getSpectrum(), mSpectrum, SPECTRUM_SIZE);

        mIsColorSelected = true;
        touchedRegionRgba.release();
        touchedRegionHsv.release();

        return false; // don't need subsequent touch events
    }

    private Scalar convertFromScalarToHsv(Scalar hsvColor) {
        Mat pointMatRgba = new Mat();
        Mat pointMatHsv = new Mat(1, 1, CvType.CV_8UC3, hsvColor);
        Imgproc.cvtColor(pointMatHsv, pointMatRgba, Imgproc.COLOR_HSV2RGB_FULL, 4);

        return new Scalar(pointMatRgba.get(0, 0));
    }
}
