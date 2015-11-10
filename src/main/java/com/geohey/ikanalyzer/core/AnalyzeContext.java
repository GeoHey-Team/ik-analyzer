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

import com.geohey.ikanalyzer.cfg.Configuration;
import com.geohey.ikanalyzer.util.CharacterUtils;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * 分词器上下文分词状态.
 *
 * @author Liangyi Lin.
 */
public class AnalyzerContext {

    /**
     * 默认缓冲区大小
     */
    private static final int BUFF_SIZE = 4096;

    /**
     * 缓冲区耗尽的临界值.
     */
    private static final int BUFF_EXHAUST_CRITICAL = 100;

    /**
     * 字符串读取缓冲.
     */
    private char[] segementBuff;

    /**
     * 字符类型数组.
     */
    private int[] charTypes;

    /**
     * 记录Reader内已分析的字符串总长度.
     * 在分多段分析词元时，该变量累积当前的segmentBuff相对于reader起始位置的位移.
     */
    private int buffOffset;

    /**
     * 当前缓冲区位置指针.
     */
    private int cursor;

    /**
     * 最近一次读入的，可处理的字符串长度.
     */
    private int available;

    /**
     * 子分词器锁. 该集合非空，说明有子分词器在占用segmentBuff.
     */
    private Set<String> buffLocker;


    /**
     * 原始分词结果集合，未经歧义处理
     */
    private QuickSortSet orgLexemes;

    /**
     * LexemePath位置索引表
     */
    private Map<Integer, LexemePath> pathMap;

    /**
     * 最终分词结果.
     */
    private LinkedList<Lexeme> results;


    /**
     * 分词器配置项.
     */
    private Configuration cfg;

    public AnalyzerContext(Configuration cfg) {
        this.cfg = cfg;
        this.segementBuff = new char[BUFF_SIZE];
        this.charTypes = new int[BUFF_SIZE];

        this.buffLocker = new HashSet<>();
        this.orgLexemes = new QuickSortSet();
        this.pathMap = new HashMap<>();
        this.results = new LinkedList<Lexeme>();
    }

    int getCursor() {
        return this.cursor;
    }

    char[] getSegementBuff() {
        return this.segementBuff;
    }

    char getCurrentChar() {
        return this.segementBuff[this.cursor];
    }

    int getCurrentCharType() {
        return this.charTypes[this.cursor];
    }

    int getBufferOffset() {
        return this.buffOffset;
    }

    /**
     * 根据context上下文情况，填充segmentBuff.
     * @param reader
     * @return
     * @throws IOException
     */
    public int fillBuffer(Reader reader) throws IOException {

        int readCount = 0;

        if (buffOffset == 0) {
            // 首次读取reader
            readCount = reader.read(segementBuff);
        } else {
            int offset = this.available - this.cursor;
            //  最近一次读取的 > 最近一次处理的，将未处理的字符串拷贝到segmentBuff头部.
            if (offset > 0) {
                System.arraycopy(segementBuff, cursor, segementBuff, 0, offset);
                readCount = offset;
            }

            // 继续读取reader，以onceReadIn - onceAnalyzed为起始位置，继续填充segmentBuff剩余部分
            readCount += reader.read(segementBuff, offset, BUFF_SIZE - offset);
        }

        // 更新最后一次从Reader中读入的可用字符串长度
        // & 重置当前指针
        this.available = readCount;
        this.cursor = 0;

        return readCount;
    }

    /**
     * 初始化buff指针，处理第一个字符.
     */
    public void initCursor() {
        cursor = 0;
        segementBuff[cursor] = CharacterUtils.regularize(segementBuff[cursor]);
        charTypes[cursor] = CharacterUtils.identifyCharType(segementBuff[cursor]);
    }

    /**
     * 指针+1
     * @return 成功返回true；如果指针已经到了buff尾部，不能继续前进，返回false.
     */
    public boolean moveCursor() {
        // 并未移动到尾部
        if (this.cursor < this.available - 1) {
            cursor++;
            segementBuff[cursor] = CharacterUtils.regularize(segementBuff[cursor]);
            charTypes[cursor] = CharacterUtils.identifyCharType(segementBuff[cursor]);

            return true;
        }

        return false;
    }

