package com.editor.system;

import java.util.Optional;

public interface ClipboardAdapter {
    Optional<String> getText();

    void setText(String text);
}
