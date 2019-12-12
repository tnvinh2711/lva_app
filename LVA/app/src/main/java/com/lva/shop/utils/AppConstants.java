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

public final class AppConstants {


    public static final String TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss";

    //web
    public static final String TITLE = "web_title";
    public static final String URL = "web_url";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String LAUNCH_APP = "not_launch_app";


    //type error
    public static final String LOAD_DATA_LOGIN_FAIL = "load data login fail";
    public static final String LOAD_DATA_HOME_FAIL = "load data home fail";
    public static final String NETWORK_ERROR = "network_error";


    //video
    public static final String VIDEO_URL = "video_url" ;

    //address
    public static final int REQUEST_ADDRESS = 11054;
    public static final String ADDRESS = "address";
    public static final int CITY = 0;
    public static final int DISTRICT = 1;
    public static final int COMMUNE = 2;

    //profile
    public static final int REQ_CODE_STORAGE_AVATAR = 1123;
    public static final int REQ_CODE_STORAGE_BANNER = 1124;
    public static final String URI_BANNER = "uri_banner";
    public static final String LIST_CART = "list_cart";
    public static final int LOGIN_RESULT = 8898;
    public static final int REQ_LOGIN_FROM_HOME = 9990;
    public static final int REQ_LOGIN_FROM_ORDER = 9991;
    public static final int REQ_LOGIN_FROM_PROFILE = 9992;
    public static final int REQ_LOGIN_FROM_CART = 9995;
    public static final int CART_RESULT = 9993;
    public static final int CART_REQ = 9995;
    public static final String USER_INFO = "userInfo";
    public static final String PHONE = "phone";
    public static final String ADDRESS_LOCAL = "address_local";
    public static final String NOTI = "noti";
    public static final String INTENT_FILTER_FROM_NOTI = "broardcast_noti";


    private AppConstants() {
        // This utility class is not publicly instantiable
    }
}
