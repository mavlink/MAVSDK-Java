package io.mavsdk.example;

import io.mavsdk.System;
import io.mavsdk.shell.Shell;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemShell {
  private static final Logger logger = LoggerFactory.getLogger(SystemShell.class);
  private static final BufferedReader BUFFERED_READER
      = new BufferedReader(new InputStreamReader(java.lang.System.in));
  private static final String CMD_EXIT = "exit";

  public static void main(String[] args) {
    logger.debug("Starting example: system shell");

    System drone = new System();

    // Logging shell response
    drone.getShell().getReceive()
          .subscribe(logger::debug, throwable -> logger.error("Failed to receive: "
                + ((Shell.ShellException) throwable).getCode()));

    // Reading and sending command
    while (true) {
      String command = readLine();

      if (command == null) {
        continue;
      }
      if (command.equalsIgnoreCase(CMD_EXIT)) {
        break;
      }

      drone.getShell().send(command)
            .subscribe(() -> { }, throwable -> logger.error("Failed to send: "
                  + ((Shell.ShellException) throwable).getCode()));
    }
  }

  private static String readLine() {
    try {
      return BUFFERED_READER.readLine();
    } catch (IOException e) {
      return null;
    }
  }
}
