package highlightAndTakeNote.takeNote;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import highlightAndTakeNote.NoteManager;
import highlightAndTakeNote.gutter.NoteGutterManager;
import highlightAndTakeNote.model.Note;

import java.awt.*;

public class TakeNoteAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);

        // Get highlighted code and its location in file
        final SelectionModel selectionModel = editor.getSelectionModel();
        final int startOffset = selectionModel.getSelectionStart();
        final int endOffset = selectionModel.getSelectionEnd();
        if (selectionModel.getSelectionStartPosition() == null) return;
        final int lineNumber = selectionModel.getSelectionStartPosition().getLine();
        final String code = selectionModel.getSelectedText();

        // Get name of the file being noted
        final VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(editor.getDocument());
        if (virtualFile == null) return;
        final String filePath = VfsUtil.getRelativeLocation(virtualFile, project.getBaseDir());

        TakeNoteDialogWrapper dialogWrapper = new TakeNoteDialogWrapper(project, true);
        dialogWrapper.show();

        if (dialogWrapper.isOK()) {
            NoteManager manager = NoteManager.getInstance(project);
            TakeNoteDialog takeNoteDialog = dialogWrapper.getTakeNoteDialog();

            String noteContent = takeNoteDialog.getText().trim();
            Color color = takeNoteDialog.getSelectedColor();

            Note newNote = manager.addNewNote(project, startOffset, endOffset, lineNumber, noteContent,
                    filePath, code, color);
            if (newNote == null) {
                return;
            }

            // update gutter UI
            NoteGutterManager.getInstance(project).updateNoteGutter(newNote, false);
        }
    }

}