    /**
     * 设置当前segmentBuff为锁定状态. 加入占有segmentBuff的子分词器名称，
     * 表示占用segmentBuff.
     *
     * @param segmenterName 子分词器的名称.
     */
    public void lockBuffer(String segmenterName) {
        buffLocker.add(segmenterName);
    }

    /**
     * 移除指定子分词器名，释放对segmentBuff的占用.
     *
     * @param segmenterName 子分词器的名称.
     */
    public void unlockBuffer(String segmenterName) {
        buffLocker.remove(segmenterName);
    }

    /**
     * 只要bufferLocker中存在segmenterName，则表明buffer被锁住.
     * @return
     */
    public boolean isBufferLocked() {
        return buffLocker.size() > 0;
    }

    /**
     * 判断当前segmentBuff是否已经用完.
     * 判断标准：当前游标cursor移至segmentBuff末端（available - 1）.
     * @return
     */
    public boolean isBufferConsumed() {
        return cursor == (available - 1) ;
    }

    /**
     * 判断segmentBuff是否需要读取新数据.
     * 满足以下条件时：
     *  √ available == BUFF_SIZE 表示buffer满载.
     *  √ 游标处于临界区内: buffIndex < (available - 1) &&  buffIndex > (available - BUFF_EXHAUST_CRITICAL)
     *  √ 没有segmenter在占用buffer: isBufferLocked()
     *
     * 要中断当前循环(buffer要进行移位，并再读取数据的操作).
     *
     * @return
     */
    public boolean needRefillBuffer() {
        return available == BUFF_SIZE &&
               cursor < (available - 1) &&
               cursor > (available - BUFF_EXHAUST_CRITICAL) &&
               !isBufferLocked();
    }

    /**
     * 累计当前的segmentBuff相对于reader起始位置的位移.
     */
    public void markBufferOffset() {
        buffOffset += cursor;
    }

    /**
     * 向分词结果集添加词元.
     * @param lexeme
     */
    public void addLexme(Lexeme lexeme) {
        orgLexemes.addLexeme(lexeme);
    }

    /**
     * 添加分词结果路径.路径起始位置 ---> 路径 映射表.
     * @param path
     */
    public void addLexmePath(LexemePath path) {
        if (path != null) {
            pathMap.put(path.getPathBegin(), path);
        }
    }

    /**
     * @return  原始分词结果.
     */
    public QuickSortSet getOrgLexemes() {
        return orgLexemes;
    }

    /**
     * 推送分词结果到结果集合：
     * 1. 从buff头部遍历到this.cursor已处理位置;
     * 2. 将map中存在的分词结果推入results;
     * 3. 将map中不存在的CJDK字符以单字推入results.
     */
    public void outputResult() {

        for (int index = 0; index <= cursor; index++) {

            // 跳过非CJK字符
            if (CharacterUtils.CHAR_USELESS == charTypes[index]) {
                continue;
            }

            // 从pathMap找出对应index位置的LexmePath
            LexemePath path = pathMap.get(index);
            if (path != null) {
                // 输出LexmePath中的lexeme到results集合
                Lexeme l = path.pollFirst();
                while (l != null) {
                    results.add(l);

                    // 将index移至lexeme后
                    index = l.getBegin() + l.getLength();
                    l = path.pollFirst();
                }
            } else {
                // pathMap中找不到index对应的LexmePath，单字输出.
                outputSingleCJK(index);
            }
        }

        // 清空当前的Map.
        pathMap.clear();
    }

    /**
     * 对CJK字符进行单字输出.
     * @param index
     */
    public void outputSingleCJK(int index) {
        if (CharacterUtils.CHAR_CHINESE == charTypes[index]) {
            Lexeme singleCharLexeme = new Lexeme(buffOffset, index, 1, Lexeme.TYPE_CNCHAR);
            results.add(singleCharLexeme);
        } else if (CharacterUtils.CHAR_OTHER_CJK == charTypes[index]) {
            Lexeme singleCharLexeme = new Lexeme(this.buffOffset , index , 1 , Lexeme.TYPE_OTHER_CJK);
            this.results.add(singleCharLexeme);
        } else ;
    }


}
