import com.sun.istack.internal.NotNull;

import javax.swing.JTabbedPane;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class TabbedPane extends JTabbedPane
{
    public void addTab(String title,
                       @NotNull Function<Predicate<IDefectInfoProvider>, List<IDefectInfoProvider>> getSynchronizedDefectInfoList,
                       @NotNull Filter tabFilter, @NotNull Consumer<IDefectInfoProvider> changeDefectHandler)
    {
        super.addTab(title, new TabPanel(title, getSynchronizedDefectInfoList, tabFilter,
                changeDefectHandler));
    }
}
