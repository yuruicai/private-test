package com.sinochem.yunlian.upm.sso.password;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhangxi
 * @created 13-12-22
 */
public class Checker {
    private static final Logger LOG = LoggerFactory.getLogger(Checker.class);
    public static List<Rule> basicRules = new ArrayList<Rule>();
    public static List<Rule> scoreRules = new ArrayList<Rule>();

    static {
        basicRules.add(new LengthRule());
        basicRules.add(new specialCharRule());
        //basicRules.add(new NumberRule());
        //basicRules.add(new LetterRule());
        //basicRules.add(new NameRule());
        //basicRules.add(new WeakPasswordRule());

        scoreRules.addAll(basicRules);
        //scoreRules.add(new RepeatRule());
        //scoreRules.add(new SpecialCharacterRule());
        // TODO weak password pattern rule : hello, password, 123456 etc
    }

    public static String basic(String username, String password) {
        for (Rule rule : basicRules) {
            RuleResult result = rule.check(username, password);
            if (result.getScore() < RuleResult.SCORE_PASS) {
                LOG.info("{} check {} score for {}, {}", new Object[]{rule.getClass(), result.getScore(), username, result.getNotice()});
                return result.getNotice();
            }
        }
        return null;
    }

    public static int score(String username, String password) {
        int score = 0;
        for (Rule rule : scoreRules) {
            RuleResult result = rule.check(username, password);
            if (result.getScore() < RuleResult.SCORE_PASS) {
                LOG.info("{} check {} score for {}, {}", new Object[]{rule.getClass(), result.getScore(), username, result.getNotice()});
            }
            score += result.getScore();
        }
        if (score < 0) {
            return 0;
        } else if (score > 100) {
            return 100;
        } else {
            return score;
        }
    }

    static class LengthRule implements Rule {
        public RuleResult check(String username, String password) {
            if (password.length() < 6) {
                return new RuleResult(RuleResult.SCORE_NEGATIVE, "密码长度不能小于6位");
            } else if(password.length() >16){
                return new RuleResult(RuleResult.SCORE_NEGATIVE, "密码长度不能大于16位");
            }else {
                return new RuleResult(password.length() * 4);
            }
        }
    }

    static class NumberRule implements Rule {
        public RuleResult check(String username, String password) {
            int count = 0;
            int seqCount = 0;
            int seqScore = 0;
            for (int i = 0; i < password.length(); i++) {
                if (Character.isDigit(password.charAt(i))) {
                    count++;
                    seqCount++;
                } else {
                    seqScore += (seqCount > 1 ? -seqCount * 5 : 0);
                    seqCount = 0;
                }
            }
            seqScore += (seqCount > 1 ? -seqCount * 5 : 0);
            if (count <= 0) {
                return new RuleResult(RuleResult.SCORE_NEGATIVE, "密码必须至少包含一位数字");
            }
            return new RuleResult(count * 4 + seqScore);
        }
    }

    static class LetterRule implements Rule {
        public RuleResult check(String username, String password) {
            int lowerCount = 0;
            int seqLowerCount = 0;
            int upperCount = 0;
            int seqUpperCount = 0;
            int seqScore = 0;
            for (int i = 0; i < password.length(); i++) {
                if (Character.isLowerCase(password.charAt(i))) {
                    lowerCount++;
                    seqLowerCount++;
                    seqScore += seqUpperCount > 1 ? -seqUpperCount * 2 : 0;
                    seqUpperCount = 0;
                } else if (Character.isUpperCase(password.charAt(i))) {
                    upperCount++;
                    seqUpperCount++;
                    seqScore += seqLowerCount > 1 ? -seqLowerCount * 3 : 0;
                    seqLowerCount = 0;
                }
            }
            if (lowerCount <= 0 || upperCount <= 0) {
                return new RuleResult(RuleResult.SCORE_NEGATIVE, "密码必须至少包含一位大写和小写字母");
            }
            seqScore += seqUpperCount > 1 ? -seqUpperCount * 2 : 0;
            seqScore += seqLowerCount > 1 ? -seqLowerCount * 3 : 0;
            //int score = (password.length() - lowerCount) * 2 + (password.length() - upperCount) * 2 + seqScore;
            int score = lowerCount * 4 + upperCount * 4 + seqScore;
            return new RuleResult(score);
        }
    }

    static class RepeatRule implements Rule {
        @Override
        public RuleResult check(String username, String password) {
            int score = 0;
            int repeatCount = 0;
            for (int i = 1; i < password.length(); i++) {
                if (password.charAt(i - 1) == password.charAt(i)) {
                    repeatCount++;
                } else {
                    score += repeatCount > 1 ? -repeatCount * repeatCount : 0;
                    repeatCount = 0;
                }
            }
            score += repeatCount > 1 ? -repeatCount * repeatCount : 0;
            return new RuleResult(score);
        }
    }

