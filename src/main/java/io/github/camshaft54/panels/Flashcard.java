package io.github.camshaft54.panels;

import io.github.camshaft54.utils.Card;

import javax.swing.*;
import java.awt.*;

public class Flashcard extends JPanel {
    public static int ENGLISH = 0;
    public static int PINYIN = 1;
    public static int CHINESE = 2;
    Card card;
    JLabel label;
    int side;

    public Flashcard(Card card, int firstSide) {
        this.card = card;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        label = new JLabel("", SwingConstants.CENTER);
        SwingUtilities.invokeLater(() -> showSide(firstSide));
        add(Box.createVerticalGlue());
        add(label);
        add(Box.createVerticalGlue());
    }

    public void showSide(int newSide) {
        side = newSide;
        Font font;
        String newText;
        if (side == ENGLISH) {
            font = Card.getEnglishFont(30);
            newText = card.getEnglish();
        } else if (side == PINYIN) {
            font = Card.getPinyinFont(30);
            newText = card.getPinyin();
        } else {
            font = Card.getChineseFont(30);
            newText = card.getChinese();
        }
        FontMetrics fm = label.getFontMetrics(font);
        StringBuilder realString = new StringBuilder("<html><div style='text-align: center;'>");
        String testString = "";
        for (String word : newText.split(" ")) {
            testString += word;
            if (SwingUtilities.computeStringWidth(fm, testString) > this.getWidth()) {
                realString.append("<br>");
                testString = word + " ";
            }
            realString.append(word).append(" ");
        }
        realString.append("</div></html>");
        label.setText(realString.toString());
        label.setFont(font);
    }

    public int getCurrentSide() {
        return side;
    }

    public static Flashcard getEndFlashcard() {
        return new Flashcard(Card.createCard("End of Set", "End of Set", "End of Set"), ENGLISH);
    }
}
