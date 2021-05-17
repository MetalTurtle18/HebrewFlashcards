package io.github.camshaft54.utils;

import java.awt.*;

public class Card {
    private String chinese;
    private String pinyin;
    private String english;

    public String getChinese() {
        return chinese;
    }

    public String getPinyin() {
        return pinyin;
    }

    public String getEnglish() {
        return english;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public static Font getChineseFont(int size) {
        return new Font("KaiTi", Font.PLAIN, size);
    }

    public static Font getEnglishFont(int size) {
        return new Font("Arial", Font.PLAIN, size);
    }

    public static Font getPinyinFont(int size) {
        return new Font("Arial", Font.PLAIN, size);
    }

    @Override
    public String toString() {
        return "Chinese: " + chinese + " English: " + english + " Pinyin: " + pinyin;
    }

    public static Card createCard(String chinese, String pinyin, String english) {
        Card card = new Card();
        card.setChinese(chinese);
        card.setPinyin(pinyin);
        card.setEnglish(english);
        return card;
    }
}
