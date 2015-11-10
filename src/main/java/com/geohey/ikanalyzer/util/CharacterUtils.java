package com.geohey.ikanalyzer.util;

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
 * 字符集识别工具类. (原始名称CharacterUtil)
 *
 * @author Liangyi Lin.
 */
public class CharacterUtils {

    public static final int CHAR_USELESS = 0;

    public static final int CHAR_ARABIC = 0X00000001;

    public static final int CHAR_ENGLISH = 0X00000002;

    public static final int CHAR_CHINESE = 0X00000003;

    public static final int CHAR_OTHER_CJK = 0X00000004;

    /**
     * 识别字符类型.
     * @param input
     * @return {@link CharacterUtils}定义的字符类型常量.
     */
    public static int identifyCharType(char input) {
        if (input >= '0' && input <= '9') {
            return CHAR_ARABIC;
        } else if ((input >= 'a' && input <= 'z') ||
                   (input >= 'A' && input <= 'Z')) {
            return CHAR_ENGLISH;
        } else {
            Character.UnicodeBlock ub = Character.UnicodeBlock.of(input);

            if(ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS ||
               ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS ||
               ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A){

                //目前已知的中文字符UTF-8集合
                return CHAR_CHINESE;

            }else if(ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS //全角数字字符和日韩字符
                    //韩文字符集
                    || ub == Character.UnicodeBlock.HANGUL_SYLLABLES
                    || ub == Character.UnicodeBlock.HANGUL_JAMO
                    || ub == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO
                    //日文字符集
                    || ub == Character.UnicodeBlock.HIRAGANA //平假名
                    || ub == Character.UnicodeBlock.KATAKANA //片假名
                    || ub == Character.UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS){
                return CHAR_OTHER_CJK;

            }
        }

        // 其他本框架暂时不做处理的字符类型
        return CHAR_USELESS;
    }

    public static char regularize(char input) {

        if (input == 12288) {
            input = (char) 32;
        } else if (input > 65280 && input < 65375) {
            input = (char) (input - 65248);
        } else if (input >= 'A' && input <= 'Z') {
            input += 32;
        }

        return input;
    }

}
