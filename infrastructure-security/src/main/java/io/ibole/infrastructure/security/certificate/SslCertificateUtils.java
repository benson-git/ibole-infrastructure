package io.ibole.infrastructure.security.certificate;

import io.ibole.infrastructure.security.key.KeyStoreManagerException;
import io.ibole.infrastructure.security.key.KeyStoreUtils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStrictStyle;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.openssl.jcajce.JcaPKCS8Generator;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8EncryptorBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.OutputEncryptor;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.util.io.pem.PemObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import javax.xml.bind.DatatypeConverter;

/*********************************************************************************************.
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p></p>
 *********************************************************************************************/


public class SslCertificateUtils { 
  
  public static final String KEY_STORE = "JKS";

  private static final String X509 = "X.509";
  
  private static final long DEFAULT_VALID_DAYS = 365L; 
  /**
   * The passphrase which is used for all SSL crypto stuff
   */
  private static final char[] PASSPHRASE = "0w45P.Z4p".toCharArray();
  /**
   * The alias name used in key stores for root ca.
   */
  private static final String SELF_ROOT_CA_JKS_ALIAS = "self_root_ca_jks";
  /**
   * The alias name used in key stores for user ca.
   */
  private static final String SELF_USER_CA_JKS_ALIAS = "self_user_ca_jks";
  
  private static final String KEYSTORE_PATH = "/META-INF/cert/keys.keystore";
  
  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  /**
   * Creates a new Root CA certificate and returns private and public key as 
   * {@link KeyStore}. The {@link KeyStore#getDefaultType()} is used. 
   * 
   * @return KeyStore 
   * @throws NoSuchAlgorithmException 
   * @throws KeyStoreManagerException 
   */ 
  public static final KeyStore createRootCA(String keyStorePath, CertificateDetailsInfo certDetails) throws NoSuchAlgorithmException, KeyStoreManagerException  { 
   final Date startDate = Calendar.getInstance().getTime(); 
   final Date expireDate = new Date(startDate.getTime()+ (DEFAULT_VALID_DAYS * 24L * 60L * 60L * 1000L)); 
  
   final KeyPairGenerator g = KeyPairGenerator.getInstance("RSA"); 
   g.initialize(2048, SecureRandom.getInstance("SHA1PRNG")); 
   final KeyPair keypair = g.genKeyPair(); 
   final PrivateKey privKey = keypair.getPrivate(); 
   final PublicKey  pubKey = keypair.getPublic(); 
   Security.addProvider(new BouncyCastleProvider()); 
   Random rnd = new Random(); 
  
   // using the hash code of the user's name and home path, keeps anonymity 
   // but also gives user a chance to distinguish between each other  
   X500NameBuilder nameBuilder = new X500NameBuilder(new BCStrictStyle());
   addField(BCStyle.C, certDetails.getCountry(), nameBuilder);
   addField(BCStyle.ST, certDetails.getState(), nameBuilder);
   addField(BCStyle.L, certDetails.getLocality(), nameBuilder);
   addField(BCStyle.O, certDetails.getOrganization(), nameBuilder);
   addField(BCStyle.OU, certDetails.getOrganizationUnit(), nameBuilder);
   addField(BCStyle.CN, certDetails.getCommonName(), nameBuilder);
    
   X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder ( 
             nameBuilder.build(), 
             BigInteger.valueOf(rnd.nextInt()), 
             startDate, 
             expireDate, 
             nameBuilder.build(), 
             pubKey 
            ); 
    
   KeyStore ks = KeyStoreUtils.getKeyStore(keyStorePath, PASSPHRASE, KEY_STORE); 
   try { 
    certGen.addExtension(Extension.subjectKeyIdentifier, false, new SubjectKeyIdentifier(pubKey.getEncoded())); 
    certGen.addExtension(Extension.basicConstraints, true, new BasicConstraints(true)); 
    certGen.addExtension(Extension.keyUsage, false, new KeyUsage(KeyUsage.keyCertSign | KeyUsage.digitalSignature | KeyUsage.keyEncipherment | KeyUsage.dataEncipherment | KeyUsage.cRLSign)); 
     
     KeyPurposeId[] eku = { 
     KeyPurposeId.id_kp_serverAuth, 
     KeyPurposeId.id_kp_clientAuth, 
     KeyPurposeId.anyExtendedKeyUsage 
    }; 
    certGen.addExtension(Extension.extendedKeyUsage, false, new ExtendedKeyUsage(eku)); 
   
    final ContentSigner sigGen = new JcaContentSignerBuilder("SHA256WithRSAEncryption").setProvider("BC").build(privKey); 
    final X509Certificate cert = new JcaX509CertificateConverter().setProvider("BC").getCertificate(certGen.build(sigGen)); 
 
    ks.setKeyEntry(SELF_ROOT_CA_JKS_ALIAS, privKey, PASSPHRASE, new Certificate[]{cert}); 
    
   } catch (final Exception e) { 
    throw new IllegalStateException("Errors during assembling root CA.", e); 
   } 
   return ks; 
  }

