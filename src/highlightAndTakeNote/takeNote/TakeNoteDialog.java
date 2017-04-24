package highlightAndTakeNote.takeNote;

import javax.swing.*;
import java.awt.*;


public class TakeNoteDialog extends JDialog {
    private JPanel contentPane;
    private JTextArea textArea;
    private JRadioButton redButton;
    private JRadioButton yellowButton;
    private JRadioButton greenButton;
    private JRadioButton blueButton;
    private ButtonGroup buttonGroup;

    public TakeNoteDialog() {
        setContentPane(this.contentPane);
        setModal(true);

        ButtonGroup chooseColor = this.buttonGroup = new ButtonGroup();
        chooseColor.add(redButton);
        chooseColor.add(yellowButton);
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
        return this.textArea.getText();
    }

    public void setText(String text) {
        this.textArea.insert(text, 0);
    }

    public Color getSelectedColor() {
        // TODO get the selected button from button group
        if (redButton.isSelected()) {
            return Color.RED;
        } else if (yellowButton.isSelected()) {
            return Color.YELLOW;
        } else if (greenButton.isSelected()) {
            return Color.GREEN;
        } else if (blueButton.isSelected()) {
            return Color.BLUE;
        } else {
            return null;
        }
    }

    public void setColor(Color color) {
        if (color == Color.RED) {
            redButton.setSelected(true);
        } else if (color == Color.YELLOW) {
            yellowButton.setSelected(true);
        } else if (color == Color.GREEN) {
            greenButton.setSelected(true);
        } else {
            blueButton.setSelected(true);
        }
    }

}
