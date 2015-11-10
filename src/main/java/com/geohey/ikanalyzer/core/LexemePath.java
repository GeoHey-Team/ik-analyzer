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
 * Lexeme链(路径).
 *
 * @author Jingyi Yu.
 * @author Liangyi Lin.
 */
public class LexemePath extends QuickSortSet implements Comparable<LexemePath> {

    /**
     * 起始位置.
     */
    private int pathBegin;

    /**
     * 结束位置.
     */
    private int pathEnd;

    /**
     * 词元链的有效字符长度.
     */
    private int payloadLength;

    public LexemePath() {
        this.pathBegin = -1;
        this.pathEnd = -1;
        this.payloadLength = 0;
    }

    /**
     * 向LexemePath追加相交的Lexeme.
     * @param lexeme
     * @return
     */
    public boolean addCrossLexeme(Lexeme lexeme) {

        if (this.isEmpty()) {
            this.addLexeme(lexeme);
            this.pathBegin = lexeme.getBegin();
            this.pathEnd = lexeme.getBegin() + lexeme.getLength();
            this.payloadLength += lexeme.getLength();

            return true;
        } else if (this.checkCross(lexeme)) {
            this.addLexeme(lexeme);
            if (lexeme.getBegin() + lexeme.getLength() > this.pathEnd) {
                this.pathEnd = lexeme.getBegin() + lexeme.getLength();
            }
            this.payloadLength = this.pathEnd - this.pathBegin;
            return true;
        } else {
            return false;
        }
    }

    /**
     * 向LexemePath追加不相交的Lexeme
     * @param lexeme
     * @return
     */
    boolean addNotCrossLexeme(Lexeme lexeme){
        if(this.isEmpty()){
            this.addLexeme(lexeme);
            this.pathBegin = lexeme.getBegin();
            this.pathEnd = lexeme.getBegin() + lexeme.getLength();
            this.payloadLength += lexeme.getLength();

            return true;
        }else if(this.checkCross(lexeme)){
            return  false;
        }else{
            this.addLexeme(lexeme);
            this.payloadLength += lexeme.getLength();

            Lexeme head = this.peekFirst();
            this.pathBegin = head.getBegin();

            Lexeme tail = this.peekLast();
            this.pathEnd = tail.getBegin() + tail.getLength();

            return true;

        }
    }

    /**
     * 移除尾部的Lexeme.
     * @return
     */
    public Lexeme removeTail() {
        Lexeme tail = this.pollLast();

        if (this.isEmpty()) {
            this.pathBegin = -1;
            this.pathEnd = -1;
            this.payloadLength = 0;
        } else {
            this.payloadLength -= tail.getLength();
            Lexeme newTail = this.peekLast();
            this.pathEnd = newTail.getBegin() + newTail.getLength();
        }

        return tail;
    }

    /**
     * 检测词元位置是否交叉(有歧义切分).
     * @param lexeme
     * @return
     */
    public boolean checkCross(Lexeme lexeme) {
        return (lexeme.getBegin() >= this.pathBegin && lexeme.getBegin() < this.pathEnd) ||
                (this.pathBegin >= lexeme.getBegin() && this.pathBegin < lexeme.getBegin() + lexeme.getLength());
    }

    public int getPathBegin() {
        return pathBegin;
    }

    public int getPathEnd() {
        return pathEnd;
    }

    public int getPayloadLength() {
        return payloadLength;
    }

    /**
     * @return 获取LexemePath的路径长度.
     */
    public int getPathLength() {
        return this.pathEnd - this.pathBegin;
    }

    /**
     * @return  X权重(词元长度积).
     */
    public int getXWeight() {
        int product = 1;
        Cell cell = this.getHead();

        while (cell != null && cell.getLexeme() != null) {
            product *= cell.getLexeme().getLength();
            cell = cell.getNext();
        }

        return product;
    }

    /**
     * @return  词元位置权重.
     */
    public int getPWeight() {
        int pWeight = 0;
        int p = 0;
        Cell cell = this.getHead();

        while (cell != null && cell.getLexeme() != null) {
            p++;
            pWeight += (p * cell.getLexeme().getLength());
            cell = cell.getNext();
        }

        return pWeight;
    }

    /**
     * 复制一个LexemePath对象.
     * @return
     */
    public LexemePath copy() {
        LexemePath theCopy = new LexemePath();
        theCopy.pathBegin = this.pathBegin;
        theCopy.pathEnd = this.pathEnd;
        theCopy.payloadLength = this.payloadLength;

        Cell cell = this.getHead();
        while (cell != null && cell.getLexeme() != null) {
            theCopy.addLexeme(cell.getLexeme());
            cell = cell.getNext();
        }

        return theCopy;
    }

    @Override
    public int compareTo(LexemePath o) {

        // 比较有效文本长度
        if (this.payloadLength > o.payloadLength) {
            return -1;
        } else if (this.payloadLength < o.payloadLength) {
            return 1;
        }

        // 有效文本长度相等，比较词元个数，越少越好
        if (this.size() < o.size()) {
            return -1;
        } else if (this.size() > o.size()) {
            return 1;
        }

        // 词元个数相等，路径跨度进行比较
        if (this.getPathLength() > o.getPathLength()) {
            return -1;
        } else if (this.getPathLength() < o.getPathLength()) {
            return 1;
        }

        // 根据统计学结论，逆向分词概率高于正向切分，因此位置越靠后的优先
        if (this.pathEnd > o.pathEnd) {
            return -1;
        } else if (this.pathEnd < o.pathEnd) {
            return 1;
        }

        // 词长越平均越好
        if (this.getXWeight() > o.getXWeight()) {
            return -1;
        } else if (this.getXWeight() < o.getXWeight()) {
            return 1;
        }

        // 词元位置权重比较
        if (this.getPWeight() > o.getPWeight()) {
            return -1;
        } else if (this.getPWeight() < o.getPWeight()) {
            return 1;
        }

        // 以上要素相等，则返回0.
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("pathBegin : ")
                  .append(pathBegin)
                  .append("\r\n")
                  .append("pathEnd : ")
                  .append(pathEnd)
                  .append("\r\n")
                  .append("payloadLength : ")
                  .append(payloadLength)
                  .append("\r\n");

        Cell head = this.getHead();
        while (head != null) {
            strBuilder.append("lexeme : ")
                      .append(head.getLexeme())
                      .append("\r\n");
            head = head.getNext();
        }

        return strBuilder.toString();
    }
}
