import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException | UnsupportedLookAndFeelException | InstantiationException ex) {
            System.out.println(ex.getMessage());
        }
        MainJFrame mainJFrame = new MainJFrame();
        mainJFrame.setTitle("Defect Note Manager");
        mainJFrame.setVisible(true);
    }
}