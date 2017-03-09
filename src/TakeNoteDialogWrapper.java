import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;

import javax.swing.*;

public class TakeNoteDialogWrapper extends DialogWrapper {
    private TakeNoteDialog takeNoteDialog;

    public TakeNoteDialogWrapper(Project project) {
        super(project);
        this.takeNoteDialog = new TakeNoteDialog();
        this.setTitle("Add Note");
        this.init();
        // TODO disable submit button if no text is inputted
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

}
