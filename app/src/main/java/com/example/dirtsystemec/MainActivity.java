package com.example.dirtsystemec;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.badlogic.androidgames.framework.impl.MultiTouchHandler;

public class MainActivity extends AppCompatActivity {


    private static final float coordinateXMin = -13.5f, coordinateXMax = 13.5f, coordinateYMin = -24, coordinateYMax = 24;
    public static String TAG;
    private AndroidFastRenderView renderView;
    private int currentApiVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.loadLibrary("liquidfun");
        System.loadLibrary("liquidfun_jni");
        TAG = getString(R.string.app_name);
        TAG = getString(R.string.app_name);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        currentApiVersion = Build.VERSION.SDK_INT;
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        decorView.setSystemUiVisibility(flags);
                    }
                }
            });
        }


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Box physicalSize = new Box(coordinateXMin, coordinateYMin, coordinateXMax, coordinateYMax);
        Box screenSize   = new Box(0, 0, metrics.widthPixels, metrics.heightPixels);
        GameWorld gw = new GameWorld(physicalSize, screenSize, this);
        gw.setGravity(-5,0);

        GameObject.createEnclosure(coordinateXMax,coordinateXMin,coordinateYMax,coordinateYMin,gw);
        GameObject.createScoreBar(6.5f,22.7f,gw);


        GameObject.createBridge(0,0,-8.5f,4.5f,-2f,
                -1f,-2f,1f,2f,1f,BridgePosition.RIGHT,gw);
        GameObject.createBridge(0,7,-8.5f,-4.5f,2f,
                -1f,2f,1f,-2f,1f,BridgePosition.LEFT,gw);

        GameObject.createSea(-13.4f,0f,-12.5f,0f,gw);
        GameObject.createGround(-11.5f,7.0f,gw);
        GameObject.createGround(-11.5f,-7.0f,gw);
        GameObject.createGround(-11.5f,-14.0f,gw);
        GameObject.createGround(-11.5f,14.0f,gw);
        GameObject.createGround(-11.5f,17.0f,gw);
        GameObject.createGround(-11.5f,-17.0f,gw);


        //gw.addGameObject(GameObject.createObstacle(-9.5f,-6.5f,4f,4f,0f,4f,0f,0f,gw));


        GameObject.createIncinerator(-12.5f,-22.7f,-10.5f,-22.7f,gw);
        GameObject.createIncinerator(-12.5f,22.7f,-10.5f,22.7f,gw);

        /*
        gw.addGameObject(GameObject.createBarrel(-7f,3f,gw));
        gw.addGameObject(GameObject.createBarrel(-7f,-3f,gw));
*/
        GameObject.createBulldozer(-6,0f,gw,1);
        GameObject.createBarrel(2f,-22f,gw);
        GameObject.createBarrel(3f,-17.5f,gw);



        renderView = new AndroidFastRenderView(this, gw);
        setContentView(renderView);
        MultiTouchHandler touchHandler = new MultiTouchHandler(renderView, 1, 1);
        gw.setTouchHandler(touchHandler);
        MyThread myThread = new MyThread(gw);
        myThread.start();
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i("Main thread", "resume");
        renderView.resume(); // starts game loop in a separate thread

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("Main thread", "pause");
        renderView.pause(); // stops the main loop
        // persistence example
    }

    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

}