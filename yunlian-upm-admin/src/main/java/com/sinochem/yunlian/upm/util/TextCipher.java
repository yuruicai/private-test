/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sinochem.yunlian.upm.util;

import java.security.GeneralSecurityException;

/**
 * 加密解密Text类型的数据
 *
 * @author sobranie
 */
public interface TextCipher {
    
    /**
     * 初始化
     * 
     * @param salt 
     */
    void init(String salt);
    
    /**
     * 加密信息
     * 
     * @param value
     * @return 
     * @throws GeneralSecurityException
     */
    String encrypt(String value) throws GeneralSecurityException;

    /**
     * 解密信息
     *
     * @param value
     * @return
     * @throws GeneralSecurityException
     */
    String decrypt(String value) throws GeneralSecurityException;
}
