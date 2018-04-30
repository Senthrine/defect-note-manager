import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.sun.istack.internal.NotNull;
import org.w3c.dom.*;


public class TabPanel extends JPanel
{
    @NotNull private JTextField searchJTextField = new JTextField();
    @NotNull private JCheckBox displayValidatedCheckBox = new JCheckBox("Display validated");
    @NotNull private JPanel defectLinesPanel = new JPanel();

    private String title;
    @NotNull private List<IDefectInfoProvider> defectInfoList;
    @NotNull private List<DefectLine> defectLineList;
    @NotNull private Function<Predicate<IDefectInfoProvider>, List<IDefectInfoProvider>> getSynchronizedDefectInfoList;
    @NotNull private Filter tabFilter;
    @NotNull private Consumer<IDefectInfoProvider> changeDefectHandler;

    public TabPanel(String title,
                    @NotNull Function<Predicate<IDefectInfoProvider>, List<IDefectInfoProvider>> getSynchronizedDefectInfoList,
                    @NotNull Filter tabFilter, @NotNull Consumer<IDefectInfoProvider> changeDefectHandler)
    {
        this.title = title;
        this.getSynchronizedDefectInfoList = getSynchronizedDefectInfoList;
        this.tabFilter = tabFilter;
        this.changeDefectHandler = changeDefectHandler;

        defectInfoList = this.getSynchronizedDefectInfoList.apply(tabFilter.getValidator());
        defectLineList = defectInfoList.stream()
                .map(defectInfo -> new DefectLine(defectInfo, this::DefectChanged))
                .collect(Collectors.toList());

        BorderLayout layout = new BorderLayout();
        this.setLayout(layout);

        FlowLayout searchPanelLayout = new FlowLayout(FlowLayout.LEFT);
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(searchPanelLayout);
        searchJTextField.setPreferredSize(new Dimension(120, searchJTextField.getPreferredSize().height));
        searchJTextField.addActionListener(this::searchFieldTextChanged);
        displayValidatedCheckBox.addActionListener(this::displayValidatedSelectionChanged);

        JLabel searchJLabel = new JLabel("Search:");
        searchPanel.add(searchJLabel);
        searchPanel.add(searchJTextField);
        searchPanel.add(displayValidatedCheckBox);

        add(searchPanel, BorderLayout.PAGE_START);

        JPanel defectLinesBackgroundPanel = new JPanel();
        defectLinesBackgroundPanel.setLayout(new BorderLayout());
        defectLinesPanel.setLayout(new BoxLayout(defectLinesPanel, BoxLayout.PAGE_AXIS));
        filterAndAddDefectLines();
        defectLinesBackgroundPanel.add(defectLinesPanel, BorderLayout.PAGE_START);

        JScrollPane scrollPane = new JScrollPane(defectLinesBackgroundPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void addDefectLineIfFit(@NotNull IDefectInfoProvider defectInfo)
    {
        if (tabFilter.getValidator().test(defectInfo)) {
            defectInfoList.add(defectInfo);
            DefectLine defectLine = new DefectLine(defectInfo, this::DefectChanged);
            defectLineList.add(defectLine);

            if (displayValidatedFilter(defectInfo) && searchFilter(defectInfo)) {
                defectLinesPanel.add(defectLine);
                defectLinesPanel.revalidate();
            }
        }
    }

    public Element getXmlElement(Document doc)
    {
        Element tabElement = doc.createElement("Tab");
        tabElement.setAttribute("Title", title);
        tabElement.appendChild(tabFilter.getXmlElement(doc));
        return tabElement;
    }

    private void filterAndAddDefectLines()
    {
        defectLinesPanel.removeAll();
        defectLineList.stream().filter(this::showDefectLineFilter).forEach(defectLinesPanel::add);
        defectLinesPanel.revalidate();
    }

    //return true if line should be displayed
    private boolean showDefectLineFilter(@NotNull DefectLine defectLine)
    {
        return searchFilter(defectLine) && displayValidatedFilter(defectLine);
    }

    //for DefectLine use showDefectLineFilter
    private boolean searchFilter(@NotNull IDefectInfoProvider defectInfo)
    {
        return searchJTextField.getText()== null || searchJTextField.getText().equals("")
                || defectInfo.getDefectName().contains(searchJTextField.getText());
    }

    //for DefectLine use showDefectLineFilter
    private boolean displayValidatedFilter(@NotNull IDefectInfoProvider defectInfo)
    {
        return displayValidatedCheckBox.isSelected() || !defectInfo.getDefectState().isValidated();
    }

    private void searchFieldTextChanged(ActionEvent e)
    {
        filterAndAddDefectLines();
    }

    private void displayValidatedSelectionChanged(ActionEvent e)
    {
        filterAndAddDefectLines();
    }

    private void DefectChanged(@NotNull DefectLine defectLine)
    {
        //todo see if we still need to show this line
        changeDefectHandler.accept(defectLine);
        Optional<IDefectInfoProvider> defectInfoProviderOptional = defectInfoList.stream()
                .filter(defectInfoProvider -> defectInfoProvider.getDefectName().equals(defectLine.getDefectName())).findAny();
        if (!defectInfoProviderOptional.isPresent())
        {
            hideDefectLine(defectLine);
            defectLineList.remove(defectLine);
            return;
        }
        IDefectInfoProvider defectInfo = defectInfoProviderOptional.get();
        if (!tabFilter.getValidator().test(defectInfo))
        {
            hideDefectLine(defectLine);
            defectInfoList.remove(defectInfo);
            return;
        }
        if (!showDefectLineFilter(defectLine))
        {
            hideDefectLine(defectLine);
        }
    }

    private void hideDefectLine(@NotNull DefectLine defectLine)
    {
        defectLinesPanel.remove(defectLine);
        defectLinesPanel.revalidate();
    }
}
