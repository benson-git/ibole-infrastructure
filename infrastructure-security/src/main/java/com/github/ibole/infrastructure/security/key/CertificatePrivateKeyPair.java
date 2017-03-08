/**
 * 
 */
package com.github.ibole.infrastructure.security.key;

import java.security.PrivateKey;
import java.security.cert.Certificate;

/*********************************************************************************************.
 * 
 * 
 * <p>Copyright 2016, iBole Inc. All rights reserved.
 * 
 * <p></p>
 *********************************************************************************************/


/**
 * @author bwang (chikaiwang@hotmail.com)
 *
 */
public class CertificatePrivateKeyPair {
  
  private Certificate certificate;
  
  private PrivateKey privateKey;

  public CertificatePrivateKeyPair(PrivateKey privKey, Certificate cert) {
    privateKey = privKey;
    certificate = cert;
  }

  /**
   * @return the certificate
   */
  public Certificate getCertificate() {
    return certificate;
  }

  /**
   * @param certificate the certificate to set
   */
  public void setCertificate(Certificate certificate) {
    this.certificate = certificate;
  }

  /**
   * @return the privateKey
   */
  public PrivateKey getPrivateKey() {
    return privateKey;
  }

  /**
   * @param privateKey the privateKey to set
   */
  public void setPrivateKey(PrivateKey privateKey) {
    this.privateKey = privateKey;
  }
  
  

}
