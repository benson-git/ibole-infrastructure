package io.ibole.infrastructure.security.key;

import io.ibole.infrastructure.common.utils.FileUtil;
import io.ibole.infrastructure.common.utils.NLS;
import io.ibole.infrastructure.security.CertificateCoder;
import io.ibole.infrastructure.security.certificate.CertificateDetailsInfo;
import io.ibole.infrastructure.security.certificate.SslCertificateUtils;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStrictStyle;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStore.Builder;
import java.security.KeyStore.Entry;
import java.security.KeyStore.PasswordProtection;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStore.ProtectionParameter;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

/*********************************************************************************************
 * .
 * 
 * 
 * <p>
 * 版权所有：(c)2016， 深圳市拓润计算机软件开发有限公司
 * 
 * <p>
 * </p>
 *********************************************************************************************/


public final class KeyStoreUtils {

  private static Logger logger = LoggerFactory.getLogger(KeyStoreUtils.class.getName());

  private static final String BC = org.bouncycastle.jce.provider.BouncyCastleProvider.PROVIDER_NAME;

  private KeyStoreUtils() {}

  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  public static void main(String[] args) throws Exception
       {
     
    
    CertificateDetailsInfo info = new CertificateDetailsInfo("Alias test", "CA", "toprank", "byd",
        "SZ", "China", "gd", "10", null, null);
     
    File file = new File("D:/work/tfs/Toprank BasePlatform Solution/toprank-infrastructure/infrastructure-security/src/main/java/cc/toprank/infrastructure/security/key/ks.cert");
    String certPath = "D:/work/tfs/Toprank BasePlatform Solution/toprank-infrastructure/infrastructure-security/src/main/java/cc/toprank/infrastructure/security/key/client.cert";
    
    createX509CertificateWithECDSA(info, file, "JKS", "mypassword".toCharArray(), "ECDSA-ALIAs");
    X509Certificate x509Certificate = (X509Certificate) SslCertificateUtils.getCertificate(
        file.getAbsolutePath(), "ECDSA-ALIAs", "mypassword");
    SslCertificateUtils.saveX509Certificate(x509Certificate, certPath);
    
    String data = "test data";
    String signData = "sign data";
    byte[] decryptedData = CertificateCoder.decryptByPrivateKey(data.getBytes(), file.getAbsolutePath(), "ECDSA-ALIAs", "mypassword");
    CertificateCoder.sign(signData.getBytes(), file.getAbsolutePath(), "ECDSA-ALIAs", "mypassword");
    Boolean flag = CertificateCoder.verify(decryptedData, signData, certPath);
    
    System.out.println(CertificateCoder.decryptByPublicKey(decryptedData, certPath));
    System.out.println(flag);
    
  }

  /**
   * . Creates a new empty KeyStore, from the default type, located at keyStoreFile with the
   * password, password
   * 
   * @param keyStoreFile The file pointing o where the new KeyStore will be located
   * @param password the password for the new KeyStore
   * @return the {@link KeyStore} representing the new KeyStore
   * @throws InvalidPasswordException
   * @throws KeyStoreException if KeyStore can't be created
   */
  public static KeyStore createKeystore(File keyStoreFile, char[] password)
      throws KeyStoreManagerException {
    return createKeystore(keyStoreFile, KeyStore.getDefaultType(), password);
  }

  /**
   * Creates a new empty KeyStore, located at keyStoreFile with the password, password
   * 
   * @param keyStoreFile The file pointing o where the new KeyStore will be located
   * @param keyStoreType The type of the new KeyStore
   * @param password the password for the new KeyStore
   * @return the {@link KeyStore} representing the new KeyStore
   * @throws InvalidPasswordException
   * @throws KeyStoreException if KeyStore can't be created
   */
  public static KeyStore createKeystore(File keyStoreFile, String keyStoreType, char[] password)
      throws KeyStoreManagerException {
    KeyStore keyStore;
    if ((keyStoreFile != null) && !keyStoreFile.exists()) {
      keyStore = loadKeystore(keyStoreFile, password, keyStoreType);
      try {
        writeKeyStore(keyStore, password, keyStoreFile);
      } catch (Exception e) {
        throw new KeyStoreManagerException(e);
      }
    } else {
      throw new KeyStoreManagerException(
          keyStoreFile == null ? "Param cannot be empty" : "File Already Exists" + keyStoreFile);
    }

    return keyStore;
  }

