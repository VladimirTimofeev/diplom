package ru.netology.graphics.image;

public class TextColorSchemaImpl implements TextColorSchema {
    private final char[] replacement = new char[] {'#', '$', '@', '%', '*', '+', '-', '`'};

    @Override
    public char convert(int color) {
        double devider = (double) 256 / replacement.length;
        return replacement[(int)Math.floor(color / devider)];
    }
}
