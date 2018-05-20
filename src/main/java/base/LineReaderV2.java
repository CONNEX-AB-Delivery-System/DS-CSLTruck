package base;

import ev3dev.sensors.BaseSensor;
import lejos.hardware.port.Port;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Mindsensors LineReader V2 sensor driver
 *
 * ev3dev hardware driver:
 * http://docs.ev3dev.org/projects/lego-linux-drivers/en/ev3dev-jessie/sensor_data.html#ms-line-leader
 *
 * Example: https://github.com/ev3dev-lang-java/ev3dev-lang-java/blob/develop/src/main/java/ev3dev/sensors/mindsensors/NXTCamV5.java
 * Example: https://github.com/ev3dev-lang-java/ev3dev-lang-java/blob/develop/src/main/java/ev3dev/hardware/EV3DevDevice.java
 * Example: https://github.com/ev3dev-lang-java/ev3dev-lang-java/blob/develop/src/main/java/ev3dev/hardware/EV3DevSensorDevice.java
 *
 * BaseSensor class: https://github.com/ev3dev-lang-java/ev3dev-lang-java/blob/develop/src/main/java/ev3dev/sensors/BaseSensor.java
 */


public class LineReaderV2 extends BaseSensor {

    private static final String MINDSENSORS_LINEREADERV2 = "ms-line-leader";

    private int lightArrayLength = 8;

    private int[] intArray = new int[this.lightArrayLength];

    private String lineColorMode = "black";

    //MODES

    /**
     * Line Follower
     */
    public static final String PID = "PID";

    /**
     * Line Follower - all values
     */
    public static final String PID_ALL = "PID-ALL";

    /**
     * Calibrated values
     */
    public static final String CAL = "CAL";

    /**
     * Uncalibrated values
     */
    public static final String RAW = "RAW";

    private final Set<String> trackingAllowedModeList = new HashSet<>(
            Arrays.asList(PID, PID_ALL, CAL, RAW));

    //???
    private void initModes() {
        this.setStringAttribute("mode", "PID");
    }

    private void setMode(String mode) {
        //TODO: allow only allowed modes
        this.setStringAttribute("mode", mode);
    }

    public LineReaderV2(final Port portName) {
        super(portName, LEGO_I2C, MINDSENSORS_LINEREADERV2 );
        this.initModes();
    }


    /**
     * Get the PID mode value
     *
     * @return PID value (percentage -100;100)
     */
    public int getPIDValue() {
        this.setMode("PID");
        return this.getIntegerAttribute("value0");
    }

    /**
     * Get the PID-ALL mode values
     *
     * @return  array of values:
     *          value0: Steering (-100 to 100)
     *          value1: Average (0 to 80)
     *          value2: Result (as bits)
     */
    public int[] getPIDALLValues() {
        int lightArrayLength = 3;
        int[] pidArray = new int[lightArrayLength];

        this.setMode("PID-ALL");
        for (int i = 0; i < lightArrayLength; i++) {
            String value = "value" + i;
            pidArray[i] = this.getIntegerAttribute(value);
        }

        return pidArray;
    }

    /**
     * Get the CAL values
     *
     * @return return array of values from individual IR lights (0 - 7)
     */
    public int[] getCALValues() {

        Arrays.fill(intArray,0);
        this.setMode("CAL");

        for (int i = 0; i < this.lightArrayLength; i++) {
            String value = "value" + i;
            intArray[i] = this.getIntegerAttribute(value);
        }

        return intArray;
    }

    /**
     * Get the RAW values
     *
     * @return return array of values from individual IR lights (0 - 7)
     */
    public int[] getRAWValues() {

        Arrays.fill(intArray,0);
        this.setMode("RAW");

        for (int i = 0; i < lightArrayLength; i++) {
            String value = "value" + i;
            intArray[i] = this.getIntegerAttribute(value);
        }

        return intArray;
    }

    /**
     * Get LineReader line color mode
     *
     * @return color name of mode
     */

    public String getLineColorMode() {
        return this.lineColorMode;
    }


    //COMMANDS

    /**
     * Command	Description
     * CAL-WHITE	Calibrate white
     * CAL-BLACK	Calibrate black
     * SLEEP [67]	Put sensor to sleep
     * WAKE [68]	Wake up the sensor
     * INV-COL	Color inversion (White line on a black background)
     * RST-COL	Reset Color inversion (black line on a white background).
     * SNAP [69]	Take a snapshot.
     * 60HZ	Configures sensor for 60Hz electrical mains
     * 50HZ	Configures sensor for 50Hz electrical mains
     * UNIVERSAL	Configures sensor for any (50/60Hz) electrical mains
     */

    public void wake() {
        this.sendCommand("WAKE");
    }

    public void sleep() {
        this.sendCommand("SLEEP");
    }

    public void calibrateWhite() {
        this.sendCommand("CAL-WHITE");
    }

    public void calibrateBlack() {
        this.sendCommand("CAL-BLACK");
    }

    /**
     * param: color: line color
     */

    public void invertColor(String color) {
        if (color.equals("black"))
        {
            this.sendCommand("RST-COL");
            this.lineColorMode = color;
        }
        if (color.equals("white"))
        {
            this.sendCommand("INV-COL");
            this.lineColorMode = color;
        }

    }

    /**
     * Send a single byte command represented by a letter
     * @param cmd the letter that identifies the command
     */
    public void sendCommand(final String cmd) {
        this.setStringAttribute("command", cmd);
    }


}