  public static void writeKeyStore(KeyStore keyStore, char[] password, File keyStoreFile)
      throws FileNotFoundException, KeyStoreException, IOException, NoSuchAlgorithmException,
      CertificateException, KeyStoreManagerException {

    writeKeyStore(keyStore, null, password, keyStoreFile);
  }

  private static void writeKeyStore(KeyStore keyStore, char[] oldPassword, char[] newPassword,
      File keyStoreFile) throws FileNotFoundException, KeyStoreException, IOException,
      NoSuchAlgorithmException, CertificateException, KeyStoreManagerException {
    FileOutputStream fos = null;
    try {
      if (oldPassword != null) {
        if (loadKeystore(keyStoreFile, oldPassword, keyStore.getType()) != null) {
          fos = new FileOutputStream(keyStoreFile);
          keyStore.store(fos, newPassword);
        }
      } else {
        fos = new FileOutputStream(keyStoreFile);
        keyStore.store(fos, newPassword);
      }
    } finally {
      if (fos != null) {
        try {
          fos.close();
        } catch (IOException e) {
          logger.error("Could not close steam while writing keystore file. " + e.getMessage());
        }
      }
    }
  }

  /**
   * Loads a KeyStore from a given file from the default type, usually JKS. If keyStoreFile path
   * don't exist then a new empty KeyStore will be created on the given location. <b>Note:</b>
   * Calling this method is the same as calling loadKeystore(keyStoreFile, password,
   * KeyStore.getDefaultType())
   * 
   * @param keyStoreFile The keyStore location.
   * @param password The KeyStore password
   * @return the {@link KeyStore} representing the file.
   * @throws KeyStoreManagerException
   */
  public static KeyStore loadKeystore(File keyStoreFile, char[] password)
      throws KeyStoreManagerException {
    return loadKeystore(keyStoreFile, password, KeyStore.getDefaultType());
  }
  /**
   * 获得KeyStore with the given path,
   *  if the keystore is not existed, create a new one for the give path.
   * 
   * @param keyStorePath
   * @param password
   * @return the instance of KeyStore
   * @throws KeyStoreManagerException
   */
  public static KeyStore getKeyStore(String keyStorePath, char[] password, String storeType)
      throws KeyStoreManagerException {
    File keyStoreFile = null;
    FileInputStream fis = null;
    KeyStore ks = null;
    try {
      keyStoreFile = new File(keyStorePath);
      if (keyStoreFile != null && keyStoreFile.exists() && keyStoreFile.length() > 0) {
        fis = new FileInputStream(keyStoreFile);
        ks = KeyStore.getInstance(storeType);
        ks.load(fis, password);
      }
      else{
        ks = createKeystore(keyStoreFile, storeType, password);
      }

    } catch (Exception e) {
      throw new KeyStoreManagerException(e);
    } finally {
      if (fis != null) {
        IOUtils.closeQuietly(fis);
      }
    }
    return ks;
  }
  /**
   * Loads a KeyStore from a given file. If keyStoreFile path don't exist then a new empty KeyStore
   * will be created on memory. If you want o create a new KeyStore file, calling createStore is
   * recommended.
   * 
   * @param keyStoreFile The keyStore location.
   * @param password The KeyStore password
   * @param storeType The Type of the keystore o be loaded.
   * @return the {@link KeyStore} representing the file.
   * @throws KeyStoreManagerException
   * @throws InvalidPasswordException
   */
  public static KeyStore loadKeystore(File keyStoreFile, char[] password, String storeType)
      throws KeyStoreManagerException {
    KeyStore keyStore = null;
    FileInputStream fis = null;
    try {
      keyStore = KeyStore.getInstance(storeType);

      if ((keyStoreFile != null) && keyStoreFile.exists() && (keyStoreFile.length() > 0)) {
        fis = new FileInputStream(keyStoreFile);
      }
      // fis = null means a new keyStore will be created
      keyStore.load(fis, password);
    } catch (Exception e) {
      throw new KeyStoreManagerException(e);
    } finally {
      if (fis != null) {
        IOUtils.closeQuietly(fis);
      }
    }

    return keyStore;
  }

