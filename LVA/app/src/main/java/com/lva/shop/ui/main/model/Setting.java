package com.lva.shop.ui.main.model;

public class Setting {
    int id;
    int icon;
    String title;

    public Setting(int id, int icon, String title) {
        this.id = id;
        this.icon = icon;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
