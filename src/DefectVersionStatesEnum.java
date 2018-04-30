import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.stream.Stream;

public enum DefectVersionStatesEnum
{
    NOT_STARTED("Not Started"),
    COMMITED("Commited"),
    PR_CREATED("PR Created"),
    DELIVERED("Delivered");

    private String title;

    DefectVersionStatesEnum(String title)
    {
        this.title = title;
    }

    @Override
    public String toString()
    {
        return title;
    }

    @Nullable
    public static DefectVersionStatesEnum getFromString(String string)
    {
        return Stream.of(values()).
                filter(defectStatesEnum -> defectStatesEnum.toString().equals(string))
                .findAny()
                .orElse(null);
    }
    @NotNull
    public static DefectVersionStatesEnum getDefault()
    {
        return NOT_STARTED;
    }
}
