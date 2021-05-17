package io.github.camshaft54.windows;

import io.github.camshaft54.panels.FlashcardsViewer;
import io.github.camshaft54.panels.WelcomePanel;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private final JPanel root;
    private WelcomePanel welcomePanel;
    private FlashcardsViewer flashcardsViewer;
    private final CardLayout cardLayout;

    public MainWindow() throws HeadlessException {

        setTitle("CFS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(512, 375);
        setVisible(true);

        // Setup and add root panel with CardLayout
        root = new JPanel();
        root.setLayout(new CardLayout());
        cardLayout = (CardLayout) root.getLayout();
        add(root);

        welcomePanel = new WelcomePanel();
        root.add(welcomePanel, "welcome");
    }

    public void showFlashcardPanel() {
        flashcardsViewer = new FlashcardsViewer();
        root.add(flashcardsViewer, "flashcards");
        cardLayout.show(root, "flashcards");
    }
}
