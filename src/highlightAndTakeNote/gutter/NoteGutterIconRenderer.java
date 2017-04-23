package highlightAndTakeNote.gutter;

import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class NoteGutterIconRenderer extends GutterIconRenderer {
    private final Icon icon = IconLoader.getIcon("/highlightAndTakeNote/images/note.png");


    @NotNull
    @Override
    public Icon getIcon() {
        return icon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoteGutterIconRenderer that = (NoteGutterIconRenderer) o;
        return icon.equals(that.getIcon());
    }

    @Override
    public int hashCode() {
        return getIcon().hashCode();
    }

}
