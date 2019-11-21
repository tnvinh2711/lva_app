package com.lva.shop.utils;

import java.io.IOException;

public class NoConnectException extends IOException {

    @Override
    public String getMessage() {
        return "No connectivity exception";
    }
}