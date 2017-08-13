package highlightAndTakeNote.gutter;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import highlightAndTakeNote.NoteManager;
import highlightAndTakeNote.model.Note;
import highlightAndTakeNote.takeNote.TakeNoteDialog;
import highlightAndTakeNote.takeNote.TakeNoteDialogWrapper;

import java.awt.*;

public class NoteGutterIconClickAction extends AnAction {

    private Note note;

    public NoteGutterIconClickAction(Note note) {
        this.note = note;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        // FOR NOW:
        //  this can only be available if user has clicked to add a note
        //  so user clicked at a line:
        //      with a note:
        //          show the dialog with the note, and highlight the associated code? (have ViewAddedNoteAction entity to handle this)
        //      without a note: do nothing (for now)

        final Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);

        NoteManager noteManager = NoteManager.getInstance(project);
        if (note != null) {
            // TODO: handle if code in the editor in the range doesn't match what's in note

            final SelectionModel selectionModel = editor.getSelectionModel();
            highlightNotedLines(note, selectionModel);

            TakeNoteDialogWrapper dialogWrapper = setupTakeNoteDialog(project, note);
            dialogWrapper.show();

            boolean isToDelete = false;
            if (dialogWrapper.isOK()) {
                TakeNoteDialog takeNoteDialog = dialogWrapper.getTakeNoteDialog();
                String noteContent = takeNoteDialog.getText();
                Color color = takeNoteDialog.getSelectedColor();
                String noteId = note.getId();
                noteManager.editNote(noteId, noteContent, color);
                selectionModel.removeSelection();
            } else if (dialogWrapper.isDeleteNoteOnExit()) {
                isToDelete = noteManager.deleteNote(note.getId());
            }

            // update gutter UI
            NoteGutterManager.getInstance(project).updateNoteGutter(note, isToDelete);
        }
    }

    private void highlightNotedLines(Note note, SelectionModel selectionModel) {
        int startOffset = note.getStartOffset();
        int endOffset = note.getEndOffset();
        selectionModel.setSelection(startOffset, endOffset);
    }

    private TakeNoteDialogWrapper setupTakeNoteDialog(Project project, Note note) {
        TakeNoteDialogWrapper dialogWrapper = new TakeNoteDialogWrapper(project, false);
        dialogWrapper.setContent(note.getContent());
        dialogWrapper.setColor(note.getColor());
        return dialogWrapper;
    }
}
