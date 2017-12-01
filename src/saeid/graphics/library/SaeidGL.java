package saeid.graphics.library;

import saeid.component.Canvas;
import saeid.drawer.Draw;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Project "Homework 01" created by Saeid Dadkhah on 2017-11-02.
 */

public class SaeidGL extends JFrame {

    private static final int FP_WIDTH = 650 + 26;
    private static final int FP_HEIGHT = 600;

    private static final int INPUT_ROWS = 3;

    private static final int SELECTOR_LINE = 0;
    private static final int SELECTOR_CIRCLE = 1;
    private static final int SELECTOR_ELLIPSE = 2;
    private static final int SELECTOR_FILL = 3;
    private static final int SELECTOR_LINE_CLIPPING_COHEN_SUTHERLAND = 4;
    private static final int SELECTOR_LINE_CLIPPING_LIANG_BARSKY = 5;

    private static final String[] LABELS_LINE = new String[]{
            "X1:", "Y1:", "X2:", "Y2:"
    };
    private static final String[] LABELS_CIRCLE = new String[]{
            "X Center:", "Y Center:", "Radius:"
    };
    private static final String[] LABELS_ELLIPSE = new String[]{
            "X Center:", "Y Center:", "X Radius:", "Y Radius:"
    };
    private static final String[] LABELS_FILL = new String[]{
            "X1:", "Y1:", "X2:", "Y2:", "X3:", "Y3:", "X4:", "Y4:", "X5:", "Y5:", "X6:", "Y6:",
    };
    private static final String[] LABELS_LINE_CLIPPING_COHEN_SUTHERLAND = new String[]{
            "X Min:", "Y Min:", "X Max:", "Y Max:", "X1:", "Y1:", "X2:", "Y2:", "X3:", "Y3:", "X4:", "Y4:",
    };
    private static final String[] LABELS_LINE_CLIPPING_LIANG_BARSKY = new String[]{
            "X Min:", "Y Min:", "X Max:", "Y Max:", "X1:", "Y1:", "X2:", "Y2:", "X3:", "Y3:", "X4:", "Y4:",
    };

    private saeid.component.Canvas canvas;
    private JComboBox<String> cb_shapeSelector;
    private JButton b_draw;
    private ArrayList<JLabel> labels;
    private ArrayList<JTextField> textFields;

    public static void main(String[] args) {
        new SaeidGL();
    }

    private SaeidGL() {
        labels = new ArrayList<>();
        textFields = new ArrayList<>();
        init();
    }

