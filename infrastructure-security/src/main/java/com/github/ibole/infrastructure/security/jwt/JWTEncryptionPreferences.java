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

package com.github.ibole.infrastructure.security.jwt;

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
public class JWTEncryptionPreferences {

  private String publicKeyID;
  private String privateKey;
  private String privateKeyPassword;
  private EncryptionAlgorithm encryptionAlgorithm;

  /**
   * Returns the ID for public key for validating the JWT signature.
   * 
   * @return the publicKeyID.
   */
  public String getPublicKeyID() {
    return this.publicKeyID;
  }

  /**
   * Sets the ID for public key for validating the JWT signature.
   * 
   * @param publicKeyID the publicKeyID to set.
   */
  public void setPublicKeyID(String publicKeyID) {
    this.publicKeyID = publicKeyID;
  }

  /**
   * Returns the private key for generating the JWT signature.
   * 
   * @return the privateKey.
   */
  public String getPrivateKey() {
    return this.privateKey;
  }

  /**
   * Sets the private key for generating the JWT signature.
   * 
   * @param privateKey the privateKey to set.
   */
  public void setPrivateKey(String privateKey) {
    this.privateKey = privateKey;
  }

  /**
   * Returns the password for the private key.
   * 
   * @return the privateKeyPassword.
   */
  public String getPrivateKeyPassword() {
    return this.privateKeyPassword;
  }

  /**
   * Sets the password for the private key.
   * 
   * @param privateKeyPassword the privateKeyPassword to set.
   */
  public void setPrivateKeyPassword(String privateKeyPassword) {
    this.privateKeyPassword = privateKeyPassword;
  }

  /**
   * Returns the type of encryption algorithm for JWT.
   * 
   * @return the encryptionAlgorithm.
   */
  public EncryptionAlgorithm getEncryptionAlgorithm() {
    return this.encryptionAlgorithm;
  }

  /**
   * Sets the type of encryption algorithm for JWT.
   * 
   * @param encryptionAlgorithm the encryptionAlgorithm to set.
   */
  public void setEncryptionAlgorithm(EncryptionAlgorithm encryptionAlgorithm) {
    this.encryptionAlgorithm = encryptionAlgorithm;
  }
}
