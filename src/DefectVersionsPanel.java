import com.sun.istack.internal.NotNull;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class DefectVersionsPanel extends JPanel
{
    @NotNull private List<DefectVersionPanel> defectVersionPanelsList = new ArrayList<>();
    @NotNull private JButton AddVersionButton = new JButton("Add Version");
    @NotNull private JPanel EmptyPanel = new JPanel();

    public DefectVersionsPanel()
    {
        defectVersionPanelsList.add(new DefectVersionPanel(this::RemoveDefectVersionPanel));

        EmptyPanel.setPreferredSize(new Dimension(400, 500));
        EmptyPanel.setMinimumSize(new Dimension(400, 0));
        AddVersionButton.addActionListener(this::AddVersionButtonClicked);
        AddVersionButton.setAlignmentX(CENTER_ALIGNMENT);

        BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
        setLayout(layout);

        defectVersionPanelsList.forEach(this::add);
        add(AddVersionButton);
        add(EmptyPanel);
    }

    public void removeAllDefectVersionPanels()
    {
        defectVersionPanelsList.forEach(this::remove);
        defectVersionPanelsList.clear();
        AddVersionButton.setEnabled(true);
        validate();
    }

    public void AddVersion(@NotNull IDefectVersionInfo defectVersionInfo)
    {
        DefectVersionPanel defectVersionPanel = new DefectVersionPanel(this::RemoveDefectVersionPanel,
                defectVersionInfo);
        AddVersionPanel(defectVersionPanel);
    }

    @NotNull
    public List<IDefectVersionInfo> getVersions()
    {
        return new ArrayList<>(defectVersionPanelsList);
    }

    private void RemoveDefectVersionPanel(@NotNull DefectVersionPanel defectVersionPanel)
    {
        defectVersionPanelsList.remove(defectVersionPanel);
        this.remove(defectVersionPanel);
        if (defectVersionPanelsList.size() < 5) {
            AddVersionButton.setEnabled(true);
        }
        validate();
    }

    private void AddVersionButtonClicked(ActionEvent e)
    {
        DefectVersionPanel defectVersionPanel = new DefectVersionPanel(this::RemoveDefectVersionPanel);
        AddVersionPanel(defectVersionPanel);
    }

    private void AddVersionPanel(DefectVersionPanel defectVersionPanel)
    {
        defectVersionPanelsList.add(defectVersionPanel);
        remove(AddVersionButton);
        remove(EmptyPanel);
        add(defectVersionPanel);
        add(AddVersionButton);
        add(EmptyPanel);
        if (defectVersionPanelsList.size() >= 5) {
            AddVersionButton.setEnabled(false);
        }
        validate();
    }
}
