# APCSChineseFlashcards
A Java Swing flashcard application (think Quizlet) but tailored for Chinese.

### Feature Overview
- Displays Flashcards with Pinyin, Chinese, and English
- Term and definition can be any two of the three options
- Ability to star cards and view the starred cards separately
- Stores flashcards persistently in yaml files in an easy-to-read format
- Keyboard shortcuts for common commands (Next: Right Arrow, Previous: Left Arrow, etc.)
- GUI flashcard creator/editor
  - Built-in translator and pinyin autofill
- Easily remove stars from sets

### How it works
  - There are many parts of the Chinese Flashcard System (CFS), but here is a basic overview
  - The window that contains the majority of the program is the MainWindow, which uses a CardLayout to switch between the 
    different feature in the CFS.
  - Here is a list of each panel and an explanation about each one:
    - `EditorPanel`: Uses a BorderLayout with a BoxLayout JScrollPane in the center. The user can press a button to add
      new cards. Each card is a BoxLayout with JTextFields for each language type. Each card also has the option to
      autofill the Pinyin and English from the Chinese using a dictionary that is parsed when the program is run. When
      the save button is pressed, a JOptionPane is presented to the user where they can name the set. This button will
      also save the set as a yaml file in the sets folder.
    - `FlashcardsViewer`: This panel uses a BorderLayout with a CardLayout in the center. Inside the CardLayout are the
      cards in the set specified by the user on the WelcomePanel. The user can click the card in the center, use the
      buttons to reset the set, go to the previous or next card, view what number card they are on in the deck, 
      star the current card, or change the term and definition language. The user can also use the following keyboard
      shortcuts to trigger the aforementioned actions: S: Add/Remove Star, Up/Down Arrow: Flip Card, Left Arrow: Previous Card,
      Right Arrow: Next Card. The Keyboard shortcuts are registered using the InputMap and ActionMap, allowing them to be
      processed in an Action.
    - `StarFlashcardsViewer`: This panel is the same as FlashcardsViewer with some additions. It is specifically for displaying
      the starred flashcards in a set, therefore it says what the card is starred for (e.g. Pinyin -> English). It also
      has a button to open the SetSettingsPopup, which can be used to remove stars from flashcards easily.
    - `ImportPanel`: This panel uses a BorderLayout and BoxLayout that contains a JTextArea and several JTextFields for
      specifying the delimiters between the Chinese, English, and Pinyin. To specify the order of terms, a drag and drop
      JList, called DndJList, is used. This component uses a MouseInputAdapter to detect when the user is trying to move
      an item in the list and relocates the item when the drop it into place.
    - `WelcomePanel`: When the program first starts the user will be greeted with this panel. It uses a BoxLayout and JButtons
      for some aforementioned JPanels.
  - The sets are stored as YAML files that contain the name, and a list of Card objects. The Card object has the Chinese,
    English, and Pinyin for each card along with the stars the user has created. When the program is started SnakeYAML,
    a YAML parsing library, is used to parse each YAML file in the sets folder and turn them into a Set object. These
    sets are stored in an ArrayList and all changes made to the sets are reflected in this list. When the program is closed
    the sets are saved with SnakeYAML.
    
### How to use it
  - To get started, import an existing set from another app, such as Quizlet, using the import panel. Simply paste the
    set and specify the separator and order of the Chinese, Pinyin, and English. When you are done, click save. Another
    option is to use the set creator. Click the new button on the main menu and enter the Chinese, Pinyin, and English.
    Finally, click save and name the set. Your sets will be saved in the "sets" folder when you close the program.
  - You can now view your flashcards in the viewer. CFS allows you to see cards that you have starred only, or all of
    your cards.
    
### Other Files
  - `Flashcard`: Displayed in the inner CardLayout of the FlashcardViewer. Contains methods for removing, adding, or checking
    if the card is starred.
  - `Card`: Stores the basic information for a card in the set. Contains Chinese, English, and Pinyin translations for the 
    card and a list of stars.
  - `DndJList`: As previously mentioned, DndJList is a drag and drop JList. It is initialized with the items in the list
    and acts like a regular JList because it inherits JList.
  - `SetSettingsPopup`: Extends JFrame. Displays buttons for removing stars in a set.
  - `ChineseFlashcards`: This is the file that needs to be run to start the program. It parses the dictionary file and 
    all sets in the "sets" folder.
  - `CFS.png`: Icon image for the MainWindow and SetSettingsPopup.
  - `cedict_ts.u8`: Chinese to English Dictionary file with Pinyin. Used for autofilling the English and Pinyin when
    creating a new set.
    
### Resources I Used
  - [Drag and Drop JList Stack Overflow Post](https://stackoverflow.com/questions/3804361/how-to-enable-drag-and-drop-inside-jlist)
  - [The Java™ Tutorials](https://docs.oracle.com/javase/tutorial/uiswing/TOC.html)
  - [JDK 15 API Documentation](https://docs.oracle.com/en/java/javase/15/docs/api/index.html)
  - [SnakeYAML Documentation](https://bitbucket.org/asomov/snakeyaml/wiki/Documentation)
  - [CC-CEDICT Chinese-English Dictionary](https://cc-cedict.org/wiki/)

### Notes about Running CFS
CFS was created on Windows 10 and is uses Maven as its build tool. Cross-compatibility with MacOS is unknown. An executable
JAR file will be available at some point.