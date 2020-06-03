package org.veupathdb.service.userds.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.veupathdb.lib.container.jaxrs.providers.LogProvider;

/**
 * InputStream wrapper that calls {@link #notify()} on the given parent object
 * when the stream is closed.
 */
public class InputStreamNotifier extends InputStream
{
  private final InputStream stream;

  private final Object parent;

  /**
   * @param stream Wrapped InputStream
   * @param parent Object to notify on stream close.
   */
  public InputStreamNotifier(InputStream stream, Object parent) {
    this.stream = stream;
    this.parent = parent;
  }

  @Override
  public int read(byte[] b) throws IOException {
    return stream.read(b);
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    var out = stream.read(b, off, len);
    LogProvider.logger(getClass()).debug(new String(b));
    return out;
  }

  @Override
  public byte[] readAllBytes() throws IOException {
    var out = stream.readAllBytes();
    LogProvider.logger(getClass()).debug(new String(out));
    return out;
  }

  @Override
  public byte[] readNBytes(int len) throws IOException {
    var out = stream.readNBytes(len);
    LogProvider.logger(getClass()).debug(new String(out));
    return out;
  }

  @Override
  public int readNBytes(byte[] b, int off, int len) throws IOException {
    return stream.readNBytes(b, off, len);
  }

  @Override
  public long skip(long n) throws IOException {
    return stream.skip(n);
  }

  @Override
  public void skipNBytes(long n) throws IOException {
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
    return stream.read();
  }
}
