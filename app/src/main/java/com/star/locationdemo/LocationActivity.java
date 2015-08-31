package com.star.locationdemo;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class LocationActivity extends Activity {

    private LocationClient mLocationClient;

    private BDLocationListener mBDLocationListener;

    private TextView mLocationResult;

    private RadioGroup mLocationModeRadioGroup;
    private RadioGroup mCoorTypeRadioGrouip;

    private LocationMode mLocationMode;
    private String mCoorType;

    private TextView mLocationModeInfo;

    private EditText mPeriod;

    private CheckBox mIsReverseGeoCodeResult;

    private Switch mLocationSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);

        mLocationClient = ((LocationApplication)getApplication()).mLocationClient;

        mBDLocationListener = ((LocationApplication)getApplication()).mBDLocationListener;

        mLocationResult = (TextView)findViewById(R.id.location_result_text_view);

        mLocationResult.setMovementMethod(ScrollingMovementMethod.getInstance());

        ((LocationApplication)getApplication()).mLocationResult = mLocationResult;

        mLocationModeRadioGroup = (RadioGroup)findViewById(R.id.location_mode_radio_group);
        mLocationMode = LocationMode.Hight_Accuracy;

        mLocationModeInfo = (TextView)findViewById(R.id.location_mode_info);
        mLocationModeInfo.setText(getString(R.string.hight_accuracy_desc));

        mLocationModeRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                String locationModeInfo = null;

                switch (checkedId) {
                    case R.id.radio_high_accuracy:
                        mLocationMode = LocationMode.Hight_Accuracy;
                        locationModeInfo = getString(R.string.hight_accuracy_desc);
                        break;

                    case R.id.radio_battery_saving:
                        mLocationMode = LocationMode.Battery_Saving;
                        locationModeInfo = getString(R.string.saving_battery_desc);
                        break;

                    case R.id.radio_device_sensors:
                        mLocationMode = LocationMode.Device_Sensors;
                        locationModeInfo = getString(R.string.device_sensor_desc);
                        break;

                    default:
                        break;
                }
                mLocationModeInfo.setText(locationModeInfo);
            }
        });

        mCoorTypeRadioGrouip = (RadioGroup)findViewById(R.id.coor_type_radio_group);
        mCoorType = "gcj02";

        mCoorTypeRadioGrouip.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radio_gcj02:
                        mCoorType = "gcj02";//国家测绘局标准
                        break;

                    case R.id.radio_bd09ll:
                        mCoorType = "bd09ll";//百度经纬度标准
                        break;

                    case R.id.radio_bd09:
                        mCoorType = "bd09";//百度墨卡托标准
                        break;

                    default:
                        break;
                }
            }
        });

        mPeriod = (EditText)findViewById(R.id.period);

        mIsReverseGeoCodeResult = (CheckBox)findViewById(R.id.is_reverse_geo_code_result);

        mLocationSwitch = (Switch) findViewById(R.id.location_switch);

        mLocationSwitch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLocationSwitch.isChecked()) {
                    initLocation();
                    mLocationClient.start();
                } else {
                    mLocationClient.stop();
                }
            }
        });
    }

    @Override
    protected void onStop() {

        super.onStop();

        mLocationClient.stop();
        mLocationClient.unRegisterLocationListener(mBDLocationListener);
        mLocationSwitch.setChecked(false);
    }

    private void initLocation(){
        LocationClientOption locationClientOption = new LocationClientOption();

        locationClientOption.setLocationMode(mLocationMode);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        locationClientOption.setCoorType(mCoorType);//可选，默认gcj02，设置返回的定位结果坐标系，

        int span=1000;
        try {
            span = Integer.valueOf(mPeriod.getText().toString());
        } catch (Exception e) {
            // TODO: handle exception
        }

        locationClientOption.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        locationClientOption.setIsNeedAddress(mIsReverseGeoCodeResult.isChecked());//可选，设置是否需要地址信息，默认不需要
        locationClientOption.setOpenGps(true);//可选，默认false,设置是否使用gps
        locationClientOption.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        locationClientOption.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        locationClientOption.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        locationClientOption.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        locationClientOption.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        mLocationClient.setLocOption(locationClientOption);
    }
}
