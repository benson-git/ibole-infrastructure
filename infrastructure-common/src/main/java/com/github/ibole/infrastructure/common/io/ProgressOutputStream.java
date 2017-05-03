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
import java.io.OutputStream;

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
 * An {@link OutputStream} that can report the progress of writing to another OutputStream to a
 * {@link ProgressListener}.
 */
class ProgressOutputStream extends OutputStream {
  private final OutputStream stream;
  private final ProgressListener listener;

  private long total;
  private long totalWritten;

  public ProgressOutputStream(OutputStream stream, ProgressListener listener, long total) {
    this.stream = stream;
    this.listener = listener;
    this.total = total;
  }

  public long getTotal() {
    return this.total;
  }

  public void setTotal(long total) {
    this.total = total;
  }

  @Override
  public void close() throws IOException {
    this.stream.close();
  }

  @Override
  public void write(byte[] b) throws IOException {
    this.stream.write(b);
    this.totalWritten += b.length;
    this.listener.onProgressChanged(this.totalWritten, this.total);
  }

  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    this.stream.write(b, off, len);
    if (len < b.length) {
      this.totalWritten += len;
    } else {
      this.totalWritten += b.length;
    }
    this.listener.onProgressChanged(this.totalWritten, this.total);
  }

  @Override
  public void write(int b) throws IOException {
    this.stream.write(b);
    this.totalWritten++;
    this.listener.onProgressChanged(this.totalWritten, this.total);
  }
}
