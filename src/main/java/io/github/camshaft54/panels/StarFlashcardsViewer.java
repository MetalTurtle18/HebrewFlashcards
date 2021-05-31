package io.github.camshaft54.panels;

import io.github.camshaft54.ChineseFlashcards;
import io.github.camshaft54.utils.Set;
import io.github.camshaft54.windows.SetSettingsPopup;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class is the same as FlashcardsViewer except for the following differences:
 * - Displays current stars for a flashcard
 * - Removes cards that are not starred when the reset button is pressed
 * - Adds settings button with SetSettingsPopup
 */
public class StarFlashcardsViewer extends JPanel implements ActionListener, MouseListener {
    public static boolean isVisible;

    private final JPanel flashcardPanel;
    private final CardLayout cardLayout;
    private final ArrayList<Flashcard> flashcards;
    private final Set selectedSet;
    private JLabel cardCounter;
    private JButton leftButton;
    private JButton rightButton;
    private JButton starButton;
    private JLabel cardStarsText;
    private int currentFlashcard;
    private int termType;
    private int definitionType;

    public StarFlashcardsViewer(Set selectedSet) {
        this.selectedSet = selectedSet;
        currentFlashcard = 0;
        isVisible = true;
        termType = Flashcard.CHINESE;
        definitionType = Flashcard.ENGLISH;

        setLayout(new BorderLayout());

        // Initialize and add inner flashcard panel
        flashcardPanel = new JPanel();
        flashcardPanel.setLayout(new CardLayout());
        cardLayout = (CardLayout) flashcardPanel.getLayout();
        flashcardPanel.addMouseListener(this);
        Border lineBorder = BorderFactory.createLineBorder(new Color(196, 196, 196), 2);
        Border margin = new EmptyBorder(10, 10, 10, 10);
        flashcardPanel.setBorder(new CompoundBorder(margin, lineBorder));
        add(flashcardPanel, BorderLayout.CENTER);

        // Add all of the flashcards panels to the inner flashcard panel
        flashcards = new ArrayList<>();
        AtomicInteger i = new AtomicInteger();
        selectedSet.getCards().forEach(card -> {
            flashcards.add(new Flashcard(card, termType));
            flashcardPanel.add(flashcards.get(flashcards.size()-1), i.get());
            i.getAndAdd(1);
        });

        // Add toolbars
        addToolbars();

        // Register keyboard shortcuts
        registerShortcuts();

        // Reset cards to initialize first one properly
        resetFlashcards();
    }

