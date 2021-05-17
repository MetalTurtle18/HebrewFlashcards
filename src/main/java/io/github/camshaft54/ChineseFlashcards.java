package io.github.camshaft54;

import io.github.camshaft54.utils.Card;
import io.github.camshaft54.utils.Set;
import io.github.camshaft54.windows.MainWindow;
import org.pushingpixels.substance.api.skin.SubstanceGraphiteChalkLookAndFeel;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ChineseFlashcards {
    public static MainWindow mainWindow;
    public static ArrayList<Set> sets;
    public static Yaml yaml;

    public static void main(String[] args) {
        Constructor setConstructor = new Constructor(Set.class);
        TypeDescription setDescription = new TypeDescription(Set.class);
        setDescription.addPropertyParameters("cards", Card.class);
        setConstructor.addTypeDescription(setDescription);
        yaml = new Yaml(setConstructor);
        sets = new ArrayList<>();
        getListOfSets("sets");

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new SubstanceGraphiteChalkLookAndFeel());
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
            mainWindow = new MainWindow();
        });
    }

    public static void convertTXTtoYAML(String inputFilename, String outputFilename) throws IOException {
        Scanner fr = new Scanner(new File(inputFilename));
        ArrayList<Card> cards = new ArrayList<>();
        while (fr.hasNextLine()) {
            String cardStr = fr.nextLine();
            String chinese = cardStr.split(",")[0];
            String pinyin = cardStr.split(",")[1].split(" - ")[0];
            String english = cardStr.split(",")[1].split(" - ")[1];
            cards.add(Card.createCard(chinese, pinyin, english));
        }
        Yaml yaml = new Yaml();
        yaml.dump(cards, new FileWriter(outputFilename));
    }

    public static void getListOfSets(String directory) {
        File dir = new File(directory);
        String[] files = dir.list((dir1, name) -> name.endsWith(".yaml") || name.endsWith(".yml"));
        if (files != null) {
            for (String name : files) {
                try {
                    FileInputStream fs = new FileInputStream("sets/" + name);
                    sets.add(yaml.load(fs));
                } catch (IOException e) {
                    System.out.println("Error loading sets!\nExitingCFS");
                    return;
                }
            }
        } else {
            System.out.println("Could not find any flashcard sets in sets folder!\nExiting CFS...");
        }
    }
}
