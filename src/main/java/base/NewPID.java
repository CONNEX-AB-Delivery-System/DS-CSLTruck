package base;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class NewPID {

    public static int calculatePID(int[] rawValues){

        List thresholdCut = new Vector();
        List originalRawValues = new Vector();

        int[] min = {0,0};
        int[] finalIndex = {0,0};

        int PIDValue = 0;
        double summation;

        for (int sensorValue: rawValues){
            originalRawValues.add(sensorValue);
        }

        for (int sensorValue: rawValues) {
            thresholdCut.add(sensorValue);
        }

        int minIndex1 = thresholdCut.indexOf(Collections.min(thresholdCut));
        min[0] = (int)thresholdCut.get(minIndex1);
        thresholdCut.remove(minIndex1);

        int minIndex2 = thresholdCut.indexOf(Collections.min(thresholdCut));
        min[1] = (int)thresholdCut.get(minIndex2);
        thresholdCut.remove(minIndex2);

        finalIndex[0] = originalRawValues.indexOf(min[0]);
        finalIndex[1] = originalRawValues.indexOf(min[1]);

        summation = (int)originalRawValues.get(finalIndex[0]) + (int)originalRawValues.get(finalIndex[1]);

        if (finalIndex[0] == 0){
            PIDValue = (int)(summation / 2 * -0.8);
        }
        else if (finalIndex[0] == 1){
            PIDValue = (int)(summation / 2 * -0.4);
        }
        else if (finalIndex[0] == 2){
            PIDValue = (int)(summation / 2 * -0.2);
        }
        else if (finalIndex[0] == 3){
            PIDValue = (int)(summation / 2 * 0.1);
        }
        else if (finalIndex[0] == 4){
            PIDValue = (int)(summation / 2 * 0.2);
        }
        else if (finalIndex[0] == 5){
            PIDValue = (int)(summation / 2 * 0.3);
        }
        else if (finalIndex[0] == 6){
            PIDValue = (int)(summation / 2 * 0.8);
        }

        return PIDValue;
    }
}


