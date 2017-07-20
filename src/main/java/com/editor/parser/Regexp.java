package com.editor.parser;

/**
 * This class includes all regexp templates
 */
public class Regexp {
    public static final String BEFORE_REGEX = "(?<=^|\\s|\\(|\\{|\\[|\\)|\\}|\\]|,|\\+|\\-|\\*|\\/)";
    public static final String AFTER_REGEX = "(?=$|\\s|\\(|\\{|\\[|\\)|\\}|\\]|,|\\+|\\-|\\*|\\/)";

    public static final String PLAIN_TEXT_WORDS = "";
    public static final String JS_RESERVED_WORDS = "(var|new|class|this|super|implements|extends|interface|import|function|public|default|private|protected|abstract|static|final|finally|void|null|int|double|long|byte|native|char|float|short|enum|synchronized|true|false|volatile|goto|while|throws|boolean|export|in|try|catch|delete|throw|with|while|for|continue|return|else|typOf|instanceOf|break|do|if|package|transient)";
    public static final String HASKELL_RESERVED_WORDS = "(case|class|data|deriving|do|else|if|import|in|infix|infixl|infixr|instance|let|of|module|newtype|then|type|where|foreign|forall|hiding|proc|rec|qualified)";
    public static final String ERLANG_RESERVED_WORDS = "(fun|module|export|import|after|case|or|and|orelse|andalso|cond|query|band|div|receive|rem|end|begin|bnot|bor|if|let|xor|bsl|bsr|not|of|bxor|try|when|ok|error|undefined|reply|noreply|stop|ignore)";

}
