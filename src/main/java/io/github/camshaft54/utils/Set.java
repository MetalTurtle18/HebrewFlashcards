package io.github.camshaft54.utils;

import java.util.ArrayList;

public class Set {
    ArrayList<Card> cards = new ArrayList<>();
    String name;

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
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
}
