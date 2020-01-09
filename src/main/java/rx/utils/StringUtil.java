package rx.utils;

import javafx.util.Pair;

import java.util.ArrayList;

public class StringUtil {
    /**
     * 分解字符串的方法
     * abcdABcd 查找"aB" ,忽略大小写,那么可以分解成 [ab] cd [AB] cd
     * @param text       内容
     * @param keywords   关键字
     * @param ignoreCase 是否忽略大小写 true 忽略, false 不忽略
     * @return
     */
    public static ArrayList<Pair<String, Boolean>> parseText(String text, String keywords, boolean ignoreCase) {
        ArrayList<Pair<String, Boolean>> list = new ArrayList<>();
        if (text == null || text.isEmpty() || keywords == null || keywords.isEmpty()||(!ignoreCase && !text.contains(keywords)) || (ignoreCase && !text.toUpperCase().contains(keywords.toUpperCase()))) {
            list.add(new Pair<String, Boolean>(text, false));
            return list;
        }
        String textTemp = text;
        String kwTemp = keywords;
        if(ignoreCase){
            textTemp = text.toUpperCase();
            kwTemp = keywords.toUpperCase();
        }
        int start = 0;
        int kwLen = keywords.length();
        int textLen = text.length();
        while (start != -1 && start < textLen) {
            int startIndex = textTemp.indexOf(kwTemp, start);
            int endIndex = (startIndex==-1)?-1:startIndex+kwLen;
            // 如果没有查找到关键字
            if (startIndex == -1 || endIndex == -1) {
                if (textLen > start) {
                    // 没有找到,但是还有剩下的文字,还是加入到list
                    list.add(new Pair<>(text.substring(start), false));
                    break;
                }
            }
            // 第一个字符串,如果不为空,就添加到list.
            String subStr = text.substring(start, startIndex);
            if(!subStr.isEmpty()){
                list.add(new Pair<>(subStr, false));
            }
            // 如果找到了关键字.那么添加到list
            list.add(new Pair<>(text.substring(startIndex, endIndex), true));
            start = endIndex;
        }
        return list;
    }

    /**
     * 分解字符串的方法2: 私有方法,用于和上面的方法比较速度用的
     * @param text       内容
     * @param keywords   关键字
     * @param ignoreCase 是否忽略大小写
     * @return
     */
    private static ArrayList<Pair<String, Boolean>> parseText2(String text, String keywords, boolean ignoreCase) {
        ArrayList<Pair<String, Boolean>> list = new ArrayList<>();
        if (text==null||keywords==null||text.isEmpty() || keywords.isEmpty() || (!ignoreCase && !text.contains(keywords)) || (ignoreCase && !text.toUpperCase().contains(keywords.toUpperCase()))) {
            list.add(new Pair<String, Boolean>(text, false));
            return list;
        }
        String textTemp = text;
        String keywordsTemp = keywords;
        if (ignoreCase) {
            textTemp = text.toUpperCase();
            keywordsTemp = keywords.toUpperCase();
        }
        int fromIndex = 0;//搜索的初始下标
        int len = keywordsTemp.length();//关键字长度
        ArrayList<Integer> kwList = new ArrayList<>();
        kwList.add(0);
        boolean flag = false;
        while ((fromIndex = textTemp.indexOf(keywordsTemp, fromIndex)) != -1) {
            if (fromIndex != 0) {
                kwList.add(fromIndex);
            } else {
                flag = true;
            }
            kwList.add(fromIndex + len);
            fromIndex += len;//如果查找到了,那么把搜索位置往后挪动
        }
        kwList.add(textTemp.length());
        for (int i = 0; i < kwList.size() - 1; i++) {
            String t = text.substring(kwList.get(i), kwList.get(i + 1));
            if (!t.isEmpty()) {
                list.add(new Pair<>(t, flag));
            }
            flag = !flag;
        }
        return list;
    }
}
