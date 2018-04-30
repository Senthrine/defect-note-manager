import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

public class OkCancelPanel extends JPanel
{

    public OkCancelPanel(String okButtonText, String cancelButtonText, ActionListener okButtonActionListener,
                         ActionListener cancelButtonActionListener)
    {
        JButton okButton = new JButton(okButtonText);
        JButton cancelButton = new JButton(cancelButtonText);
        okButton.addActionListener(okButtonActionListener);
        cancelButton.addActionListener(cancelButtonActionListener);

        FlowLayout layout = new FlowLayout(FlowLayout.RIGHT);
        this.setLayout(layout);

        add(okButton);
        add(cancelButton);

    }
}
