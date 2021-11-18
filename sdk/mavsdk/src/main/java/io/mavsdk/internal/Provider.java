package io.mavsdk.internal;

import io.mavsdk.Plugin;

public interface Provider<T extends Plugin> {
  T get();
}