    private void init() {
        Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(FP_WIDTH, FP_HEIGHT);
        setLocation((int) (ss.getWidth() - FP_WIDTH) / 2, (int) (ss.getHeight() - FP_HEIGHT) / 2);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Saeid GL");

        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        // Canvas
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 4;
        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;
        canvas = new Canvas();
        getContentPane().add(canvas, gbc);

        // Selector
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weightx = 0.25;
        gbc.weighty = 0;
        gbc.insets = new Insets(0, 3, 4, 1);
        getContentPane().add(new JLabel("Select Shape:"), gbc);

        gbc.gridx++;
        gbc.gridwidth = 2;
        gbc.weightx = 0.5;
        cb_shapeSelector = new JComboBox<>();
        cb_shapeSelector.addItem("Line");
        cb_shapeSelector.addItem("Circle");
        cb_shapeSelector.addItem("Ellipse");
        cb_shapeSelector.addItem("Fill");
        cb_shapeSelector.addItem("Line Clipping (Cohen-Sutherland)");
        cb_shapeSelector.addItem("Line Clipping (Liang-Barsky)");
        cb_shapeSelector.addActionListener(e -> {
            String[] labels;
            switch (cb_shapeSelector.getSelectedIndex()) {
                case SELECTOR_LINE:
                    labels = LABELS_LINE;
                    break;
                case SELECTOR_CIRCLE:
                    labels = LABELS_CIRCLE;
                    break;
                case SELECTOR_ELLIPSE:
                    labels = LABELS_ELLIPSE;
                    break;
                case SELECTOR_FILL:
                    labels = LABELS_FILL;
                    break;
                case SELECTOR_LINE_CLIPPING_COHEN_SUTHERLAND:
                    labels = LABELS_LINE_CLIPPING_COHEN_SUTHERLAND;
                    break;
                case SELECTOR_LINE_CLIPPING_LIANG_BARSKY:
                    labels = LABELS_LINE_CLIPPING_LIANG_BARSKY;
                    break;
                default:
                    labels = null;
            }
            if (labels != null) {
                for (int i = 0; i < labels.length; i++) {
                    SaeidGL.this.labels.get(i).setText(labels[i]);
                    SaeidGL.this.textFields.get(i).setEnabled(true);
                }
                for (int i = labels.length; i < SaeidGL.this.labels.size(); i++) {
                    SaeidGL.this.labels.get(i).setText("No Args:");
                    SaeidGL.this.textFields.get(i).setEnabled(false);
                }
            }
        });
        getContentPane().add(cb_shapeSelector, gbc);

        gbc.gridx += 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0.25;
        b_draw = new JButton("Draw");
        b_draw.addActionListener(e -> {
            Draw draw = new Draw(canvas.getGraphics());
            int[] args = new int[textFields.size()];
            for (int i = 0; i < textFields.size(); i++)
                try {
                    args[i] = Integer.parseInt(textFields.get(i).getText());
                } catch (NumberFormatException nfe) {
                    args[i] = 0;
                }
            System.out.println(canvas.getSize());
            int[][] input;
            switch (cb_shapeSelector.getSelectedIndex()) {
                case SELECTOR_LINE:
                    draw.midpointLine(args[0], args[1], args[2], args[3]);
                    break;
                case SELECTOR_CIRCLE:
                    draw.circle(args[0], args[1], args[2]);
                    break;
                case SELECTOR_ELLIPSE:
                    draw.ellipse(args[0], args[1], args[2], args[3]);
                    break;
                case SELECTOR_FILL:
                    input = getXYArray(args, 0);
                    draw.fill(input[0], input[1], canvas.getSize());
                    break;
                case SELECTOR_LINE_CLIPPING_COHEN_SUTHERLAND:
                    input = getXYArray(args, 4);
                    draw.cohenSutherlandLineClipping(args[0], args[1], args[2], args[3], input[0], input[1]);
                    break;
                case SELECTOR_LINE_CLIPPING_LIANG_BARSKY:
                    input = getXYArray(args, 4);
                    draw.liangBarskyLineClipping(args[0], args[1], args[2], args[3], input[0], input[1]);
                    break;
            }
        });
        getContentPane().add(b_draw, gbc);

        // Help label
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 4;
        gbc.weightx = 1;
        gbc.weighty = 0;
        JLabel temp = new JLabel("Clear or set all inputs to run the default test.");
        getContentPane().add(temp, gbc);


        for (int i = 0; i < INPUT_ROWS; i++) {
            // Arguments 1st Row
            gbc.gridx = 0;
            gbc.gridy++;
            gbc.gridwidth = 1;
            gbc.weightx = 0.25;
            gbc.weighty = 0;
            temp = new JLabel();
            labels.add(temp);
            getContentPane().add(temp, gbc);

            gbc.gridx++;
            temp = new JLabel();
            labels.add(temp);
            getContentPane().add(temp, gbc);

            gbc.gridx++;
            temp = new JLabel();
            labels.add(temp);
            getContentPane().add(temp, gbc);

            gbc.gridx++;
            temp = new JLabel();
            labels.add(temp);
            getContentPane().add(temp, gbc);

            gbc.gridx = 0;
            gbc.gridy++;
            JTextField temp2 = new JTextField();
            textFields.add(temp2);
            getContentPane().add(temp2, gbc);

            gbc.gridx++;
            temp2 = new JTextField();
            textFields.add(temp2);
            getContentPane().add(temp2, gbc);

            gbc.gridx++;
            temp2 = new JTextField();
            textFields.add(temp2);
            getContentPane().add(temp2, gbc);

            gbc.gridx++;
            temp2 = new JTextField();
            textFields.add(temp2);
            getContentPane().add(temp2, gbc);
        }

        for (JTextField textField : textFields)
            textField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    super.keyTyped(e);
                    if (e.getKeyChar() == '\n') {
                        b_draw.doClick();
                    }
                }
            });

        // finalize
        cb_shapeSelector.setSelectedIndex(0);
        setVisible(true);
    }

    public int[][] getXYArray(int[] args, int offset) {
        int count = 0;
        boolean exist = false;
        for (int i = 0; i < args.length; i++)
            if (args[i] != 0) {
                count = i;
                exist = true;
            }
        if (exist) {
            count -= offset;
            if (count % 2 == 1)
                count++;
            else
                count += 2;
        }
        int[][] result = new int[2][];
        result[0] = new int[count / 2];
        result[1] = new int[count / 2];
        for (int i = 0; i < count; i += 2) {
            result[0][i / 2] = args[offset + i];
            result[1][i / 2] = args[offset + i + 1];
        }
        return result;
    }

}
