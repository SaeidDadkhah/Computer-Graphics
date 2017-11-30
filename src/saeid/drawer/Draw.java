package saeid.drawer;

import java.awt.*;
import java.util.ArrayList;

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

    public void fill(int[] x, int[] y, Dimension canvasSize) {
        // check for default
        if (x.length == 0) {
            x = new int[]{130, 140, 190, 150, 170, 130, 90, 110, 70, 120};
            y = new int[]{20, 50, 50, 70, 100, 80, 100, 70, 50, 50};
        }

        // build edge table
        ETElement[] etElements = new ETElement[(int) canvasSize.getHeight()];
        for (int i = 0; i < x.length; i++) {
            int j = (i + 1) % x.length;
            int xMin;
            int yMin;
            int yMax;
            if (y[i] < y[j]) {
                xMin = x[i];
                yMin = y[i];
                yMax = y[j];
            } else {
                xMin = x[j];
                yMin = y[j];
                yMax = y[i];
            }
            if (y[i] == y[j])
                continue;

            if (etElements[yMin] == null) {
                etElements[yMin] = new ETElement(yMax, xMin, ((double) x[i] - x[j]) / (y[i] - y[j]));
            } else {
                ETElement current = etElements[yMin];
                while (current.getNextETElement() != null &&
                        current.getNextETElement().getYMax() < yMax)
                    current = current.getNextETElement();
                ETElement newEtElement = new ETElement(
                        yMax,
                        xMin,
                        ((double) x[i] - x[j]) / (y[i] - y[j]),
                        current.getNextETElement());
                current.setNextETElement(newEtElement);
            }
        }

        ArrayList<ETElement> aet = new ArrayList<>();
        for (int row = 0; row < canvasSize.getHeight(); row++) {
            // add new edges
            for (ETElement current = etElements[row]; current != null; current = current.getNextETElement())
                aet.add(current);

            for (int i = 0; i < aet.size(); i++)
                if (aet.get(i).getYMax() <= row) {
                    aet.remove(aet.get(i));
                    i--;
                }

            if (aet.isEmpty())
                continue;

            // sort
            for (int i = 0; i < aet.size() - 1; i++) {
                if (!(aet.get(i).getX() < aet.get(i + 1).getX())) {
                    ETElement temp = aet.get(i + 1);
                    aet.remove(i + 1);
                    int j = 0;
                    while (temp.getX() > aet.get(j).getX())
                        j++;
                    aet.add(j, temp);
                }
            }

            // fill
            for (int i = 0; i < aet.size() - 1; i += 2)
                for (int currentX = aet.get(i).getX(); currentX < aet.get(i + 1).getX(); currentX++)
                    putPixel(currentX, row);

            // add slope to x
            for (ETElement anAet : aet) {
                anAet.addSlopeToX();
            }
        }
    }

}

class ETElement {

    private int yMax;
    private double x;
    private double reversedSlope;
    private ETElement nextETElement;

    ETElement(int yMax, int x, double reversedSlope, ETElement nextETElement) {
        this.yMax = yMax;
        this.x = x;
        this.reversedSlope = reversedSlope;
        this.nextETElement = nextETElement;
    }

    ETElement(int yMax, int x, double reversedSlope) {
        this(yMax, x, reversedSlope, null);
    }

    int getYMax() {
        return yMax;
    }

    int getX() {
        return (int) x;
    }

    void setNextETElement(ETElement nextETElement) {
        this.nextETElement = nextETElement;
    }

    ETElement getNextETElement() {
        return nextETElement;
    }

    void addSlopeToX() {
        x += reversedSlope;
    }

}
