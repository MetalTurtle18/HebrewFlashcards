package io.github.camshaft54.chineseflashcards.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Holds a list of Cards and a name for the set. This is used when storing the sets in the "sets" folder.
 */
public class Set {
    List<Card> cards = new ArrayList<>();
    String name;
    String filename;

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Set getStarredSet(Set oldSet) {
        Set newSet = new Set();
        newSet.setName(oldSet.getName());
        for (int i = 0; i < oldSet.getCards().size(); i++) {
            if (oldSet.getCards().get(i).getStars().size() > 0) {
                newSet.getCards().add(oldSet.getCards().get(i));
            }
        }
        return newSet;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public static Set combineSets(List<Set> sets) {
        Set combinedSet = new Set();
        String combinedName = sets.stream().map(Set::getName).collect(Collectors.joining(" and "));
        combinedSet.setName(combinedName);
        combinedSet.setFilename("N/A");
        combinedSet.setCards(sets.stream().flatMap(set -> set.getCards().stream()).collect(Collectors.toList()));
        return combinedSet;
    }
}
