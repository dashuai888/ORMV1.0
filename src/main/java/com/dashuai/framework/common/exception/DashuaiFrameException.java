package com.dashuai.framework.common.exception;

import com.dashuai.framework.common.DashuaiFrameCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wangyishuai on 2017/12/13
 */
public class DashuaiFrameException extends RuntimeException {
    private static final Logger LOG = LoggerFactory.getLogger(DashuaiFrameException.class);

    private DashuaiFrameCode code;

    public DashuaiFrameException(DashuaiFrameCode code) {
        this.code = code;
        LOG.warn("DashuaiFrameException {}", code);
    }

    public DashuaiFrameCode getCode() {
        return code;
    }

    public void setCode(DashuaiFrameCode code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return getCode().toString();
    }
}
