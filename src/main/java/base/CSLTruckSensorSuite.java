package base;

import lejos.hardware.port.SensorPort;
import ev3dev.sensors.ev3.EV3IRSensor;

public class CSLTruckSensorSuite {

    //sensor for line reading - connected to sensor port S4
    private LineReaderV2 lineReader;
    //sensor for object proximity detection - connected to port S1
    private final EV3IRSensor proximitySensor;

    public void printLineValues() {
        int pidValue = this.lineReader.getPIDValue();

        System.out.println("Current value" + pidValue);
    }

    public CSLTruckSensorSuite() {

        this.lineReader = new LineReaderV2(SensorPort.S4);
        this.proximitySensor = new EV3IRSensor(SensorPort.S1);

        //int value = this.lineReader.getPIDValue();

        System.out.println("Sensors initialized");

    }

}
