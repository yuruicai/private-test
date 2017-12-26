package com.sinochem.yunlian.upm.util;

import com.google.common.base.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeUseUtils {
	private final static Logger logger = LoggerFactory.getLogger(TimeUseUtils.class);

	public static <T> T recordTimeUse(String description, Supplier<T> supplier) {
		long begin = System.currentTimeMillis();
		T result = supplier.get();
		logger.trace("{},use time:{}", description, System.currentTimeMillis() - begin);
		return result;
	}

	public static void recordTimeUse(String description, Runnable runnable) {
		long begin = System.currentTimeMillis();
		runnable.run();
		logger.trace("{},use time:{}", description, System.currentTimeMillis() - begin);
	}
}
