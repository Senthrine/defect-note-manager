import com.sun.istack.internal.NotNull;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.FlowLayout;

public class DefectSummaryPanel extends JPanel
{
    @NotNull
    private JTextField defectNameField = new JTextField();
    @NotNull
    private JTextField reportedForField = new JTextField();
    @NotNull
    private DefectStatusJComboBox defectStateComboBox = new DefectStatusJComboBox();

    public DefectSummaryPanel()
    {
        reportedForField.setPreferredSize(new Dimension(40, reportedForField.getPreferredSize().height));
        defectNameField.setPreferredSize(new Dimension(120, defectNameField.getPreferredSize().height));

        FlowLayout layout = new FlowLayout(FlowLayout.CENTER);
        this.setLayout(layout);

        add(defectNameField);
        JLabel reportedForLabel = new JLabel("reported for");
        add(reportedForLabel);
        add(reportedForField);
        add(defectStateComboBox);
    }

    @NotNull
    public JTextField getDefectNameField()
    {
        return defectNameField;
    }

    @NotNull
    public JTextField getReportedForField()
    {
        return reportedForField;
    }

    @NotNull
    public JComboBox getDefectStateComboBox()
    {
        return defectStateComboBox;
    }
}
