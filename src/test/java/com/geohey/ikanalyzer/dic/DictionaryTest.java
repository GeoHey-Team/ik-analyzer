package com.geohey.ikanalyzer.dic;

import com.geohey.ikanalyzer.cfg.Configuration;
import com.geohey.ikanalyzer.cfg.DefaultConfig;
import org.testng.annotations.Test;

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

public class DictionaryTest {

    @Test
    public void init() {

        Configuration cfg = DefaultConfig.getSingleton();
        Dictionary.initial(cfg);
        IDictionary dict = Dictionary.getSingleton();

        assert dict != null;
    }

    @Test
    public void match() {

        Configuration cfg = DefaultConfig.getSingleton();
        Dictionary.initial(cfg);
        IDictionary dict = Dictionary.getSingleton();

        Hit hit = dict.matchInMainDict("北京".toCharArray());
        assert hit.isMatch();

        boolean is = dict.isStopWord("a".toCharArray());
        assert is;
    }
}
