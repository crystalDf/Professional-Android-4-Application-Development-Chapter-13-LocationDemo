package com.star.locationdemo;

import android.app.Application;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.Poi;

import java.util.List;

/**
 * 主Application，所有百度定位SDK的接口说明请参考线上文档：http://developer.baidu.com/map/loc_refer/index.html
 *
 * 百度定位SDK官方网站：http://developer.baidu.com/map/index.php?title=android-locsdk
 */
public class LocationApplication extends Application {
    public LocationClient mLocationClient;

    public TextView mLocationResult;
    public Vibrator mVibrator;

    public BDLocationListener mBDLocationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(bdLocation.getTime());
            sb.append("\nerror code : ");
            sb.append(bdLocation.getLocType());
            sb.append("\nlatitude : ");
            sb.append(bdLocation.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(bdLocation.getLongitude());
            sb.append("\nradius : ");
            sb.append(bdLocation.getRadius());

            switch (bdLocation.getLocType()) {
                case BDLocation.TypeGpsLocation:
                    sb.append("\nspeed : ");
                    sb.append(bdLocation.getSpeed());// 单位：公里每小时
                    sb.append("\nsatellite : ");
                    sb.append(bdLocation.getSatelliteNumber());
                    sb.append("\nheight : ");
                    sb.append(bdLocation.getAltitude());// 单位：米
                    sb.append("\ndirection : ");
                    sb.append(bdLocation.getDirection());
                    sb.append("\naddr : ");
                    sb.append(bdLocation.getAddrStr());
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                    break;

                case BDLocation.TypeNetWorkLocation:
                    sb.append("\naddr : ");
                    sb.append(bdLocation.getAddrStr());
                    //运营商信息
                    sb.append("\noperationers : ");
                    sb.append(bdLocation.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                    break;

                case BDLocation.TypeOffLineLocation:
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                    break;

                case BDLocation.TypeServerError:
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                    break;

                case BDLocation.TypeNetWorkException:
                    sb.append("\ndescribe : ");
                    sb.append("网络不通导致定位失败，请检查网络是否通畅");
                    break;

                case BDLocation.TypeCriteriaException:
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                    break;

                default:
                    break;
            }

            sb.append("\nlocationdescribe : ");// 位置语义化信息
            sb.append(bdLocation.getLocationDescribe());

            List<Poi> list = bdLocation.getPoiList();// POI信息
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi poi : list) {
                    sb.append("\npoi= : ");
                    sb.append(poi.getId() + " " + poi.getName() + " " + poi.getRank());
                }
            }

            logMsg(sb.toString());

            Log.i("BaiduLocationApiDem", sb.toString());
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        mLocationClient = new LocationClient(this.getApplicationContext());

        mLocationClient.registerLocationListener(mBDLocationListener);

        mVibrator = (Vibrator) getApplicationContext().getSystemService(VIBRATOR_SERVICE);
    }

    /**
     * 显示请求字符串
     * @param str
     */
    public void logMsg(String str) {
        try {
            if (mLocationResult != null)
                mLocationResult.setText(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
