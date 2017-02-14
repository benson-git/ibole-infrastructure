package io.ibole.infrastructure.common.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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


/**
 * Helper to ensure temporary directory is sane.
 *
 */
public class TemporaryDirectory {
  private TemporaryDirectory() {
    // empty
  }

  // NOTE: Do not reset the system property, we can not ensure this value will be used

  public static File get() throws IOException {
    String location = System.getProperty("java.io.tmpdir", "tmp");
    File dir = new File(location).getCanonicalFile();
    DirectoryHelper.mkdir(dir.toPath());

    // ensure we can create temporary files in this directory
    Path file = Files.createTempFile("nexus-tmpcheck", ".tmp");
    Files.delete(file);
    return dir;
  }
}
