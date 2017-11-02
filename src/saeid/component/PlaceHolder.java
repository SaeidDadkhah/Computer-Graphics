package saeid.component;

import javax.swing.*;
import java.awt.*;

/**
 * Project "Homework 01" created by Saeid Dadkhah on 2017-11-02.
 */

public class PlaceHolder extends JButton {

    private Window parent;

    public PlaceHolder(Window parent) {
        this.parent = parent;
        this.setBackground(Color.RED);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        parent.repaint();
    }
}
