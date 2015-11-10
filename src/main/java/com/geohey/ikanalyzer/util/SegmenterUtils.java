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

import com.geohey.ikanalyzer.core.IKSegmenter;
import com.geohey.ikanalyzer.core.Lexeme;
import com.geohey.ikanalyzer.dic.Dictionary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 分词器工具集.
 *
 * @author Jingyi Yu.
 */
public final class SegmenterUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(SegmenterUtils.class);

    private SegmenterUtils() {
    }

    public static List<String> splitWordsToList(String text, boolean useSmart) {

        StringReader reader = new StringReader(text);
        IKSegmenter ik = new IKSegmenter(reader, useSmart);
        try {
            List<String> list = new ArrayList<>();

            Lexeme lexeme = null;
            while ((lexeme = ik.next()) != null) {
                list.add(lexeme.getLexemeText());
            }

            return list;
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }

        return null;
    }

    public static void disableWords(InputStream is) {
        if (is != null) {

            Collection<String> disableWords = new ArrayList<>();
            BufferedReader reader = null;

            try {
                reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String word = reader.readLine();
                while (word != null) {
                    disableWords.add(word);
                    word = reader.readLine();
                }

            } catch (Exception e) {
                LOGGER.error("读取禁止词异常", e);
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (Exception e) {
                }
            }

            Dictionary.getSingleton().disableWords(disableWords);
        }
    }

    public static List<String> splitCharsToList(String text) {

        List<String> chars = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            String str = text.substring(i, i + 1);
            if (str != null && !"".equals(str.trim())) {
                chars.add(str);
            }
        }

        return chars;
    }
}