    private void addToolbars() {
        // Create previous and next buttons for flashcards, card counter, and reset button
        cardCounter = new JLabel((currentFlashcard + 1) + " / " + flashcards.size());
        Border lineBorder = BorderFactory.createLineBorder(new Color(196, 196, 196), 2);
        Border margin = new EmptyBorder(5, 5, 5, 5);
        cardCounter.setBorder(new CompoundBorder(lineBorder, margin));
        JButton resetButton = new JButton("↻");
        resetButton.addActionListener(this);
        leftButton = new JButton("<");
        leftButton.addActionListener(this);
        leftButton.setEnabled(false);
        rightButton = new JButton(">");
        rightButton.addActionListener(this);
        rightButton.setEnabled(flashcards.size() != 1);
        if (!flashcards.get(currentFlashcard).hasStar(termType, definitionType)) {
            starButton = new JButton("☆");
        } else {
            starButton = new JButton("★");
        }
        starButton.addActionListener(this);


        JComboBox<String> termSelector = new JComboBox<>(new String[]{"Chinese", "Pinyin", "English"});
        termSelector.setName("termSelector");
        termSelector.addActionListener(this);
        JComboBox<String> definitionSelector = new JComboBox<>(new String[]{"Chinese", "Pinyin", "English"});
        definitionSelector.setSelectedIndex(2);
        definitionSelector.setName("definitionSelector");
        definitionSelector.addActionListener(this);

        // Create and add bottom toolbar
        JPanel bottomToolbar = new JPanel();
        bottomToolbar.setLayout(new BoxLayout(bottomToolbar, BoxLayout.X_AXIS));
        bottomToolbar.setBorder(new EmptyBorder(0, 10, 10, 10));
        bottomToolbar.add(cardCounter);
        bottomToolbar.add(Box.createHorizontalStrut(5));
        bottomToolbar.add(resetButton);
        bottomToolbar.add(Box.createHorizontalStrut(10));
        bottomToolbar.add(leftButton);
        bottomToolbar.add(Box.createHorizontalStrut(5));
        bottomToolbar.add(rightButton);
        bottomToolbar.add(Box.createHorizontalStrut(10));
        bottomToolbar.add(starButton);
        bottomToolbar.add(Box.createHorizontalStrut(10));
        bottomToolbar.add(termSelector);
        bottomToolbar.add(new JLabel("->"));
        bottomToolbar.add(definitionSelector);
        add(bottomToolbar, BorderLayout.SOUTH);

        // Create buttons and text for top toolbar
        JButton exitButton = new JButton("Back");
        exitButton.addActionListener(this);
        JButton shuffleButton = new JButton("Shuffle");
        shuffleButton.addActionListener(this);
        // This button opens the SetSettingsPopup
        JButton settingsButton = new JButton("Settings");
        settingsButton.addActionListener(this);
        // This label displays the stars that a card has
        cardStarsText = new JLabel("Current Stars: None");


        // Create and add top toolbar
        JPanel topToolbar = new JPanel();
        topToolbar.setLayout(new BoxLayout(topToolbar, BoxLayout.X_AXIS));
        topToolbar.setBorder(new EmptyBorder(10, 10, 0, 10));
        topToolbar.add(exitButton);
        topToolbar.add(Box.createHorizontalStrut(10));
        topToolbar.add(shuffleButton);
        topToolbar.add(Box.createHorizontalStrut(10));
        topToolbar.add(settingsButton);
        topToolbar.add(Box.createHorizontalGlue());
        topToolbar.add(cardStarsText);
        add(topToolbar, BorderLayout.NORTH);
    }

