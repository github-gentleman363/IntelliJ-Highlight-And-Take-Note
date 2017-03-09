import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class ViewAllNotesAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        ViewAllNotesDialog viewAllNotesDialog = new ViewAllNotesDialog();
        viewAllNotesDialog.show();

//        if (viewAllNotesDialog.isOK()) {
    }
}
