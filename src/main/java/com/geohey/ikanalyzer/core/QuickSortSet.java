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
 * IK分词器专用的Lexeme快速排序集合.
 *
 * @author Jingyi Yu.
 * @author Liangyi Lin.
 */
public class QuickSortSet {

    /**
     * 链表头.
     */
    private Cell head;

    /**
     * 链表尾.
     */
    private Cell tail;

    /**
     * 链表的实际大小.
     */
    private int size;

    /**
     * 向链表集合添加词元.
     * @param lexeme
     */
    public boolean addLexeme(Lexeme lexeme) {

        Cell newCell = new Cell(lexeme);
        if (this.size == 0) {
            this.head = newCell;
            this.tail = newCell;
            this.size++;

            return true;
        } else {

            // 词元与尾部词元相同，不放入集合
            if (this.tail.compareTo(newCell) == 0) {
                return false;
            } else if (this.tail.compareTo(newCell) < 0) {
                this.tail.next = newCell;
                newCell.prev = this.tail;
                this.size++;

                return true;
            } else if (this.head.compareTo(newCell) > 0) {
                this.head.prev = newCell;
                newCell.next = this.head;
                this.head = newCell;

                return true;
            } else {
                // 从尾部上逆
                Cell index = this.tail;
                while (index != null && index.compareTo(newCell) > 0) {
                    index = index.prev;
                }

                // 感觉Lin写的这一段有问题
                // 词元与集合中词元重复，不放入集合
                if (index.compareTo(newCell) == 0) {
                    return false;
                } else if (index.compareTo(newCell) < 0) {
                    newCell.prev = index;
                    newCell.next = index.next;
                    index.next.prev = newCell;
                    index.next = newCell;
                    this.size++;

                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @return 链表头部元素.
     */
    public Lexeme peekFirst() {
        return (this.head != null) ? this.head.lexeme : null;
    }

    /**
     * 取出链表的第一个元素.
     * @return 第一个元素.
     */
    public Lexeme pollFirst() {
        if (this.size > 0) {
            Lexeme first = this.head.lexeme;

            if (this.size > 1) {
                this.head = this.head.next;
            } else {
                this.head = null;
                this.tail = null;
            }

            this.size--;
            return first;
        }

        return null;
    }

    /**
     * @return 链表尾部元素.
     */
    public Lexeme peekLast() {
        return (this.tail != null) ? this.tail.lexeme : null;
    }

    /**
     * 取出链表的最后一个元素.
     * @return 最后一个元素.
     */
    public Lexeme pollLast() {
        if (this.size > 0) {
            Lexeme last = this.head.lexeme;

            if (this.size > 1) {
                this.tail = this.tail.prev;
            } else {
                this.head = null;
                this.tail = null;
            }

            this.size--;
            return last;
        }

        return null;
    }

    public int size() {
        return this.size();
    }

    /**
     * 判断集合是否为空.
     * @return
     */
    boolean isEmpty(){
        return this.size == 0;
    }

    /**
     * @return 返回lexeme链的头部.
     */
    Cell getHead(){
        return this.head;
    }

    /**
     * {@link QuickSortSet}集合单元.
     */
    class Cell implements Comparable<Cell> {

        private Cell prev;
        private Cell next;
        private Lexeme lexeme;

        public Cell (Lexeme lexeme) {
            if (lexeme == null) {
                throw new IllegalArgumentException("lexeme must not be null");
            }
            this.lexeme = lexeme;
        }

        @Override
        public int compareTo(Cell o) {
            return this.lexeme.compareTo(o.lexeme);
        }

        public Cell getPrev() {
            return prev;
        }

        public Cell getNext() {
            return next;
        }

        public Lexeme getLexeme() {
            return lexeme;
        }
    }
}
