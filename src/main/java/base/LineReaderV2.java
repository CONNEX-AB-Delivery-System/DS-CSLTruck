package base;

/**
 * Mindsensors LineReader V2 sensor driver
 *
 * ev3dev hardware driver:
 * http://docs.ev3dev.org/projects/lego-linux-drivers/en/ev3dev-jessie/sensor_data.html#ms-line-leader
 *
 */

import ev3dev.sensors.BaseSensor;
import lejos.hardware.port.Port;

public class LineReaderV2 extends BaseSensor {

    private static final String MINDSENSORS_LINEREADERV2 = "ms-line-leader";

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

    //???
    private void initModes() {
        this.setStringAttribute("mode", "TRACK");
    }

    public LineReaderV2(final Port portName) {
        super(portName, LEGO_I2C, MINDSENSORS_LINEREADERV2 );
        this.initModes();
    }

    /**
     * Send a single byte command represented by a letter
     * @param cmd the letter that identifies the command
     */
    public void sendCommand(final String cmd) {
        this.setStringAttribute("command", cmd);
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


}
