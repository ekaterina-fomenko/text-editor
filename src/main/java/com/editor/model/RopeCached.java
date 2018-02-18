package com.editor.model;

import com.editor.model.rope.Rope;
import com.editor.model.rope.RopeApi;
import com.editor.model.rope.RopeIterator;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

public class RopeCached implements RopeApi {
    private LoadingCache<Integer, Character> charAtCache;
    private Rope rope;

    public RopeCached(Rope rope) {
        this.rope = rope;

        charAtCache = CacheBuilder.newBuilder()
                .concurrencyLevel(1)
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build(
                        new CacheLoader<Integer, Character>() {
                            public Character load(Integer key) throws Exception {
                                return rope.charAt(key);
                            }
                        });
    }

    @Override
    public int getLinesNum() {
        return rope.getLinesNum();
    }

    @Override
    public int getLength() {
        return rope.getLength();
    }

    @Override
    public int getDepth() {
        return rope.getDepth();
    }

    @Override
    public Rope substring(int start, int end) {
        return rope.substring(start, end);
    }

    @Override
    public Rope append(Rope rope) {
        return rope.append(rope);
    }

    @Override
    public Rope append(char[] str) {
        return rope.append(str);
    }

    @Override
    public Rope insert(int index, Rope text) {
        return rope.insert(index, text);
    }

    @Override
    public Rope insert(int index, char[] text) {
        return rope.insert(index, text);
    }

    @Override
    public RopeIterator iterator(int start) {
        return rope.iterator(start);
    }

    @Override
    public int charIndexOfLineStart(int lineIndex) {
        return rope.charIndexOfLineStart(lineIndex);
    }

    @Override
    public int getMaxLineLength() {
        return rope.getMaxLineLength();
    }

    @Override
    public char charAt(int i) {
//        try {
         return rope.charAt(i);
//        } catch (ExecutionException e) {
//            throw new RuntimeException(e);
//        }
    }
}
