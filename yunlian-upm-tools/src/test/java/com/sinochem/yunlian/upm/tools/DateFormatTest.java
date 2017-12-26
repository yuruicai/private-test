package com.sinochem.yunlian.upm.tools;

import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author yangguo
 * @version 1.0
 * @created 15/7/23.
 */
public class DateFormatTest {

    @Test
    public void testDateFormatInitCost() {
        long begin = System.currentTimeMillis();
        int n = 10000;

        for (int i=0; i<n; i++) {
            DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        }
        System.out.println(System.currentTimeMillis() - begin);
        System.out.println((System.currentTimeMillis() - begin) * 1.0 / n);
    }

    @Test
    public void testDateFormatThreadUnsafe() {
        final SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        ExecutorService ex = Executors.newFixedThreadPool(1000);
        for (;;) {
            ex.execute(new Runnable() {
                public void run() {
                    try {
                        f.format(new Date(new Random().nextLong()));
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                };
            });
        }
    }
}
