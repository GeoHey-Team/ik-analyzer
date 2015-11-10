package com.geohey.ikanalyzer.dic;

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

import com.geohey.ikanalyzer.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * 词库操作接口.
 *
 * @author Jingyi Yu.
 * @author Liangyi Lin.
 */
public final class Dictionary extends AbstractDictionary {

    private static final Logger LOGGER = LoggerFactory.getLogger(Dictionary.class);

    private static final String RESOURCE_ENCODING = "UTF-8";

    private static AbstractDictionary singleton;

    private Dictionary(Configuration cfg) {
        super(cfg);
    }

    public static IDictionary getSingleton(){
        if(singleton == null){

        }
        return singleton;
    }

    /**
     * 初始化词典，仅第一次调用的设置的{@link Configuration}才有作用.
     * 由于IK Analyzer的词典采用Dictionary类的静态方法进行词典初始化，
     * 并采用 Lazy Load 的方法加载Dictioanary中的资源。<br/>
     * 该方法提供了一个在应用加载阶段就初始化字典的手段，但是只有第一次设置的cfg才有效果。
     * 另外，多线程情况并不能保证第一个访问的线程的cfg有效.
     *
     * @param cfg
     * @return {@link IDictionary}实例对象.
     */
    public static IDictionary initial(Configuration cfg) {
        synchronized (Dictionary.class) {
            if (singleton == null) {
                synchronized (Dictionary.class) {
                    if (singleton == null) {
                        singleton = new Dictionary(cfg);
                        return singleton;
                    }
                }
            }
        }

        return singleton;
    }

    /**
     * 加载主词典及扩展词库.
     */
    @Override
    protected void loadMainDict() {

        String key = cfg.getMainDictionary();
        LOGGER.info("加载主词典：{}", key);

        _MainDict = loadDict(_MainDict, key);

        // 加载扩展词条到主词典
        loadExtDict();
    }

    @Override
    protected void loadQuantifierDict() {

        String key = cfg.getQuantifierDictionay();
        LOGGER.info("加载量词词典：{}", key);

        _QuantifierDict = loadDict(_QuantifierDict, key);
    }

    @Override
    protected void loadExtDict() {

        List<String> extKeys = cfg.getExtDictionarys();
        if (extKeys != null) {
            for (String extKey : extKeys) {
                LOGGER.info("加载扩展词典：{}", extKey);

                _MainDict = loadDict(_MainDict, extKey);
            }
        }
    }

    @Override
    protected void loadStopWordDict() {

        List<String> stopKeys = cfg.getExtStopWordDictionarys();
        if (stopKeys != null) {
            for (String stopKey : stopKeys) {
                LOGGER.info("加载停止词典：{}", stopKey);

                _StopWordDict = loadDict(_StopWordDict, stopKey);
            }
        }

    }

    protected DictSegment loadDict(DictSegment ds, String key) {

        if (ds == null) {
            ds = new DictSegment((char) 0);
        }

        InputStream in = this.getClass().getClassLoader().getResourceAsStream(key);

        if (in == null) {
            throw new RuntimeException(String.format("%s Dictionary not found!!!", key));
        }

        BufferedReader bufReader = null;
        try {
            bufReader = new BufferedReader(new InputStreamReader(in, RESOURCE_ENCODING), 512);

            String word = null;
            do {
                word = bufReader.readLine();
                if (word != null && !"".equals(word.trim())) {
                    char[] chs = word.trim().toLowerCase().toCharArray();
                    ds.fillSegment(chs);
                }
            } while (word != null);

        } catch (IOException e) {
            LOGGER.error("加载配置文件 {} 异常, {}", key, e);
        } finally {
            try {
                if (bufReader != null) {
                    bufReader.close();
                }

                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return ds;

    }
}
