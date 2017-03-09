import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogBuilder;
import com.intellij.openapi.ui.DialogWrapper;

import java.awt.event.ActionEvent;

/**
 * Created by jiwoong.youn on 3/8/17.
 */
public class TakeNoteAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        NoteGutter noteGutter = new NoteGutter();
        editor.getGutter().registerTextAnnotation(noteGutter); // TODO

        // TODO set size + position
        TakeNoteDialogWrapper dialogWrapper = new TakeNoteDialogWrapper(project);
        dialogWrapper.show();

        if (dialogWrapper.isOK()) {
            System.out.println(dialogWrapper.getTakeNoteDialog().getText());
        }
    }
}
