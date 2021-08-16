package io.github.camshaft54.chineseflashcards.panels;

import io.github.camshaft54.chineseflashcards.utils.Card;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * A JPanel representation of a Card with helper methods for stars.
 */
public class Flashcard extends JPanel {
    public static final int ENGLISH = 0;
    public static final int PINYIN = 1;
    public static final int CHINESE = 2;
    private final Card card;
    private final JLabel label;
    private int side;

    public Flashcard(Card card, int firstSide) {
        this.card = card;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        label = new JLabel("", SwingConstants.CENTER);
        SwingUtilities.invokeLater(() -> showSide(firstSide));
        add(Box.createVerticalGlue());
        add(label);
        add(Box.createVerticalGlue());
    }

    /**
     * Changes the text so that it is the specified language
     * @param newSide the specified Flashcard language constant
     */
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
        // This gets the width of the font and keeps adding breaks to the JLabel until the width is less than the width of the flashcard
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

    public void setStar(int term, int definition) {
        if (!hasStar(term, definition)) {
            card.getStars().add(term + " " + definition);
        }
    }

    public boolean hasStar(int term, int definition) {
        return card.getStars().contains(term + " " + definition);
    }

    public boolean isStarred() {
        return card.getStars().size() > 0;
    }

    public void removeStar(int term, int definition) {
        if (hasStar(term, definition)) {
            card.getStars().remove(term + " " + definition);
        }
    }

    public String getStarsDisplayText() {
        // Used to get the display text in StarFlashcardsViewer so that user knows why flashcard was starred
        ArrayList<String> displayText = new ArrayList<>();
        card.getStars().forEach(s -> {
            String[] star = s.split(" ");
            displayText.add(getLangType(Integer.parseInt(star[0])) + " -> " + getLangType(Integer.parseInt(star[1])));
        });
        if (displayText.size() > 0) {
            return String.join(", ", displayText);
        } else {
            return "None";
        }
    }

    public String getLangType(int type) {
        if (type == ENGLISH) {
            return "English";
        } else if (type == PINYIN) {
            return "Pinyin";
        } else if (type == CHINESE) {
            return "Chinese";
        } else {
            return "Invalid";
        }
    }
}
