package com.dekolis.hebrewflashcards.utils;

/**
 * Represents each entry in the dictionary. This class simply holds the transliteration and and english for the entry in the dict
 * object in HebrewFlashcards.
 */
public class DictEntry {
    String transliteration;
    String english;
    public DictEntry(String transliteration, String english) {
        this.transliteration = transliteration;
        this.english = english;
    }

    public String getTransliteration() {
        return transliteration;
    }

    public String getEnglish() {
        return english;
    }

    @Override
    public String toString() {
        return "DictEntry{" +
                "transliteration='" + transliteration + '\'' +
                ", english='" + english + '\'' +
                '}';
    }
}
