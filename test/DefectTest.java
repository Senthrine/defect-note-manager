import com.sun.istack.internal.NotNull;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DefectTest {
    @Test
    void addDefectVersion() {
        Defect defect = new Defect("defect1", "1", DefectStatesEnum.NOT_STARTED, "", new ArrayList<>());
        IDefectVersionInfo defectVersionInfo = new IDefectVersionInfo() {
            @Override
            public String getVersion() {
                return "2";
            }

            @Override @NotNull
            public DefectVersionStatesEnum getState() {
                return DefectVersionStatesEnum.NOT_STARTED;
            }
        };
        boolean result = defect.addDefectVersion(defectVersionInfo);
        assertTrue(result, "addDefectVersion returns "+ result
                + " after trying to add new defect version, true expected");

        result = defect.addDefectVersion(defectVersionInfo);
        assertFalse(result, "addDefectVersion returns "+ result
                + " after trying to add existing defect version, false expected");

    }

}