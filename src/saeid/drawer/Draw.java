package saeid.drawer;

import java.awt.*;
import java.util.ArrayList;

/**
 * Project "Homework 01" created by Saeid Dadkhah on 2017-11-02.
 */
public class Draw {

    /*
    |_ midpoint line
    |_ circle
    |_ ellipse
    |_ fill
    |_ line clipping
      |_ Cohen-Sutherland
      |_ Liang Barsky

     */
    private Graphics graphics;

    public Draw(Graphics graphics) {
        this.graphics = graphics;
    }

    private void putPixel(int x, int y) {
        putPixel(x, y, Color.BLACK);
    }

    private void putPixel(int x, int y, Color color) {
        graphics.setColor(color);
        graphics.drawLine(x, y, x, y);
    }

//    private void putPixel(int x, int y, boolean reverse) {
//        putPixel(x, y, reverse, Color.BLACK);
//    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void putPixel(int x, int y, boolean reverse, Color color) {
        graphics.setColor(color);
        if (reverse)
            graphics.drawLine(y, x, y, x);
        else
            graphics.drawLine(x, y, x, y);
    }

    public void midpointLine(int x0, int y0, int x1, int y1) {
        midpointLine(x0, y0, x1, y1, Color.BLACK);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void midpointLine(int x0, int y0, int x1, int y1, Color color) {
        int x, y, d, dx, dy, increaseE, increaseNE;
        boolean reverse = false;

        if (x0 == 0 && y0 == 0 && x1 == 0 && y1 == 0) {
            x0 = 100;
            y0 = 300;
            x1 = 500;
            y1 = 300;
        }

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

        putPixel(x, y, reverse, color);
        if (dy >= 0) {
            while (x < x1) {
                if (d <= 0) {
                    d += increaseE;
                } else {
                    d += increaseNE;
                    y++;
                }
                x++;
                putPixel(x, y, reverse, color);
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
                putPixel(x, y, reverse, color);
            }
        }
    }

    public void circle(int x, int y, int radius) {
        if (x == 0 && y == 0 && radius == 0) {
            x = 300;
            y = 275;
            radius = 75;
        }

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
        if (xc == 0 && yc == 0 && rx == 0 && ry == 0) {
            xc = 300;
            yc = 300;
            rx = 150;
            ry = 50;
        }

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
            x = new int[]{300, 310, 360, 320, 340, 300, 260, 280, 240, 290};
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

    private static final int INSIDE = 0;
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private static final int BOTTOM = 4;
    private static final int TOP = 8;

    private int xMin;
    private int xMax;
    private int yMin;
    private int yMax;

    public void cohenSutherlandLineClipping(int xMin, int yMin, int xMax, int yMax, int x[], int y[]) {
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;

        if (x.length == 0) {
            this.xMin = 100;
            this.yMin = 100;
            this.xMax = 200;
            this.yMax = 200;

            ArrayList<Integer> xAL = new ArrayList<>();
            ArrayList<Integer> yAL = new ArrayList<>();
            xAL.add(70);
            yAL.add(120);
            xAL.add(120);
            yAL.add(70);

            xAL.add(80);
            yAL.add(140);
            xAL.add(140);
            yAL.add(80);

            xAL.add(230);
            yAL.add(120);
            xAL.add(170);
            yAL.add(70);

            xAL.add(220);
            yAL.add(140);
            xAL.add(160);
            yAL.add(80);

            xAL.add(50);
            yAL.add(150);
            xAL.add(250);
            yAL.add(150);

            xAL.add(150);
            yAL.add(50);
            xAL.add(150);
            yAL.add(250);

            xAL.add(190);
            yAL.add(130);
            xAL.add(130);
            yAL.add(190);

            x = new int[xAL.size()];
            y = new int[yAL.size()];
            for (int i = 0; i < xAL.size(); i++) {
                x[i] = xAL.get(i);
                y[i] = yAL.get(i);
            }
        }

        midpointLine(this.xMin, this.yMin, this.xMax, this.yMin, Color.BLUE);
        midpointLine(this.xMin, this.yMin, this.xMin, this.yMax, Color.BLUE);
        midpointLine(this.xMax, this.yMax, this.xMax, this.yMin, Color.BLUE);
        midpointLine(this.xMax, this.yMax, this.xMin, this.yMax, Color.BLUE);

        for (int i = 0; i < x.length; i += 2) {
            midpointLine(x[i], y[i], x[i + 1], y[i + 1]);

            Point point = cohenSutherlandLineClipper(x[i], y[i], x[i + 1], y[i + 1]);

            if (point != null)
                midpointLine(point.getX0(), point.getY0(), point.getX1(), point.getY1(), Color.RED);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private Point cohenSutherlandLineClipper(int x0, int y0, int x1, int y1) {
        int outCode0 = computeOutCode(x0, y0);
        int outCode1 = computeOutCode(x1, y1);
        boolean accept = false;

        while (true) {
            if ((outCode0 | outCode1) == 0) {
                accept = true;
                break;
            } else if ((outCode0 & outCode1) != 0) {
                break;
            } else {
                int outCode;
                if (outCode0 != 0)
                    outCode = outCode0;
                else
                    outCode = outCode1;

                int x;
                int y;
                if ((outCode & TOP) != 0) {
                    x = x0 + (yMax - y0) * (x1 - x0) / (y1 - y0);
                    y = yMax;
                } else if ((outCode & BOTTOM) != 0) {
                    x = x0 + (yMin - y0) * (x1 - x0) / (y1 - y0);
                    y = yMin;
                } else if ((outCode & RIGHT) != 0) {
                    x = xMax;
                    y = y0 + (xMax - x0) * (y1 - y0) / (x1 - x0);
                } else { // if ((outCode & LEFT) != 0)
                    x = xMin;
                    y = y0 + (xMin - x0) * (y1 - y0) / (x1 - x0);
                }

                if (outCode == outCode0) {
                    x0 = x;
                    y0 = y;
                    outCode0 = computeOutCode(x0, y0);
                } else {
                    x1 = x;
                    y1 = y;
                    outCode1 = computeOutCode(x1, y1);
                }
            }
        }

        if (accept)
            return new Point(x0, y0, x1, y1);
        else
            return null;
    }

    private int computeOutCode(int x, int y) {
        int code = INSIDE;
        if (x < xMin)
            code |= LEFT;
        else if (x > xMax)
            code |= RIGHT;
        if (y < yMin)
            code |= BOTTOM;
        else if (y > yMax)
            code |= TOP;
        return code;
    }

    public void liangBarskyLineClipping(int xMin, int yMin, int xMax, int yMax, int x[], int y[]) {
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;

        if (x.length == 0) {
            this.xMin = 400;
            this.yMin = 100;
            this.xMax = 500;
            this.yMax = 200;

            ArrayList<Integer> xAL = new ArrayList<>();
            ArrayList<Integer> yAL = new ArrayList<>();
            xAL.add(370);
            yAL.add(120);
            xAL.add(420);
            yAL.add(70);

            xAL.add(380);
            yAL.add(140);
            xAL.add(440);
            yAL.add(80);

            xAL.add(530);
            yAL.add(120);
            xAL.add(470);
            yAL.add(70);

            xAL.add(520);
            yAL.add(140);
            xAL.add(460);
            yAL.add(80);

            xAL.add(350);
            yAL.add(150);
            xAL.add(550);
            yAL.add(150);

            xAL.add(450);
            yAL.add(50);
            xAL.add(450);
            yAL.add(250);

            xAL.add(490);
            yAL.add(130);
            xAL.add(430);
            yAL.add(190);

            x = new int[xAL.size()];
            y = new int[yAL.size()];
            for (int i = 0; i < xAL.size(); i++) {
                x[i] = xAL.get(i);
                y[i] = yAL.get(i);
            }
        }

        midpointLine(this.xMin, this.yMin, this.xMax, this.yMin, Color.BLUE);
        midpointLine(this.xMin, this.yMin, this.xMin, this.yMax, Color.BLUE);
        midpointLine(this.xMax, this.yMax, this.xMax, this.yMin, Color.BLUE);
        midpointLine(this.xMax, this.yMax, this.xMin, this.yMax, Color.BLUE);

        for (int i = 0; i < x.length; i += 2) {
            midpointLine(x[i], y[i], x[i + 1], y[i + 1]);

            Point point = liangBarskyLineClipper(x[i], y[i], x[i + 1], y[i + 1]);

            if (point != null)
                midpointLine(point.getX0(), point.getY0(), point.getX1(), point.getY1(), Color.RED);
        }
    }

    private Point liangBarskyLineClipper(int x0, int y0, int x1, int y1) {
        double u1 = 0;
        double u2 = 1;
        int dx = x1 - x0;
        int dy = y1 - y0;
        int p[] = {-dx, dx, -dy, dy};
        int q[] = {x0 - xMin, xMax - x0, y0 - yMin, yMax - y0};

        for (int i = 0; i < 4; i++) {
            if (p[i] == 0) {
                if (q[i] < 0)
                    return null;
            } else {
                double u = (double) q[i] / p[i];
                if (p[i] < 0) {
                    u1 = Math.max(u, u1);
                } else {
                    u2 = Math.min(u, u2);
                }
            }
        }
        if (u1 > u2)
            return null;

        return new Point(
                (int) (x0 + u1 * dx),
                (int) (y0 + u1 * dy),
                (int) (x0 + u2 * dx),
                (int) (y0 + u2 * dy));
    }

}

// edge table element: used for scan line filling algorithm
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

class Point {

    private int x0;
    private int y0;
    private int x1;
    private int y1;

    Point(int x0, int y0, int x1, int y1) {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
    }

    int getX0() {
        return x0;
    }

    int getY0() {
        return y0;
    }

    int getX1() {
        return x1;
    }

    int getY1() {
        return y1;
    }
}
