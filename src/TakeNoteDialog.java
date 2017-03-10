import javax.swing.*;
import java.awt.*;


public class TakeNoteDialog extends JDialog {
    private JPanel contentPane;
    private JTextArea textArea;
    private JRadioButton redButton;
    private JRadioButton yellowButton;
    private JRadioButton greenButton;
    private JRadioButton blueButton;
    private ButtonGroup buttonGroup;

    // TODO disallow multiple selection

    public TakeNoteDialog() {
        // TODO add selected text as props
        setContentPane(this.contentPane);
        setModal(true);

        ButtonGroup chooseColor = this.buttonGroup = new ButtonGroup();
        chooseColor.add(redButton);
        chooseColor.add(yellowButton);
        chooseColor.add(greenButton);
        chooseColor.add(blueButton);
    }

    public JComponent getPreferredFocusedComponent() {
        return this.textArea;
    }

    public JComponent getPanel() {
        return this.contentPane;
    }

    public String getText() {
        // TODO rename
        return this.textArea.getText();
    }

    public void setText(String text) {
        this.textArea.insert(text, 0);
    }

    public Color getSelectedColor() {
        if (redButton.isSelected()) {
            return Color.RED;
        } else if (yellowButton.isSelected()) {
            return Color.YELLOW;
        } else if (greenButton.isSelected()) {
            return Color.GREEN;
        } else if (blueButton.isSelected()) {
            return Color.BLUE;
        } else {
            return null;
        }
    }

}

