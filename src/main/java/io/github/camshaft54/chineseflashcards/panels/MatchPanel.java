package io.github.camshaft54.chineseflashcards.panels;

import io.github.camshaft54.chineseflashcards.ChineseFlashcards;
import io.github.camshaft54.chineseflashcards.utils.Card;
import io.github.camshaft54.chineseflashcards.utils.MatchCell;
import io.github.camshaft54.chineseflashcards.utils.Set;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class MatchPanel extends JLayeredPane implements ActionListener, MouseListener {
    private final List<MatchCell> firstCells;
    private int score;

    public MatchPanel(Set set) {
        firstCells = new ArrayList<>();
        firstCells.add(null);
        score = 0;

        JPanel mainPanel = new JPanel(new BorderLayout());
        SwingUtilities.invokeLater(() -> mainPanel.setSize(getSize()));
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                mainPanel.setSize(getSize());
            }
        });

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
        matchGrid.setLayout(new GridLayout(3, 8, 10, 10));
        matchGrid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        Random rand = new Random();
        List<MatchCell> matchCells = new ArrayList<>();
        pickNRandomCards(set.getCards(), 12).forEach(card -> {
            int firstLanguage = rand.nextInt(3);
            matchCells.add(new MatchCell(card, firstLanguage, this));
            int secondLanguage;
            do {
                secondLanguage = rand.nextInt(3);
            } while (secondLanguage == firstLanguage);
            matchCells.add(new MatchCell(card, secondLanguage, this));
        });
        Collections.shuffle(matchCells);
        matchCells.forEach(matchGrid::add);

        // Add panels to the main panel
        mainPanel.add(topToolbar, BorderLayout.NORTH);
        mainPanel.add(bottomToolbar, BorderLayout.SOUTH);
        mainPanel.add(matchGrid, BorderLayout.CENTER);
        add(mainPanel, JLayeredPane.DEFAULT_LAYER, 0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Back" -> ChineseFlashcards.mainWindow.showWelcomePanel();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        MatchCell firstCell = firstCells.get(firstCells.size() - 1);
        if (firstCell == null) {
            firstCell = (MatchCell) e.getComponent();
            firstCell.setBackground(Color.YELLOW);
            firstCells.set(firstCells.size() - 1, firstCell);
        } else {
            if (((MatchCell) e.getComponent()).getCard().equals(firstCell.getCard()) && ((MatchCell) e.getComponent()).getLanguage() != firstCell.getLanguage()) {
                firstCell.setBackground(Color.GREEN);
                e.getComponent().setBackground(Color.GREEN);
                score++;
                firstCells.add(null);
                MatchCell finalFirstCell = firstCell;
                CompletableFuture<Void> modifyCells = CompletableFuture.runAsync(() -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(300);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    finalFirstCell.setVisible(false);
                    e.getComponent().setVisible(false);
                });
                if (score == 4) {
                    JLabel winMessage = new JLabel("YOU WON!!!");
                    winMessage.setFont(Card.getEnglishFont(80));
                    winMessage.setSize(getSize());
                    add(winMessage, JLayeredPane.MODAL_LAYER, 0);
                }
            } else {
                firstCell.setBackground(Color.RED);
                e.getComponent().setBackground(Color.RED);
                firstCells.add(null);
                MatchCell finalFirstCell = firstCell;
                CompletableFuture.runAsync(() -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(150);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    finalFirstCell.setBackground(Color.BLACK);
                    e.getComponent().setBackground(Color.BLACK);
                });
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public static List<Card> pickNRandomCards(List<Card> list, int n) {
        ArrayList<Card> copy = new ArrayList<>(list);
        Collections.shuffle(copy);
        return copy.subList(0, n);
    }
}
