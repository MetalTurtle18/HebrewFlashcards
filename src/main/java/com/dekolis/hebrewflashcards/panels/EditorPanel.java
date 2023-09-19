package com.dekolis.hebrewflashcards.panels;

import com.dekolis.hebrewflashcards.HebrewFlashcards;
import com.dekolis.hebrewflashcards.utils.Card;
import com.dekolis.hebrewflashcards.utils.Set;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 * This panel is where the user can edit a selected set or create a new set
 */
public class EditorPanel extends JPanel implements ActionListener {
    JPanel editor;
    JScrollPane scrollPane;
    JButton saveButton;
    JLabel cardCounter;
    HashMap<Integer, JPanel> cards;
    String setName;
    String setFileName;
    int idTracker = 0;

    /**
     * Constructs the Editor panel with a single blank flashcard entry (used when user clicks new button).
     */
    public EditorPanel() {
        cards = new HashMap<>();
        setName = "";
        setFileName = "";

        setLayout(new BorderLayout());

        setupToolbarAndScrollPane();

        add(scrollPane, BorderLayout.CENTER);
        addNewCard();
    }

    /**
     * Constructs the editor panel with the set specified in the editor panel (used when user clicks edit selected set).
     * @param selectedSet the set to be loaded into the editor panel after initialization
     */
    public EditorPanel(Set selectedSet) {
        cards = new HashMap<>();
        setName = selectedSet.getName();
        setFileName = selectedSet.getFilename();

        setLayout(new BorderLayout());
        setupToolbarAndScrollPane();
        for (int i = 0; i < selectedSet.getCards().size(); i++) {
            addNewCard();
            Card currCard = selectedSet.getCards().get(i);
            JPanel cardPanel = cards.get(cards.size()-1);
            ((JTextField) ((Box) cardPanel.getComponent(0)).getComponent(2)).setText(currCard.getHebrew());
            ((JTextField) ((Box) cardPanel.getComponent(2)).getComponent(2)).setText(currCard.getTransliteration());
            ((JTextField) ((Box) cardPanel.getComponent(4)).getComponent(2)).setText(currCard.getEnglish());
        }
    }

