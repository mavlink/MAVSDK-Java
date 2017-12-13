package io.dronecore.example;

import io.dronecore.DroneCore;

public class DroneCoreExample {
    public static void main(String[] args) {
        System.out.println("starting...");

        DroneCore dc = new DroneCore();
        dc.connect();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
