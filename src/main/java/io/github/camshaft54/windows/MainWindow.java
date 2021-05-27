package io.github.camshaft54.windows;

import io.github.camshaft54.panels.*;
import io.github.camshaft54.utils.Set;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MainWindow extends JFrame {
    private final JPanel root;
    private final CardLayout cardLayout;
    public WelcomePanel welcomePanel;
    public EditorPanel editorPanel;
    public FlashcardsViewer flashcardsViewer;
    public StarFlashcardsViewer starFlashcardsViewer;
    public ImportPanel importPanel;

    public MainWindow() throws HeadlessException {
        setTitle("CFS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 425);
        setVisible(true);
        // Set custom image as icon
        try {
            setIconImage(ImageIO.read(new File("src/main/resources/assets/CFS.png")));
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

    public void showFlashcardPanel(Set selectedSet, boolean isStarredSet) {
        if (flashcardsViewer != null) {
            root.remove(flashcardsViewer);
        }
        if (starFlashcardsViewer != null) {
            root.remove(starFlashcardsViewer);
        }
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
}
