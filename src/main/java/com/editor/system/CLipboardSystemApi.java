package com.editor.system;

import java.awt.datatransfer.DataFlavor;

/**
 * Created by Asya on 3/4/2018.
 */
public interface ClipboardSystemApi {
    boolean isDataFlavorAvailable(DataFlavor dataFlavor);

    void setContents(String text);

    Object getData(DataFlavor dataFlavor) throws Exception;
}