  /**
   * Simply deletes the KeyStore File
   * 
   * @param keyStoreFile teh KeyStore file to be deleted.
   * @throws KeyStoreException If any error occur.
   */
  public static void deleteKeystore(File keyStoreFile) throws KeyStoreManagerException {
    try {
      FileUtil.deleteFile(keyStoreFile);
    } catch (IOException e) {
      throw new KeyStoreManagerException(e);
    }
  }

  /**
   * Write the keyStore in to the given file, protecting it with password. Warn: Since there's
   * actually no way to change the password this method will overwrite the existing file with the
   * keyStore contents, without further warning.
   * 
   * @param keyStore the {@link KeyStore} to be written.
   * @param keyStoreFile The KeyStore location
   * @param oldPassword
   * @param sourcePassword the new Password
   * @throws KeyStoreException If file could no be write.
   */
  public static void changeKeystorePasswd(KeyStore pKeyStore, File keyStoreFile, char[] oldPassword,
      char[] newPassword) throws KeyStoreManagerException {
    try {
      KeyStore keyStore = loadKeystore(keyStoreFile, oldPassword, pKeyStore.getType());
      writeKeyStore(keyStore, oldPassword, newPassword, keyStoreFile);
    } catch (Exception e) {
      throw new KeyStoreManagerException(e);
    }
  }

  /**
   * Adds a new enty to a given keyStore.
   * 
   * @param keyStore The Keystore that will receive the entry
   * @param keyStorePassword The KeyStore password
   * @param keyStoreFile The KeyStore file path
   * @param alias The new entry alias
   * @param entry The Entry to be added
   * @param entryPassword The password to protect the entry
   * @throws KeyStoreManagerException if any error occurs.
   */
  public static void addEntry(KeyStore pKeyStore, char[] keyStorePassword, File keyStoreFile,
      String alias, Entry entry, char[] entryPassword) throws KeyStoreManagerException {
    try {
      PasswordProtection passwordProtection = new KeyStore.PasswordProtection(entryPassword);
      KeyStore keyStore = loadKeystore(keyStoreFile, keyStorePassword, pKeyStore.getType());

      if (!keyStore.containsAlias(alias)) {
        keyStore.setEntry(alias, entry, passwordProtection);
        writeKeyStore(keyStore, keyStorePassword, keyStoreFile);
      } else {
        throw new KeyStoreManagerException("Alias \"{" + alias + "}\" already exists.");
      }

    } catch (KeyStoreManagerException e) {
      throw e;
    } catch (Exception e) {
      throw new KeyStoreManagerException(e);
    }
  }

