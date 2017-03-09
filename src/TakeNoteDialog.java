import javax.swing.*;

public class TakeNoteDialog extends JDialog {
    private JPanel contentPane;
    private JTextArea textArea;

    public TakeNoteDialog() {
        // TODO add selected text as props
        setContentPane(this.contentPane);
        setModal(true);
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

