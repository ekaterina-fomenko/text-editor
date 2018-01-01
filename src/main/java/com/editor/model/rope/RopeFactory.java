package com.editor.model.rope;

public class RopeFactory {
    public static Rope create(String string) {
        if (string.length() <= Rope.MAX_LENGTH_IN_ROPE) {
            return new Rope(string);
        }
        Rope rope = new Rope("");
        for (int i = 0; i < string.length(); i = i + Rope.MAX_LENGTH_IN_ROPE) {
            if ((i + Rope.MAX_LENGTH_IN_ROPE)<=string.length()){
                rope = rope.append(new Rope(string.substring(i, i + Rope.MAX_LENGTH_IN_ROPE)));
            }else{
                rope = rope.append(new Rope(string.substring(i)));
            }
        }
        return rope;
    }
}
