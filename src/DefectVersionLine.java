import com.sun.istack.internal.NotNull;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ItemEvent;

public class DefectVersionLine extends JPanel implements IDefectVersionInfo
{
    @NotNull
    private JLabel versionLabel;
    @NotNull
    private VersionStatusJComboBox versionStatusJComboBox;

    public DefectVersionLine(@NotNull IDefectVersionInfo defectVersionInfo, @NotNull Runnable changeDefectHandler)
    {
        versionLabel = new JLabel(defectVersionInfo.getVersion());
        versionLabel.setPreferredSize(new Dimension(30, versionLabel.getPreferredSize().height));
        versionStatusJComboBox = new VersionStatusJComboBox();
        try {
            versionStatusJComboBox.setSelectedItem(defectVersionInfo.getState().toString());
        } catch (Exception ex) {
            System.out.println("Can't set version state: " + ex.getMessage());
        }
        versionStatusJComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED)
            {
                changeDefectHandler.run();
            }
        });
        add(versionLabel);
        add(versionStatusJComboBox);
        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    @Override
    @NotNull
    public String getVersion()
    {

        if (versionLabel.getText() == null) {
            return "";
        }
        return versionLabel.getText();
    }

    @Override
    @NotNull
    public DefectVersionStatesEnum getState()
    {
        String state = (String) versionStatusJComboBox.getSelectedItem();
        DefectVersionStatesEnum defectState = DefectVersionStatesEnum.getFromString(state);
        if (defectState == null) {
            return DefectVersionStatesEnum.getDefault();
        }
        return defectState;
    }
}
