package main.java.com.editor.model;

import java.util.ArrayList;
import java.util.List;

public class TextEditorModel {
    private Pointer pointer;
    private StringBuilder textBuilder;
    private List<Integer> lineLengthsList;

    public TextEditorModel() {
        this.pointer = new Pointer();
        this.textBuilder = new StringBuilder();

        this.lineLengthsList = new ArrayList<>();
        this.lineLengthsList.add(0);
    }

    public void addText(int position, String text) {
        this.textBuilder.insert(position, text);
    }

    public Pointer getPointer() {
        return pointer;
    }

    public StringBuilder getTextBuilder() {
        return textBuilder;
    }

    public List<Integer> getLineLengthsList() {
        return lineLengthsList;
    }
}
