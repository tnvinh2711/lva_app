package com.lva.shop.ui.detail.model;

import com.lva.shop.utils.CommonUtils;

public class NtfModel {
    String title;
    String content;
    long time;
    String url;
    boolean isClick;

    public NtfModel() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return CommonUtils.getTimeFormat(time);
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }
}
