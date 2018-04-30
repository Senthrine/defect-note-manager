import javax.swing.JComboBox;
import java.util.stream.Stream;

public class VersionStatusJComboBox extends JComboBox<String> {
    public VersionStatusJComboBox()
    {
        Stream.of(DefectVersionStatesEnum.values())
                .map(DefectVersionStatesEnum::toString)
                .forEach(this::addItem);
    }

}
