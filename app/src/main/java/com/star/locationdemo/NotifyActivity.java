package com.star.locationdemo;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Switch;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;

public class NotifyActivity extends Activity{

    private LocationClient mLocationClient;

    private BDLocationListener mBDLocationListener;

    private Switch mNotifySwitch;

    private Vibrator mVibrator;

    private double mLatitude;
    private double mLongitude;

    private BDNotifyListener mBDNotifyListener;

    private Handler mBDNotifyHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify);

        mLocationClient = new LocationClient(LocationApplication.getContext());

        mBDLocationListener = new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                //Receive Location
                mLongitude = bdLocation.getLongitude();
                mLatitude = bdLocation.getLatitude();
                mBDNotifyHandler.sendEmptyMessage(0);
            }
        };

        mBDNotifyListener = new BDNotifyListener() {
            @Override
            public void onNotify(BDLocation bdLocation, float v) {
                super.onNotify(bdLocation, v);

                mVibrator.vibrate(1000);//振动提醒已到设定位置附近
                Toast.makeText(NotifyActivity.this, "震动提醒", Toast.LENGTH_SHORT).show();
            }
        };

        mBDNotifyHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {

                super.handleMessage(msg);

                mBDNotifyListener.SetNotifyLocation(mLatitude, mLongitude, 3000, "gcj02");//4个参数代表要位置提醒的点的坐标，具体含义依次为：纬度，经度，距离范围，坐标系类型(gcj02,gps,bd09,bd09ll)

            }

        };

        mVibrator = (Vibrator) getApplicationContext().getSystemService(VIBRATOR_SERVICE);

        mNotifySwitch = (Switch) findViewById(R.id.notify_switch);

        mNotifySwitch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNotifySwitch.isChecked()) {
                    mLocationClient.start();
                    mLocationClient.registerLocationListener(mBDLocationListener);
                    mLocationClient.registerNotify(mBDNotifyListener);
                } else if (mBDNotifyListener != null) {
                    mLocationClient.removeNotifyEvent(mBDNotifyListener);
                    mLocationClient.unRegisterLocationListener(mBDLocationListener);
                    mLocationClient.stop();
                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();

        mLocationClient.removeNotifyEvent(mBDNotifyListener);
        mLocationClient.unRegisterLocationListener(mBDLocationListener);
        mLocationClient.stop();
        mNotifySwitch.setChecked(false);
    }
}
