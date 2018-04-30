import javax.swing.JComboBox;
import java.util.stream.Stream;

public class DefectStatusJComboBox extends JComboBox<String> {

    public DefectStatusJComboBox()
    {
        Stream.of(DefectStatesEnum.values())
                .map(DefectStatesEnum::toString)
                .forEach(this::addItem);
    }
}
