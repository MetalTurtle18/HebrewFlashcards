package io.github.camshaft54.utils;

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
