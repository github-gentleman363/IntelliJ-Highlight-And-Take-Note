package highlightAndTakeNote.gutter;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ex.MarkupModelEx;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.project.Project;
import highlightAndTakeNote.model.Note;
import highlightAndTakeNote.util.Util;

public class NoteGutter {

    private final Note note;
    private RangeHighlighter highlighter = null;

    public NoteGutter(Note note) {
        this.note = note;
    }

    public Note getNote() {
        return note;
    }

    public RangeHighlighter getHighlighter() {
        return highlighter;
    }

    public void update() {
        final Project project = this.note.getProject();
        if (project == null) {
            return;
        }

        final Util util = Util.getInstance(project);
        final Document document = util.getDocument(this.note.getFilePath());
        if (document == null) {
            return;
        }

        if (this.highlighter == null) {

            OpenFileDescriptor openFileDescriptor = util.getOpenFileDescriptor(
                    this.note.getFilePath(), this.note.getStartOffset());
            if (openFileDescriptor == null) {
                return;
            }

            final Editor editor = FileEditorManagerEx.getInstance(project).openTextEditor(openFileDescriptor, true);
            if (editor == null) {
                return;
            }

            MarkupModelEx markup = (MarkupModelEx)editor.getMarkupModel();
            // TODO: Figure out what "HighlighterLayer.ERROR + 1" means
            this.highlighter = markup.addPersistentLineHighlighter(this.note.getLineNumber(), HighlighterLayer.ERROR + 1,
                    null);
            if (this.highlighter == null) {
                return;
            }

            NoteGutterIconRenderer noteGutterIconRenderer = new NoteGutterIconRenderer(this);
            highlighter.setGutterIconRenderer(noteGutterIconRenderer);
        }
    }

}
