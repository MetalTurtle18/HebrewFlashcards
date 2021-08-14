package io.github.camshaft54.chineseflashcards.panels;

import io.github.camshaft54.chineseflashcards.ChineseFlashcards;
import io.github.camshaft54.chineseflashcards.utils.Card;
import io.github.camshaft54.chineseflashcards.utils.Set;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MatchPanel extends JPanel implements ActionListener {
    public MatchPanel(Set set) {
        setLayout(new BorderLayout());

        // Create top toolbar and the buttons for it
        JButton exitButton = new JButton("Back");
        exitButton.addActionListener(this);

        JPanel topToolbar = new JPanel();
        topToolbar.setLayout(new BoxLayout(topToolbar, BoxLayout.X_AXIS));
        topToolbar.setBorder(new EmptyBorder(10, 10, 0, 10));
        topToolbar.add(exitButton);

        // Create bottom toolbar and the buttons for it
        JButton startButton = new JButton("Start");
        startButton.addActionListener(this);
        JButton resetButton = new JButton("↻");
        resetButton.addActionListener(this);

        JPanel bottomToolbar = new JPanel();
        bottomToolbar.setLayout(new BoxLayout(bottomToolbar, BoxLayout.X_AXIS));
        bottomToolbar.setBorder(new EmptyBorder(0, 10, 10, 10));
        bottomToolbar.add(new JLabel("00:00"));
        bottomToolbar.add(Box.createHorizontalStrut(10));
        bottomToolbar.add(startButton);
        bottomToolbar.add(Box.createHorizontalStrut(5));
        bottomToolbar.add(resetButton);

        // Create Match grid panel
        JPanel matchGrid = new JPanel();
        matchGrid.setLayout(new GridLayout(3, 4, 10, 10));
        matchGrid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        for (int i = 0; i < 12; i++) {
            JLabel label = new JLabel("example");
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            label.setFont(Card.getEnglishFont(30));
            label.setHorizontalAlignment(JLabel.CENTER);
            matchGrid.add(label);
        }

        // Add panels to the main panel
        this.add(topToolbar, BorderLayout.NORTH);
        this.add(bottomToolbar, BorderLayout.SOUTH);
        this.add(matchGrid, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Back" -> ChineseFlashcards.mainWindow.showWelcomePanel();
        }
    }
}
