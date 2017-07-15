package main.java.com.editor.parser;
/**
 * This class includes all regex templates
 */
public class Regex {
    public static final String BEFORE_REGEX = "(?<=^|\\s|\\(|\\{|\\[|\\)|\\}|\\]|,|\\+|\\-|\\*|\\/)";
    public static final String AFTER_REGEX = "(?=$|\\s|\\(|\\{|\\[|\\)|\\}|\\]|,|\\+|\\-|\\*|\\/)";

    public static final String JS_RESERVED_WORDS = "(var|new|class|this|super|implements|extends|interface|import|function|public|default|private|protected|abstract|static|final|finally|void|null|int|double|long|byte|native|char|float|short|enum|synchronized|true|false|volatile|goto|while|throws|boolean|export|in|try|catch|delete|throw|with|while|for|continue|return|else|typOf|instanceOf|break|do|if|package|transient)";
}
