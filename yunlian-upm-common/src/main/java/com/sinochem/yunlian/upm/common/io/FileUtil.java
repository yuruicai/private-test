package com.sinochem.yunlian.upm.common.io;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhangxi
 * @created 13-3-1
 */
public class FileUtil {

    private FileUtil() {
    }

    /**
     * 按照正则查找该路径下名字符合的文件
     *
     * @param path
     * @param regex
     * @return
     */
    public static String[] scan(String path, final String regex) {
        File file = new File(path);
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        String[] names = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                Matcher matcher = pattern.matcher(name);
                return matcher.find();
            }
        });
        return names;
    }
}
