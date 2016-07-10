package weq;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import edu.mines.jtk.awt.ColorMap;
import edu.mines.jtk.dsp.*;
import edu.mines.jtk.io.*;
import edu.mines.jtk.mosaic.*;
import weq.AcstcWfldFD.*;
import static edu.mines.jtk.util.ArrayMath.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteOrder;

public class TestProp {

  public static void main(String[] args) throws IOException {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        go();
      }
    });
  }
  static int total = 4;
  static int rate = 100;
  
  public static void go() {
    
    // Create array
    float[][] array = new float[50][50];
    for (int i=0; i<array.length; ++i)
      for (int j=0; j<array[0].length; ++j)
        array[i][j] = (float)(i+j)*(i-j);

    // Build Panels
    PlotPanel panel = new PlotPanel();
    JPanel bPanel = new JPanel();
    bPanel.setLayout(new GridLayout(1,2));
    
    // Create and set view
    final PixelsView pv = panel.addPixels(array);
    pv.setColorModel(ColorMap.JET);
    panel.addColorBar();
 
    //Interactive components
    final JTextField fps = new JTextField("100",4);
    final JToggleButton b = new JToggleButton("Start/Stop");

    //Action listeners
    ActionListener animate = new ActionListener() {
      private int index = 0;
      public void actionPerformed(ActionEvent e) {
        float[][] array = randfloat(50,50);
        if(index < total) {
          index++;
        }
        else {
          index=0;
        }
        pv.set(array);
      }
    };
    final Timer timer = new Timer(rate,animate);
    
    ActionListener startStop = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (b.isSelected()) {
          timer.setDelay(rate);
          timer.start();
        } else {
          timer.stop();
        }
      }
    };
    
    fps.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        rate = Integer.parseInt(fps.getText());
      }
    });
    
    b.addActionListener(startStop);
    bPanel.add(b);
    bPanel.add(fps);
    bPanel.add(new Label("Speed:",Label.RIGHT));
    PlotFrame frame = new PlotFrame(panel);
    frame.add(bPanel,BorderLayout.SOUTH);
    frame.setVisible(true);
  }
}
