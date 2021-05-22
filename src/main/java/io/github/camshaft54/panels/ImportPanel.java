package io.github.camshaft54.panels;

import io.github.camshaft54.ChineseFlashcards;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ImportPanel extends JPanel implements ActionListener {


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
        JTextArea textArea = new JTextArea(10, 40);
        JScrollPane inputTextScrollPane = new JScrollPane(textArea);
        inputTextScrollPane.setMaximumSize(new Dimension((int) inputTextScrollPane.getPreferredSize().getWidth() + 5, (int) inputTextScrollPane.getPreferredSize().getHeight() + 5));
        inputTextScrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputTextScrollPane.setBorder(new LineBorder(new Color(192, 192, 192), 1));
        JLabel inputFormatLabel = new JLabel("Input Format (see help for info):");
        inputFormatLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField parseTextField = new JTextField(15);
        parseTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        parseTextField.setMaximumSize(parseTextField.getPreferredSize());
        JLabel setNameLabel = new JLabel("Set Name:");
        setNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField nameTextField = new JTextField(15);
        nameTextField.setMaximumSize(nameTextField.getPreferredSize());
        nameTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton importButton = new JButton("Import");
        importButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(inputTextScrollPane);
        main.add(Box.createVerticalStrut(10));
        main.add(inputFormatLabel);
        main.add(parseTextField);
        main.add(Box.createVerticalStrut(10));
        main.add(setNameLabel);
        main.add(nameTextField);
        main.add(Box.createVerticalStrut(10));
        main.add(importButton);

        add(toolbar, BorderLayout.NORTH);
        add(main, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Exit")) {
            ChineseFlashcards.mainWindow.showWelcomePanel();
        }
    }
}
