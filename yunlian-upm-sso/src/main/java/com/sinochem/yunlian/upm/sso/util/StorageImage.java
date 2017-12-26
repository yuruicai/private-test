package com.sinochem.yunlian.upm.sso.util;

/**
 * @author zhangxi
 * @created 14-4-21
 */
public class StorageImage {
    private String fileKey;
    private String originalFileName;
    private Integer originalFileSize;
    private String originalLink;

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public Integer getOriginalFileSize() {
        return originalFileSize;
    }

    public void setOriginalFileSize(Integer originalFileSize) {
        this.originalFileSize = originalFileSize;
    }

    @Override
    public String toString() {
        return "StorageImage [fileKey=" + fileKey + ", originalFileName=" + originalFileName
                + ", originalFileSize=" + originalFileSize + ", originalLink=" + originalLink + "]";
    }

    public String getOriginalLink() {
        return originalLink;
    }

    public void setOriginalLink(String originalLink) {
        this.originalLink = originalLink;
    }
}
