package highlightAndTakeNote.viewAllNotes;

import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Splitter;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.PsiManagerImpl;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.usageView.UsageInfo;
import com.intellij.usages.UsageViewPresentation;
import com.intellij.usages.impl.UsagePreviewPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import highlightAndTakeNote.model.Note;
import highlightAndTakeNote.NoteManager;
/**
 *  TODO
 *
 *  Style
 *  - Have 'By File' radio selected initially  >>> DONE
 *  - Take out the root >>> DONE
 *  - 'By Color': folder -> color icon?
 *  - 'By File': folder -> file icon
 *  - Have the folders expand to show lines
 *  - extra spacing between find by group button panel and the splitter
 *  - Change colors to be softer. Hard to read on most colors.
 *  - Center the dialog
 *  - Fix width + height
 *
 *  Additional Feature
 *  -  Free text search of note
 */

public class ViewAllNotesDialog extends JDialog {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;
    private static final HashMap<Color, String> COLORS = createColorMap();
    private static HashMap<Color, String> createColorMap() {
        HashMap<Color, String> colorMap = new HashMap<Color, String>();
        colorMap.put(Color.RED, "Red");
        colorMap.put(Color.GREEN, "Green");
        colorMap.put(Color.BLUE, "Blue");
        colorMap.put(Color.YELLOW, "Yellow");
        return colorMap;
    }

    private Project project;
    private JPanel contentPane;

    private JRadioButton groupByFileButton;
    private JRadioButton groupByColorButton;

    private Splitter myPreviewSplitter;
    private Splitter notesTreeAndPreviewSplitter;
    private JTree notesTree;
    private JTextPane notePreview;
    private UsagePreviewPanel notedCodePreview;

    private DefaultTreeModel noteTreeByFileModel;
    private DefaultTreeModel noteTreeByColorModel;

    public ViewAllNotesDialog(Project project) {
        this.project = project;
        setModal(true);
        this.createLayout();

        this.registerCancelEvents();
        this.registerListenersToButtons();
        this.registerMouseEvent();

        this.constructTrees();
    }

    public void registerListenersToButtons() {
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
    }

    public void createLayout(JComponent... arg) {
        this.setSize(ViewAllNotesDialog.WIDTH, ViewAllNotesDialog.HEIGHT);

        JPanel groupByButtonsPanel = setupGroupNotesButton();
        // TODO rename
        setupNotesTreeAndNotePreview();
        setupNotesAndCodePreview();

        contentPane = new JPanel(new BorderLayout());
        contentPane.add(groupByButtonsPanel, BorderLayout.PAGE_START);
        contentPane.add(myPreviewSplitter, BorderLayout.CENTER);

        setContentPane(contentPane);
    }

    private void setupNotesAndCodePreview() {
        notedCodePreview = new UsagePreviewPanel(project, new UsageViewPresentation(), true);
        notedCodePreview.setBorder(IdeBorderFactory.createBorder());
        // TODO rename
        myPreviewSplitter = new Splitter(true, 0.5f, 0.1f, 0.9f);
        myPreviewSplitter.setFirstComponent(notesTreeAndPreviewSplitter);
        myPreviewSplitter.setSecondComponent(notedCodePreview.createComponent());
    }

    private void setupNotesTreeAndNotePreview() {
        notesTree = new JTree();
        notesTree.setRootVisible(false);
        notePreview = new JTextPane();
        notesTreeAndPreviewSplitter = new Splitter(false, 0.5f, 0.1f, 0.9f);
        notesTreeAndPreviewSplitter.setFirstComponent(notesTree);
        notesTreeAndPreviewSplitter.setSecondComponent(notePreview);
    }

    @NotNull
    private JPanel setupGroupNotesButton() {
        ButtonGroup buttonGroup = new ButtonGroup();
        // TODO localize strings
        groupByFileButton = new JRadioButton("Find By File");
        groupByColorButton = new JRadioButton("Find By Note Color");
        buttonGroup.add(groupByFileButton);
        buttonGroup.add(groupByColorButton);
        buttonGroup.setSelected(groupByFileButton.getModel(), true);

        JPanel groupByButtonsPanel = new JPanel();
        GroupLayout groupLayout = new GroupLayout(groupByButtonsPanel);
        groupByButtonsPanel.setLayout(groupLayout);
        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
            .addComponent(groupByFileButton)
                .addComponent(groupByColorButton)
        );
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup()
            .addComponent(groupByFileButton)
                .addComponent(groupByColorButton)
        );
        return groupByButtonsPanel;
    }

    private void registerCancelEvents() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }


    private void registerMouseEvent() {
        MouseListener ml = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) notesTree.getLastSelectedPathComponent();
                    if (selectedNode == null || !selectedNode.isLeaf()) {
                        return;
                    }

                    Note currentNote = (Note) selectedNode.getUserObject();

                    notePreview.setBackground(currentNote.getColor());
                    if(currentNote.getColor() == Color.YELLOW){
                        notePreview.setForeground(Color.BLACK);
                    } else {
                        notePreview.setForeground(Color.WHITE);
                    }

                    if (e.getClickCount() == 1) {
                        new Thread(new Runnable() {
                            public void run() {
                                // Runs inside of the Swing UI thread
                                SwingUtilities.invokeLater(new Runnable() {
                                    private void setCodePreview(String filePath, int startOffset, int endOffset) {
                                        VirtualFile virtualFile = LocalFileSystem.getInstance().findFileByPath(filePath);
                                        PsiFile file = PsiManagerImpl.getInstance(project).findFile(virtualFile);

                                        UsageInfo usageInfo = new UsageInfo(file, startOffset, endOffset);
                                        List<UsageInfo> usageInfos = new ArrayList<UsageInfo>();
                                        usageInfos.add(usageInfo);

                                        notedCodePreview.updateLayout(usageInfos);
                                    }
                                    public void run() {
                                        this.setCodePreview(currentNote.getFilePath(), currentNote.getStartOffset(), currentNote.getEndOffset());
                                        notePreview.setText(currentNote.getContent());
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

        NoteManager noteManager = NoteManager.getInstance(this.project);
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
            DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(COLORS.get(color));
            root.add(parentNode);

            for (Note note : notes) {
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(note);
                parentNode.add(childNode);
            }
        }

        this.noteTreeByColorModel = new DefaultTreeModel(root);

    }

    private void constructGroupByFileTree() {
        NoteManager noteManager = NoteManager.getInstance(this.project);
        Map<String, List<Note>> filePathToNotes = noteManager.getFilePathToNotes();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        Set<String> filePathSet = filePathToNotes.keySet();
        for (String filePath : filePathSet) {
            List<Note> notes = filePathToNotes.get(filePath);
            DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode(filePath);
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
        dispose();
    }

    private void onCancel() {
        dispose();
    }

}
