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

import java.util.Collection;

/**
 * 词库操作的抽象类.
 *
 * @author Jingyi Yu.
 * @author Liangyi Lin.
 */
public abstract class AbstractDictionary implements IDictionary {

    /**
     * 主词典对象.
     */
    protected DictSegment _MainDict;

    /**
     * 量词词典
     */
    protected DictSegment _QuantifierDict;

    /**
     * 停止词词典
     */
    protected DictSegment _StopWordDict;

    protected Configuration cfg;

    public AbstractDictionary(Configuration cfg) {
        this.cfg = cfg;
        this.loadMainDict();
        this.loadQuantifierDict();
        this.loadExtDict();
        this.loadStopWordDict();
    }

    @Override
    public void addWords(Collection<String> words) {

        if (words != null || words.size() <= 0) {
            return ;
        }

        for (String word : words) {
            if (word != null) {
                char[] chs = word.trim().toLowerCase().toCharArray();
                _MainDict.fillSegment(chs);
            }
        }
    }

    @Override
    public void disableWords(Collection<String> words) {

        if (words != null || words.size() <= 0) {
            return ;
        }

        for (String word : words) {
            if (word != null) {
                char[] chs = word.trim().toLowerCase().toCharArray();
                _MainDict.disableSegment(chs);
            }
        }
    }

    @Override
    public Hit matchInMainDict(char[] charArray) {
        return _MainDict.match(charArray);
    }

    @Override
    public Hit matchInMainDict(char[] charArray, int begin, int length) {
        return _MainDict.match(charArray, begin, length);
    }

    @Override
    public Hit matchInQuantifierDict(char[] charArray, int begin, int length) {
        return _QuantifierDict.match(charArray, begin, length);
    }

    @Override
    public Hit matchWithHit(char[] charArray, int currentIndex, Hit matchedHit) {
        DictSegment ds = matchedHit.getMatchedDictSegment();
        return ds.match(charArray, currentIndex, 1, matchedHit);
    }

    @Override
    public boolean isStopWord(char[] charArray) {
        return _StopWordDict.match(charArray).isMatch();
    }

    @Override
    public boolean isStopWord(char[] charArray, int begin, int length) {
        return _StopWordDict.match(charArray, begin, length).isMatch();
    }

    /**
     * 加载主词典及扩展词库.
     */
    protected abstract void loadMainDict();

    /**
     * 加载量词词典.
     */
    protected abstract void loadQuantifierDict();

    /**
     * 加载用户配置的扩展词典到主词表.
     */
    protected abstract void loadExtDict();

    /**
     * 加载用户扩展的停用词词典.
     */
    protected abstract void loadStopWordDict();
}
