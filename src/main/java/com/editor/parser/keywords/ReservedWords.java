package com.editor.parser.keywords;

import com.editor.parser.Syntax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Contains list of reserved words for erlang, haskell, javascript.
 * Create trie for each list.
 */

public class ReservedWords {
    public static final List<String> js_keywords_list = Arrays.asList(
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

    public static final List<String> hs_keywords_list = Arrays.asList(
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

    public static final List<String> er_keywords_list = Arrays.asList(
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

    public static Trie getKeyWordsTrie(Syntax syntax) {
        List<String> keyWords;
        Trie js_keywords_trie = new Trie();
        switch (syntax) {
            case JAVASCRIPT:
                keyWords = js_keywords_list;
                break;
            case HASKELL:
                keyWords = hs_keywords_list;
                break;
            case ERLANG:
                keyWords = er_keywords_list;
                break;
            case TEXT:
            default:
                keyWords = new ArrayList<>();
        }
        keyWords.forEach(js_keywords_trie::put);
        return js_keywords_trie;
    }
}
