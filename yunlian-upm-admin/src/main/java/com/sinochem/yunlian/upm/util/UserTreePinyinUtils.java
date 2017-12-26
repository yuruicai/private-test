package com.sinochem.yunlian.upm.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 涉及拼音匹配问题
 * Created with IntelliJ IDEA.
 * User: john
 * Date: 13-5-26
 * Time: 下午6:23
 * To change this template use File | Settings | File Templates.
 */
public class UserTreePinyinUtils {

    static Logger logger = LoggerFactory.getLogger(UserTreePinyinUtils.class);

    /**
     * 是否匹配搜索, 支持汉字和拼音
     * @param keyWord 搜索关键词
     * @param str 要搜索的词
     * @return
     */
    public static boolean isMatch(String keyWord, String str) {
        if (StringUtils.isBlank(str) || StringUtils.isBlank(keyWord)) {
            return false;
        }
        keyWord = keyWord.toLowerCase();

        // 汉语搜素
        String[] strAry = new String[str.length()];
        for (int i = 0; i < str.length(); i++) {
            strAry[i] = String.valueOf(str.charAt(i));
        }
        boolean b1 = distinguish(keyWord.toCharArray(), 0, strAry, 0, 0);
        if (b1) {
            return b1;
        }

        // 拼音搜索. 可能有多音字
        List<String[]> pinyinAryList = getPinYinAryList(str);
        if (!CollectionUtils.isEmpty(pinyinAryList)) {
            for (String[] pinyinAry : pinyinAryList) {
                if (distinguish(keyWord.toCharArray(), 0, pinyinAry, 0, 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否匹配搜索, 支持汉字和拼音
     * @param keyWord 搜索关键词
     * @param initPinyinAryList 初始化的拼音数组列表
     * @param str 要搜索的词
     * @return
     */
    public static boolean isMatch(String keyWord, List<String[]> initPinyinAryList, String str) {
        if (StringUtils.isBlank(str) || StringUtils.isBlank(keyWord)) {
            return false;
        }
        keyWord = keyWord.toLowerCase();

        if (str.contains(keyWord)) {
            return true;
        }

        // 汉语搜素
        String[] strAry = new String[str.length()];
        for (int i = 0; i < str.length(); i++) {
            strAry[i] = String.valueOf(str.charAt(i));
        }
        boolean b1 = distinguish(keyWord.toCharArray(), 0, strAry, 0, 0);
        if (b1) {
            return b1;
        }

        // 拼音搜索. 可能有多音字

        List<String[]> pinyinAryList = initPinyinAryList;
        if (CollectionUtils.isEmpty(pinyinAryList)) {
            pinyinAryList = getPinYinAryList(str);
        }
        if (!CollectionUtils.isEmpty(pinyinAryList)) {
            for (String[] pinyinAry : pinyinAryList) {
                if (distinguish(keyWord.toCharArray(), 0, pinyinAry, 0, 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取汉语的拼音字符串, 不处理多音字, 随机取多音字的第一个拼音
     * @param chineseStr
     * @return
     */
    public static String getPinyinStrWithoutPolyphone(String chineseStr) {
        StringBuilder stringBuilder = new StringBuilder();
        if (StringUtils.isNotBlank(chineseStr)) {
            for (int i = 0; i < chineseStr.length(); i++) {
                char c = chineseStr.charAt(i);
                stringBuilder.append(concatOnePinyinString(c));
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 拼接一个汉字, 只处理汉字
     * @param c
     * @return
     */
    private static String concatOnePinyinString(char c) {
        String pinyinStr = "";
        if (Character.toString(c).matches("[\u4E00-\u9FA5]+")) {
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c);
            if ((pinyinArray != null) && (pinyinArray.length > 0)) {
                pinyinStr = pinyinArray[0];
            }
        }
        return pinyinStr;
    }

    public static List<String[]> getPinYinAryList(String inputString) {

        if (StringUtils.isBlank(inputString)) {
            return new LinkedList<String[]>();
        }

        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);

        char[] input = inputString.trim().toCharArray();
        List<List<String[]>> allPinyinList = new ArrayList<List<String[]>>();

        try {
            for (int i = 0; i < input.length; i++) {
                Set<String> oneCharSet = new LinkedHashSet<String>();
                if (Character.toString(input[i]).matches("[\u4E00-\u9FA5]+")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);

                    // 此处处理多个多音字
                    if (temp != null && temp.length > 0) {
                        oneCharSet.addAll(Arrays.asList(temp));
                    } else {
                        throw new RuntimeException("转换拼音出错");
                    }

                } else {
                    oneCharSet.add(Character.toString(input[i]));
                }

                // 放入列表数组, 方便后续计算
                List<String[]> oneCharList = new LinkedList<String[]>();
                allPinyinList.add(oneCharList);
                for (String oneChar : oneCharSet) {
                    oneCharList.add(new String[]{oneChar});
                }
            }

            // 递归实现拼接所有可能的字符串
            exchage(allPinyinList);
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            logger.error("转换拼音出错", e);
        }
        return allPinyinList.size() > 0 ? allPinyinList.get(0) : Collections.EMPTY_LIST;
    }

    /**
     * 将拼音字符串转化为 串起来的数组
     * @param allPinyinList
     */
    private static void exchage(List<List<String[]>> allPinyinList) {
        // 如果大于1个长度就使用递归的进行循环
        if (allPinyinList != null && allPinyinList.size() > 1) {
            List<String[]> firstStringList = allPinyinList.get(0);
            List<String[]> secondStringList = allPinyinList.get(1);

            // 取前两个数组进行自由组合, 组合后存入新的数组列表.
            List<String[]> newStringList = new ArrayList<String[]>();
            for (String[] firstStringAry : firstStringList) {
                for (String[] secondStringAry : secondStringList) {
                    String[] newStringAry = new String[firstStringAry.length + secondStringAry.length];
                    System.arraycopy(firstStringAry, 0, newStringAry, 0, firstStringAry.length);
                    System.arraycopy(secondStringAry, 0, newStringAry, firstStringAry.length, secondStringAry.length);
                    newStringList.add(newStringAry);
                }
            }

            // 使用新的数组列表代替合并的前两个列表
            allPinyinList.remove(0);
            allPinyinList.remove(0);
            allPinyinList.add(0, newStringList);

            // 递归循环直到所有数据都拼接
            exchage(allPinyinList);
        }
    }


    /**
     * 不完整拼音匹配算法，可用到汉字词组的自动完成
     * 拼音搜索匹配 huang hai yan  => huhy,hhy
     * 通过递归方法实现
     *
     * @param search 输入的拼音字母
     * @param pinyin 汉字拼音数组，通过pinyin4j获取汉字拼音 @see http://pinyin4j.sourceforge.net/
     * @return 匹配成功返回 true
     */
    public static boolean distinguish(char[] search, int searchIndex, String pinyin[], int wordIndex, int wordStart) {
        if (searchIndex == 0) {
            return search[0] == pinyin[0].charAt(0) &&//第一个必须匹配
                    (search.length == 1 || distinguish(search, 1, pinyin, 0, 1));//如果仅是1个字符，算匹配，否则从第二个字符开始比较
        }

        if (pinyin[wordIndex].length() > wordStart//判断不越界
                && search[searchIndex] == pinyin[wordIndex].charAt(wordStart)) {

            //判断匹配
            return searchIndex == search.length - 1 ? distinguish(search, pinyin, wordIndex)//如果这是最后一个字符，检查之前的声母是否依次出现
                    : distinguish(search, searchIndex + 1, pinyin, wordIndex, wordStart + 1);//同一个字拼音连续匹配
        } else if (pinyin.length > wordIndex + 1 //判断不越界
                && search[searchIndex] == pinyin[wordIndex + 1].charAt(0)) {

            //不能拼音连续匹配的情况下，看看下一个字拼音的首字母是否能匹配
            return searchIndex == search.length - 1 ? distinguish(search, pinyin, wordIndex) //如果这是最后一个字符，检查之前的声母是否依次出现
                    : distinguish(search, searchIndex + 1, pinyin, wordIndex + 1, 1);//去判断下一个字拼音的第二个字母
        } else if (pinyin.length > wordIndex + 1) {

            //回退试试看  对于zhuang an lan  searchIndex > 1 &&
            for (int i = 1; i < searchIndex; i++) {
                if (distinguish(search, searchIndex - i, pinyin, wordIndex + 1, 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 辅佐函数，确保pinyin[n].charAt(0)(n<=wordIndex)都按顺序依次出现在search里面
     * 防止zhou ming zhong匹配zz，跳过了ming
     *
     * @param search
     * @param pinyin
     * @param wordIndex 已经匹配到拼音索引
     * @return 都按顺序依次出现了，返回true
     */
    private static boolean distinguish(char[] search, String pinyin[], int wordIndex) {
        String searchString = new String(search);
        int lastIndex = 0;
        for (int i = 0; i < wordIndex; i++) {
            lastIndex = searchString.indexOf(pinyin[i].charAt(0), lastIndex);
            if (lastIndex == -1) return false;
            lastIndex++;
        }
        return true;
    }


    public static void main(String[] args) {
        test2();
    }

    public static void test2() {
        long s = System.currentTimeMillis();
        List<String[]> list = getPinYinAryList("我的收藏 不显示无行政区商家问题. 且现在bd能查看全国商家, 显示对应城市问题.");
        long s2 = System.currentTimeMillis();
        for (String[] a : list) {
            System.out.println(StringUtils.join(a, " "));
        }
        System.out.println("time=" + (s2 - s) + ",size=" + list.size());
    }


    public static void test1() {
        String str = "廖国成";
        System.out.println("廖 = " + isMatch("廖", str));
        System.out.println("廖国 = " + isMatch("廖国", str));
        System.out.println("国成 = " + isMatch("国成", str));
        System.out.println("廖国成 = " + isMatch("廖国成", str));
        System.out.println("廖成 = " + isMatch("廖成", str));

        System.out.println("lgc = " + isMatch("lgc", str));
        System.out.println("liaoguoc = " + isMatch("liaoguoc", str));
        System.out.println("liaogc = " + isMatch("liaogc", str));
        System.out.println("liaoguocheng = " + isMatch("liaoguocheng", str));
        System.out.println("liaoc = " + isMatch("liaoc", str));
    }

}
