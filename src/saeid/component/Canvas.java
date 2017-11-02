package saeid.component;

import javax.swing.*;
import java.awt.*;

/**
 * Project "Homework 01" created by Saeid Dadkhah on 2017-11-02.
 */
public class Canvas extends JComponent {

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(new Color(158, 218, 239));
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }
}
