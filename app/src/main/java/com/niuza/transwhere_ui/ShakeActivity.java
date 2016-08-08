package com.niuza.transwhere_ui;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
/**
 * Created by 牛杂辉 on 2016/8/8.
 */
public class ShakeActivity extends AppCompatActivity {





    private SensorManager mSensorManager=null; //定义sensor管理器, 注册监听器用
    private Vibrator vibrator = null;
    private static final int SENSOR_SHAKE = 10;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton trans_computer = (FloatingActionButton) findViewById(R.id.trans_computer);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);




        trans_computer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShakeActivity.this, ComputerActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSensorManager != null) {// 注册监听器
            mSensorManager.registerListener(sensorEventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
            // 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSensorManager != null) {// 取消监听器
            mSensorManager.unregisterListener(sensorEventListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private SensorEventListener sensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            // 传感器信息改变时执行该方法
            float[] values = event.values;
            float x = values[0]; // x轴方向的重力加速度，向右为正
            float y = values[1]; // y轴方向的重力加速度，向前为正
            float z = values[2]; // z轴方向的重力加速度，向上为正
            Log.i("shake", "x轴方向的重力加速度" + x +  "；y轴方向的重力加速度" + y +  "；z轴方向的重力加速度" + z);
            // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
            int medumValue = 19;// 三星 i9250怎么晃都不会超过20，没办法，只设置19了
            if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) {
                vibrator.vibrate(200);
                Message msg = new Message();
                msg.what = SENSOR_SHAKE;
                handler.sendMessage(msg);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    /**
     * 动作执行
     */
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SENSOR_SHAKE:
                    Snackbar.make(findViewById(R.id.trans_computer),R.string.action_shake, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    break;
            }
        }

    };










}