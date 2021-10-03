package io.github.camshaft54.chineseflashcards;

import io.github.camshaft54.chineseflashcards.utils.Card;
import io.github.camshaft54.chineseflashcards.utils.DictEntry;
import io.github.camshaft54.chineseflashcards.utils.Set;
import io.github.camshaft54.chineseflashcards.windows.MainWindow;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.swing.*;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Scanner;

public class ChineseFlashcards {
    public static MainWindow mainWindow;
    public static HashMap<String, Set> sets;
    public static Yaml yaml;
    public static HashMap<String, DictEntry> dict;
    public static String setsFolderLocation;

    /**
     * Sets up and creates the main window
     * @param args command arguments (not applicable)
     */
    public static void main(String[] args) {
        setupSetsFolder();

        parseDictionary();

        // Creates a Yaml that can store and read a Set
        Constructor setConstructor = new Constructor(Set.class);
        TypeDescription setDescription = new TypeDescription(Set.class);
        setDescription.addPropertyParameters("cards", Card.class);
        setConstructor.addTypeDescription(setDescription);
        yaml = new Yaml(setConstructor);
        sets = new HashMap<>();

        // When the program is closed, the try statement below will try to save all the flashcard sets.
        Runtime.getRuntime().addShutdownHook(new Thread(ChineseFlashcards::saveSets));

        SwingUtilities.invokeLater(() -> {
            // Opens the main window
            mainWindow = new MainWindow();
        });
    }

    /**
     * Reads from the sets folder all of the yaml files and adds them to the sets list in this class. Called by WelcomePanel.
     */
    public static void populateSetList() {
        if (sets != null) {
            saveSets();
            sets.clear();
        }

        File folder = new File(setsFolderLocation);
        String[] files = folder.list((dir, name) -> name.endsWith(".yaml") || name.endsWith(".yml"));
        if (files != null) {
            for (String name : files) {
                try {
                    FileInputStream fs = new FileInputStream(setsFolderLocation + "\\" + name);
                    Set set = yaml.load(fs);
                    set.setFilename(name);
                    sets.put(set.getName(), set);
                    fs.close();
                } catch (IOException e) {
                    System.out.println("Error loading sets!\nExitingCFS");
                    return;
                }
            }
        }
    }

    /**
     * Parse the the CEDICT dictionary using string manipulation. Saves the dictionary entries to a hashmap of the Chinese
     * to the DictEntry with Pinyin and English.
     */
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

    /**
     * Add a new folder called sets in the same directory that the program is located, if necessary. Record the location
     * of said folder to setsFolderLocation to be used by other methods.
     */
    public static void setupSetsFolder() {
        try {
            String jarLocation = new File(ChineseFlashcards.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            setsFolderLocation = jarLocation.substring(0, jarLocation.lastIndexOf("\\")) + "\\sets";
            Files.createDirectories(Path.of(setsFolderLocation));
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveSets() {
        sets.forEach((name, set) -> {
            try {
                FileOutputStream fos = new FileOutputStream(ChineseFlashcards.setsFolderLocation + "\\" + set.getFilename());
                OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_16);
                yaml.dump(set, osw);
                fos.close();
                osw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
