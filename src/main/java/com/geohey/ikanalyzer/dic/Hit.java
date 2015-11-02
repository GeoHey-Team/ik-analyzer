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

/**
 * 表示词典匹配命中.
 *
 * @author Liangyi Lin.
 *
 * @version 2012.
 */
public class Hit {

    /**
     * Hit不匹配.
     */
    private final static int UNMATCH = 0x00000000;

    /**
     * Hit完全匹配.
     */
    private final static int MATCH = 0x00000001;

    /**
     * Hit前缀匹配.
     */
    private final static int PREFIX = 0x00000010;

    /**
     * 该HIT当前状态，默认未匹配.
     */
    private int hitState = UNMATCH;

    /**
     * 记录词典匹配过程中，当前匹配到的词典分支节点.
     */
    private DictSegment matchedDictSegment;

    /*
	 * 词段开始位置.
	 */
    private int begin;

    /*
	 * 词段的结束位置.
	 */
    private int end;

    /**
     * 判断是否完全匹配.
     */
    public boolean isMatch() {
        return (this.hitState & MATCH) > 0;
    }

    /**
     * 设置当前状态为匹配.
     */
    public void setMatch() {
        this.hitState = this.hitState | MATCH;
    }

    /**
     * 判断是否是词的前缀.
     */
    public boolean isPrefix() {
        return (this.hitState & PREFIX) > 0;
    }

    /**
     * 设置当前状态为词的前缀.
     */
    public void setPrefix() {
        this.hitState = this.hitState | PREFIX;
    }

    /**
     * 判断是否是不匹配.
     */
    public boolean isUnmatch() {
        return this.hitState == UNMATCH ;
    }

    /**
     * 设置当前状态为不匹配.
     */
    public void setUnmatch() {
        this.hitState = UNMATCH;
    }

    public DictSegment getMatchedDictSegment() {
        return matchedDictSegment;
    }

    public void setMatchedDictSegment(DictSegment matchedDictSegment) {
        this.matchedDictSegment = matchedDictSegment;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
