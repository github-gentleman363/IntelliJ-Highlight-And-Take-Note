import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.VisualPosition;
import com.intellij.openapi.project.Project;


public class TakeNoteAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);

        final SelectionModel selectionModel = editor.getSelectionModel();
        final VisualPosition startPosition = selectionModel.getSelectionStartPosition();
        final VisualPosition endPosition = selectionModel.getSelectionEndPosition();
        String code = selectionModel.getSelectedText();

        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        final String filePath = project.getProjectFilePath();

        // TODO set position & size
        TakeNoteDialogWrapper dialogWrapper = new TakeNoteDialogWrapper(project);
        dialogWrapper.show();

        if (dialogWrapper.isOK()) {
            // TODO fix duplicate annotation
            // TODO display color / icon next to where note is taken
            //      AND add action logic
            NoteGutter noteGutter = new NoteGutter(project);
            editor.getGutter().registerTextAnnotation(noteGutter, noteGutter);

            NoteManager manager = NoteManager.getInstance();
            String comment = dialogWrapper.getTakeNoteDialog().getText();
            Note newNote = manager.addNewNote(startPosition, endPosition, code, comment, filePath);
        }
    }
}
