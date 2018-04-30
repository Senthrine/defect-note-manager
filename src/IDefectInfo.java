import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface IDefectInfo extends IDefectInfoProvider
{
    Element getXmlElement(Document doc);
}
