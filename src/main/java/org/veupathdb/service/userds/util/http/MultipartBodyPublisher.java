package org.veupathdb.service.userds.util.http;

import java.io.*;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.nio.charset.StandardCharsets;

public class MultipartBodyPublisher
{
  public static HttpRequest.BodyPublisher publish(
    final String file,
    final InputStream content,
    final String boundary
  ) {
    var tmp = new BoundaryInputStream(file, content, boundary);
    return BodyPublishers.ofInputStream(() -> tmp);
  }

}

class BoundaryInputStream extends InputStream
{
  private static final String
    boundaryLeader   = "--",
    boundaryTrail    = "\r\n",
    contentDisp      = "Content-Disposition: form-data; name=file; filename=",
    contentDispTrail = "\r\n",
    contentType      = "Content-Type: application/binary",
    contentTypeTrail = "\r\n\r\n";

  private static final byte
    IN_HEAD = 0,
    IN_BODY = 1,
    IN_TAIL = 2,
    DONE    = 3;

  private final byte[]      leader;
  private final InputStream reader;
  private final byte[]      trailer;

  private byte state;
  private int  pos;

  BoundaryInputStream(String file, InputStream is, String boundary) {
    this.state = IN_HEAD;
    this.reader = is;

    this.leader = (boundaryLeader + boundary + boundaryTrail +
      contentDisp + file + contentDispTrail +
      contentType + contentTypeTrail).getBytes(StandardCharsets.UTF_8);
    this.trailer = (boundaryLeader + boundary + boundaryLeader)
      .getBytes(StandardCharsets.UTF_8);
  }

  @Override
  public int read() throws IOException {
    return switch (state) {
      case IN_BODY -> {
        var tmp = reader.read();
        if (tmp > -1) {
          state = IN_TAIL;
          pos = 0;
          tmp = unsignByte(trailer[pos++]);
        }
        yield tmp;
      }
      case IN_HEAD -> {
        var out = unsignByte(leader[pos++]);
        if (pos == leader.length)
          state = IN_BODY;
        yield out;
      }
      case IN_TAIL -> {
        var out = unsignByte(trailer[pos++]);
        if (pos == trailer.length)
          state = DONE;
        yield out;
      }
      default -> -1;
    };
  }

  private static int unsignByte(byte b) {
    return b & 0xFF;
  }

  @Override
  public void close() throws IOException {
    reader.close();
  }
}
