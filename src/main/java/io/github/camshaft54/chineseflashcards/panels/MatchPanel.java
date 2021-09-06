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
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MatchPanel extends JLayeredPane implements ActionListener, MouseListener {
    private final List<MatchCell> firstCells;
    private int score;
    private Timer timer;
    private long startTime;
    private long currentTime;
    private final JLabel timerLabel;
    private final JButton startButton;
    private final JButton resetButton;
    private final List<MatchCell> matchCells;
    private final JPanel matchGrid;
    private final Set set;

    public MatchPanel(Set set) {
        this.set = set;
        firstCells = new ArrayList<>();
        firstCells.add(null);
        score = 0;

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel modalPanel = new JPanel(new BorderLayout());
        SwingUtilities.invokeLater(() -> {
            mainPanel.setSize(getSize());
            modalPanel.setSize(getSize());
        });
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                mainPanel.setSize(getSize());
                modalPanel.setSize(getSize());
            }
        });
        ChineseFlashcards.mainWindow.addWindowStateListener(e -> {
            ChineseFlashcards.mainWindow.reloadMatchPanel();
            mainPanel.setSize(getSize());
            modalPanel.setSize(getSize());
        });

        // Create top toolbar and the buttons for it
        JButton exitButton = new JButton("Back");
        exitButton.addActionListener(this);

        JPanel topToolbar = new JPanel();
        topToolbar.setLayout(new BoxLayout(topToolbar, BoxLayout.X_AXIS));
        topToolbar.setBorder(new EmptyBorder(10, 10, 0, 10));
        topToolbar.add(exitButton);

        // Create bottom toolbar and the buttons for it
        startButton = new JButton("Start");
        startButton.addActionListener(this);
        resetButton = new JButton("Reset");
        resetButton.addActionListener(this);
        resetButton.setEnabled(false);

        JPanel bottomToolbar = new JPanel();
        bottomToolbar.setLayout(new BoxLayout(bottomToolbar, BoxLayout.X_AXIS));
        bottomToolbar.setBorder(new EmptyBorder(0, 10, 10, 10));
        bottomToolbar.add(timerLabel = new JLabel("00:00:00"));
        bottomToolbar.add(Box.createHorizontalStrut(10));
        bottomToolbar.add(startButton);
        bottomToolbar.add(Box.createHorizontalStrut(5));
        bottomToolbar.add(resetButton);

        // Create Match grid panel
        matchGrid = new JPanel();
        matchGrid.setLayout(new GridLayout(3, 8, 10, 10));
        matchGrid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        matchCells = new ArrayList<>();
        addMatchCells(set, matchGrid, matchCells);


        // Add panels to the main panel
        mainPanel.add(topToolbar, BorderLayout.NORTH);
        mainPanel.add(bottomToolbar, BorderLayout.SOUTH);
        mainPanel.add(matchGrid, BorderLayout.CENTER);
        add(mainPanel, JLayeredPane.DEFAULT_LAYER, 0);
        modalPanel.setOpaque(false);
        add(modalPanel, JLayeredPane.MODAL_LAYER, 0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Back" -> ChineseFlashcards.mainWindow.showWelcomePanel();
            case "Start" -> {
                matchCells.forEach(mc -> mc.getLabel().setEnabled(true));
                ChineseFlashcards.mainWindow.repaint();
                startTime = System.currentTimeMillis();
                timer = new Timer(100, timer -> timerLabel.setText(formatToHHMMSSmmmm((currentTime = System.currentTimeMillis()) - startTime)));
                timer.start();
                startButton.setEnabled(false);
                resetButton.setEnabled(true);
            }
            case "Reset" -> {
                score = 0;
                timerLabel.setText("00:00:00");
                timer.stop();
                firstCells.clear();
                firstCells.add(null);
                matchCells.clear();

                // Remove congratulations message if present
                ((JPanel) getComponentsInLayer(JLayeredPane.MODAL_LAYER)[0]).removeAll();
                addMatchCells(set, matchGrid, matchCells);
                ChineseFlashcards.mainWindow.repaint();
                resetButton.setEnabled(false);
                startButton.setEnabled(true);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        MatchCell firstCell = firstCells.get(firstCells.size() - 1);
        if (firstCell == null) {
            firstCell = (MatchCell) e.getComponent();
            firstCell.setBackground(MatchCell.partial);
            firstCells.set(firstCells.size() - 1, firstCell);
        } else {
            if (((MatchCell) e.getComponent()).getCard().equals(firstCell.getCard())) {
                if (((MatchCell) e.getComponent()).getLanguage() == firstCell.getLanguage()) {
                    firstCell.setBackground(MatchCell.regular);
                    ChineseFlashcards.mainWindow.revalidate();
                    ChineseFlashcards.mainWindow.repaint();
                    firstCells.add(null);
                } else {
                    score++;
                    firstCells.add(null);
                    fadeComponent(firstCell);
                    fadeComponent((MatchCell) e.getComponent());
                    if (score == matchCells.size()/2) { // The user won!
                        timer.stop();
                        JLabel winMessage = new JLabel("Congratulations, you won in " + formatToHHMMSSmmmm(currentTime - startTime) + "!");
                        winMessage.setHorizontalAlignment(JLabel.CENTER);
                        winMessage.setFont(Card.getEnglishFont(80));
                        winMessage.setSize(getSize());
                        JPanel modalPanel = (JPanel) getComponentsInLayer(JLayeredPane.MODAL_LAYER)[0];
                        modalPanel.add(winMessage, BorderLayout.CENTER);
                        resetButton.setEnabled(true);
                    }
                }
            } else {
                firstCell.setBackground(MatchCell.incorrect);
                e.getComponent().setBackground(MatchCell.incorrect);
                MatchCell finalFirstCell = firstCell;
                Timer timer = new Timer(100, ae -> {
                    finalFirstCell.setBackground(MatchCell.regular);
                    e.getComponent().setBackground(MatchCell.regular);
                    ChineseFlashcards.mainWindow.revalidate();
                    ChineseFlashcards.mainWindow.repaint();
                });
                timer.setRepeats(false);
                timer.start();
                firstCells.add(null);
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

    public static void fadeComponent(MatchCell c) {
        c.setBackground(MatchCell.correct);
        c.setBorder(null);
        AtomicInteger counter = new AtomicInteger(255);
        Timer timer = new Timer(1, ae -> {
            if (counter.get() > 0) {
                c.getLabel().setForeground(new Color(MatchCell.textColor.getRed(), MatchCell.textColor.getGreen(), MatchCell.textColor.getBlue(), counter.get()));
                c.setBackground(new Color(MatchCell.correct.getRed(), MatchCell.correct.getGreen(), MatchCell.correct.getRed(), counter.get()));
                ChineseFlashcards.mainWindow.revalidate();
                ChineseFlashcards.mainWindow.repaint();
                counter.getAndAdd(-10);
            } else if (counter.get() == -5) {
                c.setVisible(false);
                ((Timer) ae.getSource()).stop();
            }
        });
        timer.start();
    }

    public static String formatToHHMMSSmmmm(long time) {
        return String.format("%02d:%02d:%02d.%03d",
                TimeUnit.MILLISECONDS.toHours(time),
                TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)),
                TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)),
                time - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(time))
        );
    }

    public void addMatchCells(Set set, JPanel matchGrid, List<MatchCell> matchCells) {
        matchGrid.removeAll();
        Random rand = new Random();
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
    }
}
