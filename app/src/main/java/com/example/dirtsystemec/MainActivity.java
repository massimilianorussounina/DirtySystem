package com.example.dirtsystemec;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ViewSwitcher;

import com.badlogic.androidgames.framework.Audio;
import com.badlogic.androidgames.framework.Music;
import com.badlogic.androidgames.framework.impl.AndroidAudio;
import com.badlogic.androidgames.framework.impl.MultiTouchHandler;

import java.util.zip.Inflater;

public class MainActivity extends Activity {


    //private static final float coordinateXMin = -13.5f, coordinateXMax = 13.5f, coordinateYMin = -24, coordinateYMax = 24;
    private static final float coordinateXMin = -13.5f, coordinateXMax = 13.5f, coordinateYMin = -24f, coordinateYMax = 24f;
    public static String TAG;
    private AndroidFastRenderView renderView;
    private int currentApiVersion;
    private Audio audio;
    private Music bulldozerMusic,backgroundMusic;
    private GameWorld gw;
    private MyThread myThread;
    private HandlerUI handlerUI;
    public static float  volumeMusic=0.8f, volumeSoundEffect=0.5f;

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

        handlerUI=new HandlerUI(this);

        /* Inizializzazione Audio */
        audio = new AndroidAudio(this);
        CollisionSounds.init(audio);
        bulldozerMusic = audio.newMusic("soundTractor.mp3");
        bulldozerMusic.play();
        bulldozerMusic.setLooping(true);
        bulldozerMusic.setVolume(volumeSoundEffect);
        backgroundMusic = audio.newMusic("soundtrack.mp3");
        backgroundMusic.play();
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(volumeMusic);



        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Box physicalSize = new Box(coordinateXMin, coordinateYMin, coordinateXMax, coordinateYMax);
        Box screenSize   = new Box(0, 0, metrics.widthPixels, metrics.heightPixels);
        gw = new GameWorld(physicalSize, screenSize, this,handlerUI);
        gw.setGravity(-10,0);

        GameObject.createTimer(11,-2.7f,gw);

        GameObject.createEnclosure(coordinateXMax,coordinateXMin,coordinateYMax,coordinateYMin,gw);

        GameObject.createBridge(-7.6f,0,-8.5f,4.5f,-2f,0f,-2f,1f,2f,1f
                ,-8.5f,-4.5f,2f,0f,2f,1f,-2f,1f,gw);



        GameObject.createSea(-13.4f,0f,-12.5f,0f,gw);
        GameObject.createGround(-11.5f,7.0f,gw);
        GameObject.createGround(-11.5f,-7.0f,gw);
        GameObject.createGround(-11.5f,-14.0f,gw);
        GameObject.createGround(-11.5f,14.0f,gw);
        GameObject.createGround(-11.5f,17.0f,gw);
        GameObject.createGround(-11.5f,-17.0f,gw);


        GameObject.createIncinerator(-12.5f,-22.7f,-10.1f,-22.7f,gw);
        GameObject.createIncinerator(-12.5f,22.7f,-10.1f,22.7f,gw);



        /* GameObject.createBarrel(10,1,gw);
        GameObject.createBarrel(3f,-17.5f,gw); */


        GameObject.createBulldozer(-6.6f,0,gw,-1,this,null);
        GameObject.createScoreBar(4.2f,22.5f,gw);
        GameObject.createTextNumberBarrel(9.5f,-23.45f,gw);
        GameObject.createTextscore(11.25f,-21f,gw);
        GameObject.createButtonPause(11f,22f,gw);
        GameObject.createBarrelIcon(12f,-22.8f,gw);

        renderView = new AndroidFastRenderView(this, gw);

        setContentView(renderView);
        MultiTouchHandler touchHandler = new MultiTouchHandler(renderView, 1, 1);
        gw.setTouchHandler(touchHandler);
        myThread = new MyThread(gw);
        myThread.start();




    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i("Main thread", "resume");
            bulldozerMusic.setVolume(volumeSoundEffect);
            backgroundMusic.setVolume(volumeMusic);
            bulldozerMusic.play();
            backgroundMusic.play();
            gw.setTimeResume(System.currentTimeMillis());
            renderView.resume(); // starts game loop in a separate thread
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("Main thread", "pause");

            bulldozerMusic.pause();
            backgroundMusic.pause();
            gw.setTimerPause(System.currentTimeMillis());
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

    public void showMenu(){
        Intent i= new Intent(this,StartActivity.class);
        i.putExtra("FLAG", "true");
        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);



    }

    public  HandlerUI getHandlerUI(){
        return handlerUI;
    }

}