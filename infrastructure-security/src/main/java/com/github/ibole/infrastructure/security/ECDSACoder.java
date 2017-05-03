/*
 * Copyright 2016-2017 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.github.ibole.infrastructure.security;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p>
 * </p>
 *********************************************************************************************/


/**
 * @author bwang
 *
 */
public class ECDSACoder extends Coder {

  /**
   * 用私钥对信息生成数字签名.
   * 
   * @param data 加密数据
   * @param privateKeyStr 私钥
   * 
   * @return
   * @throws Exception
   */
  public static String sign(byte[] data, String privateKeyStr) throws Exception {
    // 解密由base64编码的私钥
    byte[] keyBytes = decryptBASE64(privateKeyStr);

    // 执行签名
    PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);

    KeyFactory keyFactory = KeyFactory.getInstance("EC");
    PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
    Signature signature = Signature.getInstance("SHA256withECDSA");
    signature.initSign(privateKey);
    signature.update(data);

    return encryptBASE64(signature.sign());
  }

  /**
   * 校验数字签名.
   * 
   * @param data 加密数据
   * @param publicKey 公钥
   * @param sign 数字签名
   * 
   * @return 校验成功返回true 失败返回false
   * @throws Exception
   * 
   */
  public static boolean verify(byte[] data, String publicKeyStr, String sign) throws Exception {
    // 解密由base64编码的私钥
    byte[] keyBytes = decryptBASE64(publicKeyStr);
    // 验证签名
    X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("EC");
    PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
    Signature signature = Signature.getInstance("SHA256withECDSA");
    signature.initVerify(publicKey);
    signature.update(data);
    // 验证签名是否正常
    return signature.verify(decryptBASE64(sign));
  }
}
