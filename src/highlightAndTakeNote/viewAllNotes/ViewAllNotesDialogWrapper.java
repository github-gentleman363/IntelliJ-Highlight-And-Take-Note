package highlightAndTakeNote.viewAllNotes;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;

import javax.swing.*;

public class ViewAllNotesDialogWrapper extends DialogWrapper {
    private ViewAllNotesDialog viewAllNotesDialog;

    public ViewAllNotesDialogWrapper(Project project) {
        super(project);
        this.viewAllNotesDialog = new ViewAllNotesDialog(project);
        this.setTitle("Find All Notes");
        this.init();
        this.getButton(this.getCancelAction()).setVisible(false);
        this.setOKButtonText("Close");
    }

    @Override
    protected String getDimensionServiceKey() {
        return getClass().getName();
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return this.viewAllNotesDialog.getPreferredFocusedComponent();
    }

    @Override
    protected JComponent createCenterPanel() {
        return this.viewAllNotesDialog.getPanel();
    }


}
