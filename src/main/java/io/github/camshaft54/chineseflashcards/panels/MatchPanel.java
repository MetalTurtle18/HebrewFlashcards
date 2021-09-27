package io.github.camshaft54.chineseflashcards.panels;

import io.github.camshaft54.chineseflashcards.ChineseFlashcards;
import io.github.camshaft54.chineseflashcards.utils.Card;
import io.github.camshaft54.chineseflashcards.utils.MatchCell;
import io.github.camshaft54.chineseflashcards.utils.Set;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MatchPanel extends JLayeredPane implements ActionListener, MouseListener, ChangeListener {
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
    private final JComboBox<String> firstSelector;
    private final JComboBox<String> secondSelector;
    private final JPanel modalPanel;
    private int matchCellFontSize;

    public MatchPanel(Set set) {
        this.set = set;
        firstCells = new ArrayList<>();
        firstCells.add(null);
        score = 0;
        matchCellFontSize = 30;

        JPanel mainPanel = new JPanel(new BorderLayout());
        modalPanel = new JPanel(new BorderLayout());
        SwingUtilities.invokeLater(() -> {
            mainPanel.setSize(getSize());
            modalPanel.setSize(getSize());
        });
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                mainPanel.setSize(getSize());
                modalPanel.setSize(getSize());
                ChineseFlashcards.mainWindow.revalidate();
                ChineseFlashcards.mainWindow.repaint();
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

        timerLabel = new JLabel("00:00:00");
        timerLabel.setFont(Card.getEnglishFont(20));
        timerLabel.setOpaque(true);

        // Create bottom toolbar and the buttons for it
        startButton = new JButton("Start");
        startButton.addActionListener(this);
        resetButton = new JButton("Reset");
        resetButton.addActionListener(this);

        firstSelector = new JComboBox<>(new String[]{"Random", "English", "Pinyin", "Chinese"});
        firstSelector.addActionListener(this);
        firstSelector.setToolTipText("Set the first card type, press \"Reset\" button to update");
        secondSelector = new JComboBox<>(new String[]{"Random", "English", "Pinyin", "Chinese"});
        secondSelector.addActionListener(this);
        secondSelector.setToolTipText("Set the second card type, press \"Reset\" button to update");

        JSlider fontSizeSlider = new JSlider(JSlider.HORIZONTAL, 15, 65, matchCellFontSize);
        fontSizeSlider.addChangeListener(this);

        JPanel bottomToolbar = new JPanel();
        bottomToolbar.setLayout(new BoxLayout(bottomToolbar, BoxLayout.X_AXIS));
        bottomToolbar.setBorder(new EmptyBorder(0, 10, 10, 10));
        bottomToolbar.add(timerLabel);
        bottomToolbar.add(Box.createHorizontalStrut(10));
        bottomToolbar.add(startButton);
        bottomToolbar.add(Box.createHorizontalStrut(5));
        bottomToolbar.add(resetButton);
        bottomToolbar.add(Box.createHorizontalGlue());
        bottomToolbar.add(firstSelector);
        bottomToolbar.add(new JLabel("->"));
        bottomToolbar.add(secondSelector);
        bottomToolbar.add(Box.createHorizontalStrut(10));
        bottomToolbar.add(new JLabel("Font Size:"));
        bottomToolbar.add(fontSizeSlider);

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
                matchCells.forEach(mc -> mc.setEnabled(true));
                ChineseFlashcards.mainWindow.repaint();
                startTime = System.currentTimeMillis();
                timer = new Timer(100, timer -> timerLabel.setText(formatToHHMMSSmmmm((currentTime = System.currentTimeMillis()) - startTime)));
                timer.start();
                startButton.setEnabled(false);
                resetButton.setEnabled(true);
                firstSelector.setEnabled(false);
                secondSelector.setEnabled(false);
                modalPanel.remove(0); // Remove "click to start to begin" message
            }
            case "Reset" -> {
                score = 0;
                timerLabel.setText("00:00:00");
                if (timer != null) timer.stop();
                firstCells.clear();
                firstCells.add(null);
                matchCells.clear();

                // Remove congratulations message if present
                ((JPanel) getComponentsInLayer(JLayeredPane.MODAL_LAYER)[0]).removeAll();
                // If adding match cells succeeds, re-enable buttons and selectors
                if (addMatchCells(set, matchGrid, matchCells)) {
                    startButton.setEnabled(true);
                    firstSelector.setEnabled(true);
                    secondSelector.setEnabled(true);
                    // Both revalidate() and repaint() are required for the cells to properly update
                    ChineseFlashcards.mainWindow.revalidate();
                    ChineseFlashcards.mainWindow.repaint();
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Prevent mouse pressed events from being processed if the cell is not enabled (ie the game has not started)
        if (!e.getComponent().isEnabled()) {
            return;
        }

        MatchCell firstCell = firstCells.get(firstCells.size() - 1);
        if (firstCell == null) {
            firstCell = (MatchCell) e.getComponent();
            firstCell.setBackground(MatchCell.partial);
            firstCells.set(firstCells.size() - 1, firstCell);
        } else {
            if (((MatchCell) e.getComponent()).getCard().equals(firstCell.getCard())) { // If user did choose correct card
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
                    if (score == matchCells.size() / 2) { // The user won!
                        timer.stop();
                        JLabel winMessage = new JLabel("Congratulations, you won in " + formatToHHMMSSmmmm(currentTime - startTime) + "!");
                        winMessage.setHorizontalAlignment(JLabel.CENTER);
                        winMessage.setFont(Card.getEnglishFont(80));
                        winMessage.setSize(getSize());
                        modalPanel.add(winMessage, BorderLayout.CENTER);
                        resetButton.setEnabled(true);
                        firstSelector.setEnabled(true);
                        secondSelector.setEnabled(true);
                    }
                }
            } else { // If user did not choose correct card
                firstCell.setBackground(MatchCell.incorrect);
                e.getComponent().setBackground(MatchCell.incorrect);
                MatchCell finalFirstCell = firstCell;
                startTime -= 1000; // Add 1 second as penalty for answering incorrectly
                timerLabel.setBackground(MatchCell.incorrect);
                Timer timer = new Timer(100, ae -> {
                    finalFirstCell.setBackground(MatchCell.regular);
                    e.getComponent().setBackground(MatchCell.regular);
                    timerLabel.setBackground(new Color(238, 238, 238)); // default background color
                    ChineseFlashcards.mainWindow.revalidate();
                    ChineseFlashcards.mainWindow.repaint();
                });
                timer.setRepeats(false);
                timer.start();
                firstCells.add(null);
            }
        }
    }

    // Listener for font size adjustment
    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() instanceof JSlider) {
            matchCellFontSize = ((JSlider) e.getSource()).getValue();
            matchCells.forEach(mc -> mc.getLabel().setFont(Card.getFont(mc.getLanguage(), matchCellFontSize)));
            ChineseFlashcards.mainWindow.revalidate();
            ChineseFlashcards.mainWindow.repaint();
        }
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
        Timer timer = new Timer(10, ae -> {
            if (counter.get() > 0) {
                c.getLabel().setForeground(new Color(MatchCell.textColor.getRed(), MatchCell.textColor.getGreen(), MatchCell.textColor.getBlue(), counter.get()));
                c.setBackground(new Color(MatchCell.correct.getRed(), MatchCell.correct.getGreen(), MatchCell.correct.getRed(), counter.get()));
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

    public boolean addMatchCells(Set set, JPanel matchGrid, List<MatchCell> matchCells) {
        int firstLanguage = firstSelector.getSelectedIndex() - 1; // this will make random = -1, english = 0, pinyin = 1, chinese = 2
        int secondLanguage = secondSelector.getSelectedIndex() - 1; // this will make random = -1, english = 0, pinyin = 1, chinese = 2
        // If languages are same and not random, show warning popup and exit
        if (firstLanguage == secondLanguage && firstLanguage != -1) {
            JOptionPane.showMessageDialog(this, "Please select two different languages. You can set random for both, however.");
            return false;
        }

        matchGrid.removeAll();
        Random rand = new Random();
        pickNRandomCards(set.getCards(), 12).forEach(card -> {
            int selectedFirstLanguage = (firstLanguage == -1) ? rand.nextInt(3) : firstLanguage;
            matchCells.add(new MatchCell(card, selectedFirstLanguage, this, matchCellFontSize));

            int selectedSecondLanguage;
            if (secondLanguage == -1) { // If second card is meant to be random, make sure it does not match the first card.
                do {
                    selectedSecondLanguage = rand.nextInt(3);
                } while (selectedSecondLanguage == selectedFirstLanguage);
            } else {
                selectedSecondLanguage = secondLanguage;
            }
            matchCells.add(new MatchCell(card, selectedSecondLanguage, this, matchCellFontSize));
        });
        Collections.shuffle(matchCells);
        matchCells.forEach(matchGrid::add);
        JLabel clickStartMessage = new JLabel("Click start to begin!");
        clickStartMessage.setHorizontalAlignment(JLabel.CENTER);
        clickStartMessage.setFont(Card.getEnglishFont(80));
        clickStartMessage.setForeground(Color.WHITE);
        JPanel clickStartPanel = new JPanel(new BorderLayout());
        clickStartPanel.setBackground(new Color(0, 0, 0, 100));
        clickStartPanel.add(clickStartMessage, BorderLayout.CENTER);
        modalPanel.add(clickStartPanel, BorderLayout.CENTER);
        modalPanel.add(Box.createVerticalStrut(44), BorderLayout.NORTH);
        modalPanel.add(Box.createVerticalStrut(44), BorderLayout.SOUTH);
        return true;
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
}
