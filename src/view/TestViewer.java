package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import edu.mines.jtk.awt.ColorMap;
import edu.mines.jtk.mosaic.*;
import edu.mines.jtk.util.*;

import static edu.mines.jtk.util.ArrayMath.*;

import weq.TestProp;

public class TestViewer {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        float[][][] array = new float[3][50][50];
        int total = array.length;
        int ni = array[0].length;
        int nj = array[0][0].length;
        for (int k = 0; k < total; ++k) {
          for (int j = 0; j < nj; ++j) {
            for (int i = 0; i < ni; ++i) {
              if (k == 0) {
                array[k][j][i] = (float) (i + j) * (i - j);
              } else if (k == 1) {
                array[k][j][i] = (float) (i + j) * (i + j);
              } else if (k == 2) {
                array[k][j][i] = (float) (i - j) * (i - j);
              }
            }
          }
        }
        TestViewer tst = new TestViewer();
        tst.go(array);
      }
    });
  }

  public void go(float[][][] array) {

    final int total = array.length;
    final int ni = array[0].length;
    final int nj = array[0][0].length;
    final int[] rate = new int[1];
    rate[0] = 100;
    // Build Panels
    PlotPanel panel = new PlotPanel();
    JPanel bPanel = new JPanel();
    bPanel.setLayout(new GridLayout(1, 2));

    // Create and set view
    final float[][] slice = zerofloat(ni, nj);
    final SimpleFloat3 s3 = new SimpleFloat3(array);
    s3.get12(ni, nj, 0, 0, 0, slice);
    final PixelsView pv = panel.addPixels(slice);
    pv.setColorModel(ColorMap.JET);
    pv.setClips(-2000, 8000); // TODO: will need to pass these as arguments
    panel.addColorBar();

    String[] sp = { "50", "100", "150", "200", "250", "300", "350" };
    // Interactive components
    final JComboBox fpscc = new JComboBox(sp);
    final JToggleButton b = new JToggleButton("Start/Stop");

    fpscc.setSelectedItem("100");

    // Action listeners
    ActionListener animate = new ActionListener() {
      private int index = 0;

      public void actionPerformed(ActionEvent e) {
        if (index < total - 1) {
          index++;
        } else {
          index = 0;
        }
        s3.get12(ni, nj, 0, 0, index, slice);
        pv.setClips(-2000, 8000);
        pv.set(slice);
      }
    };
    final Timer timer = new Timer(rate[0], animate);

    ActionListener startStop = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (b.isSelected()) {
          timer.setDelay(rate[0]);
          timer.start();
        } else {
          timer.stop();
        }
      }
    };

    fpscc.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent action) {
        rate[0] = Integer.parseInt((String) fpscc.getSelectedItem());
      }
    });

    b.addActionListener(startStop);
    bPanel.add(b);
    bPanel.add(fpscc, BorderLayout.SOUTH);
    PlotFrame frame = new PlotFrame(panel);
    frame.add(bPanel, BorderLayout.SOUTH);
    frame.setVisible(true);
  }
}
