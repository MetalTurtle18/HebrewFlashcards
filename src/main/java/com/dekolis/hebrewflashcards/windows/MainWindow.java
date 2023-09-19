package com.dekolis.hebrewflashcards.windows;

import com.dekolis.hebrewflashcards.HebrewFlashcards;
import com.dekolis.hebrewflashcards.panels.*;
import com.dekolis.hebrewflashcards.utils.Set;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MainWindow extends JFrame {
    private final JPanel root;
    private final CardLayout cardLayout;
    public WelcomePanel welcomePanel;
    public EditorPanel editorPanel;
    public FlashcardsViewer flashcardsViewer;
    public StarFlashcardsViewer starFlashcardsViewer;
    public ImportPanel importPanel;
    public MatchPanel matchPanel;

    public MainWindow() throws HeadlessException {
        setTitle("CFS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(675, 520);
        setVisible(true);

        // Set custom image as icon
        try {
            setIconImage(ImageIO.read(HebrewFlashcards.class.getResourceAsStream("/assets/CFS.png"))); // TODO: logo
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Setup and add root panel with CardLayout
        root = new JPanel();
        root.setLayout(new CardLayout());
        cardLayout = (CardLayout) root.getLayout();
        add(root);

        welcomePanel = new WelcomePanel();
        root.add(welcomePanel, "welcome");
    }

    // All the methods below remove the current panel if applicable and replace it with a new one. They then set the
    // CardLayout to display the new panel.

    public void showFlashcardPanel(Set selectedSet, boolean isStarredSet) {
        if (flashcardsViewer != null) {
            root.remove(flashcardsViewer);
        }
        if (starFlashcardsViewer != null) {
            root.remove(starFlashcardsViewer);
        }
        // Use StarFlashcardsViewer if starred set, FlashcardsViewer if not
        if (!isStarredSet) {
            flashcardsViewer = new FlashcardsViewer(selectedSet);
            root.add(flashcardsViewer, "flashcards");
            cardLayout.show(root, "flashcards");
        } else {
            starFlashcardsViewer = new StarFlashcardsViewer(selectedSet);
            root.add(starFlashcardsViewer, "starFlashcards");
            cardLayout.show(root, "starFlashcards");
        }
    }

    public void showWelcomePanel() {
        if (welcomePanel != null) {
            root.remove(welcomePanel);
        }
        welcomePanel = new WelcomePanel();
        root.add(welcomePanel, "welcome");
        cardLayout.show(root, "welcome");
    }

    public void showEditorPanel() {
        if (editorPanel != null) {
            root.remove(editorPanel);
        }
        editorPanel = new EditorPanel();
        root.add(editorPanel, "editor");
        cardLayout.show(root, "editor");
    }

    public void showEditorPanel(Set set) {
        if (editorPanel != null) {
            root.remove(editorPanel);
        }
        editorPanel = new EditorPanel(set);
        root.add(editorPanel, "editor");
        cardLayout.show(root, "editor");
    }

    public void showImportPanel() {
        if (importPanel != null) {
            root.remove(importPanel);
        }
        importPanel = new ImportPanel();
        root.add(importPanel, "import");
        cardLayout.show(root, "import");
    }

    public void showGameSelectorPopup(Set set) {
        Object[] gameNames = {"Match"};
        String selectedGame = (String) JOptionPane.showInputDialog(this, "Select Game:", "Game Selector",
                JOptionPane.PLAIN_MESSAGE, null, gameNames, "Match");
        if (selectedGame == null) return;
        switch (selectedGame) {
            case "Match" -> {
                if (matchPanel != null) {
                    root.remove(matchPanel);
                }
                if (set.getCards().size() < MatchPanel.cellCount) {
                    JOptionPane.showMessageDialog(this, "The Match game requires at least 12 cards in the set to play.", "Match Game", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                matchPanel = new MatchPanel(set);
                root.add(matchPanel, "match");
                cardLayout.show(root, "match");
            }
        }
    }

    public void reloadMatchPanel() {
        root.remove(matchPanel);
        revalidate();
        root.add(matchPanel, "match");
        cardLayout.show(root, "match");
    }
}
