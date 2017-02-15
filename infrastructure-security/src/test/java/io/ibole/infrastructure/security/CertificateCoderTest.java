package io.ibole.infrastructure.security;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

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

public class CertificateCoderTest {
  private String password = "0w45P.Z4p";
  private String alias = "self_user_ca_jks";
  
  private String keyStorePath  = "D:/work/tfs/Toprank BasePlatform Solution/toprank-infrastructure/infrastructure-security/src/main/resources/META-INF/cert/keys.keystore";
  private String certificatePath = "D:/work/tfs/Toprank BasePlatform Solution/toprank-infrastructure/infrastructure-security/src/main/resources/META-INF/cert/client.crt";


 
  public void test() throws Exception {
      System.err.println("公钥加密——私钥解密");
      String inputStr = "Ceritifcate";
      byte[] data = inputStr.getBytes();

      byte[] encrypt = CertificateCoder.encryptByPublicKey(data,
              certificatePath);

      byte[] decrypt = CertificateCoder.decryptByPrivateKey(encrypt,
              keyStorePath, alias, password);
      String outputStr = new String(decrypt);

      System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);

      // 验证数据一致
      assertArrayEquals(data, decrypt);

      // 验证证书有效
      assertTrue(CertificateCoder.verifyCertificate(certificatePath));

  }

 
  public void testSign() throws Exception {
      System.err.println("私钥加密——公钥解密");

      String inputStr = "sign";
      byte[] data = inputStr.getBytes();

      byte[] encodedData = CertificateCoder.encryptByPrivateKey(data,
              keyStorePath, alias, password);

      byte[] decodedData = CertificateCoder.decryptByPublicKey(encodedData,
              certificatePath);

      String outputStr = new String(decodedData);
      System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);
      //assertEquals(inputStr, outputStr);

      System.err.println("私钥签名——公钥验证签名");
      // 产生签名
      String sign = CertificateCoder.sign(encodedData, keyStorePath, alias,
              password);
      System.err.println("签名:\r" + sign);

      // 验证签名
      boolean status = CertificateCoder.verify(encodedData, sign,
              certificatePath);
      System.err.println("状态:\r" + status);
      //assertTrue(status);

  }

 
  public void testHttps() throws Exception {
      URL url = new URL("https://www.zlex.org/examples/");
      HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

      conn.setDoInput(true);
      conn.setDoOutput(true);

      CertificateCoder.configSSLSocketFactory(conn, password, keyStorePath,
              keyStorePath);

      InputStream is = conn.getInputStream();

      int length = conn.getContentLength();

      DataInputStream dis = new DataInputStream(is);
      byte[] data = new byte[length];
      dis.readFully(data);

      dis.close();
      conn.disconnect();
      System.err.println(new String(data));
  }
}

