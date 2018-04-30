import com.sun.istack.internal.NotNull;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.function.Consumer;

public class DefectInfoJFrame extends JFrame implements IDefectInfoProvider
{


    @NotNull private DefectSummaryPanel defectSummaryPanel;
    @NotNull private DefectVersionsPanel defectVersionsPanel;
    @NotNull private JTextArea commentsField = new JTextArea();
    @NotNull private Consumer<IDefectInfoProvider> saveButtonEventHandler;

    public static DefectInfoJFrame getAddDefectFrame(@NotNull Consumer<IDefectInfoProvider> saveButtonEventHandler)
    {
        return new DefectInfoJFrame(saveButtonEventHandler, "Add Defect");
    }

    public static DefectInfoJFrame getChangeDefectFrame(@NotNull Consumer<IDefectInfoProvider> saveButtonEventHandler,
                                                        @NotNull IDefectInfoProvider defectInfo)
    {
        return new DefectInfoJFrame(saveButtonEventHandler, defectInfo, "Change Defect");
    }

    private DefectInfoJFrame(@NotNull Consumer<IDefectInfoProvider> saveButtonEventHandler, @NotNull IDefectInfoProvider defectInfo,
                             String title)
    {
        this(saveButtonEventHandler, title);
        defectSummaryPanel.getDefectNameField().setText(defectInfo.getDefectName());
        defectSummaryPanel.getReportedForField().setText(defectInfo.getReportedVersion());

        defectSummaryPanel.getDefectStateComboBox().setSelectedItem(defectInfo.getDefectState());

        commentsField.setText(defectInfo.getComment());
        defectVersionsPanel.removeAllDefectVersionPanels();
        //todo cheeki breeki done
        defectInfo.getVersionInfoList().forEach(defectVersionsPanel::AddVersion);
    }

    private DefectInfoJFrame(@NotNull Consumer<IDefectInfoProvider> saveButtonEventHandler, String title)
    {
        setTitle(title);
        this.saveButtonEventHandler = saveButtonEventHandler;
        setResizable(false);
        JPanel panel = new JPanel();
        BoxLayout layout = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
        panel.setLayout(layout);

        defectSummaryPanel = new DefectSummaryPanel();
        defectVersionsPanel = new DefectVersionsPanel();
        defectVersionsPanel.setPreferredSize(new Dimension(400, 220));
        OkCancelPanel okCancelPanel = new OkCancelPanel("Save", "Cancel",
                this::saveButtonClicked, this::cancelButtonClicked);

        panel.add(defectSummaryPanel);
        panel.add(defectVersionsPanel);
        panel.add(commentsField);
        panel.add(okCancelPanel);
        this.setPreferredSize(new Dimension(400, 500));
        this.add(panel);
        pack();
    }

    public void saveButtonClicked(ActionEvent e)
    {
        saveButtonEventHandler.accept(this);
        this.dispose();
    }

    public void cancelButtonClicked(ActionEvent e)
    {
        this.dispose();
    }

    @Override @NotNull
    public String getDefectName()
    {
        if (defectSummaryPanel.getDefectNameField().getText() == null)
        {
            return "";
        }
        return defectSummaryPanel.getDefectNameField().getText();
    }

    @Override @NotNull
    public String getReportedVersion()
    {
        if (defectSummaryPanel.getReportedForField().getText() == null)
        {
            return "";
        }
        return defectSummaryPanel.getReportedForField().getText();
    }

    @Override @NotNull
    public DefectStatesEnum getDefectState()
    {
        String state = (String) defectSummaryPanel.getDefectStateComboBox().getSelectedItem();
        DefectStatesEnum defectState = DefectStatesEnum.getFromString(state);
        if (defectState == null)
        {
            return DefectStatesEnum.getDefault();
        }
        return defectState;
    }

    @Override @NotNull
    public List<IDefectVersionInfo> getVersionInfoList()
    {
        return defectVersionsPanel.getVersions();
    }

    @Override @NotNull
    public String getComment()
    {
        if (commentsField.getText() == null)
        {
            return "";
        }
        //todo cheeki breeki done
        return commentsField.getText();
    }
}
