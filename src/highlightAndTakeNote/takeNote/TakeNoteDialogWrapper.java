package highlightAndTakeNote.takeNote;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TakeNoteDialogWrapper extends DialogWrapper {

    private TakeNoteDialog takeNoteDialog;

    private static final int DELETE_NOTE_EXIT_CODE = NEXT_USER_EXIT_CODE;

    private boolean isAddMode;     // view/edit mode if false

    public TakeNoteDialogWrapper(Project project, boolean isAddMode) {
        super(project);
        this.takeNoteDialog = new TakeNoteDialog();
        this.isAddMode = isAddMode;
        this.setTitle(this.isAddMode ? "Add Note" : "View / Edit Note");

        this.init();
        // TODO disable submit button if no text is inputted
        //        this.setOKActionEnabled(false);
        //        this.takeNoteDialog.setKeyListener(new KeyAdapter() {
        //            @Override
        //            public void keyReleased(KeyEvent e) {
        //                setOKActionEnabled(!"".equals(((JTextField)e.getComponent()).getText()));
        //            }
        //        });
        if (this.isAddMode) {
            this.getButton(this.getCancelAction()).setVisible(false);
        }
        this.setOKButtonText("Save");
    }

    @Override
    protected String getDimensionServiceKey() {
        return getClass().getName();
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return this.takeNoteDialog.getPreferredFocusedComponent();
    }

    @Override
    protected JComponent createCenterPanel() {
        return this.takeNoteDialog.getPanel();
    }

    @Override
    @NotNull
    protected Action[] createActions() {
        if (this.isAddMode)
            return super.createActions();
        return new Action[]{getOKAction(), new DeleteNoteAction(), getHelpAction()};
    }

    public boolean isDeleteNoteOnExit() {
        return getExitCode() == DELETE_NOTE_EXIT_CODE;
    }

    public TakeNoteDialog getTakeNoteDialog() {
        return takeNoteDialog;
    }

    public void setContent(String content) {
        this.takeNoteDialog.setText(content);
    }

    public void setColor(Color color) {
        this.takeNoteDialog.setColor(color);
    }

    protected class DeleteNoteAction extends DialogWrapperAction {
        private DeleteNoteAction() {
            super("Delete Note");
        }

        @Override
        protected void doAction(ActionEvent e) {
            close(DELETE_NOTE_EXIT_CODE);
        }
    }

}
