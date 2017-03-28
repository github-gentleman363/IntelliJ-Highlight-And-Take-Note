package takenote;

import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;


public class ViewAllNotesDialog extends JDialog {
    private JPanel contentPane;
    private JTree notesTree;
    private JTextPane codePane;
    private JTextPane notePane;
    private JRadioButton groupByFileButton;
    private JRadioButton groupByColorButton;

    private Project project;
    private DefaultTreeModel noteTreeByFileModel;
    private DefaultTreeModel noteTreeByColorModel;

    private static final HashMap<Color, String> COLORS = createColorMap();
    private static HashMap<Color, String> createColorMap() {
        HashMap<Color, String> colorMap = new HashMap<Color, String>();
        colorMap.put(Color.RED, "Red");
        colorMap.put(Color.GREEN, "Green");
        colorMap.put(Color.BLUE, "Blue");
        colorMap.put(Color.YELLOW, "Yellow");
        return colorMap;
    }

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

        this.constructTrees();

        this.groupByFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notesTree.setCellRenderer(new NoteCellRenderer(false));
                notesTree.setModel(noteTreeByFileModel);
            }
        });

        this.groupByColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notesTree.setCellRenderer(new NoteCellRenderer(true));
                notesTree.setModel(noteTreeByColorModel);
            }
        });

        this.registerMouseEvent();
    }

    private void registerMouseEvent() {
        MouseListener ml = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int selRow = notesTree.getRowForLocation(e.getX(), e.getY());
                if (selRow != -1) {
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) notesTree.getLastSelectedPathComponent();
                    if (selectedNode == null || !selectedNode.isLeaf()) {
                        return;
                    }

                    Note currentNote = (Note) selectedNode.getUserObject();
                    notePane.setBackground(currentNote.getColor());
                    if(currentNote.getColor() == Color.YELLOW){
                        notePane.setForeground(Color.BLACK);
                    } else {
                        notePane.setForeground(Color.WHITE);
                    }

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

    private void constructTrees() {
        this.constructGroupByFileTree();
        this.constructGroupByColorTree();
        this.notesTree.setCellRenderer(new NoteCellRenderer(false));
        this.notesTree.setModel(this.noteTreeByFileModel);
    }

    private void constructGroupByColorTree() {
        Map<Color, List<Note>> colorToNoteMap = new HashMap<Color, List<Note>>();

        NoteManager noteManager = NoteManager.getInstance();
        Map<String, List<Note>> filePathToNotes = noteManager.getFilePathToNotes();
        Set<String> filePathSet = filePathToNotes.keySet();
        for (String filePath : filePathSet) {
            List<Note> notes = filePathToNotes.get(filePath);
            for (Note note : notes) {
                if (!colorToNoteMap.containsKey(note.getColor())) {
                    colorToNoteMap.put(note.getColor(), new ArrayList<>());
                }
                colorToNoteMap.get(note.getColor()).add(note);
            }
        }

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        Set<Color> colorSet = colorToNoteMap.keySet();
        for (Color color : colorSet) {
            List<Note> notes = colorToNoteMap.get(color);
            DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(
                    COLORS.get(color)
            );
            root.add(parentNode);

            for (Note note : notes) {
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(note);
                parentNode.add(childNode);
            }
        }

        this.noteTreeByColorModel = new DefaultTreeModel(root);

    }

    private void constructGroupByFileTree() {
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

        this.noteTreeByFileModel = new DefaultTreeModel(root);
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
