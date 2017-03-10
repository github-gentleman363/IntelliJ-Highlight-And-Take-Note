import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.treeStructure.Tree;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ViewAllNotesDialog extends JDialog {
    private JPanel contentPane;
    private JTree NotesTree;
    private JTextPane codePane;
    private JTextPane notePane;

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

        this.constructTree();
        this.setTitle("Find all notes");

    }

    private void constructTree() {
        // TODO rename me!
        NoteManager noteManager = NoteManager.getInstance();
        Map<String, List<Note>> filePathToNotes = noteManager.getFilePathToNotes();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        Iterator it = filePathToNotes.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            String filePath = (String) pair.getKey();
            List<Note> notes = (List<Note>) pair.getValue();
            DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(filePath);
            root.add(parentNode);

            for(Note note : notes){
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(note);
                parentNode.add(childNode);
            }
            it.remove(); // avoids a ConcurrentModificationException
        }

        DefaultTreeModel treeModel = new DefaultTreeModel(root);
        this.NotesTree.setModel(treeModel);
        this.NotesTree.setCellRenderer(new NoteCellRenderer());

        // TODO refactor me!
        this.NotesTree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) NotesTree.getLastSelectedPathComponent();
                // get corresponding note
                // and display the following details on the right:
                //      associated code (highlighted code) at the top
                //      note content at the bottom

                Note currentNote = (Note) selectedNode.getUserObject();
                codePane = new JTextPane();
                Document doc = codePane.getDocument();
                
                try {
                    doc.insertString(0, currentNote.getHighlightedCode(), null);
                    System.out.println(currentNote.getHighlightedCode());
                } catch (BadLocationException ble) {
                    System.err.println("Couldn't insert initial text into text pane.");
                }

            }
        });
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
