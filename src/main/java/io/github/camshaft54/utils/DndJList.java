package io.github.camshaft54.utils;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;

// Adapted from https://stackoverflow.com/questions/3804361/how-to-enable-drag-and-drop-inside-jlist
public class DndJList extends JList<String> {
    DefaultListModel<String> myListModel;
    List<String> items;

    /**
     * Initializes the list with a StringListModel and adds the mouseAdapter
     * @param items the items to put it in the list
     */
    public DndJList(List<String> items) {
        this.items = items;
        myListModel = createStringListModel();
        setModel(myListModel);
        setFont(new Font("Arial", Font.PLAIN, 20));
        MouseAdaptor myMouseAdaptor = new MouseAdaptor();
        addMouseListener(myMouseAdaptor);
        addMouseMotionListener(myMouseAdaptor);
    }

    /**
     * This inner class listens for when the mouse is pressed and moves the item the mouse started at to the point where
     * it is dragged to.
     */
    private class MouseAdaptor extends MouseInputAdapter {
        private boolean mouseDragging = false;
        private int dragSourceIndex;

        // Triggered at start of drag
        @Override
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                dragSourceIndex = getSelectedIndex();
                mouseDragging = true;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            mouseDragging = false;
        }

        // Triggered at end of drag
        @Override
        public void mouseDragged(MouseEvent e) {
            if (mouseDragging) {
                int currentIndex = locationToIndex(e.getPoint());
                if (currentIndex != dragSourceIndex) {
                    int dragTargetIndex = getSelectedIndex();
                    String dragElement = myListModel.get(dragSourceIndex);
                    myListModel.remove(dragSourceIndex);
                    myListModel.add(dragTargetIndex, dragElement);
                    dragSourceIndex = currentIndex;
                }
            }
        }
    }

    /**
     * Returns a ListModel with the items the user created the DndJList with
     * @return a DefaultListModel<String>
     */
    private DefaultListModel<String> createStringListModel() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addAll(items);
        return listModel;
    }
}