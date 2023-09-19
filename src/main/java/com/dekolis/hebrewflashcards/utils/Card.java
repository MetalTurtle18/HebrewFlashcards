package com.dekolis.hebrewflashcards.utils;

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
    private static final Font englishFont = new Font("Arial", Font.PLAIN, 30);
    private static final Font pinyinFont = new Font("Arial", Font.PLAIN, 30);
    private static final Font chineseFont = new Font("KaiTi", Font.PLAIN, 30);

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

    public String getLanguageEntry(int language) {
        switch (language) {
            case Flashcard.ENGLISH -> {
                return getEnglish();
            }
            case Flashcard.CHINESE -> {
                return getChinese();
            }
            case Flashcard.PINYIN -> {
                return getPinyin();
            }
            default -> throw new IllegalArgumentException("Invalid language. Must be Flashcard.CHINESE, Flashcard.ENGLISH, or Flashcard.PINYIN");
        }
    }

    public static Font getChineseFont(int size) {
        return chineseFont.deriveFont((float) size);
    }

    public static Font getEnglishFont(int size) {
        return englishFont.deriveFont((float) size);
    }

    public static Font getPinyinFont(int size) {
        return pinyinFont.deriveFont((float) size);
    }

    public static Font getFont(int language, int size) {
        switch (language) {
            case Flashcard.ENGLISH -> {
                return getEnglishFont(size);
            }
            case Flashcard.CHINESE -> {
                return getChineseFont(size);
            }
            case Flashcard.PINYIN -> {
                return getPinyinFont(size);
            }
            default -> throw new IllegalArgumentException("Invalid language. Must be Flashcard.CHINESE, Flashcard.ENGLISH, or Flashcard.PINYIN");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Card) {
            Card otherCard = (Card) obj;
            return otherCard.getPinyin().equals(pinyin) && otherCard.getChinese().equals(chinese) && otherCard.getEnglish().equals(english);
        }
        return false;
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
