import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by erikayoon on 3/8/17.
 */
public class TakeNoteDialogWrapper extends DialogWrapper {
    private TakeNoteDialog takeNoteDialog;

    public TakeNoteDialogWrapper(Project project) {
        super(project);
        this.takeNoteDialog = new TakeNoteDialog();
        this.setTitle("Add Note");
        this.init();
//        this.setOKActionEnabled(false);
//        this.takeNoteDialog.setKeyListener(new KeyAdapter() {
//            @Override
//            public void keyReleased(KeyEvent e) {
//                setOKActionEnabled(!"".equals(((JTextField)e.getComponent()).getText()));
//            }
//        });
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

    public TakeNoteDialog getTakeNoteDialog() {
        return takeNoteDialog;
    }

    public void setTakeNoteDialog(TakeNoteDialog takeNoteDialog) {
        this.takeNoteDialog = takeNoteDialog;
    }

    /*
    @Override
    protected void doOKAction() {
        if(isOKActionEnabled()) {
            if(saveReviewsForm.fileExists()) {
                if(Messages.showOkCancelDialog("This file already exists." +
                                " Would you like to overwrite it?",
                        "File Already Exists",
                        Messages.getWarningIcon()) == Messages.OK) {
                    super.doOKAction();
                }
            } else {
                super.doOKAction();
            }
        }
    }*/
}
