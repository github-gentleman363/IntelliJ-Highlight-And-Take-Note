import javax.swing.*;
import java.awt.event.*;


public class TakeNoteDialog extends JDialog {
    private JPanel contentPane;
    private JTextArea textArea;
    private JRadioButton redButton;
    private JRadioButton purpleButton;
    private JRadioButton greenButton;
    private JRadioButton blueButton;

    public TakeNoteDialog() {
        // TODO add selected text as props
        setContentPane(this.contentPane);
        setModal(true);

        ButtonGroup chooseColor = new ButtonGroup();
        chooseColor.add(redButton);
        chooseColor.add(purpleButton);
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
}

