package base;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class NewPID {

    /**
     * This class is used for calculating PID for Mindsensors LineReader V2 sensor
     *
     * This is how the algorithm works:
     * After getting the 8 calibrated sensor values as an argument, the two smallest values which should be
     * occupying the neighboring cells are selected. These selected values should be less than the "threshold"
     * value. The threshold value is calculated from a calibration function which will be run before executing
     * other parts of the code so the sensors values are adjusted to the environment.
     *
     * The function returns -100 as an error value if:
     *      1) the smallest values are above the threshold
     *      2) they are not from the neighboring cells
     *
     * The PID value is calculated based on the position of the black line under the sensor
     *
     * The neighboring cells:
     *      The cell to left: x1
     *      The cell to right: x2
     *
     * Sensor numbers and positions (p):
     *      0  1  2  3  4  5  6  7
     *               ----
     *                0
     *            ----  ----
     *            -15    15
     *         ----        ----
     *         -30          30
     *      ----              ----
     *      -45                45
     *
     * The PID is calculated as:
     *      PID = p + f(x1, x2) where f(x1, x2) = (x1 - x2) / 2
     *      * if f(x1, x2) is more than 15 then the value is replace by 15
     *
     * An example:
     *      0  1  2  3  4  5  6  7
     *      38 37 50 76 87 92 88 85
     *
     *      x1 = 38
     *      x2 = 37
     *
     *      PID = -45 + (38 - 37) / 2 = -44
     */

    public static int calculatePID(int[] rawValues, int thresholdValue){

        List<Integer> thresholdCut = new Vector<>();
        List<Integer> originalRawValues = new Vector<>();

        int[] min = {0,0};
        int[] finalIndex = {0,0};

        int PIDValue = 0;
        int x1, x2;
        int calculationIndex;

        // putting the values into list
        for (int sensorValue: rawValues){
            originalRawValues.add(sensorValue);
        }

        for (int sensorValue: rawValues) {
            thresholdCut.add(sensorValue);
        }

        // selecting the smallest values indexes
        int minIndex1 = thresholdCut.indexOf(Collections.min(thresholdCut));
        min[0] = thresholdCut.get(minIndex1);
        thresholdCut.remove(minIndex1);

        int minIndex2 = thresholdCut.indexOf(Collections.min(thresholdCut));
        min[1] = thresholdCut.get(minIndex2);
        thresholdCut.remove(minIndex2);

        // checking if the selected values are below threshold
        if (min[0] > thresholdValue | min[1] > thresholdValue){
            return -100;
        }

        //checking if the cells are neighbors
        if (Math.abs(minIndex1 - minIndex2) > 1){
            return  -100;
        }

        finalIndex[0] = originalRawValues.indexOf(min[0]);
        finalIndex[1] = originalRawValues.indexOf(min[1]);

        if (finalIndex[0] < finalIndex[1]){
            x1 = originalRawValues.get(finalIndex[0]);
            x2 = originalRawValues.get(finalIndex[1]);
            calculationIndex = finalIndex[0];
        }
        else{
            x1 = originalRawValues.get(finalIndex[1]);
            x2 = originalRawValues.get(finalIndex[0]);
            calculationIndex = finalIndex[1];
        }


        if (calculationIndex == 0){
            PIDValue = -60 + positionAdjustment(x1, x2);
        }
        else if (calculationIndex == 1){
            PIDValue = -40 + positionAdjustment(x1, x2);
        }
        else if (calculationIndex == 2){
            PIDValue = -15 + positionAdjustment(x1, x2);
        }
        else if (calculationIndex == 3){
            PIDValue = positionAdjustment(x1, x2);
        }
        else if (calculationIndex == 4){
            PIDValue = 15 + positionAdjustment(x1, x2);
        }
        else if (calculationIndex == 5){
            PIDValue = 40 + positionAdjustment(x1, x2);
        }
        else if (calculationIndex == 6){
            PIDValue = 60 + positionAdjustment(x1, x2);
        }

        return PIDValue;
    }

    private static int positionAdjustment(int x1, int x2){
        if ((x1 - x2) > 14){
            return 14;
        }
        else if ((x1 - x2) < -14){
            return -14;
        }
        else{
            return (x1 - x2) / 2;
        }
    }
}


