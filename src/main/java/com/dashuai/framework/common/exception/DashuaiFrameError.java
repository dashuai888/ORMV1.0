package com.dashuai.framework.common.exception;

/**
 * Created by wangyishuai on 2017/12/13
 */
public class DashuaiFrameError extends Error {
    public DashuaiFrameError() {
        super();
    }

    public DashuaiFrameError(String msg) {
        super(msg);
    }
}
