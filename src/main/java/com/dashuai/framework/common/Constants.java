package com.dashuai.framework.common;

/**
 * Created by wangyishuai on 2017/12/13
 */
public enum Constants {
    ConfigName("dashuaiFramework.properties"), DefaultJDBCUrl("com.mysql.jdbc.Driver");

    private String value;

    Constants(String value) {
        this.value = value;
    }

    public String valueOf() {
        return this.value;
    }
}
