package com.editor.parser;

public enum Syntax {
    TEXT("", ""),
    JAVASCRIPT("js", "//"),
    HASKELL("hs", "--"),
    ERLANG("erl", "%");

    private String fileExtension;
    private String lineComments;

    Syntax(String fileExtension, String lineComments) {
        this.fileExtension = fileExtension;
        this.lineComments = lineComments;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public String getLineComments(){
        return lineComments;
    }
}
