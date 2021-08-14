package io.github.camshaft54.chineseflashcards.panels;

import io.github.camshaft54.chineseflashcards.ChineseFlashcards;
import io.github.camshaft54.chineseflashcards.utils.Card;
import io.github.camshaft54.chineseflashcards.utils.Set;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WelcomePanel extends JPanel implements ActionListener {
    private final JCheckBox showStarredCards;
    private final JButton startButton;
    private final JButton selectGameButton;
    private final JButton editButton;
    private final List<JCheckBox> setSelectionCheckboxes;

    /**
     * Constructor for WelcomePanel. Adds text and button to the panel.
     */
    public WelcomePanel() {
        ChineseFlashcards.populateSetList();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel welcomeText1 = new JLabel("Welcome to the");
        Font welcomeFont = new Font("Arial Nova", Font.PLAIN, 38);
        welcomeText1.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeText1.setFont(welcomeFont);
        JLabel welcomeText2 = new JLabel("Chinese Flashcard System!");
        welcomeText2.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeText2.setFont(welcomeFont);

        JLabel setMenuInfo = new JLabel("Select Set:");
        setMenuInfo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create JPanel in a JScrollPane with checkboxes for all the loaded sets. This is for the user to select the set(s) they want to view or edit.
        JPanel selectSetPanel = new JPanel();
        selectSetPanel.setLayout(new BoxLayout(selectSetPanel, BoxLayout.Y_AXIS));
        JScrollPane selectSetScrollPane = new JScrollPane(selectSetPanel);
        selectSetScrollPane.setMaximumSize(new Dimension(300, 100));
        selectSetScrollPane.setBorder(new LineBorder(Color.WHITE, 1, true));
        setSelectionCheckboxes = new ArrayList<>();
        ChineseFlashcards.sets.keySet().forEach(name -> {
            JCheckBox checkBox = new JCheckBox(name);
            checkBox.setActionCommand("SetSelectionCheckbox");
            checkBox.addActionListener(this);
            setSelectionCheckboxes.add(checkBox);
            selectSetPanel.add(checkBox);
        });

        editButton = new JButton("Edit Selected Set");
        editButton.setFont(new Font("Arial Nova", Font.PLAIN, 10));
        editButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        editButton.addActionListener(this);

        showStarredCards = new JCheckBox("Starred Cards");
        showStarredCards.setAlignmentX(Component.CENTER_ALIGNMENT);

        startButton = new JButton("Start Flashcards");
        startButton.setFont(Card.getEnglishFont(20));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(this);

        selectGameButton = new JButton("Games");
        selectGameButton.setFont(Card.getEnglishFont(20));
        selectGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectGameButton.addActionListener(this);

        JButton newButton = new JButton("New");
        newButton.setFont(Card.getEnglishFont(20));
        newButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        newButton.addActionListener(this);

        JButton importButton = new JButton("Import");
        importButton.setFont(Card.getEnglishFont(20));
        importButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        importButton.addActionListener(this);

        showStarredCards.setEnabled(false);
        startButton.setEnabled(false);
        selectGameButton.setEnabled(false);
        editButton.setEnabled(false);

        add(welcomeText1);
        add(welcomeText2);
        add(Box.createVerticalStrut(5));
        add(setMenuInfo);
        add(Box.createVerticalStrut(5));
        add(selectSetScrollPane);
        add(Box.createVerticalStrut(5));
        add(editButton);
        add(Box.createVerticalStrut(10));
        add(showStarredCards);
        add(Box.createVerticalStrut(10));
        add(startButton);
        add(Box.createVerticalStrut(10));
        add(selectGameButton);
        add(Box.createVerticalStrut(10));
        add(newButton);
        add(Box.createVerticalStrut(10));
        add(importButton);
    }

    /**
     * Handles button presses for Start, New, Edit Selected Set, and Import buttons
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        List<Set> selectedSets = setSelectionCheckboxes.stream()
                .filter(AbstractButton::isSelected)
                .map(jCheckBox -> ChineseFlashcards.sets.get(jCheckBox.getText()))
                .collect(Collectors.toList());
        switch (e.getActionCommand()) {
            case "Start Flashcards" -> {
                Set selectedSet = getRequestedSet(selectedSets);
                if (selectedSet != null) {
                    ChineseFlashcards.mainWindow.showFlashcardPanel(selectedSet, showStarredCards.isSelected());
                }
            }
            case "New" -> ChineseFlashcards.mainWindow.showEditorPanel();
            case "Edit Selected Set" -> ChineseFlashcards.mainWindow.showEditorPanel(selectedSets.get(0));
            case "Import" -> ChineseFlashcards.mainWindow.showImportPanel();
            case "SetSelectionCheckbox" -> {
                switch ((int) setSelectionCheckboxes.stream().filter(AbstractButton::isSelected).count()) {
                    case 0 -> { // If no sets are selected, the edit set, start, and showStarredCards checkbox buttons will not work.
                        showStarredCards.setEnabled(false);
                        startButton.setEnabled(false);
                        editButton.setEnabled(false);
                        selectGameButton.setEnabled(false);
                    }
                    case 1 -> { // If one set is selected, all buttons and the showStarredCards checkbox will work.
                        showStarredCards.setEnabled(true);
                        startButton.setEnabled(true);
                        editButton.setEnabled(true);
                        selectGameButton.setEnabled(true);
                    }
                    default -> { // If more than one set is selected the edit set button should not work.
                        showStarredCards.setEnabled(true);
                        startButton.setEnabled(true);
                        editButton.setEnabled(false);
                        selectGameButton.setEnabled(true);
                    }
                }
            }
            case "Games" -> {
                Set selectedSet = getRequestedSet(selectedSets);
                if (selectedSet != null) {
                    ChineseFlashcards.mainWindow.showGameSelectorPopup(selectedSet);
                }
            }
        }
    }

    public Set getRequestedSet(List<Set> selectedSets) {
        Set selectedSet = (selectedSets.size() == 1) ? selectedSets.get(0) : Set.combineSets(selectedSets);
        if (showStarredCards.isSelected()) {
            selectedSet = Set.getStarredSet(selectedSet);
            if (selectedSet.getCards().size() == 0) {
                JOptionPane.showMessageDialog(this, "You have no starred cards for this set!", "No Starred Cards", JOptionPane.WARNING_MESSAGE);
                return null;
            }
        }
        return selectedSet;
    }
}
