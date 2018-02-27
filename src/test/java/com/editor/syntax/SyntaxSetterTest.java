package com.editor.syntax;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SyntaxSetterTest {

    @Test
    public void setCurrentSyntaxTest() {
        SyntaxSetter.setCurrentSyntax(SyntaxType.ERLANG);
        assertEquals(SyntaxSetter.getCurrentSyntax(), SyntaxType.ERLANG);
    }

    @Test
    public void setCurrentSyntaxByFileExtensionTest() {
        SyntaxSetter.setCurrentSyntaxByFileExtension("js");
        assertEquals(SyntaxSetter.getCurrentSyntax(), SyntaxType.JAVASCRIPT);

        SyntaxSetter.setCurrentSyntaxByFileExtension("hs");
        assertEquals(SyntaxSetter.getCurrentSyntax(), SyntaxType.HASKELL);

        SyntaxSetter.setCurrentSyntaxByFileExtension("erl");
        assertEquals(SyntaxSetter.getCurrentSyntax(), SyntaxType.ERLANG);

        SyntaxSetter.setCurrentSyntaxByFileExtension("er");
        assertEquals(SyntaxSetter.getCurrentSyntax(), SyntaxType.TEXT);

        SyntaxSetter.setCurrentSyntaxByFileExtension("java");
        assertEquals(SyntaxSetter.getCurrentSyntax(), SyntaxType.TEXT);

        SyntaxSetter.setCurrentSyntaxByFileExtension("txt");
        assertEquals(SyntaxSetter.getCurrentSyntax(), SyntaxType.TEXT);

        SyntaxSetter.setCurrentSyntaxByFileExtension("j");
        assertEquals(SyntaxSetter.getCurrentSyntax(), SyntaxType.TEXT);
    }
}
