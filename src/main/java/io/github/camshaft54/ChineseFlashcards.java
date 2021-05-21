package io.github.camshaft54;

import io.github.camshaft54.utils.Card;
import io.github.camshaft54.utils.DictEntry;
import io.github.camshaft54.utils.Set;
import io.github.camshaft54.windows.MainWindow;
import org.pushingpixels.substance.api.skin.SubstanceGraphiteChalkLookAndFeel;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ChineseFlashcards {
    public static MainWindow mainWindow;
    public static ArrayList<Set> sets;
    public static ArrayList<String> setFiles;
    public static Yaml yaml;
    public static HashMap<String, DictEntry> dict;

    public static void main(String[] args) {
        parseDictionary();

        Constructor setConstructor = new Constructor(Set.class);
        TypeDescription setDescription = new TypeDescription(Set.class);
        setDescription.addPropertyParameters("cards", Card.class);
        setConstructor.addTypeDescription(setDescription);
        yaml = new Yaml(setConstructor);
        sets = new ArrayList<>();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                for (int i = 0; i < sets.size(); i++) {
                    yaml.dump(sets.get(i), new FileWriter("sets/" + setFiles.get(i)));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

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

    public static void populateSetList(String directory) {
        if (setFiles != null && sets != null) {
            try {
                for (int i = 0; i < sets.size(); i++) {
                    yaml.dump(sets.get(i), new FileWriter("sets/" + setFiles.get(i)));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            sets.clear();
            setFiles.clear();
        }

        File dir = new File(directory);
        String[] files = dir.list((dir1, name) -> name.endsWith(".yaml") || name.endsWith(".yml"));
        if (files != null) {
            setFiles = (ArrayList<String>) Arrays.stream(files).collect(Collectors.toList());
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

    public static void parseDictionary() {
        dict = new HashMap<>();
        try {
            Scanner scan = new Scanner(new File("src/main/resources/translate/cedict_ts.u8"));
            int count = 0;
            while (scan.hasNextLine()) {
                count++;
                String line = scan.nextLine();
                if (count < 32) {
                    break;
                }
                String[] lineArr = line.split(" ");
                String traditional = lineArr[0];
                String simplified = lineArr[1];
                String pinyin = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
                String definition = line.substring(line.indexOf("/")+1, line.length()-1);
                definition = definition.substring(0, (definition.contains("/")) ? definition.indexOf("/") : definition.length());
                if (traditional.equals(simplified)) {
                    dict.put(simplified, new DictEntry(pinyin, definition));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
