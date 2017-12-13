package com.dashuai.framework.common;

/**
 * Created by wangyishuai on 2017/12/13
 */
public enum DashuaiFrameCode {
    SystemError(-1),

    ;
    private int code;

    DashuaiFrameCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
