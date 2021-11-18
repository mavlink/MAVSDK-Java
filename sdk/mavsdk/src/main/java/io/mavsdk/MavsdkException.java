package io.mavsdk;

public class MavsdkException extends Exception {

    public MavsdkException(String message) {
        super(message);
    }

    public MavsdkException(String message, Throwable cause) {
        super(message, cause);
    }

    public MavsdkException(Throwable cause) {
        super(cause);
    }
}
