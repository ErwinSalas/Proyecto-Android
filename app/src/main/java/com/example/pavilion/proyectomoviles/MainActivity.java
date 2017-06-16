package com.example.pavilion.proyectomoviles;

import java.util.ArrayList;
import java.util.List;

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
import android.hardware.Camera.Size;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.pavilion.proyectomoviles.Services.DeficientController;
import com.example.pavilion.proyectomoviles.Services.Deficient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements CvCameraViewListener2, OnTouchListener {
    private static final String TAG = "OCVSample::Activity";
    private boolean mIsColorSelected = false;
    boolean hammer, justOneTouch;
    private DeficientController deficientController = new DeficientController();
    private int cantidadDeCoincidencias;
    private Scalar mBlobColorRgba;
    private Scalar mBlobColorHsv;
    private String namesD;
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
    private List<String> stringNamesList;
    private SubMenu mResolutionMenu;
    private List<Deficient> deficientList;
    private Deficient deficient;
    private FloatingActionButton btnCamara, btnReanudarFoto, btnAgregarColor;

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
        deficientList = new ArrayList<>();
        mOpenCvCameraView = (OpcvCamaraV) findViewById(R.id.camera_view);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

        hammer=false;
        cantidadDeCoincidencias = 0;
        btnCamara = (FloatingActionButton) findViewById(R.id.btnTomaFoto);
        btnReanudarFoto = (FloatingActionButton) findViewById(R.id.btnReanudarFoto);
        btnAgregarColor = (FloatingActionButton) findViewById(R.id.btnAgregarColor);
        mOpenCvCameraView.setCvCameraViewListener(this);
        namesD = "";
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
        btnAgregarColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hammer=true;
                mIsColorSelected = true;

                Bundle bundle = new Bundle();
                bundle.putString("titulo","Indicacion");
                bundle.putString("descripcion", "Haga tap en el color deseado");

                MessageFragment messageFragment = new MessageFragment();
                messageFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.main, messageFragment);
                fragmentTransaction.commit();
                closefragmentM(messageFragment);

            }
        });

        scalarList = new ArrayList<Scalar>();
        stringNamesList = new ArrayList<String>();
        mBlobColorHsv = new Scalar(60,255,120,0);
        scalarList.add(mBlobColorHsv);
        mBlobColorHsv = new Scalar(156,252,92,0);
        //setTargetColorScalar();
        scalarList.add(mBlobColorHsv);
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
                    Bundle bundle = new Bundle();
                    Log.e(TAG, deficient.getName() + "*************????????????????????????****" );
                    bundle.putString("titulo", deficient.getName());
                    bundle.putString("descripcion", deficient.getDescription());

                    MessageFragment messageFragment = new MessageFragment();
                    messageFragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.add(R.id.main, messageFragment);
                    fragmentTransaction.commit();
                    closefragmentM(messageFragment);
                }
            }
            return mRgba;
        }
        mRgba=inputFrame.rgba();
        return mRgba ;
    }

    private void connectToGetData() {
        Log.e(TAG, "************CONECTING TO DATA***********" );
        Call<List<Deficient>> call = deficientController.getService().getDeficients();
        Log.e(TAG, "************ALMOST TO DATA***********" );
        call.enqueue(new Callback<List<Deficient>>() {
            @Override
            public void onResponse(Call<List<Deficient>> call, Response<List<Deficient>> response) {
                List<Deficient> deficientList = response.body();
                llenarListaColores(deficientList);
                Log.e(TAG, "*************//////+++++99999999999999999++++++/////7**************" );
                Log.e(TAG,response.body().toString());
            }
            @Override
            public void onFailure(Call<List<Deficient>> call, Throwable t) {
                Log.e(TAG, "*************//////+++++1010010101010101010+++++++/////7**************" );
            }
        });
        Log.e(TAG, String.valueOf(deficientList.size()) );
        Log.e(TAG, "************FINISH***********" );
    }

    private void llenarListaColores(List<Deficient> deficientLis) {
        deficientList = deficientLis;
        int contador = 0;
        while (contador < deficientList.size()) {
            Log.e(TAG, deficientList.get(contador).getName());
            Log.e(TAG, deficientList.get(contador).getDescription());
            Log.e(TAG, "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" );
            double r = deficientList.get(contador).getR();
            double g = deficientList.get(contador).getG();
            double b = deficientList.get(contador).getB();
            double a = deficientList.get(contador).getA();
            mBlobColorHsv = new Scalar(r,g,b,a);
            scalarList.add(mBlobColorHsv);

            contador ++;
        }
    }

    private boolean colorSelectedByUser() {
        //mBlobColorHsv = new Scalar(60,255,120,0);
        connectToGetData();
        int posicion = 0;
        while (posicion < scalarList.size()) {
            mDetector.setHsvColor(scalarList.get(posicion));
            Log.e(TAG, "*****************************************************//*////////////////////////////////////********************************************************************" );

            mDetector.process(mRgba);
            contours = mDetector.getContours();
            Log.e(TAG, "Contours count: " + contours.size());

            if (contours.size() != 0) {
                Log.e(TAG, "PERRAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA----------00-------------AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + contours.size());
                Imgproc.drawContours(mRgba, contours, -1, CONTOUR_COLOR);
                Log.e(TAG, "PERRAAAAAAAAAAAAAAAA----------11-------------AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                deficient = deficientList.get(posicion);
                Log.e(TAG, "PERRAAAAAAAAAAAAAAAA----------22-------------AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                Mat spectrumLabel = mRgba.submat(4, 4 + mSpectrum.rows(), 70, 70 + mSpectrum.cols());
                Log.e(TAG, "PERRAAAAAAAAAAAAAAAA----------33-------------AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                mSpectrum.copyTo(spectrumLabel);
                Log.e(TAG, "PERRAAAAAAAAAAAAAAAA----------44-------------AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
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

        mBlobColorRgba = convertFromScalarToHsv(mBlobColorHsv);


        Log.i(TAG, "Touched rgba color: (" + mBlobColorRgba.val[0] + ", " + mBlobColorRgba.val[1] +
                ", " + mBlobColorRgba.val[2] + ", " + mBlobColorRgba.val[3] + ")");

        Log.e(TAG, "*****************************************************//*////////////////////////////////////********************************************************************" );

        mDetector.setHsvColor(mBlobColorHsv);
        mDetector.process(mRgba);
        contours = mDetector.getContours();
        Log.e(TAG, "Contours count: " + contours.size());

        Log.e(TAG, "PERRAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA-----------------------AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + contours.size());
        Imgproc.drawContours(mRgba, contours, -1, CONTOUR_COLOR);

        Mat spectrumLabel = mRgba.submat(4, 4 + mSpectrum.rows(), 70, 70 + mSpectrum.cols());
        mSpectrum.copyTo(spectrumLabel);



        touchedRegionRgba.release();
        touchedRegionHsv.release();


        Bundle bundle = new Bundle();



        bundle.putString("color",mBlobColorHsv.toString());

        AddDiseaseFragment addDiseaseFragment = new AddDiseaseFragment();

        addDiseaseFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main, addDiseaseFragment);
        fragmentTransaction.commit();

        closefragmentA(addDiseaseFragment);

        return false; // don't need subsequent touch events
    }

    private void closefragmentA(AddDiseaseFragment addDiseaseFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.remove(addDiseaseFragment);
        fragmentTransaction.commit();

    }

    private void closefragmentM(MessageFragment addDiseaseFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.remove(addDiseaseFragment);
        fragmentTransaction.commit();

    }

    private Scalar convertFromScalarToHsv(Scalar hsvColor) {
        Mat pointMatRgba = new Mat();
        Mat pointMatHsv = new Mat(1, 1, CvType.CV_8UC3, hsvColor);
        Imgproc.cvtColor(pointMatHsv, pointMatRgba, Imgproc.COLOR_HSV2RGB_FULL, 4);

        return new Scalar(pointMatRgba.get(0, 0));
    }
}
