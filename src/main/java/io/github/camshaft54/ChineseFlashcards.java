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
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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
    public static String setsFolderLocation;

    public static void main(String[] args) {
        setupSetsFolder();

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
                    yaml.dump(sets.get(i), new OutputStreamWriter(new FileOutputStream(ChineseFlashcards.setsFolderLocation + "\\" + setFiles.get(i)), StandardCharsets.UTF_16));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new SubstanceGraphiteChalkLookAndFeel());
            } catch (UnsupportedLookAndFeelException ignored) {}
            mainWindow = new MainWindow();
        });
    }

    public static void populateSetList() throws URISyntaxException {
        if (setFiles != null && sets != null) {
            try {
                for (int i = 0; i < sets.size(); i++) {
                    yaml.dump(sets.get(i), new OutputStreamWriter(new FileOutputStream(ChineseFlashcards.setsFolderLocation + "\\" + setFiles.get(i)), StandardCharsets.UTF_16));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            sets.clear();
            setFiles.clear();
        }

        File folder = new File(setsFolderLocation);
        String[] files = folder.list((dir, name) -> name.endsWith(".yaml") || name.endsWith(".yml"));
        if (files != null) {
            setFiles = (ArrayList<String>) Arrays.stream(files).collect(Collectors.toList());
            for (String name : files) {
                try {
                    FileInputStream fs = new FileInputStream(setsFolderLocation + "\\" + name);
                    sets.add(yaml.load(fs));
                } catch (IOException e) {
                    System.out.println("Error loading sets!\nExitingCFS");
                    return;
                }
            }
        }
    }

    public static void parseDictionary() {
        dict = new HashMap<>();
        Scanner scan = new Scanner(new InputStreamReader(ChineseFlashcards.class.getResourceAsStream("/translate/cedict_ts.u8"), StandardCharsets.UTF_8));
        int count = 0;
        while (scan.hasNextLine()) {
            count++;
            String line = scan.nextLine();
            if (count < 32) {
                continue;
            }
            String[] lineArr = line.split(" ");
            String traditional = lineArr[0];
            String simplified = lineArr[1];
            String pinyin = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
            String definition = line.substring(line.indexOf("/")+1, line.length()-1);
            definition = definition.substring(0, (definition.contains("/")) ? definition.indexOf("/") : definition.length());
            dict.put(simplified, new DictEntry(pinyin, definition));
            dict.put(traditional, new DictEntry(pinyin, definition));
        }
    }

    public static void setupSetsFolder() {
        try {
            String jarLocation = new File(ChineseFlashcards.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            setsFolderLocation = jarLocation.substring(0, jarLocation.lastIndexOf("\\")) + "\\sets";
            Files.createDirectories(Path.of(setsFolderLocation));
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }
}