  public static KeyStore createCertForHost(KeyStore keystore, CertificateDetailsInfo certDetails, String hostname) throws NoSuchAlgorithmException,
      InvalidKeyException, CertificateException, NoSuchProviderException, SignatureException,
      KeyStoreException, IOException, UnrecoverableKeyException {

    if (hostname == null) {
      throw new IllegalArgumentException("Error, 'hostname' is not allowed to be null!");
    }
    
    X509Certificate rootCaCert = (X509Certificate) keystore.getCertificate(SELF_ROOT_CA_JKS_ALIAS);
    PrivateKey caPrivKey =
        (RSAPrivateKey) keystore.getKey(SELF_ROOT_CA_JKS_ALIAS, PASSPHRASE);
    PublicKey caPubKey = rootCaCert.getPublicKey();

    if (rootCaCert == null || caPrivKey == null || caPubKey == null) {
      throw new MissingRootCertificateException("Cannot find root certificate in Keystore '"+SELF_ROOT_CA_JKS_ALIAS+"'");
    }
    final Random rnd = new Random();
    rnd.setSeed(System.currentTimeMillis());
    // prevent browser certificate caches, cause of doubled serial numbers
    // using 48bit random number
    long sl = ((long) rnd.nextInt()) << 32 | (rnd.nextInt() & 0xFFFFFFFFL);
    // let reserve of 16 bit for increasing, serials have to be positive
    sl = sl & 0x0000FFFFFFFFFFFFL;
    AtomicLong serial = new AtomicLong(sl);

    final KeyPair mykp = createKeyPair();
    final PrivateKey privKey = mykp.getPrivate();
    final PublicKey pubKey = mykp.getPublic();

    X500NameBuilder namebld = new X500NameBuilder(BCStyle.INSTANCE);
    namebld.addRDN(BCStyle.CN, hostname);
    namebld.addRDN(BCStyle.OU, certDetails.getOrganizationUnit());
    namebld.addRDN(BCStyle.O, certDetails.getOrganization());
    namebld.addRDN(BCStyle.C, certDetails.getCountry());
    namebld.addRDN(BCStyle.EmailAddress, certDetails.getEmailAddress());

    X509v3CertificateBuilder certGen =
        new JcaX509v3CertificateBuilder(new X509CertificateHolder(rootCaCert.getEncoded()).getSubject(),
            BigInteger.valueOf(serial.getAndIncrement()),
            new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 30),
            new Date(System.currentTimeMillis() + 100 * (1000L * 60 * 60 * 24 * 30)),
            namebld.build(), pubKey);

    certGen.addExtension(Extension.subjectKeyIdentifier, false,
        new SubjectKeyIdentifier(pubKey.getEncoded()));
    certGen.addExtension(Extension.basicConstraints, false, new BasicConstraints(false));

    ContentSigner sigGen;
    try {
      sigGen =
          new JcaContentSignerBuilder("SHA256WithRSAEncryption").setProvider("BC").build(caPrivKey);
    } catch (OperatorCreationException e) {
      throw new CertificateException(e);
    }
    final X509Certificate cert =
        new JcaX509CertificateConverter().setProvider("BC").getCertificate(certGen.build(sigGen));
    cert.checkValidity(new Date());
    cert.verify(caPubKey);

    final Certificate[] chain = new Certificate[2];
    chain[1] = rootCaCert;
    chain[0] = cert;
    keystore.setKeyEntry(SELF_USER_CA_JKS_ALIAS, privKey, PASSPHRASE, chain);
    
