import com.sun.istack.internal.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DefectVersion implements IDefectVersionInfo
{
    @NotNull private String version;
    @NotNull private DefectVersionStatesEnum state;

    public DefectVersion(@NotNull IDefectVersionInfo defectVersionInfo)
    {
        version = defectVersionInfo.getVersion();
        state = defectVersionInfo.getState();
    }

    public void changeState(@NotNull DefectVersionStatesEnum state)
    {
        this.state = state;
    }

    @NotNull public String getVersion()
    {
        return version;
    }

    @NotNull public DefectVersionStatesEnum getState()
    {
        return state;
    }

    public Element getXmlElement(@NotNull Document doc)
    {
        Element defectVersionElement = doc.createElement("DefectVersion");
        defectVersionElement.setAttribute("Version", version);
        defectVersionElement.setAttribute("State", state.toString());
        return defectVersionElement;
    }
}
