package com.dekolis.hebrewflashcards.panels;

import com.dekolis.hebrewflashcards.HebrewFlashcards;
import com.dekolis.hebrewflashcards.utils.DndJList;
import com.dekolis.hebrewflashcards.utils.Set;
import com.dekolis.hebrewflashcards.utils.Card;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class ImportPanel extends JPanel implements ActionListener {
    JTextArea inputTextArea;
    JTextField nameTextField;
    JTextField firstSeparatorTextField;
    JTextField secondSeparatorTextField;
    JTextField cardSeparatorTextField;
    DndJList dndJList;

    public ImportPanel() {
        setLayout(new BorderLayout());

        // Create top toolbar with exit button
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.X_AXIS));
        toolbar.setBorder(new EmptyBorder(10, 10, 10, 10));
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(this);
        toolbar.add(exitButton);

        // Create main JPanel that will contains input text box and other information about delimiters and name
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        inputTextArea = new JTextArea(10, 40);
        JScrollPane inputTextScrollPane = new JScrollPane(inputTextArea);
        inputTextScrollPane.setMaximumSize(new Dimension((int) inputTextScrollPane.getPreferredSize().getWidth() + 5, (int) inputTextScrollPane.getPreferredSize().getHeight() + 5));
        inputTextScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputTextScrollPane.setBorder(new LineBorder(new Color(192, 192, 192), 1));

        JPanel formatPanel = new JPanel();
        formatPanel.setLayout(new BoxLayout(formatPanel, BoxLayout.X_AXIS));
        formatPanel.setAlignmentX(CENTER_ALIGNMENT);

        // This panel contains a drag and drop JList used to determine the desired order of the parts of the card
        JPanel dndJListPanel = new JPanel();
        dndJListPanel.setBorder(new CompoundBorder(new LineBorder(new Color(196, 196, 196), 2), new EmptyBorder(5, 5, 5, 5)));
        dndJListPanel.setLayout(new BoxLayout(dndJListPanel, BoxLayout.Y_AXIS));
        JLabel dndTitle = new JLabel("Rearrange Order of Items:");
        dndTitle.setAlignmentX(CENTER_ALIGNMENT);
        dndJList = new DndJList(Arrays.asList("≡ Hebrew", "≡ Transliteration", "≡ English"));
        dndJList.setAlignmentX(CENTER_ALIGNMENT);
        dndJListPanel.add(dndTitle);
        dndJListPanel.add(Box.createRigidArea(new Dimension(0,5)));
        dndJListPanel.add(dndJList);
        dndJListPanel.add(Box.createVerticalGlue());

        // Add text boxes for separators between items and cards
        JPanel separators = new JPanel();
        separators.setLayout(new BoxLayout(separators, BoxLayout.Y_AXIS));
        separators.setBorder(new CompoundBorder(new LineBorder(new Color(196, 196, 196), 2), new EmptyBorder(5, 5, 5, 5)));
        JLabel firstSeparatorTitle = new JLabel("Separator Between 1st and 2nd Items:");
        firstSeparatorTitle.setAlignmentX(CENTER_ALIGNMENT);
        firstSeparatorTextField = new JTextField(10);
        firstSeparatorTextField.setMaximumSize(firstSeparatorTextField.getPreferredSize());
        JLabel secondSeparatorTitle = new JLabel("Separator Between 2nd and 3rd Items:");
        secondSeparatorTitle.setAlignmentX(CENTER_ALIGNMENT);
        secondSeparatorTextField = new JTextField(10);
        secondSeparatorTextField.setMaximumSize(secondSeparatorTextField.getPreferredSize());
        JLabel cardSeparatorTitle = new JLabel("Separator Between Cards:");
        cardSeparatorTitle.setAlignmentX(CENTER_ALIGNMENT);
        cardSeparatorTextField = new JTextField(10);
        cardSeparatorTextField.setMaximumSize(cardSeparatorTextField.getPreferredSize());
        separators.add(firstSeparatorTitle);
        separators.add(Box.createRigidArea(new Dimension(0,2)));
        separators.add(firstSeparatorTextField);
        separators.add(Box.createRigidArea(new Dimension(0,7)));
        separators.add(secondSeparatorTitle);
        separators.add(Box.createRigidArea(new Dimension(0,2)));
        separators.add(secondSeparatorTextField);
        separators.add(Box.createRigidArea(new Dimension(0,7)));
        separators.add(cardSeparatorTitle);
        separators.add(Box.createRigidArea(new Dimension(0,2)));
        separators.add(cardSeparatorTextField);

        formatPanel.add(separators);
        formatPanel.add(dndJListPanel);

        // Adds text field for name and builds main panel with the panels from above
        JLabel setNameLabel = new JLabel("Set Name:");
        setNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameTextField = new JTextField(15);
        nameTextField.setMaximumSize(nameTextField.getPreferredSize());
        nameTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton importButton = new JButton("Import");
        importButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        importButton.addActionListener(this);
        main.add(setNameLabel);
        main.add(nameTextField);
        main.add(Box.createVerticalStrut(10));
        main.add(inputTextScrollPane);
        main.add(Box.createVerticalStrut(10));
        main.add(formatPanel);
        main.add(Box.createVerticalStrut(10));
        main.add(importButton);

        add(toolbar, BorderLayout.NORTH);
        add(main, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Exit")) {
            HebrewFlashcards.mainWindow.showWelcomePanel();
        } else if (e.getActionCommand().equals("Import")) {
            if (nameTextField.getText().equals("") || inputTextArea.getText().equals("") ||
                    firstSeparatorTextField.getText().equals("") || secondSeparatorTextField.getText().equals("") ||
                    cardSeparatorTextField.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Please make sure all text fields contain text!",
                        "Missing Required Fields", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String[] unparsedCards = inputTextArea.getText().split(cardSeparatorTextField.getText());
            ArrayList<Card> cards = new ArrayList<>();
            // this for loop gets the order from the DndJList and adds to an array
            int index = 0;
            int[] typeIndex = new int[3];
            for (int i = 0; i < dndJList.getModel().getSize(); i++) {
                String element = dndJList.getModel().getElementAt(i);
                int type;
                if (element.endsWith("Hebrew")) {
                    type = 0;
                } else if (element.endsWith("Transliteration")) {
                    type = 1;
                } else if (element.endsWith("English")) {
                    type = 2;
                } else {
                    System.out.println("Error reading from JList! Not importing...");
                    return;
                }
                typeIndex[i] = type;
            }

            // this enhanced for loop attempts to parse out the text and create a card. If it fails, it will alert the user
            try {
                for (String unparsedCard : unparsedCards) {
                    String[] cardParts = new String[3];
                    cardParts[typeIndex[0]] = unparsedCard.substring(0, unparsedCard.indexOf(firstSeparatorTextField.getText()));
                    cardParts[typeIndex[1]] = unparsedCard.substring(unparsedCard.indexOf(firstSeparatorTextField.getText()) +
                            firstSeparatorTextField.getText().length(), unparsedCard.lastIndexOf(secondSeparatorTextField.getText()));
                    cardParts[typeIndex[2]] = unparsedCard.substring(unparsedCard.lastIndexOf(secondSeparatorTextField.getText()) +
                            secondSeparatorTextField.getText().length());
                    cards.add(Card.createCard(cardParts[0], cardParts[1], cardParts[2]));
                    index++;
                }
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(this, "Error parsing card on line " + (index + 1) + "! Content of the line: " + unparsedCards[index], "Parsing Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Create the Set, save it to file, then exit the panel
            Set set = new Set();
            set.setCards(cards);
            set.setName(nameTextField.getText());
            try {
                FileOutputStream fos = new FileOutputStream(HebrewFlashcards.setsFolderLocation + "/" + nameTextField.getText() + ".yaml");
                OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_16);
                HebrewFlashcards.yaml.dump(set, osw);
                fos.close();
                osw.close();
            } catch (IOException ioException) {
                JOptionPane.showMessageDialog(this, "Error saving file!", "File Save Error", JOptionPane.ERROR_MESSAGE);
            }
            JOptionPane.showMessageDialog(this, "Successfully imported set!", "Success", JOptionPane.INFORMATION_MESSAGE);
            HebrewFlashcards.mainWindow.showWelcomePanel();
        }
    }
}
