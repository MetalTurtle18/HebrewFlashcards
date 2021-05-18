package io.github.camshaft54.panels;

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
import java.util.concurrent.atomic.AtomicInteger;

public class FlashcardsViewer extends JPanel implements ActionListener, MouseListener {
    public static boolean isVisible;

    private final JPanel flashcardPanel;
    private final CardLayout cardLayout;
    private JLabel cardCounter;
    private JButton leftButton;
    private JButton rightButton;
    private final ArrayList<Flashcard> flashcards = new ArrayList<>();
    private int currentFlashcard = 0;
    private int termType;
    private int definitionType;

    public FlashcardsViewer() {
        isVisible = true; // TODO: Implement this
        termType = Flashcard.CHINESE;
        definitionType = Flashcard.ENGLISH;

        setLayout(new BorderLayout());

        // Initialize and add inner flashcard panel
        flashcardPanel = new JPanel();
        flashcardPanel.setLayout(new CardLayout());
        cardLayout = (CardLayout) flashcardPanel.getLayout();
        flashcardPanel.addMouseListener(this);
        Border lineBorder = BorderFactory.createLineBorder(Color.WHITE, 2);
        Border margin = new EmptyBorder(10, 10, 10, 10);
        flashcardPanel.setBorder(new CompoundBorder(margin, lineBorder));
        add(flashcardPanel, BorderLayout.CENTER);

        // Add all of the flashcards panels to the inner flashcard panel
        AtomicInteger i = new AtomicInteger();
        WelcomePanel.selectedSet.getCards().forEach(card -> {
            flashcards.add(new Flashcard(card, termType));
            flashcardPanel.add(flashcards.get(flashcards.size()-1), i.get());
            i.getAndAdd(1);
        });

        // Add toolbar
        addToolbar();

        // Register keyboard shortcuts
        registerShortcuts();
    }

    private void addToolbar() {
        // Create previous and next buttons for flashcards and card counter
        leftButton = new JButton("<");
        leftButton.addActionListener(this);
        leftButton.setEnabled(false);
        rightButton = new JButton(">");
        rightButton.addActionListener(this);
        cardCounter = new JLabel((currentFlashcard + 1) + " / " + flashcards.size());
        Border lineBorder = BorderFactory.createLineBorder(Color.WHITE, 2);
        Border margin = new EmptyBorder(5, 5, 5, 5);
        cardCounter.setBorder(new CompoundBorder(lineBorder, margin));
        JButton resetButton = new JButton("↻");
        resetButton.addActionListener(this);


        JComboBox<String> termSelector = new JComboBox<>(new String[]{"Chinese", "Pinyin", "English"});
        termSelector.setName("termSelector");
        termSelector.addActionListener(this);
        JComboBox<String> definitionSelector = new JComboBox<>(new String[]{"Chinese", "Pinyin", "English"});
        definitionSelector.setSelectedIndex(2);
        definitionSelector.setName("definitionSelector");
        definitionSelector.addActionListener(this);

        // Create and add toolbar JPanel
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.X_AXIS));
        toolbar.setBorder(new EmptyBorder(0, 10, 10, 10));
        toolbar.add(cardCounter);
        toolbar.add(Box.createHorizontalStrut(5));
        toolbar.add(resetButton);
        toolbar.add(Box.createHorizontalStrut(10));
        toolbar.add(leftButton);
        toolbar.add(Box.createHorizontalStrut(5));
        toolbar.add(rightButton);
        toolbar.add(Box.createHorizontalStrut(10));
        toolbar.add(termSelector);
        toolbar.add(new JLabel("->"));
        toolbar.add(definitionSelector);
        add(toolbar, BorderLayout.SOUTH);
    }

    private void registerShortcuts() {
        InputMap map = leftButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        map.put(KeyStroke.getKeyStroke("LEFT"), "previous");
        map = rightButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        map.put(KeyStroke.getKeyStroke("RIGHT"), "next");
        map = flashcardPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        map.put(KeyStroke.getKeyStroke("UP"), "flip");
        map.put(KeyStroke.getKeyStroke("DOWN"), "flip");
        Action keyAction = new AbstractAction() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (actionEvent.getSource() instanceof JPanel) {
                    flipFlashcard();
                    return;
                }
                String actionCommand = ((JButton) actionEvent.getSource()).getText();
                if (actionCommand.equals("<")) {
                    decrementFlashcard();
                } else if (actionCommand.equals(">")) {
                    incrementFlashcard();
                }
            }
        };
        leftButton.getActionMap().put("previous", keyAction);
        rightButton.getActionMap().put("next", keyAction);
        flashcardPanel.getActionMap().put("flip", keyAction);
    }

    public void incrementFlashcard() {
        cardLayout.next(flashcardPanel);
        currentFlashcard++;
        flashcards.get(currentFlashcard).showSide(termType);
        rightButton.setEnabled(currentFlashcard < flashcards.size() - 1);
        leftButton.setEnabled(currentFlashcard > 0);
        cardCounter.setText((currentFlashcard + 1) + " / " + flashcards.size());
    }

    public void decrementFlashcard() {
        cardLayout.previous(flashcardPanel);
        currentFlashcard--;
        flashcards.get(currentFlashcard).showSide(termType);
        rightButton.setEnabled(currentFlashcard < flashcards.size() - 1);
        leftButton.setEnabled(currentFlashcard > 0);
        cardCounter.setText((currentFlashcard + 1) + " / " + flashcards.size());
    }

    public void resetFlashcards() {
        cardLayout.first(flashcardPanel);
        currentFlashcard = 0;
        flashcards.get(currentFlashcard).showSide(termType);
        leftButton.setEnabled(false);
        rightButton.setEnabled(currentFlashcard < flashcards.size() - 1);
        cardCounter.setText("1 / " + flashcards.size());
    }

    public void flipFlashcard() {
        if (flashcards.get(currentFlashcard).getCurrentSide() == termType) {
            flashcards.get(currentFlashcard).showSide(definitionType);
        } else {
            flashcards.get(currentFlashcard).showSide(termType);
        }
    }

    public void updateTerm(String termType) {
        switch (termType) {
            case "English" -> this.termType = Flashcard.ENGLISH;
            case "Pinyin" -> this.termType = Flashcard.PINYIN;
            case "Chinese" -> this.termType = Flashcard.CHINESE;
        }
        flashcards.get(currentFlashcard).showSide(this.termType);
    }

    public void updateDefinition(String definitionType) {
        switch (definitionType) {
            case "English" -> this.definitionType = Flashcard.ENGLISH;
            case "Pinyin" -> this.definitionType = Flashcard.PINYIN;
            case "Chinese" -> this.definitionType = Flashcard.CHINESE;
        }
        flashcards.get(currentFlashcard).showSide(this.termType);
    }

    @SuppressWarnings("rawtypes")
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
