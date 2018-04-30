import com.sun.istack.internal.NotNull;

import java.util.List;

public interface IDefectInfoProvider
{
    @NotNull
    String getDefectName();

    @NotNull
    String getReportedVersion();

    @NotNull
    DefectStatesEnum getDefectState();

    @NotNull
    List<IDefectVersionInfo> getVersionInfoList();

    @NotNull
    String getComment();
}
