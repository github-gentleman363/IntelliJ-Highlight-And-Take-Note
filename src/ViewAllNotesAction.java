import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;

public class ViewAllNotesAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        ViewAllNotesDialogWrapper viewAllNotesDialogWrapper = new ViewAllNotesDialogWrapper(project);
        viewAllNotesDialogWrapper.show();
    }
}
