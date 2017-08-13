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
        return new NoteGutterIconClickAction(this.getNote());
    }

    private Note getNote() {
        return this.noteGutter.getNote();
    }

}
