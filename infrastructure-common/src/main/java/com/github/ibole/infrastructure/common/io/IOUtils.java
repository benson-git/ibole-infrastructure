package com.github.ibole.infrastructure.common.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class IOUtils {
  private static final int BUFFER_SIZE = 1024 * 8;

  private IOUtils() {}

  /**
   * write.
   * 
   * @param is InputStream instance.
   * @param os OutputStream instance.
   * @return count Long.
   * @throws IOException if IO exception
   */
  public static long write(InputStream is, OutputStream os) throws IOException {
    return write(is, os, BUFFER_SIZE);
  }

  /**
   * write.
   * 
   * @param is InputStream instance.
   * @param os OutputStream instance.
   * @param bufferSize buffer size.
   * @return count Long.
   * @throws IOException if IO exception
   */
  public static long write(InputStream is, OutputStream os, int bufferSize) throws IOException {
    int read;
    long total = 0;
    byte[] buff = new byte[bufferSize];
    while (is.available() > 0) {
      read = is.read(buff, 0, buff.length);
      if (read > 0) {
        os.write(buff, 0, read);
        total += read;
      }
    }
    return total;
  }

  /**
   * read string.
   * 
   * @param reader Reader instance.
   * @return file content String.
   * @throws IOException if IO exception
   */
  public static String read(Reader reader) throws IOException {
    StringWriter writer = new StringWriter();
    try {
      write(reader, writer);
      return writer.getBuffer().toString();
    } finally {
      writer.close();
    }
  }

  /**
   * write string.
   * 
   * @param writer Writer instance.
   * @param string String.
   * @return count Long.
   * @throws IOException if IO exception
   */
  public static long write(Writer writer, String string) throws IOException {
    Reader reader = new StringReader(string);
    try {
      return write(reader, writer);
    } finally {
      reader.close();
    }
  }

  /**
   * write.
   * 
   * @param reader Reader.
   * @param writer Writer.
   * @return count long.
   * @throws IOException IOException
   */
  public static long write(Reader reader, Writer writer) throws IOException {
    return write(reader, writer, BUFFER_SIZE);
  }

  /**
   * write.
   * 
   * @param reader Reader.
   * @param writer Writer.
   * @param bufferSize buffer size.
   * @return count Long.
   * @throws IOException IOException
   */
  public static long write(Reader reader, Writer writer, int bufferSize) throws IOException {
    int read;
    long total = 0;
    char[] buf = new char[BUFFER_SIZE];
    while ((read = reader.read(buf)) != -1) {
      writer.write(buf, 0, read);
      total += read;
    }
    return total;
  }

  /**
   * read lines.
   * 
   * @param file file.
   * @return lines String[].
   * @throws IOException IOException
   */
  public static String[] readLines(File file) throws IOException {
    if (file == null || !file.exists() || !file.canRead())
      return new String[0];

    return readLines(new FileInputStream(file));
  }

  /**
   * read lines.
   * 
   * @param is input stream.
   * @return lines String[].
   * @throws IOException IOException
   */
  public static String[] readLines(InputStream is) throws IOException {
    List<String> lines = new ArrayList<String>();
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    try {
      String line;
      while ((line = reader.readLine()) != null)
        lines.add(line);
      return lines.toArray(new String[0]);
    } finally {
      reader.close();
    }
  }

  /**
   * read lines.
   * 
   * @param is input stream.
   * @param  <T> T Type parameter
   * @param type Class
   * @return object from inputstream.
   * @throws IOException Exception
   * @throws ClassNotFoundException Exception
   */
  @SuppressWarnings("unchecked")
  public static <T> T readFils(InputStream is, Class<? extends T> type) throws IOException,
      ClassNotFoundException {
    T t = null;
    ObjectInputStream objIn = null;
    try {
      objIn = new ObjectInputStream(is);
      t = (T) objIn.readObject();

    } finally {
      if (objIn != null) {
        objIn.close();
      }
    }
    return t;
  }

  /**
   * write lines.
   * 
   * @param os output stream.
   * @param lines lines.
   * @throws IOException IOException
   */
  public static void writeLines(OutputStream os, String[] lines) throws IOException {
    PrintWriter writer = new PrintWriter(new OutputStreamWriter(os));
    try {
      for (String line : lines)
        writer.println(line);
      writer.flush();
    } finally {
      writer.close();
    }
  }

  /**
   * write lines.
   * 
   * @param file file.
   * @param lines lines.
   * @throws IOException IOException
   */
  public static void writeLines(File file, String[] lines) throws IOException {
    if (file == null)
      throw new IOException("File is null.");
    writeLines(new FileOutputStream(file), lines);
  }

  /**
   * append lines.
   * 
   * @param file file.
   * @param lines lines.
   * @throws IOException IOException
   */
  public static void appendLines(File file, String[] lines) throws IOException {
    if (file == null)
      throw new IOException("File is null.");
    writeLines(new FileOutputStream(file, true), lines);
  }

  public static void closeInputStream(InputStream inputStream) {

    if (inputStream != null) {
      try {
        inputStream.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

  }

  public static String returnsResource(InputStream in) {
    try {
      byte[] buffer = new byte[in.available()];
      in.read(buffer);
      in.close();
      return new String(buffer);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  public static String returnsResource(String file) {
    try {
      return returnsResource(new FileInputStream(file));
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
