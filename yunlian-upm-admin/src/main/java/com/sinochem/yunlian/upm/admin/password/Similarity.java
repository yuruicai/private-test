package com.sinochem.yunlian.upm.admin.password;


/**
 * 根据Levenshtein计算字符串相似度
 */
public class Similarity {

    private static int min(int one, int two, int three) {
        int min = one;
        if (two < min) {
            min = two;
        }
        if (three < min) {
            min = three;
        }
        return min;
    }

    public static int ld(String str1, String str2) {
        int d[][]; // 矩阵
        int n = str1.length();
        int m = str2.length();
        int i; // 遍历str1的
        int j; // 遍历str2的
        char ch1; // str1的
        char ch2; // str2的
        int temp; // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        d = new int[n + 1][m + 1];
        for (i = 0; i <= n; i++) { // 初始化第一列
            d[i][0] = i;
        }
        for (j = 0; j <= m; j++) { // 初始化第一行
            d[0][j] = j;
        }
        for (i = 1; i <= n; i++) { // 遍历str1
            ch1 = str1.charAt(i - 1);
            // 去匹配str2
            for (j = 1; j <= m; j++) {
                ch2 = str2.charAt(j - 1);
                if (ch1 == ch2) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                // 左边+1,上边+1, 左上角+temp取最小
                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
            }
        }
        return d[n][m];
    }

    public static int ld2(String str1, String str2) {

        if (str1.length() > str2.length()) {
            String tem = str2;
            str2 = str1;
            str1 = tem;
        }
        int common = 0;
        char[] s1Arr = str1.toCharArray();
        char[] s2Arr = str2.toCharArray();
        for (char c : s1Arr) {
            for (char c2 : s2Arr) {
                if (c == c2) {
                    common++;
                    break;
                }
            }
        }
        return common;
    }

    public static float sim(String str1, String str2) {
        if (str1 == null || str1.isEmpty() || str2 == null || str2.isEmpty()) {
            return 0;
        }
        int ld = ld(str1, str2);
        int max = Math.max(str1.length(), str2.length());
        return 1 - (float) ld / (max == 0 ? 1 : max);
    }

    public static float sim2(String str1, String str2, boolean useMax) {
        if (str1 == null || str1.isEmpty() || str2 == null || str2.isEmpty()) {
            return 0;
        }
        int ld = ld2(str1, str2);
        float max = 0;
        if (useMax) {
            max = Math.max(str1.length(), str2.length());
        } else {
            max = (str1.length() + str2.length()) / 2;
        }
        return ld / max;
    }

    public static void main(String[] args) {
        System.out.println(sim("zhangxi", "xzh23ang423xi"));
        System.out.println(sim2("zhangxi", "xzh23ang423xi", true));
        System.out.println(sim2("zhangxi", "xzh23ang423xi", false));

        System.out.println(sim("zhangxi", "xzh23ang423xi2342"));
        System.out.println(sim2("zhangxi", "xzh23ang423xi2342", true));
        System.out.println(sim2("zhangxi", "xzh23ang423xi2342", false));

        System.out.println(sim("zhangxi", "zh1wdv32hang3lo01423xi"));
        System.out.println(sim2("zhangxi", "zh1wdv32hang3lo01423xi", true));
        System.out.println(sim2("zhangxi", "zh1wdv32hang3lo01423xi", false));
    }

    private static String comSubstring(String str1, String str2) {
        String[] aA = str1.split("-|/");
        str1 = "";
        for (String at : aA) {
            if (at.length() > 4)
                str1 = str1 + at;
        }

        aA = str2.split("-|/");
        str2 = "";
        for (String at : aA) {
            if (at.length() > 4)
                str2 = str2 + at;
        }

        String minStr = "";
        String maxStr = "";
        if (str1.length() != str2.length()) {
            minStr = str1.length() >= str2.length() ? str2 : str1;
            maxStr = str1.length() <= str2.length() ? str2 : str1;
        } else {
            minStr = str1;
            maxStr = str2;
        }
        String result = "";

        for (int i = 0; i < minStr.length(); i++) {
            for (int j = i + 1; j < minStr.length(); j++) {
                String subStr = minStr.substring(i, j);
                if (maxStr.indexOf(subStr) > -1) {
                    if (result.length() < subStr.length())
                        result = subStr;
                } else {
                    break;
                }
            }
        }
        return result;
    }
}
