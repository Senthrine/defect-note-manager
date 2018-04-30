import com.sun.istack.internal.NotNull;

public interface IDefectVersionInfo
{
    @NotNull
    String getVersion();

    @NotNull
    DefectVersionStatesEnum getState();
}
