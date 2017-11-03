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

    @SuppressWarnings("SuspiciousNameCombination")
    private void putPixel(int x, int y, boolean reverse) {
        if (reverse)
            graphics.drawLine(y, x, y, x);
        else
            graphics.drawLine(x, y, x, y);
    }

    @SuppressWarnings("SuspiciousNameCombination")
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
            increaseNE = 2 * (-dy - dx);
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
        int tmpX, tmpY, err;
        tmpX = radius;
        tmpY = 0;
        err = 0;

        while (tmpX >= tmpY) {
            putPixel(x + tmpX, y + tmpY);
            putPixel(x + tmpY, y + tmpX);
            putPixel(x - tmpY, y + tmpX);
            putPixel(x - tmpX, y + tmpY);
            putPixel(x - tmpX, y - tmpY);
            putPixel(x - tmpY, y - tmpX);
            putPixel(x + tmpY, y - tmpX);
            putPixel(x + tmpX, y - tmpY);

            if (err <= 0) {
                tmpY += 1;
                err += 2 * tmpY + 1;
            } else {
                tmpX -= 1;
                err -= 2 * tmpX + 1;
            }
        }
    }

    public void ellipse(int xc, int yc, int rx, int ry) {
        int x, y, p;
        x = 0;
        y = ry;
        p = (ry * ry) - (rx * rx * ry) + ((rx * rx) / 4);
        while ((2 * x * ry * ry) < (2 * y * rx * rx)) {
            putPixel(xc + x, yc - y);
            putPixel(xc - x, yc + y);
            putPixel(xc + x, yc + y);
            putPixel(xc - x, yc - y);

            if (p < 0) {
                x = x + 1;
                p = p + (2 * ry * ry * x) + (ry * ry);
            } else {
                x = x + 1;
                y = y - 1;
                p = p + (2 * ry * ry * x + ry * ry) - (2 * rx * rx * y);
            }
        }
        p = (int) ((x + 0.5) * (x + 0.5) * ry * ry + (y - 1) * (y - 1) * rx * rx - rx * rx * ry * ry);

        while (y >= 0) {
            putPixel(xc + x, yc - y);
            putPixel(xc - x, yc + y);
            putPixel(xc + x, yc + y);
            putPixel(xc - x, yc - y);

            if (p > 0) {
                y = y - 1;
                p = p - (2 * rx * rx * y) + (rx * rx);

            } else {
                y = y - 1;
                x = x + 1;
                p = p + (2 * ry * ry * x) - (2 * rx * rx * y) - (rx * rx);
            }
        }
    }

}
