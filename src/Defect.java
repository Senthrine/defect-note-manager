import com.sun.istack.internal.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Defect implements IDefectInfo
{

    @NotNull private String name;
    @NotNull private String reportedVersion;
    @NotNull private DefectStatesEnum state;
    @NotNull private List<DefectVersion> versions = new ArrayList<>();
    @NotNull private String comment;

    public Defect(@NotNull String name, @NotNull String reportedVersion, @NotNull DefectStatesEnum state,
                  @NotNull String comment, @NotNull List<DefectVersion> versions)
    {
        this.name = name;
        this.reportedVersion = reportedVersion;
        this.state = state;
        this.comment = comment;
        this.versions = versions;
    }

    public Defect(@NotNull IDefectInfoProvider defectInfo)
    {
        name = defectInfo.getDefectName();
        reportedVersion = defectInfo.getReportedVersion();
        state = defectInfo.getDefectState();
        comment = defectInfo.getComment();
        versions = defectInfo.getVersionInfoList()
                .stream()
                .filter(Objects::nonNull)
                .map(DefectVersion::new)
                .collect(Collectors.toList());
    }

    @NotNull
    public String getDefectName()
    {
        return name;
    }

    @NotNull
    public String getReportedVersion()
    {
        return reportedVersion;
    }

    @NotNull
    public DefectStatesEnum getDefectState()
    {
        return state;
    }

    public void setState(@NotNull DefectStatesEnum state)
    {
        this.state = state;
    }

    @NotNull
    public List<IDefectVersionInfo> getVersionInfoList()
    {
        return new ArrayList<>(versions);
    }

    public void setVersions(@NotNull List<DefectVersion> versions)
    {
        this.versions = versions;
    }

    @NotNull
    public String getComment()
    {
        return comment;
    }

    public void setComment(@NotNull String comment)
    {
        this.comment = comment;
    }

    public Element getXmlElement(@NotNull Document doc)
    {
        Element defectElement = doc.createElement("Defect");
        defectElement.setAttribute("Name", name);
        defectElement.setAttribute("ReportedVersion", reportedVersion);
        defectElement.setAttribute("State", state.toString());
        versions.stream()
                .map(defectVersion -> defectVersion.getXmlElement(doc))
                .forEach(defectElement::appendChild);
        defectElement.setAttribute("Comment", comment);
        return defectElement;
    }

    public boolean addDefectVersion(@NotNull IDefectVersionInfo defectVersionInfo)
    {
        if (versions.stream().anyMatch(
                defectVersion -> defectVersion.getVersion().equals(defectVersionInfo.getVersion()))) {
            return false;
        }

        versions.add(new DefectVersion(defectVersionInfo));
        return true;
    }
}
