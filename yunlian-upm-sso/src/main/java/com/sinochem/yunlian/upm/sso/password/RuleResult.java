package com.sinochem.yunlian.upm.sso.password;

/**
 * @author zhangxi
 * @created 13-12-22
 */
public class RuleResult {
    public static final int SCORE_NEGATIVE = -100000;
    public static final float SCORE_PASS = SCORE_NEGATIVE / 2;

    private int score;
    private String notice;

    public RuleResult(int score) {
        this.score = score;
    }

    public RuleResult(int score, String notice) {
        this.score = score;
        this.notice = notice;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
}
