package com.bob.utils;

import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.InitializingBean;

public class Config implements InitializingBean {

    private Properties properties = new Properties();

    public Config() {
    }

    public Config(Properties properties) throws IOException {
        this.properties = properties;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defValue) {
        return properties.getProperty(key, defValue);
    }

    public boolean containsKey(String key) {
        return properties.containsKey(key);
    }

    public Properties getProperties() {
        return properties;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO Auto-generated method stub

    }

}
