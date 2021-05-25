package io.github.camshaft54.panels;

import io.github.camshaft54.ChineseFlashcards;
import io.github.camshaft54.utils.DndJList;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class ImportPanel extends JPanel implements ActionListener {
    JTextArea inputTextArea;
    JTextField nameTextField;

    public ImportPanel() {
        setLayout(new BorderLayout());

        JPanel toolbar = new JPanel();
        toolbar.setLayout(new BoxLayout(toolbar, BoxLayout.X_AXIS));
        toolbar.setBorder(new EmptyBorder(10, 10, 10, 10));
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(this);
        toolbar.add(exitButton);

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

        JPanel dndJListPanel = new JPanel();
        dndJListPanel.setBorder(new CompoundBorder(new LineBorder(new Color(196, 196, 196), 2), new EmptyBorder(5, 5, 5, 5)));
        dndJListPanel.setLayout(new BoxLayout(dndJListPanel, BoxLayout.Y_AXIS));
        JLabel dndTitle = new JLabel("Rearrange Order of Items:");
        dndTitle.setAlignmentX(CENTER_ALIGNMENT);
        DndJList dndJList = new DndJList(Arrays.asList("≡ Chinese", "≡ Pinyin", "≡ English"));
        dndJList.setAlignmentX(CENTER_ALIGNMENT);
        dndJListPanel.add(dndTitle);
        dndJListPanel.add(dndJList);
        dndJListPanel.add(Box.createVerticalGlue());

        JPanel separators = new JPanel();
        separators.setLayout(new BoxLayout(separators, BoxLayout.Y_AXIS));
        separators.setBorder(new CompoundBorder(new LineBorder(new Color(196, 196, 196), 2), new EmptyBorder(5, 5, 5, 5)));
        JLabel firstSeparatorTitle = new JLabel("Separator Between 1st and 2nd Items:");
        firstSeparatorTitle.setAlignmentX(CENTER_ALIGNMENT);
        JTextField firstSeparatorTextField = new JTextField(10);
        firstSeparatorTextField.setMaximumSize(firstSeparatorTextField.getPreferredSize());
        JLabel secondSeparatorTitle = new JLabel("Separator Between 2nd and 3rd Items:");
        secondSeparatorTitle.setAlignmentX(CENTER_ALIGNMENT);
        JTextField secondSeparatorTextField = new JTextField(10);
        secondSeparatorTextField.setMaximumSize(secondSeparatorTextField.getPreferredSize());
        JLabel cardSeparatorTitle = new JLabel("Separator Between Cards:");
        cardSeparatorTitle.setAlignmentX(CENTER_ALIGNMENT);
        JTextField cardSeparatorTextField = new JTextField(10);
        cardSeparatorTextField.setMaximumSize(cardSeparatorTextField.getPreferredSize());
        separators.add(firstSeparatorTitle);
        separators.add(firstSeparatorTextField);
        separators.add(Box.createRigidArea(new Dimension(0,10)));
        separators.add(secondSeparatorTitle);
        separators.add(secondSeparatorTextField);
        separators.add(Box.createRigidArea(new Dimension(0,10)));
        separators.add(cardSeparatorTitle);
        separators.add(cardSeparatorTextField);

        formatPanel.add(separators);
        formatPanel.add(dndJListPanel);

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
            ChineseFlashcards.mainWindow.showWelcomePanel();
        } else if (e.getActionCommand().equals("Import")) {

        }
    }
}
