import javax.swing.*;
import java.awt.event.*;
import java.util.List;
import java.util.Map;

public class ViewAllNotesDialog extends JDialog {
    private JPanel contentPane;
    private JTree NotesTree;
    private JTextPane noteTextPane;
    private JTextPane textPane2;

    public ViewAllNotesDialog() {
        setContentPane(contentPane);
        setModal(true);
//        getRootPane().setDefaultButton(buttonOK);

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

        NoteManager noteManager = NoteManager.getInstance();
        Map<String, List<Note>> filePathToNotes = noteManager.getFilePathToNotes();

    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}
