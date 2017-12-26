package com.sinochem.yunlian.upm.util;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @author yangguo03
 * @version 1.0
 * @created 15-5-15
 */
public class PositionUtils {
    private static final Set<Integer> BDMPositionIds = Sets.newHashSet(
            167     // BD经理
    );

    public static boolean isBDM(Integer positionId) {
        return null != positionId && BDMPositionIds.contains(positionId);
    }

    private PositionUtils() {}
}
