package test;

import javafx.util.Pair;

import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
//        String input = "abcdefgabc";
//        String t = ".$|()[{^?*+\\";
//        String kw = "ab";
//        StringBuilder sb = new StringBuilder();
//        for (char c : kw.toCharArray()) {
//            if (t.indexOf(String.valueOf(c)) != -1) {
//                sb.append("\\" + c);
//            } else {
//                sb.append(c);
//            }
//        }

        String text  = "There are moments in life when you miss someone so much thatpeoplfdsafdsfffafdasfdsfsadddddddddddddddddffsdasdfsafdsaaaaae you just want to pick them from your dreams and hug them for real! Dream what you want to dream;go where you want to go;be what you want to be,because you have only one life and one chance to do all the things you want to do.\n" +
                "\n" +
                "　　May you have enough happiness topeoplfdsafdsfffafdasfdsfsadddddddddddddddddffsdasdfsafdsaaaaae make you sweet,enough trials to make you strong,enough sorrow to keep you human,enough hope to make you happy? Always put yourself in others’shoes.If you feel that it hurts you,it probably hurts the other person, too.\n" +
                "\n" +
                "　　The happiest of people don’t necessarily have the best ofpeoplfdsafdsfffafdasfdsfsadddddddddddddddddffsdasdfsafdsaaaaae everything;they just make the most of everything that comes along their way.Happiness lies for those who cry,those who hurt, those who have searched,and those who have tried,for only they can appreciate the importance of people\n" +
                "\n" +
                "　　who have touched their lives.Lovepeoplfdsafdsfffafdasfdsfsadddddddddddddddddffsdasdfsafdsaaaaae begins with a smile,grows with a kiss and ends with a tear.The brightest future will always be based on a forgotten past, you can’t go on well in lifeuntil you let go of your past failures and heartaches.\n" +
                "\n" +
                "　　When you were born,you were crying and everyone around you was smiling.Live your life so that when you die,you're the one who is smiling and everyone around you is crying.\n" +
                "\n" +
                "　　Please send this messagepeoplfdsafdsfffafdasfdsfsadddddddddddddddddffsdasdfsafdsaaaaae to those people who mean something to you,to those who have touched your life in one way or another,to those who make you smile when you really need it,to those that make you see the brighter side of things when you are really down,to those who you want to let them know that you appreciate their friendship.And if you don’t, don’t worry,nothing bad will happen to you,you will just miss out on the opportunity to brighten someone’s day with this message.";
        System.out.println(text.length());
        String kw = "f";

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 5000; i++) {
            ArrayList<Pair<String, Boolean>> pairs = parseText2(text, kw, true);
        }

        long endTime = System.currentTimeMillis() - startTime;
        System.out.println("----");

        long startTime2 = System.currentTimeMillis();
        for (int i = 0; i < 5000; i++) {
            ArrayList<Pair<String, Boolean>> pairs2 = parseText(text, kw, true);
        }
        long endTime2 = System.currentTimeMillis() - startTime2;


        System.out.println("方法parseText: "+endTime);
        System.out.println("方法updateText: "+endTime2);

    }

    /**
     * 此算法私有,用于测试和比较性能
     * @param text       内容
     * @param keywords   关键字
     * @param ignoreCase 是否忽略大小写
     * @return
     */
    private static ArrayList<Pair<String, Boolean>> parseText2(String text, String keywords, boolean ignoreCase) {
        ArrayList<Pair<String, Boolean>> list = new ArrayList<>();
        if (text.isEmpty() || keywords.isEmpty() || (!ignoreCase && !text.contains(keywords)) || (ignoreCase && !text.toUpperCase().contains(keywords.toUpperCase()))) {
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

    private static ArrayList<Pair<String, Boolean>> parseText(String text, String keywords, boolean ignoreCase) {
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
            int startPos = textTemp.indexOf(kwTemp, start);
            int endPos = (startPos==-1)?-1:startPos+kwLen;
            // 如果没有查找到关键字
            if (startPos == -1 || endPos == -1) {
                if (textLen > start) {
                    // 没有找到,但是还有剩下的文字,还是加入到list
                    list.add(new Pair<>(text.substring(start), false));
                    break;
                }
            }
            // 第一个字符串,如果不为空,就添加到list.
            String subStr = text.substring(start, startPos);
            if(!subStr.isEmpty()){
                list.add(new Pair<>(subStr, false));
            }
            // 如果找到了关键字.那么添加到list
            String substring = text.substring(startPos, endPos);
            if(substring.isEmpty()){
                System.out.println("substring.isEmpty() = " + substring.isEmpty());
            }
            list.add(new Pair<>(substring, true));
            start = endPos;
        }
        return list;
    }
}
