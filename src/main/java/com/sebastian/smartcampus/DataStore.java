package com.sebastian.smartcampus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataStore {

    public static final Map<String, Room> rooms = new HashMap<>();
    public static final Map<String, Sensor> sensors = new HashMap<>();
    public static final Map<String, List<SensorReading>> readings = new HashMap<>();

    static {
        rooms.put("LIB-301", new Room("LIB-301", "Library Quiet Study", 40));
        rooms.put("ENG-101", new Room("ENG-101", "Engineering Lab", 25));

        sensors.put("TEMP-001", new Sensor("TEMP-001", "Temperature", "ACTIVE", 21.5, "LIB-301"));
        sensors.put("CO2-001", new Sensor("CO2-001", "CO2", "ACTIVE", 550.0, "ENG-101"));

        rooms.get("LIB-301").getSensorIds().add("TEMP-001");
        rooms.get("ENG-101").getSensorIds().add("CO2-001");

        readings.put("TEMP-001", new ArrayList<>());
        readings.put("CO2-001", new ArrayList<>());

        readings.get("TEMP-001").add(new SensorReading("R-001", System.currentTimeMillis(), 21.5));
        readings.get("CO2-001").add(new SensorReading("R-002", System.currentTimeMillis(), 550.0));
    }
}