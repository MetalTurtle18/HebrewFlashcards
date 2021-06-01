package io.github.camshaft54.utils;

/**
 * Represents each entry in the dictionary. This class simply holds the pinyin and and english for the entry in the dict
 * object in ChineseFlashcards.
 */
public class DictEntry {
    String pinyin;
    String english;
    public DictEntry(String pinyin, String english) {
        this.pinyin = pinyin;
        this.english = english;
    }

    public String getPinyin() {
        return pinyin;
    }

    public String getEnglish() {
        return english;
    }

    @Override
    public String toString() {
        return "DictEntry{" +
                "pinyin='" + pinyin + '\'' +
                ", english='" + english + '\'' +
                '}';
    }
}
