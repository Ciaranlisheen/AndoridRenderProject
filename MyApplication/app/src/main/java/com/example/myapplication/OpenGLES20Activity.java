package com.example.myapplication;


import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class OpenGLES20Activity extends Activity{
    private GLSurfaceView gLView;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetic;
    private static float[] rotationMatrix = new float[9];
    private static float[] mOrientationAngles = new float[3];
    private static float[] accel = new float[3];
    private static float[] mag = new float[3];



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
        mMagnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        mSensorManager.registerListener(sensorListener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(sensorListener, mMagnetic, SensorManager.SENSOR_DELAY_NORMAL);
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

            if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                accel[0] = event.values[0];//x
                accel[1] = event.values[1];//y
                accel[2] = event.values[2];//z
            }

            else if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
                mag[0] = event.values[0];
                mag[1] = event.values[1];
                mag[2] = event.values[2];
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    public static float[] updateOrientation(){
        SensorManager.getRotationMatrix(rotationMatrix, null, accel, mag);
        SensorManager.getOrientation(rotationMatrix, mOrientationAngles);

        return mOrientationAngles;
    }

}