    private void registerShortcuts() {
        InputMap map = leftButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        map.put(KeyStroke.getKeyStroke("LEFT"), "previous");
        map = rightButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        map.put(KeyStroke.getKeyStroke("RIGHT"), "next");
        map = flashcardPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        map.put(KeyStroke.getKeyStroke("UP"), "flip");
        map.put(KeyStroke.getKeyStroke("DOWN"), "flip");
        map = starButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        map.put(KeyStroke.getKeyStroke("S"), "star");
        Action keyAction = new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (isVisible) {
                    if (actionEvent.getSource() instanceof JPanel) {
                        flipFlashcard();
                        return;
                    }
                    String actionCommand = ((JButton) actionEvent.getSource()).getText();
                    switch (actionCommand) {
                        case "<" -> decrementFlashcard();
                        case ">" -> incrementFlashcard();
                        case "☆", "★" -> toggleStar();
                    }
                }
            }
        };
        leftButton.getActionMap().put("previous", keyAction);
        rightButton.getActionMap().put("next", keyAction);
        flashcardPanel.getActionMap().put("flip", keyAction);
        starButton.getActionMap().put("star", keyAction);
    }

    public void incrementFlashcard() {
        cardLayout.next(flashcardPanel);
        currentFlashcard++;
        refreshFlashcard();
    }

    public void decrementFlashcard() {
        cardLayout.previous(flashcardPanel);
        currentFlashcard--;
        refreshFlashcard();
    }

    public void refreshFlashcard() {
        flashcards.get(currentFlashcard).showSide(termType);
        rightButton.setEnabled(currentFlashcard < flashcards.size() - 1 && flashcards.size() != 1);
        leftButton.setEnabled(currentFlashcard > 0);
        cardCounter.setText((currentFlashcard + 1) + " / " + flashcards.size());
        if (!flashcards.get(currentFlashcard).hasStar(termType, definitionType)) {
            starButton.setText("☆");
        } else {
            starButton.setText("★");
        }
        // Update current stars
        cardStarsText.setText("Current Stars: " + flashcards.get(currentFlashcard).getStarsDisplayText());
    }

    public void resetFlashcards() {
        // Remove any cards that are not starred.
        for (int i = 0; i < flashcards.size(); i++) {
            if (!flashcards.get(i).isStarred()) {
                flashcardPanel.remove(flashcards.get(i));
                flashcards.remove(flashcards.get(i));
                i--;
            }
        }
        // If there are no cards left in the set, exit.
        if (flashcards.size() == 0) {
            JOptionPane.showMessageDialog(this, "There are no starred cards remaining in this set!", "No Starred Cards", JOptionPane.WARNING_MESSAGE);
            ChineseFlashcards.mainWindow.showWelcomePanel();
            isVisible = false;
            return;
        }
        cardLayout.first(flashcardPanel);
        currentFlashcard = 0;
        refreshFlashcard();
    }

    public void flipFlashcard() {
        if (flashcards.get(currentFlashcard).getCurrentSide() == termType) {
            flashcards.get(currentFlashcard).showSide(definitionType);
        } else {
            flashcards.get(currentFlashcard).showSide(termType);
        }
    }

    public void toggleStar() {
        if (!flashcards.get(currentFlashcard).hasStar(termType, definitionType)) {
            flashcards.get(currentFlashcard).setStar(termType, definitionType);
            starButton.setText("★");
        } else {
            flashcards.get(currentFlashcard).removeStar(termType, definitionType);
            starButton.setText("☆");
        }
        refreshFlashcard();
    }

    public void updateTerm(String termType) {
        switch (termType) {
            case "English" -> this.termType = Flashcard.ENGLISH;
            case "Pinyin" -> this.termType = Flashcard.PINYIN;
            case "Chinese" -> this.termType = Flashcard.CHINESE;
        }
        flashcards.get(currentFlashcard).showSide(this.termType);
        if (!flashcards.get(currentFlashcard).hasStar(this.termType, definitionType)) {
            starButton.setText("☆");
        } else {
            starButton.setText("★");
        }
    }

    public void updateDefinition(String definitionType) {
        switch (definitionType) {
            case "English" -> this.definitionType = Flashcard.ENGLISH;
            case "Pinyin" -> this.definitionType = Flashcard.PINYIN;
            case "Chinese" -> this.definitionType = Flashcard.CHINESE;
        }
        flashcards.get(currentFlashcard).showSide(termType);
        if (!flashcards.get(currentFlashcard).hasStar(termType, this.definitionType)) {
            starButton.setText("☆");
        } else {
            starButton.setText("★");
        }
    }

    public void shuffleSet() {
        // Shuffle flashcards
        Collections.shuffle(flashcards);

        // Re-add flashcards to flashcardPanel
        flashcardPanel.removeAll();
        AtomicInteger i = new AtomicInteger();
        flashcards.forEach(flashcard -> {
            flashcardPanel.add(flashcard, i.get());
            i.getAndAdd(1);
        });

        resetFlashcards();
    }

    @SuppressWarnings({"rawtypes", "ConstantConditions"})
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JComboBox && ((JComboBox) e.getSource()).getSelectedItem() != null) {
            switch (((JComboBox) e.getSource()).getName()) {
                case "termSelector" -> updateTerm((String) ((JComboBox) e.getSource()).getSelectedItem());
                case "definitionSelector" -> updateDefinition((String) ((JComboBox) e.getSource()).getSelectedItem());
            }
        }
        switch (e.getActionCommand()) {
            case "<" -> decrementFlashcard();
            case ">" -> incrementFlashcard();
            case "↻" -> resetFlashcards();
            case "☆", "★" -> toggleStar();
            case "Back" -> {
                isVisible = false;
                ChineseFlashcards.mainWindow.showWelcomePanel();
            }
            // Opens SetSettingsPopup
            case "Settings" -> new SetSettingsPopup(selectedSet, flashcards.get(currentFlashcard), termType + " " + definitionType);
            case "Shuffle" -> shuffleSet();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        flipFlashcard();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        flashcardPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        flashcardPanel.setCursor(Cursor.getDefaultCursor());
    }

    // Unused mouse listeners
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
}
