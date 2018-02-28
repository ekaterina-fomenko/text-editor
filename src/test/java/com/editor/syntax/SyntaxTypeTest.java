package com.editor.syntax;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SyntaxTypeTest {
    @Test
    public void getByExtension() {
        assertEquals(SyntaxType.getByExtension("js"), SyntaxType.JAVASCRIPT);
        assertEquals(SyntaxType.getByExtension("hs"), SyntaxType.HASKELL);
        assertEquals(SyntaxType.getByExtension("erl"), SyntaxType.ERLANG);
        assertEquals(SyntaxType.getByExtension("txt"), SyntaxType.TEXT);
        assertEquals(SyntaxType.getByExtension("er"), SyntaxType.TEXT);
    }
}
