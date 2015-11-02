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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 默认管理类实现. <br/>
 * 目前只是简单的封装成单例类，针对实际不同的运行环境，可以继承{@link AbstractConfig}实现自定义的管理类.
 *
 * @author Jingyi Yu.
 * @author Liangyi Lin.
 *
 * @version 2015-11-02.
 */
public final class DefaultConfig extends AbstractConfig {

    private final static Logger LOGGER = LoggerFactory.getLogger(DefaultConfig.class);

    private final static Configuration singleton = new DefaultConfig();

    private DefaultConfig() {
        super();
    }

    /**
     * @return 获得Configuration的单例.
     */
    public static Configuration getSingleton() {
        return singleton;
    }

}
