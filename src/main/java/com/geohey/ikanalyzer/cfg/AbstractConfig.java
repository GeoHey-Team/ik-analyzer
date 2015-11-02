package com.geohey.ikanalyzer.cfg;

/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * NOTE: 前源代码由林良益(linliangyi2005@gmail.com)提供
 *
 * 当前版本由GeoHey(https://geohey.com)fork，维护和更新.
 * provided by GeoHey now.
 *
 */


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

/**
 * Configuration抽类，封装了基本的配置属性.
 *
 * @author Jingyi Yu.
 *
 * @version 2015-11-02.
 */
public abstract class AbstractConfig implements Configuration {

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractConfig.class);

    /**
     * 配置文件名.
     */
    protected final static String CONFIG = "IKAnalyzer.cfg.xml";

    /**
     * 配置文件中主字典库标识.
     */
    protected final static String CORE_MAIN = "core-main";

    /**
     * 配置文件中量词字典库标识.
     */
    protected final static String CORE_QUANTIFIER = "core-quantifier";

    /**
     * 配置文件中扩展字典库标识.
     */
    protected final static String EXT_DICT = "ext_dict";

    /**
     * 配置文件中停用词字典库标识.
     */
    protected final static String EXT_STOP = "ext_stopwords";

    /**
     * 是否采用智能分词.
     */
    protected boolean useSmart;

    /**
     * 配置的属性集合.
     */
    protected Properties props;

    public AbstractConfig() {

        props = new Properties();

        InputStream in = null;
        try {
            in = this.getClass().getClassLoader().getResourceAsStream(CONFIG);
            props.loadFromXML(in);
        } catch (InvalidPropertiesFormatException e) {
            LOGGER.error("读取配置文件 {} 格式不合法, {}", CONFIG, e);
        } catch (IOException e) {
            LOGGER.error("读取配置文件 {} 异常, {}", CONFIG, e);
        } finally {

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    LOGGER.error("关闭配置文件 {} 异常, {}", CONFIG, e);
                }
            }
        }

    }

    @Override
    public boolean useSmart() {
        return useSmart;
    }

    @Override
    public void setUseSmart(boolean useSmart) {
        this.useSmart = useSmart;
    }

    @Override
    public String getMainDictionary() {
        return CORE_MAIN;
    }

    @Override
    public String getQuantifierDictionay() {
        return CORE_QUANTIFIER;
    }

    @Override
    public List<String> getExtDictionarys() {

        List<String> extDictFiles = new ArrayList<>(2);
        String extDictCfg = props.getProperty(EXT_DICT);

        if(extDictCfg != null){
            //使用;分割多个扩展字典配置
            String[] filePaths = extDictCfg.split(";");
            if(filePaths != null){
                for(String filePath : filePaths){
                    if(filePath != null && !"".equals(filePath.trim())){
                        extDictFiles.add(filePath.trim());
                    }
                }
            }
        }

        return extDictFiles;
    }

    @Override
    public List<String> getExtStopWordDictionarys() {

        List<String> extStopWordDictFiles = new ArrayList<>(2);
        String extStopWordDictCfg = props.getProperty(EXT_STOP);

        if(extStopWordDictCfg != null){
            //使用;分割多个扩展字典配置
            String[] filePaths = extStopWordDictCfg.split(";");
            if(filePaths != null){
                for(String filePath : filePaths){
                    if(filePath != null && !"".equals(filePath.trim())){
                        extStopWordDictFiles.add(filePath.trim());
                    }
                }
            }
        }

        return extStopWordDictFiles;
    }

}
