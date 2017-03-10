import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

public class NoteCellRenderer implements TreeCellRenderer {
    private JLabel lineNumber = new JLabel();
    private JLabel noteContent = new JLabel();
    private JLabel filePath = new JLabel();

    private JPanel panel = new JPanel();

    private DefaultTreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();

    private Color backgroundSelectionColor;
    private Color backgroundNonSelectionColor;

    private boolean includeFilePath;

    public NoteCellRenderer(boolean includeFilePath) {

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

        backgroundSelectionColor = defaultRenderer.getBackgroundSelectionColor();
        backgroundNonSelectionColor = defaultRenderer.getBackgroundNonSelectionColor();

    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected,
                                                  boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Component rendererComponent = null;
        if ((value != null) && (value instanceof DefaultMutableTreeNode)) {
            Object userObject = ((DefaultMutableTreeNode) value).getUserObject(); // TODO pass in Note object
            if (userObject instanceof Note) {
                Note note = (Note) userObject;
                if (includeFilePath) {
                    String[] chars = note.getFilePath().split("/");
                    filePath.setText(chars[chars.length-1]);
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
