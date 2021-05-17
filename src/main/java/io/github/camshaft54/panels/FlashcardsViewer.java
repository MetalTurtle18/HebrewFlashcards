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

    public FlashcardsViewer() {
        isVisible = true; // TODO: Implement this

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
            flashcards.add(new Flashcard(card, 0)); // TODO: change this to whatever user selects
            flashcardPanel.add(flashcards.get(flashcards.size()-1), i.get());
            i.getAndAdd(1);
        });
//        flashcardPanel.add(Flashcard.getEndFlashcard(), i.get());

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

        // Create and add toolbar JPanel
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.X_AXIS));
        toolbar.setBorder(new EmptyBorder(0, 10, 10, 10));
        toolbar.add(cardCounter);
        toolbar.add(Box.createHorizontalGlue());
        toolbar.add(leftButton);
        toolbar.add(Box.createHorizontalStrut(5));
        toolbar.add(rightButton);
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
        flashcards.get(currentFlashcard).showSide(0);
        currentFlashcard++;
        rightButton.setEnabled(currentFlashcard < flashcards.size() - 1);
        leftButton.setEnabled(currentFlashcard > 0);
        cardCounter.setText((currentFlashcard + 1) + " / " + flashcards.size());
    }

    public void decrementFlashcard() {
        cardLayout.previous(flashcardPanel);
        flashcards.get(currentFlashcard).showSide(0);
        currentFlashcard--;
        rightButton.setEnabled(currentFlashcard < flashcards.size() - 1);
        leftButton.setEnabled(currentFlashcard > 0);
        cardCounter.setText((currentFlashcard + 1) + " / " + flashcards.size());
    }

    // TODO: implement user feedback from JComboBox
    public void flipFlashcard() {
        if (flashcards.get(currentFlashcard).getCurrentSide() == Flashcard.PINYIN) {
            flashcards.get(currentFlashcard).showSide(Flashcard.ENGLISH);
        } else {
            flashcards.get(currentFlashcard).showSide(Flashcard.PINYIN);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "<" -> decrementFlashcard();
            case ">" -> incrementFlashcard();
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
