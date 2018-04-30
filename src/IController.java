import com.sun.istack.internal.NotNull;

import java.util.List;
import java.util.function.Predicate;

public interface IController
{
    void addDefect(@NotNull IDefectInfoProvider defectInfo);
    void changeDefect(@NotNull IDefectInfoProvider defectInfo);
    void cleanData();
    List<IDefectInfoProvider> getDefectInfoSublist(Predicate<IDefectInfoProvider> validator);
    List<IDefectInfo> getDefectInfoList();
}
