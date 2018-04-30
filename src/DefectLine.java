import com.sun.istack.internal.NotNull;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DefectLine extends JPanel implements IDefectInfoProvider
{

    @NotNull
    private JLabel reportedVersionLabel;
    @NotNull
    private JLabel defectNameLabel;
    @NotNull
    private DefectStatusJComboBox defectState = new DefectStatusJComboBox();
    @NotNull
    private List<DefectVersionLine> versionsComponents;
    @NotNull
    private String defectComment;
    @NotNull
    private Consumer<DefectLine> changeDefectLineHandler;
    @NotNull
    private JButton changeDefectButton;

    public DefectLine(@NotNull IDefectInfoProvider defectInfo, @NotNull Consumer<DefectLine> changeDefectLineHandler)
    {
        this.changeDefectLineHandler = changeDefectLineHandler;
        setLayout(new FlowLayout(FlowLayout.LEFT));
        reportedVersionLabel = new JLabel();
        defectNameLabel = new JLabel();
        changeDefectButton = new JButton("open");
        changeDefectButton.addActionListener(this::openButtonClicked);
        defectState.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED)
            {
                stateChanged();
            }
        });

        setBorder(BorderFactory.createLineBorder(Color.black));

        setContent(defectInfo);
        reportedVersionLabel.setPreferredSize(new Dimension(40, reportedVersionLabel.getPreferredSize().height));
        defectNameLabel.setPreferredSize(new Dimension(120, defectNameLabel.getPreferredSize().height));

        reAddComponents();
    }

    @NotNull
    public String getDefectName()
    {
        if (defectNameLabel.getText() == null) {
            return "";
        }
        return defectNameLabel.getText();
    }

    @NotNull
    public String getReportedVersion()
    {
        if (reportedVersionLabel.getText() == null) {
            return "";
        }
        return reportedVersionLabel.getText();
    }

    @NotNull
    public DefectStatesEnum getDefectState()
    {
        String state = (String) defectState.getSelectedItem();
        DefectStatesEnum defectState = DefectStatesEnum.getFromString(state);
        if (defectState == null) {
            return DefectStatesEnum.getDefault();
        }
        return defectState;
    }

    @NotNull
    public List<IDefectVersionInfo> getVersionInfoList()
    {
        return new ArrayList<>(versionsComponents);
    }

    @NotNull
    public String getComment()
    {
        return defectComment;
    }

    private void stateChanged()
    {
        changeDefectLineHandler.accept(this);
    }

    private void openButtonClicked(ActionEvent e)
    {
        DefectInfoJFrame.getChangeDefectFrame(this::changeThis, this).setVisible(true);
    }

    private void changeThis(@NotNull IDefectInfoProvider defectInfo)
    {
        setContent(defectInfo);
        reAddComponents();
        changeDefectLineHandler.accept(this);
    }

    private void setContent(@NotNull IDefectInfoProvider defectInfo)
    {
        reportedVersionLabel.setText(defectInfo.getReportedVersion());
        defectNameLabel.setText(defectInfo.getDefectName());

        try {
            defectState.setSelectedItem(defectInfo.getDefectState().toString());
        } catch (Exception ex) {
            System.out.println("Can't set defect state: " + ex.getMessage());
        }
        versionsComponents = defectInfo.getVersionInfoList().stream()
                .map(defectVersionInfo -> new DefectVersionLine(defectVersionInfo, this::stateChanged))
                .collect(Collectors.toList());
        versionsComponents.sort(Comparator.comparing(DefectVersionLine::getVersion));

        defectComment = defectInfo.getComment();
    }

    private void reAddComponents()
    {
        removeAll();
        add(reportedVersionLabel);
        add(defectNameLabel);
        add(defectState);
        versionsComponents.forEach(this::add);
        add(changeDefectButton);
    }

}
