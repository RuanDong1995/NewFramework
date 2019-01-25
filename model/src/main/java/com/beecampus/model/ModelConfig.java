package com.beecampus.model;

/*******************************************************************
 * ModelConfig.java  2018/12/13
 * <P>
 * Model 配置类<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public interface ModelConfig {
    /**
     * 主机地址
     */
    String HOST = "http://119.27.169.84:19001/";

    /**
     * 超时时间
     */
    long REQUEST_TIMEOUT = 1000 * 10;
    /**
     * 配置更新间隔
     */
    long CONFIG_UPDATE_INTERVAL = 1000 * 10;
}
