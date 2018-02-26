package com.editor.model;

import com.editor.utils.StringUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class StringUtilsTest {

    @Test
    public void concat() throws Exception {
        assertArrayEquals(new char[]{'c', 'd', 'e'}, StringUtils.concat(new char[]{'c', 'd'}, new char[]{'e'}));
        assertArrayEquals(new char[]{'c', 'd', 'e'}, StringUtils.concat(new char[]{'c', 'd', 'e'}, new char[]{}));
    }
}