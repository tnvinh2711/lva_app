/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.lva.shop.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.provider.Settings;

import com.lva.shop.R;
import com.lva.shop.ui.location.model.AddressReqRes;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CommonUtils {

    private static final String TAG = "CommonUtils";

    private CommonUtils() {
        // This utility class is not publicly instantiable
    }

    public static SweetAlertDialog showLoadingDialog(Context context) {
        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText(context.getString(R.string.loading));
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }

    @SuppressLint("all")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String loadJSONFromAsset(Context context, String jsonFileName)
            throws IOException {

        AssetManager manager = context.getAssets();
        InputStream is = manager.open(jsonFileName);

        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();

        return new String(buffer, "UTF-8");
    }

    public static String getTimeStamp() {
        return new SimpleDateFormat(AppConstants.TIMESTAMP_FORMAT, Locale.US).format(new Date());
    }

    public static String getConvertPhone(String phone) {
        return "+84" + phone.substring(1);
    }

    public static String convertAddress(AddressReqRes addressReqRes) {
        return addressReqRes.getAddress() + ", " + addressReqRes.getWard() + ", " + addressReqRes.getDistrict() + ", " + addressReqRes.getCity();
    }

    public static String convertHtmlToString(String originHtml) {
        originHtml = originHtml.replaceAll("\n", ".");
        originHtml = originHtml.replaceAll("\r", "");
        originHtml = originHtml.replaceAll("\\<.*?\\>", "");
        originHtml = originHtml.replaceAll("&nbsp;", "");
        originHtml = originHtml.replaceAll("&amp;", "");
        return originHtml;
    }

    public static String convertMoney(String price, int value) {
        String currency = "đ";
        double priceD = Double.parseDouble(price)*value;
        String formattedString = null;
        try {
            DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance();
            DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.GERMAN);
            otherSymbols.setDecimalSeparator('.');
            otherSymbols.setGroupingSeparator('.');
            formatter.setDecimalFormatSymbols(otherSymbols);
            formattedString = formatter.format((int) priceD);
            //setting text after format to EditText
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        return formattedString + currency;
    }
}
