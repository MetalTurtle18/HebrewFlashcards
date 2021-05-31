package io.github.camshaft54.windows;

import io.github.camshaft54.ChineseFlashcards;
import io.github.camshaft54.panels.Flashcard;
import io.github.camshaft54.utils.Set;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * SetSettingsPopup presents to the user three options regarding the removal of stars:
 * - Remove all stars on all cards in a set
 * - Remove stars for the current language configuration on all cards in a set
 * - Remove all stars for the current card
 */
public class SetSettingsPopup extends JFrame implements ActionListener {
    Set set;
    Flashcard currentFlashcard;
    String currentLangConfig;

    /**
     * Initializes JFrame with the three buttons
     * @param set The set to make modifications to
     * @param currentFlashcard The flashcard to make modifications to
     * @param currentLangConfig The current language config in the StarFlashcardsViewer
     */
    public SetSettingsPopup(Set set, Flashcard currentFlashcard, String currentLangConfig) {
        this.set = set;
        this.currentFlashcard = currentFlashcard;
        this.currentLangConfig = currentLangConfig;

        setTitle("Set Settings");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(ChineseFlashcards.mainWindow);
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        getRootPane().setBorder(new EmptyBorder(10, 10, 10, 10));
        // Set custom image as icon
        try {
            setIconImage(ImageIO.read(ChineseFlashcards.class.getResourceAsStream("/assets/CFS.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JButton clearAllSetStarsButton = new JButton("Clear All Stars for Set");
        clearAllSetStarsButton.setAlignmentX(CENTER_ALIGNMENT);
        clearAllSetStarsButton.addActionListener(this);
        JButton clearCurrLangSetStarsButton = new JButton("Clear Stars with Current Language Configuration");
        clearCurrLangSetStarsButton.setAlignmentX(CENTER_ALIGNMENT);
        clearCurrLangSetStarsButton.addActionListener(this);
        JButton clearAllCurrCardStarsButton = new JButton("Clear All Stars for Current Card");
        clearAllCurrCardStarsButton.setAlignmentX(CENTER_ALIGNMENT);
        clearAllCurrCardStarsButton.addActionListener(this);

        add(clearAllSetStarsButton);
        add(clearCurrLangSetStarsButton);
        add(clearAllCurrCardStarsButton);

        pack();
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Clear All Stars for Set" -> set.getCards().forEach(card -> card.getStars().clear());
            case "Clear Stars with Current Language Configuration" -> set.getCards().forEach(card -> card.getStars().removeIf(s -> s.equals(currentLangConfig)));
            case "Clear All Stars for Current Card" -> currentFlashcard.removeStar(Character.getNumericValue(currentLangConfig.charAt(0)), Character.getNumericValue(currentLangConfig.charAt(2)));
        }
        if (ChineseFlashcards.mainWindow.starFlashcardsViewer != null) {
            ChineseFlashcards.mainWindow.starFlashcardsViewer.refreshFlashcard();
        }
        dispose();
    }
}
