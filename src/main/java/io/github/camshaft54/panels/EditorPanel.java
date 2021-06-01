package io.github.camshaft54.panels;

import io.github.camshaft54.ChineseFlashcards;
import io.github.camshaft54.utils.Card;
import io.github.camshaft54.utils.Set;

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
import java.util.HashMap;

public class EditorPanel extends JPanel implements ActionListener {
    JPanel editor;
    JScrollPane scrollPane;
    JButton saveButton;
    JLabel cardCounter;
    HashMap<Integer, JPanel> cards;
    int idTracker = 0;

    public EditorPanel() throws HeadlessException {
        cards = new HashMap<>();

        setLayout(new BorderLayout());

        setupToolbarAndScrollPane();

        add(scrollPane, BorderLayout.CENTER);
        addNewCard();
    }

    public EditorPanel(Set selectedSet) {
        cards = new HashMap<>();

        setLayout(new BorderLayout());
        setupToolbarAndScrollPane();
        for (int i = 0; i < selectedSet.getCards().size(); i++) {
            addNewCard();
            Card currCard = selectedSet.getCards().get(i);
            JPanel cardPanel = cards.get(cards.size()-1);
            ((JTextField) ((Box) cardPanel.getComponent(0)).getComponent(2)).setText(currCard.getChinese());
            ((JTextField) ((Box) cardPanel.getComponent(2)).getComponent(2)).setText(currCard.getPinyin());
            ((JTextField) ((Box) cardPanel.getComponent(4)).getComponent(2)).setText(currCard.getEnglish());
        }
    }

    private void setupToolbarAndScrollPane() {
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

        editor = new JPanel();
        editor.setLayout(new BoxLayout(editor, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(editor);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addNewCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new CompoundBorder(new LineBorder(new Color(192, 192, 192), 2), new EmptyBorder(10, 10, 10, 10)));

        Box chineseBox = new Box(BoxLayout.X_AXIS);
        JTextField chineseTextField = new JTextField(20);
        chineseTextField.setMaximumSize(chineseTextField.getPreferredSize());
        JButton deleteButton = new JButton("✕");
        deleteButton.setActionCommand("x " + idTracker);
        deleteButton.addActionListener(this);
        chineseBox.add(new JLabel("Chinese:"));
        chineseBox.add(Box.createHorizontalStrut(5));
        chineseBox.add(chineseTextField);
        chineseBox.add(Box.createHorizontalGlue());
        chineseBox.add(deleteButton);

        Box pinyinBox = new Box(BoxLayout.X_AXIS);
        JTextField pinyinTextField = new JTextField(20);
        pinyinTextField.setMaximumSize(pinyinTextField.getPreferredSize());
        JButton genPinyinButton = new JButton("Generate Pinyin from Chinese");
        genPinyinButton.setActionCommand("genPinyin " + idTracker);
        genPinyinButton.addActionListener(this);
        pinyinBox.add(new JLabel("Pinyin:"));
        pinyinBox.add(Box.createHorizontalStrut(14));
        pinyinBox.add(pinyinTextField);
        pinyinBox.add(Box.createHorizontalGlue());
        pinyinBox.add(genPinyinButton);

        Box englishBox = new Box(BoxLayout.X_AXIS);
        JTextField englishTextField = new JTextField(20);
        englishTextField.setMaximumSize(englishTextField.getPreferredSize());
        JButton genEnglishButton = new JButton("Generate English from Chinese");
        genEnglishButton.setActionCommand("genEnglish " + idTracker);
        genEnglishButton.addActionListener(this);
        englishBox.add(new JLabel("English:"));
        englishBox.add(Box.createHorizontalStrut(8));
        englishBox.add(englishTextField);
        englishBox.add(Box.createHorizontalGlue());
        englishBox.add(genEnglishButton);

        card.add(chineseBox);
        card.add(Box.createVerticalStrut(5));
        card.add(pinyinBox);
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

    private void saveSet(String name) {
        Set set = new Set();
        set.setName(name);
        ArrayList<Card> cards = set.getCards();
        for (HashMap.Entry<Integer, JPanel> entry : this.cards.entrySet()) {
            JPanel panel = entry.getValue();
            String chinese = ((JTextField) ((Box) panel.getComponent(0)).getComponent(2)).getText();
            String pinyin = ((JTextField) ((Box) panel.getComponent(2)).getComponent(2)).getText();
            String english = ((JTextField) ((Box) panel.getComponent(4)).getComponent(2)).getText();
            Card card = new Card();
            card.setChinese(chinese);
            card.setPinyin(pinyin);
            card.setEnglish(english);
            cards.add(card);
        }
        try {
            ChineseFlashcards.yaml.dump(set, new OutputStreamWriter(new FileOutputStream(ChineseFlashcards.setsFolderLocation + "\\" + name + ".yaml"), StandardCharsets.UTF_16));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ChineseFlashcards.mainWindow.showWelcomePanel();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Exit")) {
            ChineseFlashcards.mainWindow.showWelcomePanel();
        } else if (e.getActionCommand().equals("New Card")) {
            addNewCard();
            saveButton.setEnabled(cards.size() != 0);
        } else if (e.getActionCommand().startsWith("x ")) {
            int index = Integer.parseInt(e.getActionCommand().replace("x ", ""));
            editor.remove(cards.get(index));
            scrollPane.revalidate();
            scrollPane.repaint();
            cards.remove(index);
            saveButton.setEnabled(cards.size() != 0);
            cardCounter.setText(cards.size() + " card" + ((cards.size() != 1) ? "s" : ""));
        } else if (e.getActionCommand().equals("Save")) {
            String proposedName = "";
            while (proposedName != null) {
                proposedName = JOptionPane.showInputDialog("Enter a name for this set:");
                if (proposedName != null && !proposedName.trim().equals("")) {
                    String finalProposedName = proposedName;
                    if (!proposedName.trim().equals("") && ChineseFlashcards.sets.stream().noneMatch(set -> set.getName().equals(finalProposedName.trim()))) {
                        saveSet(proposedName.trim());
                        JOptionPane.showMessageDialog(this, "Saved \"" + proposedName.trim() + "\" to file!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                } else if (proposedName != null) {
                    JOptionPane.showMessageDialog(this, "Invalid Set Name! please try again", "Invalid Name", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else if (e.getActionCommand().startsWith("genPinyin ")) {
            JPanel panel = cards.get(Integer.parseInt(e.getActionCommand().replace("genPinyin ", "")));
            String chinese = ((JTextField) ((Box) panel.getComponent(0)).getComponent(2)).getText();
            if (!chinese.equals("")) {
                try {
                    ((JTextField) ((Box) panel.getComponent(2)).getComponent(2)).setText(ChineseFlashcards.dict.get(chinese).getPinyin());
                } catch (Exception ignored) { }
            }
        } else if (e.getActionCommand().startsWith("genEnglish ")) {
            JPanel panel = cards.get(Integer.parseInt(e.getActionCommand().replace("genEnglish ", "")));
            String chinese = ((JTextField) ((Box) panel.getComponent(0)).getComponent(2)).getText();
            if (!chinese.equals("")) {
                try {
                    ((JTextField) ((Box) panel.getComponent(4)).getComponent(2)).setText(ChineseFlashcards.dict.get(chinese).getEnglish());
                } catch (Exception ignored) { }
            }
        }
    }
}
