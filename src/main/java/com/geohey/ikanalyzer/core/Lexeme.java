package com.geohey.ikanalyzer.core;

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
 * IK的词元对象.
 */
public class Lexeme {

    // lexemeType常量
    /** 未知  */
    public static final int TYPE_UNKNOWN = 0;
    /** 英文  */
    public static final int TYPE_ENGLISH = 1;
    /** 数字  */
    public static final int TYPE_ARABIC = 2;
    /** 英文数字混合  */
    public static final int TYPE_LETTER = 3;
    /** 中文词元  */
    public static final int TYPE_CNWORD = 4;
    /** 中文单字  */
    public static final int TYPE_CNCHAR = 64;
    /** 日韩文字  */
    public static final int TYPE_OTHER_CJK = 8;
    /** 中文数词  */
    public static final int TYPE_CNUM = 16;
    /** 中文量词  */
    public static final int TYPE_COUNT = 32;
    /** 中文数量词  */
    public static final int TYPE_CQUAN = 48;

    /**
     * 词元的起始位移.
     */
    private int offset;
    /**
     * 词元的相对起始位置.
     */
    private int begin;
    /**
     * 词元的长度.
     */
    private int length;
    /**
     * 词元文本.
     */
    private String lexemeText;
    /**
     * 词元类型.
     */
    private int lexemeType;

    public Lexeme(int offset, int begin, int length, int lexemeType) {
        this.offset = offset;
        this.begin = begin;
        if (length < 0) {
            throw new IllegalArgumentException("length < 0");
        }

        this.length = length;
        this.lexemeType = lexemeType;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        if (length < 0) {
            throw new IllegalArgumentException("length < 0");
        }
        this.length = length;
    }

    public String getLexemeText() {
        return (lexemeText == null) ? "" : lexemeText;
    }

    public void setLexemeText(String lexemeText) {

        if(lexemeText == null) {
            this.lexemeText = "";
            this.length = 0;
        } else {
            this.lexemeText = lexemeText;
            this.length = lexemeText.length();
        }
    }

    public int getLexemeType() {
        return lexemeType;
    }

    public String getLexemeTypeString() {
        switch(lexemeType) {

            case TYPE_ENGLISH :
                return "ENGLISH";

            case TYPE_ARABIC :
                return "ARABIC";

            case TYPE_LETTER :
                return "LETTER";

            case TYPE_CNWORD :
                return "CN_WORD";

            case TYPE_CNCHAR :
                return "CN_CHAR";

            case TYPE_OTHER_CJK :
                return "OTHER_CJK";

            case TYPE_COUNT :
                return "COUNT";

            case TYPE_CNUM :
                return "TYPE_CNUM";

            case TYPE_CQUAN:
                return "TYPE_CQUAN";

            default :
                return "UNKONW";
        }
    }

    public void setLexemeType(int lexemeType) {
        this.length = lexemeType;
    }

    /**
     * @return 获取词元在文本中的起始位置.
     */
    public int getBeginPosition() {
        return offset + begin;
    }

    /**
     * @return 获取词元在文本中的结束位置.
     */
    public int getEndPosition() {
        return offset + begin + length;
    }

    /**
     * 合并两个相邻的词元.
     * @param lexeme
     * @param lexemeType
     * @return 合并成功返回true，否则返回false.
     */
    public boolean append(Lexeme lexeme, int lexemeType) {
        if (lexeme != null && this.getEndPosition() == lexeme.getBeginPosition()) {
            this.length += lexeme.getLength();
            this.lexemeType = lexemeType;
            return true;
        }

        return false;
    }

    /**
     * 词元在排序集合中的比较算法.
     * @see {@link Comparable#compareTo(Object)}
     * @param other
     * @return
     */
    public int compareTo(Lexeme other) {

        // 起始位置优先
        if (this.begin == other.getBegin()) {

            // 词元长度优先
            if (this.length > other.getLength()) {
                return -1;
            } else if (this.length == other.getLength()) {
                return 0;
            } else {
                return 1;
            }

        } else if (this.begin < other.getBegin()) {
            return -1;
        } else {    // this.begin > other.getBegin()
            return 1;
        }
    }

    /**
     * 判断词元是否相等.
     * 判断依据：起始位置偏移、起始位置、及终止位置.
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {

        if (o == null) {
            return false;
        }

        if (this == o) {
            return true;
        }

        if (o instanceof Lexeme) {
            Lexeme other = (Lexeme) o;
            return (this.offset == other.getOffset() &&
                    this.begin == other.getBegin() &&
                    this.length == other.getLength());
        }

        return false;
    }

    /**
     * @return 词元哈希编码.
     */
    @Override
    public int hashCode() {
        int absBegin = getBeginPosition();
        int absEnd = getEndPosition();
        return (absBegin * 37) + (absEnd * 31) + ((absBegin * absEnd) % getLength()) * 11;
    }

    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getBeginPosition())
                     .append("-")
                     .append(this.getEndPosition())
                     .append(" : ")
                     .append(this.lexemeText)
                     .append(" : \t")
                     .append(this.getLexemeTypeString());

        return stringBuilder.toString();
    }
}