    static class SpecialCharacterRule implements Rule {
        public RuleResult check(String username, String password) {
            int count = 0;
            for (int i = 0; i < password.length(); i++) {
                if (!Character.isLetterOrDigit(password.charAt(i))) {
                    count++;
                }
            }
            return new RuleResult(count * 6);
        }
    }

    static class NameRule implements Rule {
        public RuleResult check(String username, String password) {
            String nameChars = username.toLowerCase();
            String passChars = password.toLowerCase();
            if (nameChars != null && passChars.contains(nameChars)) {
                return new RuleResult(RuleResult.SCORE_NEGATIVE, "密码里含有用户名");
            }
            float sim = Similarity.sim(nameChars, passChars);
            if (sim > 0.8f) {
                return new RuleResult(RuleResult.SCORE_NEGATIVE, "密码与用户名过于相似");
            } else if (sim > 0.42f) {
                return new RuleResult((int) (-sim * nameChars.length() * 6), "密码与用户名过于相似");
            }
            return new RuleResult(0);
        }
    }

    //弱密码列表
    private static final String [] weekpasswords = {"123456",
            "123456789",
            "000000",
            "111111",
            "123123",
            "5201314",
            "666666",
            "123321",
            "1314520",
            "1234567890",
            "888888",
            "1234567",
            "654321",
            "12345678",
            "520520",
            "7758521",
            "112233",
            "147258",
            "123654",
            "987654321",
            "88888888",
            "147258369",
            "666888",
            "5211314",
            "521521",
            "a123456",
            "zxcvbnm",
            "999999",
            "222222",
            "123123123",
            "1314521",
            "201314",
            "woaini",
            "789456",
            "555555",
            "qwertyuiop",
            "100200",
            "168168",
            "qwerty",
            "258369",
            "456789",
            "110110",
            "789456123",
            "159357",
            "123789",
            "123456a",
            "121212",
            "456123",
            "987654",
            "111222",
            "1111111111",
            "7758258",
            "00000000",
            "333333",
            "1111111",
            "369369",
            "888999",
            "asdfgh",
            "11111111",
            "woaini1314",
            "258258",
            "0123456789",
            "369258",
            "aaaaaa",
            "778899",
            "0000000000",
            "0000000",
            "159753",
            "abc123",
            "585858",
            "asdfghjkl",
            "321654",
            "211314",
            "584520",
            "abcdefg",
            "777777",
            "0123456",
            "a123456789",
            "123654789",
            "abc123456",
            "336699",
            "abcdef",
            "518518",
            "888666",
            "708904",
            "135246",
            "12345678910",
            "147369",
            "110119",
            "qq123456",
            "789789",
            "251314",
            "555666",
            "111111111",
            "123000",
            "zxcvbn",
            "qazwsx",
            "123456abc",
            "asdf1234",
            "qwer1234",
            "1234qwer",
            "1234asdf",
            "qazwsx12",
            "qazwsx123",
            "1q2w3e4r",
            "1qaz2wsx",
            "jkl;1234",
            "2wsx3edc",
            "uiop1234",
            "uiop7890",
            "meituan888",
            "meituan123",
            "meituan2015",
            "meituan666"
    };

    static class WeakPasswordRule implements Rule {
        public RuleResult check(String username, String password) {
            for(String weekPassword : weekpasswords){
                if(weekPassword.toLowerCase().indexOf(password.toLowerCase()) >=0){
                    return new RuleResult(RuleResult.SCORE_NEGATIVE, "密码过于简单");
                }
            }
            return new RuleResult(0);
        }
    }

    /**
     * 非法字符判断,密码长度为6~16位大小写字母、数字、特殊符号,符号包括：-+_!@#$%^&*()
     * @param str
     * @return
     */
    public static boolean isSpecialChar(String str) {
        String regEx = "^[A-Za-z0-9-+_!@#$%^&*()]+$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 空格验证
     * @param str
     * @return
     */
    public static boolean isSapce(String str) {
        boolean flag = false;
        if(str.indexOf(" ")!=-1){
            flag =true;
        }
        return flag;
    }

    /**
     * 测试非法字符
     * @param args
     */
    public static void main(String[] args){
        String str = "123456-+_!@#$%^&*()黑";
        boolean flag = isSpecialChar(str);
        if(str.indexOf(" ")!=-1){
            System.out.println("不能包含空格");
        }
        System.out.println(flag);
    }

    static class specialCharRule implements Rule {
        public RuleResult check(String username, String password) {
            if(isSapce(password)) {
                return new RuleResult(RuleResult.SCORE_NEGATIVE, "密码中不能含有空格,密码只能由字母、数字及-+_!@#$%^&*()相关字符组成");
            }
            if(!isSpecialChar(password)) {
                return new RuleResult(RuleResult.SCORE_NEGATIVE, "包含非法字符,密码只能由字母、数字及-+_!@#$%^&*()相关字符组成");
            }
                return new RuleResult(password.length() * 4);
        }


    }
}
