package io.github.camshaft54.chineseflashcards.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

public class MatchCell extends JPanel {
    public static Color regular = new Color(0, 0, 0, 75);
    public static Color correct = new Color(0, 255, 0, 255);
    public static Color partial = new Color(255, 255, 0, 255);
    public static Color incorrect = new Color(255, 0, 0, 255);
    public static Color border = new Color(0, 0, 0, 125);
    public static Color textColor = new Color(0, 0, 0, 255);

    private final Card card;
    private final int language;
    private final JLabel label;

    public MatchCell(Card card, int language, MouseListener mouseListener, int fontSize) {
        this.card = card;
        this.language = language;

        label = new JLabel(card.getLanguageEntry(language));
        label.setFont(Card.getFont(language, fontSize));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setName("tile");

        setBorder(BorderFactory.createLineBorder(border, 2));
        addMouseListener(mouseListener);
        setBackground(regular);
        setLayout(new BorderLayout());
        add(label, BorderLayout.CENTER);
        setEnabled(false);
    }

    public Card getCard() {
        return card;
    }

    public int getLanguage() {
        return language;
    }

    public JLabel getLabel() {
        return label;
    }
}
