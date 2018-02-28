package com.editor.syntax;

import java.util.stream.Stream;

/**
 * This class provides different syntax for text-editor.
 * Also provides file extensions specific for each syntax.
 */

public enum SyntaxType {

    TEXT(""),
    JAVASCRIPT("js"),
    HASKELL("hs"),
    ERLANG("erl");

    private String fileExtension;

    SyntaxType(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public static SyntaxType getByExtension(String ext) {
        return Stream.of(SyntaxType.values())
                .filter(i -> i.getFileExtension().equals(ext))
                .findFirst()
                .orElse(TEXT);
    }
}
