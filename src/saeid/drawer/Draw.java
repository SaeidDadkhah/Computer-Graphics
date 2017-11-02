package saeid.drawer;

import java.awt.*;

/**
 * Project "Homework 01" created by Saeid Dadkhah on 2017-11-02.
 */
public class Draw {

    private Graphics graphics;

    public Draw(Graphics graphics) {
        this.graphics = graphics;
    }

    private void putPixel(int x, int y) {
        graphics.drawLine(x, y, x, y);
    }

    private void putPixel(int x, int y, boolean reverse) {
        if (reverse)
            graphics.drawLine(y, x, y, x);
        else
            graphics.drawLine(x, y, x, y);
    }

    public void midpointLine(int x0, int y0, int x1, int y1) {
        int x, y, d, dx, dy, increaseE, increaseNE;
        boolean reverse = false;

        if (Math.abs(y0 - y1) > Math.abs(x0 - x1)) {
            x = x0;
            x0 = y0;
            y0 = x;

            x = x1;
            x1 = y1;
            y1 = x;

            reverse = true;
        }

        if (x0 > x1) {
            x = x0;
            x0 = x1;
            x1 = x;

            y = y0;
            y0 = y1;
            y1 = y;
        }

        dx = x1 - x0;
        dy = y1 - y0;

        d = 2 * dy - dx;
        increaseE = 2 * dy;
        increaseNE = 2 * (dy - dx);
        if (dy < 0) {
            increaseNE = 2 * (- dy - dx);
        }

        x = x0;
        y = y0;

        putPixel(x, y, reverse);
        if (dy >= 0) {
            while (x < x1) {
                if (d <= 0) {
                    d += increaseE;
                } else {
                    d += increaseNE;
                    y++;
                }
                x++;
                putPixel(x, y, reverse);
            }
        } else {
            while (x < x1) {
                if (d <= 0) {
                    d -= increaseE;
                } else {
                    d += increaseNE;
                    y--;
                }
                x++;
                putPixel(x, y, reverse);
            }
        }
    }

    public void circle(int x, int y, int radius) {
        graphics.drawLine(x, y, x + radius, y + radius);
    }

    public void ellipse(int x1, int y1, int x2, int y2, int r) {
        graphics.drawLine(x1, y1, x2 + r, y2 + r);
    }

}
