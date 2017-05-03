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

package com.github.ibole.infrastructure.common.io;

import java.io.IOException;
import java.io.InputStream;

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
 * 
 * An {@link InputStream} that can report the progress of reading from another InputStream to a 
 * {@link ProgressListener}.
 * @author bwang
 *
 */
public class ProgressInputStream extends InputStream {
  private final InputStream stream;
  private final ProgressListener listener;

  private long total;
  private long totalRead;
  private int progress;

  /**
   * Constructs a ProgressInputStream that wraps another InputStream.
   * 
   * @param stream the stream whose progress will be monitored.
   * @param listener the listener that will receive progress updates.
   * @param total the total number of bytes that are expected to be read from the stream.
   */
  public ProgressInputStream(InputStream stream, ProgressListener listener, long total) {
    this.stream = stream;
    this.listener = listener;
    this.total = total;
  }

  /**
   * Gets the total number of bytes that are expected to be read from the stream.
   * 
   * @return the total number of bytes.
   */
  public long getTotal() {
    return this.total;
  }

  /**
   * Sets the total number of bytes that are expected to be read from the stream.
   * 
   * @param total the total number of bytes
   */
  public void setTotal(long total) {
    this.total = total;
  }

  @Override
  public void close() throws IOException {
    this.stream.close();
  }

  @Override
  public int read() throws IOException {
    int read = this.stream.read();
    this.totalRead++;
    this.listener.onProgressChanged(this.totalRead, this.total);

    return read;
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    int read = this.stream.read(b, off, len);
    this.totalRead += read;
    this.listener.onProgressChanged(this.totalRead, this.total);

    return read;
  }
}
