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

import java.util.List;

/**
 * 配置管理类接口.
 *
 * @author Jingyi Yu.
 * @author Liangyi Lin.
 *
 * @version 2015-11-02.
 */
public interface Configuration {

    /**
     * 返回当前是否采用智能分词.
     * @return 如果智能分词则返回true, 否则返回false.
     */
    public boolean useSmart();

    /**
     * 设置是否采用智能分词。如果useSmart=true,采用智能分词策略，useSmart=false采用细粒度分词策略.
     * @param useSmart  是否采用智能分词.
     */
    public void setUseSmart(boolean useSmart);

    /**
     * 获取主词典路径
     *
     * @return String 主词典路径
     */
    public String getMainDictionary();

    /**
     * 获取量词词典路径
     *
     * @return String 量词词典路径
     */
    public String getQuantifierDictionay();

    /**
     * 获取扩展字典配置路径
     *
     * @return List<String> 相对类加载器的路径
     */
    public List<String> getExtDictionarys();

    /**
     * 获取扩展停止词典配置路径
     *
     * @return List<String> 相对类加载器的路径
     */
    public List<String> getExtStopWordDictionarys();
}
