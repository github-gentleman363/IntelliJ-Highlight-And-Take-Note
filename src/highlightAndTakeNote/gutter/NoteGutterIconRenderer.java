package highlightAndTakeNote.gutter;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import highlightAndTakeNote.NoteManager;
import highlightAndTakeNote.model.Note;
import highlightAndTakeNote.takeNote.TakeNoteDialog;
import highlightAndTakeNote.takeNote.TakeNoteDialogWrapper;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class NoteGutterIconRenderer extends GutterIconRenderer {

    private static final String IMAGE_PATH = "/highlightAndTakeNote/images/";

    private static final String RED_ICON_PATH = NoteGutterIconRenderer.IMAGE_PATH + "icons8-Comments-red.png";
    private static final String GREEN_ICON_PATH = NoteGutterIconRenderer.IMAGE_PATH + "icons8-Comments-green.png";
    private static final String BLUE_ICON_PATH = NoteGutterIconRenderer.IMAGE_PATH + "icons8-Comments-blue.png";
    private static final String YELLOW_ICON_PATH = NoteGutterIconRenderer.IMAGE_PATH + "icons8-Comments-yellow.png";

    // https://stackoverflow.com/questions/6802483/how-to-directly-initialize-a-hashmap-in-a-literal-way
    private static final Map<Color, String> colorToImagePathMap = createColorToImagePathMap();
    private static Map<Color, String> createColorToImagePathMap() {
        Map<Color,String> colorToImagePathMap = new HashMap<Color, String>();
        colorToImagePathMap.put(Color.RED, RED_ICON_PATH);
        colorToImagePathMap.put(Color.GREEN, GREEN_ICON_PATH);
        colorToImagePathMap.put(Color.BLUE, BLUE_ICON_PATH);
        colorToImagePathMap.put(Color.YELLOW, YELLOW_ICON_PATH);
        return colorToImagePathMap;
    }

    private NoteGutter noteGutter;

    private Icon icon;

    public NoteGutterIconRenderer(NoteGutter noteGutter) {
        this.noteGutter = noteGutter;
    }

    @NotNull
    @Override
    public Icon getIcon() {
        this.icon = IconLoader.getIcon(colorToImagePathMap.get(this.getNote().getColor()));
        return icon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoteGutterIconRenderer that = (NoteGutterIconRenderer) o;
        return getIcon().equals(that.getIcon());
    }

    @Override
    public int hashCode() {
        return getIcon().hashCode();
    }

    @Override
    public boolean isNavigateAction() {
        return true;
    }

    @Override
    public String getTooltipText() {
        return this.getNote().getContent();
    }

    @Override
    public AnAction getClickAction() {
        Note note = this.getNote();

        // TODO: extract this class into a separate file
        return new AnAction() {
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

        };
    }

    private Note getNote() {
        return this.noteGutter.getNote();
    }

}
