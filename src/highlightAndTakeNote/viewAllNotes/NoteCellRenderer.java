package highlightAndTakeNote.viewAllNotes;

import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;
import highlightAndTakeNote.model.Note;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.net.URL;

public class NoteCellRenderer extends DefaultTreeCellRenderer {
    private static String IMAGE_PATH = "/images/very-basic-file-icon.png";

    private JLabel lineNumber = new JLabel();
    private JLabel noteContent = new JLabel();
    private JLabel filePath = new JLabel();
    private JPanel panel = new JPanel();

    private DefaultTreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();

    private Color backgroundSelectionColor;
    private Color backgroundNonSelectionColor;

    private boolean includeFilePath;

    public NoteCellRenderer(boolean includeFilePath) {
        // TODO fix
        URL imageURL = getClass().getResource(NoteCellRenderer.IMAGE_PATH);
        if (imageURL != null) {
            ImageIcon imageIcon = new ImageIcon(imageURL, "");
            setIcon(imageIcon);
            setClosedIcon(imageIcon);
            setOpenIcon(imageIcon);
            setLeafIcon(imageIcon);
        }

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = JBUI.insets(0, 0, 0, 10);

        this.includeFilePath = includeFilePath;
        if (includeFilePath) {
            panel.add(filePath, gbc);
        }

        panel.add(lineNumber, gbc);
        lineNumber.setForeground(JBColor.GRAY);
        lineNumber.setVerticalTextPosition(JLabel.CENTER);
        lineNumber.setVerticalAlignment(JLabel.CENTER);

        panel.add(noteContent, gbc);
        noteContent.setVerticalTextPosition(JLabel.CENTER);
        noteContent.setVerticalAlignment(JLabel.CENTER);

        // set background colors
        backgroundSelectionColor = defaultRenderer.getBackgroundSelectionColor();
        backgroundNonSelectionColor = defaultRenderer.getBackgroundNonSelectionColor();

    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected,
                                                  boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Component rendererComponent = null;
        if ((value != null) && (value instanceof DefaultMutableTreeNode)) {
            Object userObject = ((DefaultMutableTreeNode) value).getUserObject();
            if (userObject instanceof Note) {
                Note note = (Note) userObject;

                if (includeFilePath) {
                    filePath.setText(note.getFilePath());
                }
                lineNumber.setText(((Integer) note.getLineNumber()).toString());
                noteContent.setText(note.getContent());

                if (selected) {
                    panel.setBackground(backgroundSelectionColor);
                } else {
                    panel.setBackground(backgroundNonSelectionColor);
                }
                panel.setEnabled(tree.isEnabled());

                rendererComponent = panel;
            }
        }
        if (rendererComponent == null) {
            rendererComponent = defaultRenderer.getTreeCellRendererComponent(tree, value, selected, expanded,
                    leaf, row, hasFocus);
        }
        return rendererComponent;
    }
}
