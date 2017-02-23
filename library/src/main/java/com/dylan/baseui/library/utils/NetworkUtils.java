package com.dylan.baseui.library.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;

import java.net.InetAddress;
import java.net.URL;
import java.util.Locale;

/**
 * Copyright (C) 2014-2020,Qiniu Tech. Co., Ltd.
 * Author: dylan
 * Date: 2016-03-24
 * Description:
 */
public class NetworkUtils {

    private static final String TAG = "NetworkUtils";

    public static final int CHINA_UNICOM = 1;
    public static final int CHINA_MOBILE = 2;
    public static final int CHINA_TELECOM = 3;
    public static final int WIFI = 4;
    public static final int INVALID_OPERATOR = -1;

    /**
     * 判断网络是否有效.
     *
     * @param context 上下文
     * @return 网络连接情况
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null && info.isConnected()) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 获取运营商
     *
     * @return {@link #CHINA_UNICOM}, {@link #CHINA_MOBILE}, {@link #CHINA_TELECOM}, {@link #WIFI}，如果是其他运营商或没网络就是{@link #INVALID_OPERATOR}
     */
    public static int getNetworkOperator(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Network[] allNetworks = connManager.getAllNetworks();
            if (allNetworks != null) {
                for (Network network : allNetworks) {
                    NetworkInfo networkInfo = connManager.getNetworkInfo(network);
                    if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected()) {
                        return WIFI;
                    }
                }
            }
        } else {
            NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifi != null && wifi.isConnected()) {
                return WIFI;
            }
        }
        String mccMnc = getMccMnc(context);
        if (mccMnc != null) {
            if (mccMnc.startsWith("46000") || mccMnc.startsWith("46002") || (mccMnc.startsWith("46007"))) {
                return CHINA_MOBILE;
            } else if (mccMnc.startsWith("46001") || mccMnc.startsWith("46006") || mccMnc.startsWith("46009")) {
                return CHINA_UNICOM;
            } else if (mccMnc.startsWith("46003") || mccMnc.startsWith("46005") || mccMnc.startsWith("46011")) {
                return CHINA_TELECOM;
                //            } else if (mccMnc.startsWith("46020")) {
                //                // Tie Tong
            }
        }

        return INVALID_OPERATOR;
    }

    /**
     * 返回MCC+MNC，如果获取不到返回空字符串，如果SIM卡没插好并且是CDMA制式，也有可能返回完整的ISMI
     */
    public static String getMccMnc(Context context) {
        final TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final int configMcc = context.getResources().getConfiguration().mcc;
        final int configMnc = context.getResources().getConfiguration().mnc;
        String mccMnc = "";
        if (manager.getSimState() == TelephonyManager.SIM_STATE_READY) {
            mccMnc = manager.getSimOperator();
        } else if (manager.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) {
            mccMnc = manager.getNetworkOperator();
        } else if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            mccMnc = manager.getSubscriberId();
        } else if (configMcc != 0 && configMnc != 0) {
            mccMnc = String.format(Locale.getDefault(), "%03d%d", configMcc, configMnc == Configuration.MNC_ZERO ? 0 : configMnc);
        }
        return mccMnc;
    }

    /**
     * 根据url获取对应的ip地址
     *
     * @return 正确的ip地址，或空字符串
     */
    public static String getIpForUrl(String url) {
        try {
            InetAddress address = InetAddress.getByName(new URL(url).getHost());
            return address.getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
