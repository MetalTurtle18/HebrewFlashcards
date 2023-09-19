package com.dekolis.hebrewflashcards.panels;

import com.dekolis.hebrewflashcards.HebrewFlashcards;
import com.dekolis.hebrewflashcards.utils.Flashcard;
import com.dekolis.hebrewflashcards.utils.Set;

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

public class FlashcardsViewer extends JPanel implements ActionListener, MouseListener {
    public static boolean isVisible;

    private final JPanel flashcardPanel;
    private final CardLayout cardLayout;
    private final ArrayList<Flashcard> flashcards;
    private JLabel cardCounter;
    private JButton leftButton;
    private JButton rightButton;
    private JButton starButton;
    private int currentFlashcard;
    private int termType;
    private int definitionType;

    /**
     * Initializes a Flashcard Viewer with a toolbar and loads the set specified.
     * @param selectedSet the set to be loaded into the viewer.
     */
    public FlashcardsViewer(Set selectedSet) {
        currentFlashcard = 0;
        isVisible = true;
        termType = Flashcard.HEBREW;
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

        // Add all the flashcards panels to the inner flashcard panel
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

    /**
     * Adds the top and bottom toolbars for the viewer.
     */
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


        JComboBox<String> termSelector = new JComboBox<>(new String[]{"Hebrew", "Transliteration", "English"});
        termSelector.setName("termSelector");
        termSelector.addActionListener(this);
        JComboBox<String> definitionSelector = new JComboBox<>(new String[]{"Hebrew", "Transliteration", "English"});
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
        bottomToolbar.add(Box.createHorizontalGlue());
        bottomToolbar.add(termSelector);
        bottomToolbar.add(new JLabel("->"));
        bottomToolbar.add(definitionSelector);
        add(bottomToolbar, BorderLayout.SOUTH);

        // Create buttons for top toolbar
        JButton exitButton = new JButton("Back");
        exitButton.addActionListener(this);
        JButton shuffleButton = new JButton("Shuffle");
        shuffleButton.addActionListener(this);

        // Create and add top toolbar
        JPanel topToolbar = new JPanel();
        topToolbar.setLayout(new BoxLayout(topToolbar, BoxLayout.X_AXIS));
        topToolbar.setBorder(new EmptyBorder(10, 10, 0, 10));
        topToolbar.add(exitButton);
        topToolbar.add(Box.createHorizontalStrut(10));
        topToolbar.add(shuffleButton);
        add(topToolbar, BorderLayout.NORTH);
    }

    /**
     * Registers the arrow key and s key shortcuts using InputMap and ActionMap.
     */
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

    /**
     * Increments the current flashcard by 1.
     */
    public void incrementFlashcard() {
        cardLayout.next(flashcardPanel);
        currentFlashcard++;
        refreshFlashcard();
    }

    /**
     * Decrements the current flashcard by 1.
     */
    public void decrementFlashcard() {
        cardLayout.previous(flashcardPanel);
        currentFlashcard--;
        refreshFlashcard();
    }

    /**
     * Refreshes the current flashcard by changing the side, changing the functionality of the buttons,
     * adjusting the card counter, or updating the stars if necessary.
     */
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
    }

    /**
     * Resets the flashcard viewer back to the first card.
     */
    public void resetFlashcards() {
        cardLayout.first(flashcardPanel);
        currentFlashcard = 0;
        refreshFlashcard();
    }

    /**
     * Flips the current flashcard to the definition or term depending on its current state.
     */
    public void flipFlashcard() {
        if (flashcards.get(currentFlashcard).getCurrentSide() == termType) {
            flashcards.get(currentFlashcard).showSide(definitionType);
        } else {
            flashcards.get(currentFlashcard).showSide(termType);
        }
    }

    /**
     * Adds or removes a star for the current language configuration depending on the current state.
     */
    public void toggleStar() {
        if (!flashcards.get(currentFlashcard).hasStar(termType, definitionType)) {
            flashcards.get(currentFlashcard).setStar(termType, definitionType);
            starButton.setText("★");
        } else {
            flashcards.get(currentFlashcard).removeStar(termType, definitionType);
            starButton.setText("☆");
        }
    }

    /**
     * When the user changes the term for the viewer, this method updates the term of the flashcard accordingly.
     * @param termType the type of term to change the old term to.
     */
    public void updateTerm(String termType) {
        switch (termType) {
            case "English" -> this.termType = Flashcard.ENGLISH;
            case "Transliteration" -> this.termType = Flashcard.TRANSLITERATION;
            case "Hebrew" -> this.termType = Flashcard.HEBREW;
        }
        flashcards.get(currentFlashcard).showSide(this.termType);
        if (!flashcards.get(currentFlashcard).hasStar(this.termType, definitionType)) {
            starButton.setText("☆");
        } else {
            starButton.setText("★");
        }
    }

    /**
     * When the user changes the definition for the viewer, this method updates the definition of the flashcard accordingly.
     * @param definitionType the type of definition to change the old definition to.
     */
    public void updateDefinition(String definitionType) {
        switch (definitionType) {
            case "English" -> this.definitionType = Flashcard.ENGLISH;
            case "Transliteration" -> this.definitionType = Flashcard.TRANSLITERATION;
            case "Hebrew" -> this.definitionType = Flashcard.HEBREW;
        }
        flashcards.get(currentFlashcard).showSide(termType);
        if (!flashcards.get(currentFlashcard).hasStar(termType, this.definitionType)) {
            starButton.setText("☆");
        } else {
            starButton.setText("★");
        }
    }

    /**
     * Shuffles the flashcards and updates the order of the flashcards in the panel. This does not affect the order of the cards
     * in the Set.
     */
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
            // Updates definition or term to new selection.
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
                HebrewFlashcards.mainWindow.showWelcomePanel();
            }
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
