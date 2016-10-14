package com.example.temperatureapplication;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAmbientTemperature;

    private TextView tv_Monday;
    private TextView tv_Tuesday;
    private TextView tv_Wednesday;
    private TextView tv_Thursday;
    private TextView tv_Friday;

    private TextView temperatureUpdates;

    Switch temperature_switch;

    static boolean isFarhenheit = false;

    private float[] temperatureList = new float[5]; //Storing the Celsius Temperature for 5 days in an Array

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_Monday = (TextView) findViewById(R.id.textview1);
        tv_Tuesday = (TextView) findViewById(R.id.textview2);
        tv_Wednesday = (TextView) findViewById(R.id.textview3);
        tv_Thursday = (TextView) findViewById(R.id.textview4);
        tv_Friday = (TextView) findViewById(R.id.textview5);

        temperatureUpdates = (TextView) findViewById(R.id.temperature_activity);

        temperature_switch = (Switch) findViewById(R.id.switch1);

        //Creating instance of SensorManager and passing SENSOR_SERVICE argument
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAmbientTemperature = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        //Print if Sensor is Unavailable in a Device
        if(mAmbientTemperature == null) {
            temperatureUpdates.setText("Sensor unavailable");
        }

        //Set a CheckedChange Listener for Switch Button
        temperature_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on) {
                isFarhenheit = on;
                updateTemperatures(isFarhenheit);
            }
        });

        //Assigning Random generated Celsius Temperature to the Array
        for (int i = 0; i < 5; i++) {
            temperatureList[i] = randnumgenerator();
        }

        diplayTemperature(temperatureList,"°C");
    }

    //Displaying the Temperature
    public void diplayTemperature(float[] list, String s) {
        tv_Monday.setText(list[0] + s);
        tv_Tuesday.setText(list[1] + s);
        tv_Wednesday.setText(list[2] + s);
        tv_Thursday.setText(list[3] + s);
        tv_Friday.setText(list[4] + s);
    }

    //Random Celsius Temperature Generator at the start of the App

    public static float randnumgenerator(){
        Random r = new Random();
        int Low = -20;
        int High = 60;
        float result = (int)(Math.random() * (High - Low)) + Low;
        return result;
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     * @param temperatureList
     */
    public native float[] fahrenheit(float[] temperatureList);

    //Calling updateTemperature method to display Temperature in Celsius if Switch-OFF or display Temperature in Fahrenheit if Switch-ON
    public void updateTemperatures(boolean isFarhenheit) {
        if(isFarhenheit) {
            float[] arrayFromCPP = fahrenheit(temperatureList);         // get array of converted temperature to C / F
            diplayTemperature(arrayFromCPP,"°F");
        }
        else{
            diplayTemperature(temperatureList,"°C");
        }

    }

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    protected void onResume() {
        super.onResume();
        if(mAmbientTemperature != null) {
            mSensorManager.registerListener(this, mAmbientTemperature, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    protected void onPause() {
        super.onPause();
        if(mAmbientTemperature != null) {
            mSensorManager.unregisterListener(this);
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        temperatureUpdates.setText(event.values[0] + " C");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