  /**
   * Adds a new enty to a given keyStore.
   * 
   * @param keyStore The Keystore that will receive the entry
   * @param keyStorePassword The KeyStore password
   * @param keyStoreFile The KeyStore file path
   * @param alias The new entry alias
   * @param entry The Entry to be added
   * @param entryPassword The password to protect the entry
   * @throws KeyStoreManagerException if any error occurs.
   */
  public static void changeEntryPassword(KeyStore keyStore, char[] keyStorePassword,
      File keyStoreFile, String alias, Entry entry, char[] entryPassword)
      throws KeyStoreManagerException {
    try {
      PasswordProtection passwordProtection = new KeyStore.PasswordProtection(entryPassword);
      keyStore.setEntry(alias, entry, passwordProtection);
      writeKeyStore(keyStore, keyStorePassword, keyStoreFile);
    } catch (Exception e) {
      throw new KeyStoreManagerException("Error attempting to change password for " + alias, e);
    }
  }

  
  public static void createX509CertificateWithECDSA(CertificateDetailsInfo certDetails, File keyStoreFile, String keyStoreType, char[] password, String alias) throws KeyStoreManagerException {

    X509Certificate cert;
   
    try {

      //1.初始化密钥 
      KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", BC);
      keyPairGenerator.initialize(256);
      KeyPair keyPair = keyPairGenerator.generateKeyPair();
      ECPublicKey ecPublicKey = (ECPublicKey)keyPair.getPublic();
      ECPrivateKey ecPrivateKey = (ECPrivateKey)keyPair.getPrivate();   
      
      //2.执行签名
      PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(ecPrivateKey.getEncoded());
      X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(ecPublicKey.getEncoded());
      //
      // set up the keys
      //
      PrivateKey privKey;
      PublicKey pubKey;

      KeyFactory keyFactory = KeyFactory.getInstance("ECDSA", BC);
      
      privKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
      pubKey = keyFactory.generatePublic(x509EncodedKeySpec);

      //
      // distinguished name table.
      X500NameBuilder nameBuilder = new X500NameBuilder(new BCStrictStyle());
      addField(BCStyle.C, certDetails.getCountry(), nameBuilder);
      addField(BCStyle.ST, certDetails.getState(), nameBuilder);
      addField(BCStyle.L, certDetails.getLocality(), nameBuilder);
      addField(BCStyle.O, certDetails.getOrganization(), nameBuilder);
      addField(BCStyle.OU, certDetails.getOrganizationUnit(), nameBuilder);
      addField(BCStyle.CN, certDetails.getCommonName(), nameBuilder);

      X500Name subjectName = nameBuilder.build();
      X500Name issuerName = subjectName;

      //
      // create the certificate - version 3
      //
      ContentSigner sigGen =
          new JcaContentSignerBuilder("SHA256withECDSA").setProvider(BC).build(privKey);
      X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(issuerName,
          BigInteger.valueOf(new SecureRandom().nextInt()),
          GregorianCalendar.getInstance().getTime(), certDetails.getExpirationDate(), subjectName,
          pubKey);

      cert = new JcaX509CertificateConverter().setProvider(BC).getCertificate(certGen.build(sigGen));
      
      KeyStore ks = KeyStore.getInstance(keyStoreType);
      ks.load(null, null);
      ks.setKeyEntry(alias, privKey, password, new Certificate[]{cert}); 
      
      writeKeyStore(ks, password, keyStoreFile);
      
    } catch (OperatorCreationException | CertificateException | NoSuchAlgorithmException
        | NoSuchProviderException | InvalidKeySpecException | KeyStoreException | IOException ex) {
      throw new KeyStoreManagerException("Error attempting to create X509Certificate with ECDSA", ex);
    }
  }

  private static void addField(ASN1ObjectIdentifier objectId, String value,
      X500NameBuilder nameBuilder) {
    if (value.length() > 0) {
      nameBuilder.addRDN(objectId, value);
    }
  }

  public static void deleteEntry(KeyStore keyStore, char[] password, File keyStoreFile,
      String alias) throws KeyStoreManagerException {
    try {
      keyStore = loadKeystore(keyStoreFile, password, keyStore.getType());

      keyStore.deleteEntry(alias);
      writeKeyStore(keyStore, password, keyStoreFile);
    } catch (Exception e) {
      logger.error("Delete '{0}' error happened", alias, e);
      throw new KeyStoreManagerException("Delete" + alias + "error happened", e);
    }
  }

