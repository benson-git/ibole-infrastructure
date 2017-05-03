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

import com.github.ibole.infrastructure.common.utils.HexUtil;

import org.apache.commons.codec.binary.Base64;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>
 * Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p>
 * </p>
 *********************************************************************************************/


/**
 * @author bwang
 *
 */
public class ECDSA {

  private static String src = "ecdsa security";

  public static void main(String[] args) {
    jdkECDSA();
  }

  public static void jdkECDSA() {
    try {
      // 1.初始化密钥
      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
      keyPairGenerator.initialize(256);
      KeyPair keyPair = keyPairGenerator.generateKeyPair();
      ECPublicKey ecPublicKey = (ECPublicKey) keyPair.getPublic();
      ECPrivateKey ecPrivateKey = (ECPrivateKey) keyPair.getPrivate();


      // 2.执行签名
      PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(ecPrivateKey.getEncoded());

      KeyFactory keyFactory = KeyFactory.getInstance("EC");
      PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
      Signature signature = Signature.getInstance("SHA256withECDSA");
      signature.initSign(privateKey);
      signature.update(src.getBytes());
      byte[] res = signature.sign();
      System.out.println("签名：" + Base64.encodeBase64String(res));

      // 3.验证签名
      X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(ecPublicKey.getEncoded());
      keyFactory = KeyFactory.getInstance("EC");
      PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
      signature = Signature.getInstance("SHA256withECDSA");
      signature.initVerify(publicKey);
      signature.update(src.getBytes());
      boolean bool = signature.verify(res);
      System.out.println("验证：" + bool);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
