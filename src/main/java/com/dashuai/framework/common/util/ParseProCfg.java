package com.dashuai.framework.common.util;

import com.dashuai.framework.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.Properties;

/**
 * Created by wangyishuai on 2017/12/13
 */
public enum ParseProCfg {
    INSTANCE;

    private static final Logger LOG = LoggerFactory.getLogger(ParseProCfg.class);
    private static final Properties properties = new Properties();

    private void initPath(String defaultPath) {
        URL confFile = ParseProCfg.class.getClassLoader().getResource(defaultPath);
        String path = Optional.ofNullable(confFile).map(URL::getFile).orElse("");
        initProperties(path);
    }

    private void initProperties(String path) {
        InputStream inputStream;

        try {
            inputStream = new FileInputStream(path);
            properties.load(inputStream);
            LOG.info("Successfully initProperties {}", path);
        } catch (IOException e) {
            LOG.error("initProperties {} failed : {}", path, e);
            System.exit(-1);
        }
    }

    public Properties parseFile() {
        initPath(Constants.ConfigName.valueOf());
        return properties;
    }
}
