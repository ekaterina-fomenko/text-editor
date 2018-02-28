package com.editor.syntax.keywords;

import com.editor.syntax.SyntaxSetter;
import com.editor.syntax.SyntaxType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Contains list of reserved words for erlang, haskell, javascript.
 * Create trie for each list.
 */

public class Keywords {
    private static final List<String> js_keywords_list = Arrays.asList(
            "var",
            "new",
            "class",
            "this",
            "super",
            "implements",
            "extends",
            "interface",
            "import",
            "function",
            "public",
            "default",
            "private",
            "protected",
            "abstract",
            "static",
            "final",
            "finally",
            "void",
            "null",
            "int",
            "double",
            "long",
            "byte",
            "native ",
            "char",
            "float",
            "short",
            "enum",
            "synchronized",
            "true",
            "false",
            "volatile",
            "goto",
            "while",
            "throws",
            "boolean",
            "export",
            "in",
            "try",
            "catch",
            "delete",
            "throw",
            "with",
            "while",
            "for",
            "continue",
            "return",
            "else",
            "typOf",
            "instanceOf",
            "break",
            "do",
            "if",
            "package",
            "transient"
    );

    private static final List<String> hs_keywords_list = Arrays.asList(
            "return",
            "case",
            "class",
            "data",
            "deriving",
            "do",
            "else",
            "if",
            "import",
            "in",
            "infix",
            "infixr",
            "instance",
            "let",
            "of",
            "module",
            "newtype",
            "then",
            "type",
            "where",
            "foreign",
            "forall",
            "hiding",
            "proc",
            "rec",
            "qualified"
    );

    private static final List<String> er_keywords_list = Arrays.asList(
            "fun",
            "module",
            "export",
            "import",
            "after",
            "case",
            "or",
            "and",
            "orelse",
            "andalso",
            "cond",
            "query",
            "band",
            "div",
            "receive",
            "rem",
            "end",
            "begin",
            "bnot",
            "bor",
            "if",
            "let",
            "xor",
            "bsl",
            "not",
            "of",
            "bxor",
            "try",
            "when",
            "ok",
            "error",
            "undefined",
            "reply",
            "noreply",
            "stop",
            "ignore"
    );

    public static SyntaxResolver js_keywords_trie = new SyntaxResolver(js_keywords_list);
    public static SyntaxResolver er_keywords_trie = new SyntaxResolver(er_keywords_list);
    public static SyntaxResolver hs_keywords_trie = new SyntaxResolver(hs_keywords_list);
    public static SyntaxResolver text_keywords_trie = new SyntaxResolver(new ArrayList<>());

    public static SyntaxResolver getCurrentSyntaxTrie() {
        SyntaxType syntax = SyntaxSetter.getCurrentSyntax();
        switch (syntax) {
            case JAVASCRIPT:
                return js_keywords_trie;
            case HASKELL:
                return hs_keywords_trie;
            case ERLANG:
                return er_keywords_trie;
            case TEXT:
            default:
                return text_keywords_trie;
        }
    }
}
