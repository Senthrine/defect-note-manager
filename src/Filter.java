import java.util.function.Predicate;

import com.sun.istack.internal.NotNull;
import org.w3c.dom.*;

public class Filter
{
    @NotNull private String reportedForVersion;

    public Filter(@NotNull String reportedForVersion)
    {
        this.reportedForVersion = reportedForVersion;
    }

    @NotNull
    public Predicate<IDefectInfoProvider> getValidator()
    {
        return this::validate;
    }

    public String getReportedForVersion()
    {
        return reportedForVersion;
    }

    public Element getXmlElement(@NotNull Document doc)
    {
        @NotNull Element filterElement = doc.createElement("Filter");
        filterElement.setAttribute("ReportedForVersion", reportedForVersion);
        return filterElement;
    }

    private boolean validate(@NotNull IDefectInfoProvider defectInfo)
    {
        return reportedForVersion.isEmpty() || defectInfo.getDefectName().contains(reportedForVersion);

    }
}

