import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DefectsController implements IController
{

    @NotNull private List<Defect> defects = new ArrayList<>();

    public void addDefect(@NotNull IDefectInfoProvider defectInfo)
    {
        defects.add(new Defect(defectInfo));
    }

    public void changeDefect(@NotNull IDefectInfoProvider defectInfo)
    {
        defects.stream()
                .filter(defect -> defect.getDefectName().equals(defectInfo.getDefectName()))
                .findFirst()
                .ifPresent(defect -> changeDefectTo(defect, defectInfo));
    }

    private void changeDefectTo(@NotNull Defect defect, @NotNull IDefectInfoProvider defectInfo)
    {
        defect.setComment(defectInfo.getComment());
        defect.setState(defectInfo.getDefectState());
        List<DefectVersion> versions = defectInfo.getVersionInfoList()
                .stream()
                .map(DefectVersion::new)
                .collect(Collectors.toList());
        defect.setVersions(versions);
    }

    public void cleanData()
    {
        defects.clear();
    }

    public List<IDefectInfoProvider> getDefectInfoSublist(Predicate<IDefectInfoProvider> validator)
    {
        return defects.stream()
                .filter(validator)
                .collect(Collectors.toList());
    }

    public List<IDefectInfo> getDefectInfoList()
    {
        return new ArrayList<>(defects);
    }
}
