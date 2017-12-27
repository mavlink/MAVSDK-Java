package io.dronecore.core;

public class Device {
  private long uuid;

  public Device(long uuid) {
    this.uuid = uuid;
  }

  public long getUuid() {
    return uuid;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Device device = (Device) o;

    return uuid == device.uuid;
  }

  @Override
  public int hashCode() {
    return (int) (uuid ^ (uuid >>> 32));
  }
}
