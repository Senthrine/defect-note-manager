import com.sun.istack.internal.NotNull;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public class DefectVersionPanel extends JPanel implements IDefectVersionInfo
{
    @NotNull private JTextField defectVersionField = new JTextField();
    @NotNull private VersionStatusJComboBox defectVersionStateComboBox = new VersionStatusJComboBox();
    @NotNull private Consumer<DefectVersionPanel> panelClosingConsumer;

    public DefectVersionPanel(@NotNull Consumer<DefectVersionPanel> panelClosingConsumer)
    {
        this.panelClosingConsumer = panelClosingConsumer;
        defectVersionField.setPreferredSize(new Dimension(40, defectVersionField.getPreferredSize().height));
        JButton removeDefectVersionButton = new JButton("X");
        removeDefectVersionButton.addActionListener(this::RemoveDefectVersionButtonClicked);

        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        setLayout(layout);

        add(defectVersionField);
        add(defectVersionStateComboBox);
        add(removeDefectVersionButton);
    }

    public DefectVersionPanel(@NotNull Consumer<DefectVersionPanel> panelClosingConsumer, @NotNull IDefectVersionInfo defectVersionInfo)
    {
        this(panelClosingConsumer);
        defectVersionField.setText(defectVersionInfo.getVersion());
        try {
            defectVersionStateComboBox.setSelectedItem(defectVersionInfo.getState());
        } catch (Exception ex)
        {
            System.out.println("Can't set version state: " + ex.getMessage());
        }
    }

    @Override @NotNull
    public String getVersion()
    {
        if (defectVersionField.getText() == null)
        {
            return "";
        }
        return defectVersionField.getText();
    }

    @Override @NotNull
    public DefectVersionStatesEnum getState()
    {
        String state = (String) defectVersionStateComboBox.getSelectedItem();
        DefectVersionStatesEnum defectState = DefectVersionStatesEnum.getFromString(state);
        if (defectState == null)
        {
            return DefectVersionStatesEnum.getDefault();
        }
        return defectState;

    }

    private void RemoveDefectVersionButtonClicked(ActionEvent e)
    {
        panelClosingConsumer.accept(this);
    }

}