  /**
   * Change a keyStore type.
   * 
   * @param keyStoreFile The KeyStoreFile
   * @param password The KeyStore Password
   * @param originalType the original Type
   * @param destinationType the new KeyStore Type
   * @throws KeyStoreManagerException If any error occurs, the operation will be canceled and
   *         reverted automatically.
   * @throws InvalidPasswordException
   */
  public static void changeKeyStoreType(File keyStoreFile, char[] password, String originalType,
      String destinationType, Map<String, String> aliases) throws KeyStoreManagerException {
    boolean rollBack = false;
    String timeStamp = Long.toString(Calendar.getInstance().getTimeInMillis());
    File oldKsFile = new File(keyStoreFile.getAbsolutePath() + "_" + timeStamp);
    oldKsFile.delete();
    boolean renamed = false;
    renamed = keyStoreFile.renameTo(oldKsFile);
    if (renamed) {
      try {
        Builder oldKsBuilder = KeyStore.Builder.newInstance(originalType, null, oldKsFile,
            new PasswordProtection(password));
        KeyStore oldKeyStore = oldKsBuilder.getKeyStore();

        KeyStore newKeyStore = createKeystore(keyStoreFile, destinationType, password);
        for (String alias : aliases.keySet()) {
          ProtectionParameter protectionParameter =
              new PasswordProtection(aliases.get(alias).toCharArray());
          Entry entry = oldKeyStore.getEntry(alias, protectionParameter);
          newKeyStore.setEntry(alias, entry, protectionParameter);
        }
        writeKeyStore(newKeyStore, password, keyStoreFile);
      } catch (KeyStoreManagerException e) {
        rollBack = true;
        logger.error(
            "Invalid password while trying to create a new keystore, changing a keyStore type.", e);

      } catch (Exception e) {
        if (e.getMessage().contains("password was incorrect")
            || e.getCause().getMessage().contains("password was incorrect")) {
          keyStoreFile.delete();
          oldKsFile.renameTo(keyStoreFile);
          throw new KeyStoreManagerException(e.getMessage());
        } else {
          logger.error("Exception occurred while attempting to change a keyStore type.", e);
          rollBack = true;
        }
      }

      if (rollBack) {
        keyStoreFile.delete();
        oldKsFile.renameTo(keyStoreFile);

        throw new KeyStoreManagerException(NLS
            .bind("Could not convert the KeyStore {0} to type {1}", keyStoreFile, destinationType));
      }
    } else {
      throw new KeyStoreManagerException(NLS.bind(
          "Could not convert the KeyStore {0} to type {1}, could not backup the current keyStore file, maybe it's in use by another program.",
          keyStoreFile, destinationType));
    }
    oldKsFile.delete();
  }

  /**
   * Import a set of entries from sourcekeystore into the targetkeystore. If alias already exists on
   * the target keystore then the alias is concatenated with the source keystore file name.
   * 
   * @param targetKeyStore
   * @param targetFile
   * @param targetType
   * @param targetPasswd
   * @param sourceKeyStore
   * @param sourceKeyStoreFile
   * @param sourcePasswd
   * @param aliases a map<String, String> containing alias as key and its password as value. this
   *        method assume that the password is correct
   * @throws InvalidPasswordException
   * @throws KeyStoreManagerException
   */
  public static void importKeys(KeyStore targetKeyStore, File targetFile, String targetType,
      char[] targetPasswd, KeyStore sourceKeyStore, File sourceKeyStoreFile, char[] sourcePasswd,
      Map<String, String> aliases) throws KeyStoreManagerException {
    if (!isValidKeyStorePasswd(targetFile, targetType, targetPasswd)) {
      throw new KeyStoreManagerException("Invalid Keystore Password.");
    }

    try {
      for (String alias : aliases.keySet()) {
        if (sourceKeyStore.containsAlias(alias)) {
          ProtectionParameter protectionParameter =
              new PasswordProtection(aliases.get(alias).toCharArray());
          Entry entry = sourceKeyStore.getEntry(alias, protectionParameter);
          if (targetKeyStore.containsAlias(alias)) {
            alias += "_" + sourceKeyStoreFile.getName();
          }
          int i = 1;
          while (targetKeyStore.containsAlias(alias)) {
            alias += "_" + i;
            i++;
          }
          targetKeyStore.setEntry(alias, entry, protectionParameter);
        } else {
          logger.error(NLS.bind(
              "Alias {0} could not be imported because it doesn't exists on originKeyStore",
              alias));
        }
      }
      writeKeyStore(targetKeyStore, targetPasswd, targetFile);
    } catch (Exception e) {
      throw new KeyStoreManagerException(
          "Could not import the selected aliases into " + targetFile.getName(), e);
    }
  }

  /**
   * Verifies if the password if valid
   * 
   * @param keyStoreFile
   * @param keyStoreType
   * @param passwd
   * @return true if password is valid, false otherwise.
   * @throws KeyStoreManagerException
   */
  public static boolean isValidKeyStorePasswd(File keyStoreFile, String keyStoreType,
      char[] passwd) {
    KeyStore keystore = null;
    try {
      keystore = loadKeystore(keyStoreFile, passwd, keyStoreType);
    } catch (KeyStoreManagerException e) {
      // Do nothing, password is invalid
    }
    return keystore != null;
  }
}
