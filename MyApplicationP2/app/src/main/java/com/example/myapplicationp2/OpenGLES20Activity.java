package com.example.myapplicationp2;


import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import java.util.Date;

public class OpenGLES20Activity extends Activity{
    private GLSurfaceView gLView;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private long prevtime;
    private int i=0;
    public static float xD =0;
    public static float yD =0;
    public static float zD =0;
    private float xV =0;
    private float yV =0;
    private float zV =0;
    public static boolean accurate = false;
    private float[] gravity = new float[3];
    private float[] accel = new float[3];




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        gLView = new MyGLSurfaceView(this);
        setContentView(gLView);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(sensorListener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }


    private class MyGLSurfaceView extends GLSurfaceView {
        private final MyGLRenderer renderer;

        public MyGLSurfaceView(Context context) {
            super(context);

            // Create an OpenGL ES 2.0 context
            setEGLContextClientVersion(2);

            renderer = new MyGLRenderer();

            // Set the Renderer for drawing on the GLSurfaceView
            setRenderer(renderer);
            //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        }
    }


    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            {
                // alpha is calculated as t / (t + dT)
                // with t, the low-pass filter's time-constant
                // and dT, the event delivery rate

                final float alpha = 0.8f;

                gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
                gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
                gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];
                accel[0] = event.values[0] - gravity[0];
                accel[1] = event.values[1] - gravity[1];
                accel[2] = event.values[2] - gravity[2];


                //long time = new Date().getTime();
                //float dt = time - prevtime;
                //prevtime = time;


                if(i>100){
                    accurate = true;
                    xV = (xV + accel[0]);
                    xD = xV;

                    yV = (yV + accel[1]);
                    yD = yV;

                    zV = (zV + accel[2]);
                    zD = zV;

                }
                else i++;
            }
        }


        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
