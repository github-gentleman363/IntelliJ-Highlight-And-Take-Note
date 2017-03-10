import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.event.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ViewAllNotesDialog extends JDialog {
    private JPanel contentPane;
    private JTree notesTree;
    private JTextPane codePane;
    private JTextPane notePane;

    private Project project;

    public ViewAllNotesDialog(Project project) {
        this.project = project;
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
    }

    private void constructTree() {
        // TODO rename me!
        NoteManager noteManager = NoteManager.getInstance();
        Map<String, List<Note>> filePathToNotes = noteManager.getFilePathToNotes();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        Set<String> filePathSet = filePathToNotes.keySet();
        for (String filePath : filePathSet) {
            List<Note> notes = filePathToNotes.get(filePath);
            DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(
                    VfsUtil.getRelativeLocation(LocalFileSystem.getInstance().findFileByPath(filePath),
                            project.getBaseDir())
            );
            root.add(parentNode);

            for (Note note : notes) {
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(note);
                parentNode.add(childNode);
            }
        }

        DefaultTreeModel treeModel = new DefaultTreeModel(root);
        this.notesTree.setModel(treeModel);
        this.notesTree.setCellRenderer(new NoteCellRenderer());

        MouseListener ml = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int selRow = notesTree.getRowForLocation(e.getX(), e.getY());
                if (selRow != -1) {
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) notesTree.getLastSelectedPathComponent();
                    if (selectedNode == null || !selectedNode.isLeaf()) {
                        return;
                    }

                    Note currentNote = (Note) selectedNode.getUserObject();

                    if (e.getClickCount() == 1) {
                        new Thread(new Runnable() {
                            public void run() {
                                // Runs inside of the Swing UI thread
                                SwingUtilities.invokeLater(new Runnable() {
                                    public void run() {
                                        codePane.setText(currentNote.getHighlightedCode());
                                        notePane.setText(currentNote.getContent());
                                    }
                                });
                            }
                        }).start();
                    } else if (e.getClickCount() == 2) {
                        VirtualFile file = LocalFileSystem.getInstance().findFileByPath(currentNote.getFilePath());
                        if (file == null) return;
                        new OpenFileDescriptor(project, file, currentNote.getStartOffset()).navigate(true);
                    }
                }
            }
        };
        this.notesTree.addMouseListener(ml);
    }

    public JComponent getPreferredFocusedComponent() {
        return this.notesTree;
    }

    public JComponent getPanel() {
        return this.contentPane;
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
