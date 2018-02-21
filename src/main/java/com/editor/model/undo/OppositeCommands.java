package com.editor.model.undo;

import static com.editor.model.undo.OppositeCommands.Command.*;

public class OppositeCommands {
    public enum Command {
        PASTE,
        DELETE,
        UNKNOWN
    }

    public static Command getOppositeCommand(Command command) {
        switch (command) {
            case PASTE:
                return DELETE;
            case DELETE:
                return PASTE;
        }

        return UNKNOWN;
    }
}
