package org.veupathdb.service.userds.model;

public class IrodsStatus
{
  public static final byte
    SUCCESS = 0,
    FAILURE = 1;

  private final byte status;

  private final int dsId;

  private IrodsStatus(byte status, int dsId) {
    this.status = status;
    this.dsId = dsId;
  }

  public boolean isSuccess() {
    return status == SUCCESS;
  }

  public byte getStatus() {
    return status;
  }

  public int getDsId() {
    return dsId;
  }

  public static IrodsStatus success(int dsId) {
    return new IrodsStatus(SUCCESS, dsId);
  }
  public static IrodsStatus failure() {
    return new IrodsStatus(FAILURE, -1);
  }
}
