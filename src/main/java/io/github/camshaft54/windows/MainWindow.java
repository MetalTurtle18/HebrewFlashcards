package io.github.camshaft54.windows;

import io.github.camshaft54.panels.FlashcardsViewer;
import io.github.camshaft54.panels.StarFlashcardsViewer;
import io.github.camshaft54.panels.WelcomePanel;
import io.github.camshaft54.utils.Set;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private final JPanel root;
    private final CardLayout cardLayout;

    public MainWindow() throws HeadlessException {
        setTitle("CFS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 425);
        setVisible(true);

        // Setup and add root panel with CardLayout
        root = new JPanel();
        root.setLayout(new CardLayout());
        cardLayout = (CardLayout) root.getLayout();
        add(root);

        WelcomePanel welcomePanel = new WelcomePanel();
        root.add(welcomePanel, "welcome");
    }

    public void showFlashcardPanel(Set selectedSet, boolean isStarredSet) {
        if (!isStarredSet) {
            FlashcardsViewer flashcardsViewer = new FlashcardsViewer(selectedSet);
            root.add(flashcardsViewer, "flashcards");
        } else {
            StarFlashcardsViewer starFlashcardsViewer = new StarFlashcardsViewer(selectedSet);
            root.add(starFlashcardsViewer, "flashcards");
        }
        cardLayout.show(root, "flashcards");
    }

    public void showWelcomePanel() {
        cardLayout.first(root);
    }
}
