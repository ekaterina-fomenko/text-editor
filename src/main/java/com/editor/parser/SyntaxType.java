package com.editor.parser;

/**
 * This class provides different syntax for text-editor and also provide file extensions and line comments symbols specific for each syntax
 */

public enum SyntaxType {

    TEXT("", ""),
    JAVASCRIPT("js", "//"),
    HASKELL("hs", "--"),
    ERLANG("erl", "%");

    private String fileExtension;
    private String lineComments;

    SyntaxType(String fileExtension, String lineComments) {
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
