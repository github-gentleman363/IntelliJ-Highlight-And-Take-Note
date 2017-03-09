import javax.swing.*;
import java.awt.event.*;

public class TakeNoteDialog extends JDialog {
    private JPanel contentPane;
    private JTextArea textArea1;

    public TakeNoteDialog() {
        // TODO add selected text as props
        setContentPane(this.contentPane);
        setModal(true);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public JComponent getPreferredFocusedComponent() {
        return this.textArea1;
    }

    public JComponent getPanel() {
        return this.contentPane;
    }

    public String getText() {
        // TODO rename
        return this.textArea1.getText();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        TakeNoteDialog dialog = new TakeNoteDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
