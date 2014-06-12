package com.example.magnetometer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
 
public class MainActivity extends Activity implements SensorEventListener {
 
  
  private SensorManager mSensorManager;
  Sensor accelerometer;
  Sensor magnetometer;
 TextView t;
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);    // Register the sensor listeners
    t = (TextView) findViewById(R.id.textView1);
    mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
      accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
    magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
  }
 
  protected void onResume() {
    super.onResume();
    mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
    mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
  }
 
  protected void onPause() {
    super.onPause();
    mSensorManager.unregisterListener(this);
  }
 
  public void onAccuracyChanged(Sensor sensor, int accuracy) { 
	  
  }
 
  float[] mGravity;
  float[] mGeomagnetic;
  public void onSensorChanged(SensorEvent event) {
    if (event.sensor.getType() == Sensor.TYPE_GRAVITY)
      mGravity = event.values;
    if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
      mGeomagnetic = event.values;
    if (mGravity != null && mGeomagnetic != null) {
      float R[] = new float[9];
      float I[] = new float[9];
      float [] A_D = mGeomagnetic.clone();
      float [] A_W = new float[3];
      boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
      if (R.length==9) {
    	
          A_W[0] = R[0] * A_D[0] + R[1] * A_D[1] + R[2] * A_D[2];
          A_W[1] = R[3] * A_D[0] + R[4] * A_D[1] + R[5] * A_D[2];
          A_W[2] = R[6] * A_D[0] + R[7] * A_D[1] + R[8] * A_D[2]; 
      }
      if (R.length==16) {
    	 
          A_W[0] = R[0] * A_D[0] + R[1] * A_D[1] + R[2] * A_D[2];
          A_W[1] = R[4] * A_D[0] + R[5] * A_D[1] + R[6] * A_D[2];
          A_W[2] = R[8] * A_D[0] + R[9] * A_D[1] + R[10] * A_D[2]; 
      }
      double total = Math.sqrt((A_W[0]*A_W[0])+(A_W[1]*A_W[1])+(A_W[2]*A_W[2]));
    		  
     t.setText("X : " + A_W[0] + "\nY : " + A_W[1] + "\nZ : " + A_W[2] + "\nTOTAL : " + total);
    }
    
  }
}
