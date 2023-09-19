package com.dekolis.hebrewflashcards.utils;

import java.awt.*;
import java.util.ArrayList;

/**
 * Represents a "three-sided" card with a JavaBeans conforming class. Stores the stars the card has as pairs of numbers.
 */
public class Card {
    private ArrayList<String> stars = new ArrayList<>();
    private String hebrew;
    private String transliteration;
    private String english;
    private static final Font englishFont = new Font("Arial", Font.PLAIN, 30);
    private static final Font transliterationFont = new Font("Arial", Font.PLAIN, 30);
    private static Font hebrewFont = new Font("Arial Hebrew", Font.PLAIN, 30);

    public ArrayList<String> getStars() {
        return stars;
    }

    @SuppressWarnings("unused")
    public void setStars(ArrayList<String> stars) {
        this.stars = stars;
    }

    public String getHebrew() {
        return hebrew;
    }

    public String getTransliteration() {
        return transliteration;
    }

    public String getEnglish() {
        return english;
    }

    public void setHebrew(String hebrew) {
        this.hebrew = hebrew;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public void setTransliteration(String transliteration) {
        this.transliteration = transliteration;
    }

    public String getLanguageEntry(int language) {
        switch (language) {
            case Flashcard.ENGLISH -> {
                return getEnglish();
            }
            case Flashcard.HEBREW -> {
                return getHebrew();
            }
            case Flashcard.TRANSLITERATION -> {
                return getTransliteration();
            }
            default -> throw new IllegalArgumentException("Invalid language. Must be Flashcard.HEBREW, Flashcard.ENGLISH, or Flashcard.TRANSLITERATION");
        }
    }

    public static Font getHebrewFont(int size) {
        return hebrewFont.deriveFont((float) size);
    }

    /**
     * Changes the Hebrew font from print to script or vice versa
     *
     * @return true if font was changed to print, false if font was changed to script
     */
    public static boolean toggleHebrewFontScriptPrint() {
        if (hebrewFont.getName().equals("Arial Hebrew")) {
            hebrewFont = new Font("Dana Yad AlefAlefAlef", Font.PLAIN, 30);
            return false;
        } else {
            hebrewFont = new Font("Arial Hebrew", Font.PLAIN, 30);
            return true;
        }
    }

    public static Font getEnglishFont(int size) {
        return englishFont.deriveFont((float) size);
    }

    public static Font getTransliterationFont(int size) {
        return transliterationFont.deriveFont((float) size);
    }

    public static Font getFont(int language, int size) {
        switch (language) {
            case Flashcard.ENGLISH -> {
                return getEnglishFont(size);
            }
            case Flashcard.HEBREW -> {
                return getHebrewFont(size);
            }
            case Flashcard.TRANSLITERATION -> {
                return getTransliterationFont(size);
            }
            default -> throw new IllegalArgumentException("Invalid language. Must be Flashcard.HEBREW, Flashcard.ENGLISH, or Flashcard.TRANSLITERATION");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Card) {
            Card otherCard = (Card) obj;
            return otherCard.getTransliteration().equals(transliteration) && otherCard.getHebrew().equals(hebrew) && otherCard.getEnglish().equals(english);
        }
        return false;
    }

    @Override
    public String toString() {
        return "Hebrew: " + hebrew + " English: " + english + " Transliteration: " + transliteration;
    }

    public static Card createCard(String hebrew, String transliteration, String english) {
        Card card = new Card();
        card.setHebrew(hebrew);
        card.setTransliteration(transliteration);
        card.setEnglish(english);
        return card;
    }
}
