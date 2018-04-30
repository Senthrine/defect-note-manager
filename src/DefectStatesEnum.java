import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.stream.Stream;

public enum DefectStatesEnum
{
    NOT_STARTED("Not Started"),
    IN_PROGRESS("In Progress"),
    RESOLVED("Resolved"),
    REJECTED("Rejected"),
    VALIDATED("Validated");

    @NotNull private String title;

    DefectStatesEnum(@NotNull String title)
    {
        this.title = title;
    }

    @Override @NotNull
    public String toString()
    {
        return title;
    }

    public boolean isValidated()
    {
        return this == VALIDATED;
    }

    @Nullable
    public static DefectStatesEnum getFromString(String string)
    {
        return Stream.of(values())
                .filter(defectStatesEnum -> defectStatesEnum.toString().equals(string))
                .findAny()
                .orElse(null);
    }

    @NotNull
    public static DefectStatesEnum getDefault()
    {
        return NOT_STARTED;
    }
}
