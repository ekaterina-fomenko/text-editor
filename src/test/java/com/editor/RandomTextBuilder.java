package com.editor;

import java.util.Random;

public class RandomTextBuilder {
    String text = "";

    public RandomTextBuilder addLetters(int symbolsCount) {
        Random random = new Random();

        text = text.concat(new RandomString(symbolsCount, random).nextString());

        return this;
    }

    public RandomTextBuilder addEnters(int entersCount) {
        for (int i = 0; i < entersCount; i++) {
            text += "\n";
        }

        return this;
    }

    public String getText() {
        return text;
    }
}

