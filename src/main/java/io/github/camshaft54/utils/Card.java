package io.github.camshaft54.utils;

import java.awt.*;
import java.util.ArrayList;

/**
 * Represents a "three-sided" card with a JavaBeans conforming class. Stores the stars the card has as pairs of numbers.
 */
public class Card {
    private ArrayList<String> stars = new ArrayList<>();
    private String chinese;
    private String pinyin;
    private String english;

    public ArrayList<String> getStars() {
        return stars;
    }

    @SuppressWarnings("unused")
    public void setStars(ArrayList<String> stars) {
        this.stars = stars;
    }

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
