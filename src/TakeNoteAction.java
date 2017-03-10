import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;


public class TakeNoteAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);

        final SelectionModel selectionModel = editor.getSelectionModel();
        final int startOffset = selectionModel.getSelectionStart();
        final int endOffset = selectionModel.getSelectionEnd();
        final int lineNumber = selectionModel.getSelectionStartPosition().getLine();

        String code = selectionModel.getSelectedText();
        Document document = editor.getDocument();
        VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(document);
        final String filePath = virtualFile.getPath();

        // TODO set position & size
        TakeNoteDialogWrapper dialogWrapper = new TakeNoteDialogWrapper(project, true);
        dialogWrapper.show();

        if (dialogWrapper.isOK()) {
            NoteManager manager = NoteManager.getInstance();
            String comment = dialogWrapper.getTakeNoteDialog().getText();

            manager.addNewNote(startOffset, endOffset, lineNumber, comment, filePath, code);

            // TODO fix duplicate annotation
            // TODO display color / icon next to where note is taken
            //      AND add action logic
            if (manager.hasAnyNoteInFile(filePath)) {
                editor.getGutter().closeAllAnnotations();
            }
            NoteGutter noteGutter = new NoteGutter(project, editor);
            editor.getGutter().registerTextAnnotation(noteGutter, noteGutter);
        }
    }
}
