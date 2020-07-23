package org.veupathdb.service.userds.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicLong;

/**
 * InputStream wrapper that calls {@link #notify()} on the given parent object
 * when the stream is closed.
 */
public class InputStreamNotifier extends InputStream
{
  private final InputStream stream;

  private final Object parent;

  private final AtomicLong bytesRead;

  /**
   * @param stream Wrapped InputStream
   * @param parent Object to notify on stream close.
   */
  public InputStreamNotifier(final InputStream stream, final Object parent) {
    this.stream = stream;
    this.parent = parent;

    this.bytesRead = new AtomicLong();
  }

  public long bytesRead() {
    return bytesRead.get();
  }

  @Override
  public int read(byte[] b) throws IOException {
    final var out = stream.read(b);
    bytesRead.addAndGet(out);
    return out;
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    final var out = stream.read(b, off, len);
    bytesRead.addAndGet(out);
    return out;
  }

  @Override
  public byte[] readAllBytes() throws IOException {
    final var out = stream.readAllBytes();
    bytesRead.addAndGet(out.length);
    return out;
  }

  @Override
  public byte[] readNBytes(int len) throws IOException {
    final var out = stream.readNBytes(len);
    bytesRead.addAndGet(out.length);
    return out;
  }

  @Override
  public int readNBytes(byte[] b, int off, int len) throws IOException {
    final var out = stream.readNBytes(b, off, len);
    bytesRead.addAndGet(out);
    return out;
  }

  @Override
  public long skip(long n) throws IOException {
    final var out = stream.skip(n);
    bytesRead.addAndGet(out);
    return out;
  }

  @Override
  public void skipNBytes(long n) throws IOException {
    bytesRead.addAndGet(n);
    stream.skipNBytes(n);
  }

  @Override
  public int available() throws IOException {
    return stream.available();
  }

  @Override
  public void close() throws IOException {
    stream.close();
    synchronized (parent) {
      parent.notifyAll();
    }
  }

  @Override
  public synchronized void mark(int readlimit) {
    stream.mark(readlimit);
  }

  @Override
  public synchronized void reset() throws IOException {
    stream.reset();
  }

  @Override
  public boolean markSupported() {
    return stream.markSupported();
  }

  @Override
  public long transferTo(OutputStream out) throws IOException {
    return stream.transferTo(out);
  }

  @Override
  public int read() throws IOException {
    bytesRead.incrementAndGet();
    return stream.read();
  }
}
