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
 * 词库操作接口.
 *
 * @author Jingyi Yu.
 *
 */
public interface IDictionary {

    /**
     * 批量加载新词条.
     * @param words 词条列表.
     */
    void addWords(Collection<String> words);

    /**
     * 批量移除（屏蔽）词条
     * @param words 屏蔽词项.
     */
    void disableWords(Collection<String> words);

    /**
     * 检索匹配主词库.
     * @param charArray
     * @return  匹配结果描述对象Hit.
     */
    Hit matchInMainDict(char[] charArray);

    /**
     * 检索匹配主词典
     * @param charArray
     * @param begin
     * @param length
     * @return 匹配结果描述对象Hit.
     */
    Hit matchInMainDict(char[] charArray, int begin, int length);

    /**
     * 检索匹配量词词典
     * @param charArray
     * @param begin
     * @param length
     * @return 匹配结果描述对象Hit.
     */
    Hit matchInQuantifierDict(char[] charArray, int begin, int length);

    /**
     * 从已匹配的Hit中直接取出DictSegment，继续向下匹配.
     * @param charArray
     * @param currentIndex
     * @param matchedHit
     * @return 匹配结果描述对象Hit.
     */
    Hit matchWithHit(char[] charArray, int currentIndex, Hit matchedHit);

    /**
     * 判断是否是停用词.
     * @param charArray
     * @return
     */
    boolean isStopWord(char[] charArray);

    /**
     * 判断是否是停用词.
     * @param charArray
     * @param begin
     * @param length
     * @return
     */
    boolean isStopWord(char[] charArray, int begin, int length);
}
