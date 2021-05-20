package io.github.camshaft54.panels;

import io.github.camshaft54.ChineseFlashcards;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class EditorPanel extends JPanel implements ActionListener {
    JPanel editor;
    ArrayList<JPanel> cards;

    public EditorPanel() throws HeadlessException {
        cards = new ArrayList<>();

        setLayout(new BorderLayout());
        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.X_AXIS));
        toolbar.setBorder(new EmptyBorder(10, 10, 10, 10));
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(this);
        JButton newCardButton = new JButton("New Card");
        newCardButton.addActionListener(this);
        toolbar.add(exitButton);
        toolbar.add(newCardButton);
        add(toolbar, BorderLayout.NORTH);

        editor = new JPanel();
        editor.setLayout(new BoxLayout(editor, BoxLayout.Y_AXIS));
        JScrollPane editorScrollPane = new JScrollPane(editor);
        add(editorScrollPane, BorderLayout.CENTER);
        addNewCard();
    }

    private void addNewCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new LineBorder(new Color(192, 192, 192), 2));

        JPanel chinesePanel = new JPanel();
        JTextField chineseTextField = new JTextField("你好", 20);
        chinesePanel.add(new JLabel("Chinese:"));
        chinesePanel.add(chineseTextField);
        chinesePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel pinyinPanel = new JPanel();
        JTextField pinyinTextField = new JTextField("nihao", 20);
        pinyinPanel.add(new JLabel("Pinyin:"));
        pinyinPanel.add(pinyinTextField);
        pinyinPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel englishPanel = new JPanel();
        JTextField englishTextField = new JTextField("hello", 20);
        englishPanel.add(new JLabel("English:"));
        englishPanel.add(englishTextField);
        englishPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(chinesePanel, 0);
        card.add(pinyinPanel, 1);
        card.add(englishPanel, 2);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, (int) card.getPreferredSize().getHeight()));
        editor.add(card);
        cards.add(card);
        editor.add(Box.createVerticalStrut(5));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Exit")) {
            ChineseFlashcards.mainWindow.showWelcomePanel();
        } else if (e.getActionCommand().equals("New Card")) {
            System.out.println(e.getActionCommand());
            addNewCard();
        }
    }
}