    return keystore;
  }
  
  
  
  private static void addField(ASN1ObjectIdentifier objectId, String value,
      X500NameBuilder nameBuilder) {
    if (value.length() > 0) {
      nameBuilder.addRDN(objectId, value);
    }
  }
  
  /**
   * 获得KeyStore
   * 
   * @param keyStorePath
   * @param password
   * @return
   * @throws Exception
   */
  public static KeyStore getKeyStore(String keyStorePath, String password)
          throws Exception {
      FileInputStream is = new FileInputStream(keyStorePath);
      KeyStore ks = KeyStore.getInstance(KEY_STORE);
      ks.load(is, password.toCharArray());
      is.close();
      return ks;
  }
  
  /**
   * 由KeyStore获得私钥
   * 
   * @param keyStorePath
   * @param alias
   * @param password
   * @return
   * @throws Exception
   */
  public static PrivateKey getPrivateKey(String keyStorePath, String alias,
          String password) throws Exception {
      KeyStore ks = getKeyStore(keyStorePath, password);
      PrivateKey key = (PrivateKey) ks.getKey(alias, password.toCharArray());
      return key;
  }

  /**
   * 由Certificate获得公钥
   * 
   * @param certificatePath
   * @return
   * @throws Exception
   */
  public static PublicKey getPublicKey(String certificatePath)
          throws Exception {
      Certificate certificate = getCertificate(certificatePath);
      PublicKey key = certificate.getPublicKey();
      return key;
  }

  /**
   * 获得Certificate
   * 
   * @param certificatePath
   * @return
   * @throws Exception
   */
  public static Certificate getCertificate(String certificatePath)
          throws Exception {
      CertificateFactory certificateFactory = CertificateFactory
              .getInstance(X509);
      FileInputStream in = new FileInputStream(certificatePath);

      Certificate certificate = certificateFactory.generateCertificate(in);
      in.close();

      return certificate;
  }

  /**
   * 获得Certificate
   * 
   * @param keyStorePath
   * @param alias
   * @param password
   * @return
   * @throws Exception
   */
  public static Certificate getCertificate(String keyStorePath,
          String alias, String password) throws Exception {
      KeyStore ks = getKeyStore(keyStorePath, password);
      Certificate certificate = ks.getCertificate(alias);

      return certificate;
  }
 
  
  /**
   * Generates a 2048 bit RSA key pair using SHA1PRNG.
   * 
   * @return
   * @throws NoSuchAlgorithmException
   */
  private static KeyPair createKeyPair() throws NoSuchAlgorithmException {
    final KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
    final SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
    random.setSeed(Long.toString(System.currentTimeMillis()).getBytes());
    keyGen.initialize(2048, random);
    final KeyPair keypair = keyGen.generateKeyPair();
    return keypair;
  }

  
  /**
   * @param keystore 
   * @return 
   * @throws KeyStoreException 
   * @throws IOException 
   * @throws CertificateException 
   * @throws NoSuchAlgorithmException 
   */ 
  public static final String keyStore2String(KeyStore keystore) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException { 
   final ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
   keystore.store(baos, PASSPHRASE); 
   final byte[] bytes = baos.toByteArray(); 
   baos.close(); 
   return Base64.encodeBase64URLSafeString(bytes); 
  } 
  
  /**
   * @param str 
   * @return 
   * @throws KeyStoreException 
   * @throws IOException 
   * @throws CertificateException 
   * @throws NoSuchAlgorithmException 
   */ 
  public static final KeyStore string2Keystore(String str) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException { 
   final byte[] bytes = Base64.decodeBase64(str); 
   final ByteArrayInputStream bais = new ByteArrayInputStream(bytes); 
   final KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType()); 
   ks.load(bais, PASSPHRASE); 
   bais.close(); 
   return ks; 
  } 
  
  
  public static void savePEM(KeyStore keystore, String alias, String pemFilePath) throws CertificateManagerException{
    try {
      PrivateKey key = (PrivateKey) keystore.getKey(alias, PASSPHRASE);
      savePEM(key, PASSPHRASE, pemFilePath);
      
    } catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException | OperatorCreationException | IOException ex) {
       throw new CertificateManagerException(ex);
    }
  }
  
  public static void saveX509Certificate(KeyStore keystore, String alias, String caCertPath) throws KeyStoreException, CertificateManagerException{
    
    X509Certificate certificate = (X509Certificate) keystore.getCertificate(alias);
    saveX509Certificate(certificate, caCertPath);
  }

  public static void saveX509Certificate(X509Certificate certificate, String caCertPath)
      throws CertificateManagerException {
    FileOutputStream stream = null;
    try {
      stream = new FileOutputStream(caCertPath);
      stream.write(certificate.getEncoded());

    } catch (CertificateEncodingException | IOException ex) {
      throw new CertificateManagerException(ex);
    } finally {
        IOUtils.closeQuietly(stream);
    }

  }

  /**
   * 
   * @param key
   * @param pemPassword the password of the pem file
   * @param pemPath like "private.pem"
   * @throws OperatorCreationException 
   * @throws IOException 
   * @throws Exception
   */
  public static void savePEM(PrivateKey privateKey, char[] pemPassword, String pemPath) throws OperatorCreationException, IOException
      {
    FileWriter fos = null;
    try {
      ASN1ObjectIdentifier m = org.bouncycastle.openssl.PKCS8Generator.PBE_SHA1_3DES;
      JceOpenSSLPKCS8EncryptorBuilder encryptorBuilder =
          new org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8EncryptorBuilder(m);
      encryptorBuilder.setRandom(new java.security.SecureRandom());
      encryptorBuilder.setPasssword(pemPassword);
      OutputEncryptor oe = encryptorBuilder.build();
      JcaPKCS8Generator gen = new org.bouncycastle.openssl.jcajce.JcaPKCS8Generator(privateKey, oe);
      PemObject privKeyObj = gen.generate();
      fos = new java.io.FileWriter(pemPath);
      JcaPEMWriter pem = new org.bouncycastle.openssl.jcajce.JcaPEMWriter(fos);
      pem.writeObject(privKeyObj);
      pem.flush();
      fos.close();

    } finally {
      IOUtils.closeQuietly(fos);
    }

  }

  
  /**
   * Code c/o http://stackoverflow.com/questions/12501117/programmatically-obtain-keystore-from-pem 
   * @param pemFile 
   * @return 
   * @throws IOException 
   * @throws CertificateException 
   * @throws InvalidKeySpecException 
   * @throws NoSuchAlgorithmException 
   * @throws KeyStoreException 
   */ 
  public static KeyStore pem2Keystore(File pemFile) throws IOException, CertificateException,  
    InvalidKeySpecException, NoSuchAlgorithmException, KeyStoreException { 
   byte[] certAndKey = FileUtils.readFileToByteArray(pemFile); 
      byte[] certBytes = parseDERFromPEM(certAndKey, "-----BEGIN CERTIFICATE-----", "-----END CERTIFICATE-----"); 
      byte[] keyBytes = parseDERFromPEM(certAndKey, "-----BEGIN PRIVATE KEY-----", "-----END PRIVATE KEY-----"); 
  
      X509Certificate cert = generateCertificateFromDER(certBytes);               
      RSAPrivateKey key  = generatePrivateKeyFromDER(keyBytes); 
       
      KeyStore keystore = KeyStore.getInstance(KEY_STORE); 
      keystore.load(null); 
      keystore.setCertificateEntry("cert-alias", cert); 
      keystore.setKeyEntry(SELF_USER_CA_JKS_ALIAS, key, PASSPHRASE, new Certificate[] {cert}); 
      return keystore; 
  } 
  
  private static byte[] parseDERFromPEM(byte[] pem, String beginDelimiter, String endDelimiter) { 
      String data = new String(pem); 
      String[] tokens = data.split(beginDelimiter); 
      tokens = tokens[1].split(endDelimiter); 
      return DatatypeConverter.parseBase64Binary(tokens[0]);         
  } 
  
  private static RSAPrivateKey generatePrivateKeyFromDER(byte[] keyBytes) throws InvalidKeySpecException, NoSuchAlgorithmException { 
      PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes); 
  
      KeyFactory factory = KeyFactory.getInstance("RSA"); 
  
      return (RSAPrivateKey)factory.generatePrivate(spec);         
  } 
  
  private static X509Certificate generateCertificateFromDER(byte[] certBytes) throws CertificateException { 
      CertificateFactory factory = CertificateFactory.getInstance("X.509"); 
  
      return (X509Certificate)factory.generateCertificate(new ByteArrayInputStream(certBytes));       
  } 
  
  public static void main(String[] args) throws Exception{
    
    String keyStorePath = "D:/work/tfs/Toprank BasePlatform Solution/toprank-infrastructure/infrastructure-security/src/main/resources/META-INF/cert/keys.keystore";
    String clientCertPath = "D:/work/tfs/Toprank BasePlatform Solution/toprank-infrastructure/infrastructure-security/src/main/resources/META-INF/cert/client.crt";
    String clientKeyPath = "D:/work/tfs/Toprank BasePlatform Solution/toprank-infrastructure/infrastructure-security/src/main/resources/META-INF/cert/client.key";

    CertificateDetailsInfo rootInfo = new CertificateDetailsInfo("Alias test", "Toprank Root CA", "Toprank Root CA", "BYD Root CA",
        Integer.toHexString(System.getProperty("user.name").hashCode()) 
        + Integer.toHexString(System.getProperty("user.home").hashCode()), "CN", "gd", "10", "", null);
    
    CertificateDetailsInfo userInfo = new CertificateDetailsInfo("Alias test", "192.168.1.1", "Toprank", "BYD",
        Integer.toHexString(System.getProperty("user.name").hashCode()) 
        + Integer.toHexString(System.getProperty("user.home").hashCode()), "CN", "gd", "10", "wangzj@toprank.cc", null);
    
    KeyStore keystore = SslCertificateUtils.createRootCA(keyStorePath, rootInfo);
    
    KeyStore userkeystore = SslCertificateUtils.createCertForHost(keystore, userInfo, "toprank.cc");
    //save Keystore instance into keystore file
    File keyStoreFile = new File(keyStorePath);
    KeyStoreUtils.writeKeyStore(userkeystore, PASSPHRASE, keyStoreFile);
    
    saveX509Certificate(userkeystore, "self_user_ca_jks", clientCertPath);
    savePEM(userkeystore, "self_user_ca_jks", clientKeyPath);

    
  }
 }
