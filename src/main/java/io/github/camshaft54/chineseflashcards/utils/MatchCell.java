package io.github.camshaft54.chineseflashcards.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

public class MatchCell extends JLabel {
    private final Card card;
    private final int language;

    public MatchCell(Card card, int language, MouseListener mouseListener) {
        this.card = card;
        this.language = language;

        setText(card.getLanguageEntry(language));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        setFont(Card.getFont(language, 30));
        setHorizontalAlignment(JLabel.CENTER);
        setName("tile");
        addMouseListener(mouseListener);
        setBackground(Color.BLACK);
        setOpaque(true);
    }

    public Card getCard() {
        return card;
    }

    public int getLanguage() {
        return language;
    }
}
