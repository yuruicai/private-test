package com.sinochem.yunlian.upm.tools;

/**
 * @author zhangxi
 * @created 13-1-16
 */
class Paging {
    private String previous;
    private String next;
    private int totalCount = 0;

    public Paging() {
    }

    public Paging(String prev, String next) {
        this.previous = prev;
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public String toString() {
        return "previous=" + previous + ", next=" + next + ", totalCount=" + totalCount;
    }
}
