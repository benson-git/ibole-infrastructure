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

package com.github.ibole.infrastructure.security.key;

import com.github.ibole.infrastructure.common.exception.MoreThrowables;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
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
public class PemUtils {
  
  private static byte[] parsePEMFile(File pemFile) throws IOException {
    if (!pemFile.isFile() || !pemFile.exists()) {
      throw new FileNotFoundException(String.format("The file '%s' doesn't exist.",
          pemFile.getAbsolutePath()));
    }
    PemReader reader = null;
    PemObject pemObject;
    try {
      reader = new PemReader(new FileReader(pemFile));
      pemObject = reader.readPemObject();
    } finally {
      IOUtils.closeQuietly(reader);
    }
    return pemObject.getContent();
  }

  private static PublicKey getPublicKey(byte[] keyBytes, String algorithm) {
    PublicKey publicKey = null;
    try {
      KeyFactory kf = KeyFactory.getInstance(algorithm);
      EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
      publicKey = kf.generatePublic(keySpec);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      MoreThrowables.throwIfUnchecked(e);
    }
    return publicKey;
  }

  private static PrivateKey getPrivateKey(byte[] keyBytes, String algorithm) {
    PrivateKey privateKey = null;
    try {
      KeyFactory kf = KeyFactory.getInstance(algorithm);
      EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
      privateKey = kf.generatePrivate(keySpec);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      MoreThrowables.throwIfUnchecked(e);
    }
    return privateKey;
  }

  public static PublicKey readPublicKeyFromFile(String filepath, String algorithm)
      throws IOException {
    byte[] bytes = PemUtils.parsePEMFile(new File(filepath));
    return PemUtils.getPublicKey(bytes, algorithm);
  }

  public static PrivateKey readPrivateKeyFromFile(String filepath, String algorithm)
      throws IOException {
    byte[] bytes = PemUtils.parsePEMFile(new File(filepath));
    return PemUtils.getPrivateKey(bytes, algorithm);
  }

}
