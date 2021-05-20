package io.github.camshaft54.panels;

import io.github.camshaft54.ChineseFlashcards;
import io.github.camshaft54.utils.Card;
import io.github.camshaft54.utils.Set;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;

public class WelcomePanel extends JPanel implements ActionListener {
    private boolean useStarredCards;
    private final JComboBox<String> setMenu;

    public WelcomePanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel welcomeText1 = new JLabel("Welcome to the");
        Font welcomeFont = new Font("Arial Nova", Font.PLAIN, 38);
        welcomeText1.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeText1.setFont(welcomeFont);
        JLabel welcomeText2 = new JLabel("Chinese Flashcard System!");
        welcomeText2.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeText2.setFont(welcomeFont);

        JLabel setMenuInfo = new JLabel("Select Set:");
        setMenuInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] setMenuOptions = new String[ChineseFlashcards.sets.size()];
        for (int i = 0; i < setMenuOptions.length; i++) {
            setMenuOptions[i] = ChineseFlashcards.sets.get(i).getName();
        }
        setMenu = new JComboBox<>(setMenuOptions);
        setMenu.setMaximumSize(new Dimension(175, 30));
        setMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        setMenu.addActionListener(this);

        JCheckBox showStarredCards = new JCheckBox("Starred Cards");
        showStarredCards.addItemListener(e -> useStarredCards = e.getStateChange() == ItemEvent.SELECTED);

        JButton startButton = new JButton("Start");
        startButton.setFont(Card.getEnglishFont(20));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(this);

        add(welcomeText1);
        add(welcomeText2);
        add(Box.createVerticalStrut(10));
        add(setMenuInfo);
        add(setMenu);
        add(Box.createVerticalStrut(10));
        add(showStarredCards);
        add(Box.createVerticalStrut(10));
        add(startButton);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Start")) {
            Set selectedSet = ChineseFlashcards.sets.get(setMenu.getSelectedIndex());
            if (useStarredCards) {
                selectedSet = Set.getStarredSet(selectedSet);
                if (selectedSet.getCards().size() == 0) {
                    JOptionPane.showMessageDialog(this, "You have no starred cards for this set!", "No Starred Cards", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            ChineseFlashcards.mainWindow.showFlashcardPanel(selectedSet, useStarredCards);
        }
    }
}