    /**
     * Sets up the toolbar and JScrollPane. The JScrollPane contains all of the flashcard entries
     */
    private void setupToolbarAndScrollPane() {
        // Creates toolbar with exitButton, saveButton, and cardCounter
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.X_AXIS));
        toolbar.setBorder(new EmptyBorder(10, 10, 10, 10));
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(this);
        JButton newCardButton = new JButton("New Card");
        newCardButton.addActionListener(this);
        saveButton = new JButton("Save");
        saveButton.addActionListener(this);
        cardCounter = new JLabel("1 Card");
        toolbar.add(exitButton);
        toolbar.add(Box.createHorizontalStrut(10));
        toolbar.add(newCardButton);
        toolbar.add(Box.createHorizontalStrut(10));
        toolbar.add(saveButton);
        toolbar.add(Box.createHorizontalGlue());
        toolbar.add(cardCounter);
        add(toolbar, BorderLayout.NORTH);

        // Create and add JScrollPane
        editor = new JPanel();
        editor.setLayout(new BoxLayout(editor, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(editor);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Creates a blank card in the JScrollPane.
     */
    private void addNewCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new CompoundBorder(new LineBorder(new Color(192, 192, 192), 2), new EmptyBorder(10, 10, 10, 10)));

        // Add the hebrew text box and "X" button for closing window
        Box hebrewBox = new Box(BoxLayout.X_AXIS);
        JTextField hebrewTextField = new JTextField(20);
        hebrewTextField.setMaximumSize(hebrewTextField.getPreferredSize());
        JButton deleteButton = new JButton("✕");
        deleteButton.setActionCommand("x " + idTracker);
        deleteButton.addActionListener(this);
        deleteButton.setFocusable(false);
        hebrewBox.add(new JLabel("Hebrew:"));
        hebrewBox.add(Box.createHorizontalStrut(5));
        hebrewBox.add(hebrewTextField);
        hebrewBox.add(Box.createHorizontalGlue());
        hebrewBox.add(deleteButton);

        // Add transliteration text box and genTransliterationButton that generates transliteration based on hebrew that is entered
        Box transliterationBox = new Box(BoxLayout.X_AXIS);
        JTextField transliterationTextField = new JTextField(20);
        transliterationTextField.setMaximumSize(transliterationTextField.getPreferredSize());
        JButton genTransliterationButton = new JButton("Generate Transliteration from Hebrew");
        genTransliterationButton.setActionCommand("genTransliteration " + idTracker);
        genTransliterationButton.addActionListener(this);
        genTransliterationButton.setFocusable(false);
        transliterationBox.add(new JLabel("Transliteration:"));
        transliterationBox.add(Box.createHorizontalStrut(14));
        transliterationBox.add(transliterationTextField);
        transliterationBox.add(Box.createHorizontalGlue());
        transliterationBox.add(genTransliterationButton);

        // Add english text box and genEnglishButton that generates english based on hebrew that is entered
        Box englishBox = new Box(BoxLayout.X_AXIS);
        JTextField englishTextField = new JTextField(20);
        englishTextField.setMaximumSize(englishTextField.getPreferredSize());
        JButton genEnglishButton = new JButton("Generate English from Hebrew");
        genEnglishButton.setActionCommand("genEnglish " + idTracker);
        genEnglishButton.addActionListener(this);
        genEnglishButton.setFocusable(false);
        englishBox.add(new JLabel("English:"));
        englishBox.add(Box.createHorizontalStrut(8));
        englishBox.add(englishTextField);
        englishBox.add(Box.createHorizontalGlue());
        englishBox.add(genEnglishButton);

        // Create the card panel with the 3 Boxes from above
        card.add(hebrewBox);
        card.add(Box.createVerticalStrut(5));
        card.add(transliterationBox);
        card.add(Box.createVerticalStrut(5));
        card.add(englishBox);
        card.add(Box.createVerticalStrut(5));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) card.getPreferredSize().getHeight()));
        editor.add(card);
        cards.put(idTracker, card);
        scrollPane.revalidate();
        idTracker++;
        cardCounter.setText(cards.size() + " card" + ((cards.size() != 1) ? "s" : ""));
    }

    /**
     * This method goes through all of the JPanels in cards and extracts the hebrew, transliteration, and english. It then saves
     * these values as a Card in a Set.
     * @param name the name for the set.
     */
    private void saveSet(String name) {
        Set set = new Set();
        set.setName(name);
        for (HashMap.Entry<Integer, JPanel> entry : this.cards.entrySet()) {
            JPanel panel = entry.getValue();
            String hebrew = ((JTextField) ((Box) panel.getComponent(0)).getComponent(2)).getText();
            String transliteration = ((JTextField) ((Box) panel.getComponent(2)).getComponent(2)).getText();
            String english = ((JTextField) ((Box) panel.getComponent(4)).getComponent(2)).getText();
            Card card = new Card();
            card.setHebrew(hebrew);
            card.setTransliteration(transliteration);
            card.setEnglish(english);
            set.getCards().add(card);
        }
        set.setFilename((setFileName != null && !setFileName.equals("")) ? setFileName : name + ".yaml");
        HebrewFlashcards.sets.put(set.getName(), set);

        HebrewFlashcards.mainWindow.showWelcomePanel();
    }

    /**
     * Handles buttons in each card by getting the card ID from the ActionCommand. Also handles save and exit button.
     * @param e ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Exit")) {
            HebrewFlashcards.mainWindow.showWelcomePanel();
        } else if (e.getActionCommand().equals("New Card")) {
            addNewCard();
            saveButton.setEnabled(cards.size() != 0);
            // Scroll to bottom to show new card
            HebrewFlashcards.mainWindow.revalidate();
            HebrewFlashcards.mainWindow.repaint();
            scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
        } else if (e.getActionCommand().startsWith("x ")) {
            int index = Integer.parseInt(e.getActionCommand().replace("x ", ""));
            editor.remove(cards.get(index));
            scrollPane.revalidate();
            scrollPane.repaint();
            cards.remove(index);
            saveButton.setEnabled(cards.size() != 0);
            cardCounter.setText(cards.size() + " card" + ((cards.size() != 1) ? "s" : ""));
        } else if (e.getActionCommand().equals("Save")) {
            if (!setName.equals("")) {
                saveSet(setName);
                JOptionPane.showMessageDialog(this, "Saved \"" + setName + "\" to file!", "Success", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String proposedName = "";
            while (proposedName != null) {
                proposedName = JOptionPane.showInputDialog("Enter a name for this set:"); // Get input from user for set name
                if (proposedName != null && !proposedName.trim().equals("")) { // If the user did not press cancel and did not leave the input blank, continue
                    // The stream can only accept objects that are effectively final, so a "final" version of proposedName must be created
                    String finalProposedName = proposedName.trim();
                    // If proposed name does not match other sets' name or filename, save the set and alert the user
                    if (!HebrewFlashcards.sets.containsKey(finalProposedName) && HebrewFlashcards.sets.values().stream().noneMatch(set -> set.getFilename().equals(finalProposedName))) {
                        saveSet(proposedName.trim());
                        JOptionPane.showMessageDialog(this, "Saved \"" + proposedName.trim() + "\" to file!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    } else {
                        JOptionPane.showMessageDialog(this, "Set name must not match another set name. Please try again.", "Invalid Name", JOptionPane.WARNING_MESSAGE);
                    }
                } else if (proposedName != null) {
                    JOptionPane.showMessageDialog(this, "Invalid Set Name. Please try again.", "Invalid Name", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else if (e.getActionCommand().startsWith("genTransliteration ")) {
            JPanel panel = cards.get(Integer.parseInt(e.getActionCommand().replace("genTransliteration ", "")));
            String hebrew = ((JTextField) ((Box) panel.getComponent(0)).getComponent(2)).getText();
            if (!hebrew.equals("")) {
                try {
                    // Attempts to find a value in the dictionary that matches the Hebrew, then fills it in.
                    ((JTextField) ((Box) panel.getComponent(2)).getComponent(2)).setText(HebrewFlashcards.dict.get(hebrew).getTransliteration());
                } catch (Exception ignored) { }
            }
        } else if (e.getActionCommand().startsWith("genEnglish ")) {
            JPanel panel = cards.get(Integer.parseInt(e.getActionCommand().replace("genEnglish ", "")));
            String hebrew = ((JTextField) ((Box) panel.getComponent(0)).getComponent(2)).getText();
            if (!hebrew.equals("")) {
                try {
                    // Attempts to find a value in the dictionary that matches the Hebrew, then fills it in.
                    ((JTextField) ((Box) panel.getComponent(4)).getComponent(2)).setText(HebrewFlashcards.dict.get(hebrew).getEnglish());
                } catch (Exception ignored) { }
            }
        }
    }
}
