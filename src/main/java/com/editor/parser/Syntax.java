package com.editor.parser;

public enum Syntax {
    TEXT(""),
    JAVASCRIPT("js"),
    HASKELL("hs"),
    ERLANG("erl");

    public String getFileExtension() {
        return fileExtension;
    }

    private String fileExtension;

    Syntax(String fileExtension) {
        this.fileExtension = fileExtension;
    }
}
