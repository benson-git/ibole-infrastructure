package io.ibole.infrastructure.common.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

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
 * 3DES加密工具类.
 *
 */
public class TripleDes {
  // 算法名称
  private static final String KEY_ALGORITHM = "DESede";
  // 算法名称/加密模式/填充方式
  // private static final String CIPHER_ALGORITHM_ECB =
  // "DESede/ECB/PKCS5Padding";
  private static final String CIPHER_ALGORITHM_CBC = "DESede/CBC/PKCS5Padding";
  // 密钥长度不得小于24
  private String secretKey;
  // 向量可有可无终端后台也要约定
  private String iv;

  private TripleDes(String secretKey, String iv) {
    this.secretKey = secretKey;
    this.iv = iv;
  }

  /**
   * 3DES加密.
   *
   * @param rawData 字节数据
   * @return encryptData 已经加密的数据
   * @throws Exception 加密异常
   */
  public byte[] encrypt(byte[] rawData) throws Exception {
    byte[] encryptData = getEncryptCipher().doFinal(rawData);
    return encryptData;
  }
  
  /**
   * DES加密文件.
   * 
   * @param file 源文件
   * @param destFile 加密后的文件
   * @throws Exception 加密异常
   */
  public void encrypt(String file, String destFile) throws Exception {
    CipherInputStream cis = null;
    InputStream is = null;
    OutputStream out = null;;
    try {
      is = new FileInputStream(file);
      out = new FileOutputStream(destFile);
      cis = new CipherInputStream(is, getEncryptCipher());
      byte[] buffer = new byte[1024];
      int r;
      while ((r = cis.read(buffer)) > 0) {
        out.write(buffer, 0, r);
      }
    } finally {
      cis.close();
      is.close();
      out.close();
    }
  }
  
  /**
   * 3DES加密.
   *
   * @param plainText 普通文本
   * @return encryptData 已经加密的数据
   * @throws Exception 加密异常
   */
  public byte[] encryptString(String plainText) throws Exception {
    return encrypt(plainText.getBytes());
  }

  /**
   * 3DES解密.
   *
   * @param encryptData 加密的数据
   * @return decryptData 已经解密的数据
   * @throws Exception 解密异常
   */
  public byte[] decrypt(byte[] encryptData) throws Exception {
    byte[] decryptData = getDecryptCipher().doFinal(encryptData);
    return decryptData;
  }
  
  /**
   * DES解密文件.
   * 
   * @param file 需要解密的文件
   * @param dest 解密后的文件
   * @throws Exception 解密异常
   */
  public void decrypt(String file, String dest) throws Exception {
    InputStream is = null;
    OutputStream out = null;
    CipherOutputStream cos = null;
    try {
      is = new FileInputStream(file);
      out = new FileOutputStream(dest);
      cos = new CipherOutputStream(out, getDecryptCipher());
      byte[] buffer = new byte[1024];
      int r;
      while ((r = is.read(buffer)) >= 0) {
        cos.write(buffer, 0, r);
      }
    } finally {
      cos.close();
      out.close();
      is.close();
    }
  }
  
  private Cipher getEncryptCipher() throws Exception {
    DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
    SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
    Key deskey = keyfactory.generateSecret(spec);
    Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
    IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
    cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
    return cipher;
  }
  
  private Cipher getDecryptCipher() throws Exception {
    DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
    SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
    Key deskey = keyfactory.generateSecret(spec);
    Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
    IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
    cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
    return cipher;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private String secretKey = "123456789012345678901234";
    private String iv = "01234567";

    /**
     * Default Constructor of Builder.
     * 
     * @param secretKey 密钥
     * @param iv 向量
     */
    public Builder(String secretKey, String iv) {
      this.secretKey = secretKey;
      this.iv = iv;
    }

    public Builder() {
      // do nothing.
    }

    /**
     * Set Secret Key.
     * 
     * @param secretKey the secretKey to set
     */
    public Builder setSecretKey(String secretKey) {
      this.secretKey = secretKey;
      return this;
    }

    /**
     * Set iv.
     * 
     * @param iv the iv to set
     */
    public Builder setIv(String iv) {
      this.iv = iv;
      return this;
    }

    public TripleDes build() {
      return new TripleDes(secretKey, iv);
    }

  }

  /**
   * main function for test.
   */
  public static void main(String[] args) throws Exception {
    //testString();
    testFile();
  }
  
  private static void testFile() throws Exception {
    String inFile =
        "D:/work/tfs-rms-workspace/Toprank Platform Solution/toprank-infrastructure/infrastructure-common/test.zip";
    String outFile =
        "D:/work/tfs-rms-workspace/Toprank Platform Solution/toprank-infrastructure/infrastructure-common/test-out.zip";
    TripleDes.builder().build().encrypt(inFile, outFile);
    TripleDes.builder().build().decrypt(outFile, inFile);
  }

  private static void testString() throws Exception {
    String str = "不要等";
    System.out.println("----加密前-----：" + str);
    byte[] encodeStr = TripleDes.builder().build().encryptString(str);
    System.out.println("----加密后-----：" + new String(encodeStr));
    System.out
        .println("----解密后-----：" + new String(TripleDes.builder().build().decrypt(encodeStr)));

    // 24字节的密钥（我们可以取apk签名的指纹的前12个byte和后12个byte拼接在一起为我们的密钥）
    final byte[] keyBytes =
        {0x11, 0x22, 0x4F, 0x58, (byte) 0x88, 0x10, 0x40, 0x38, 0x28, 0x25, 0x79, 0x51, (byte) 0xCB,
            (byte) 0xDD, 0x55, 0x66, 0x77, 0x29, 0x74, (byte) 0x98, 0x30, 0x40, 0x36, (byte) 0xE2};

    String szSrc = "This is a 3DES test. 测试";

    System.out.println("加密前的字符串:" + szSrc);

    byte[] encoded =
        TripleDes.builder().setSecretKey(new String(keyBytes)).build().encrypt(szSrc.getBytes());
    System.out.println("加密后的字符串:" + new String(encoded));

    Builder dd = new Builder();
    dd.setSecretKey("dddd");

    byte[] srcBytes =
        TripleDes.builder().setSecretKey(new String(keyBytes)).build().decrypt(encoded);
    System.out.println("解密后的字符串:" + (new String(srcBytes)));
  }
}
