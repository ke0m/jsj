package weq;

/****************************************************************************
Copyright 2009, Colorado School of Mines and others.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
****************************************************************************/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import edu.mines.jtk.awt.ColorMap;
import edu.mines.jtk.mosaic.*;

/**
 * Demos {@link edu.mines.jtk.mosaic.ContoursView}
 * @author Chris Engelsma, Colorado School of Mines
 * @version 2009.07.17
 */
@SuppressWarnings({"rawtypes","unchecked"}) 
// Use of JComboBox below is OK in Java 6, 
// but must be JComboBox<E> in Java 7
public class ContoursViewDemo {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        go();
      }
    });
  }

  public static void go() {

    //Create array
    float[][] array = new float[50][50];
    for (int i=0; i<array.length; ++i)
      for (int j=0; j<array[0].length; ++j)
        array[i][j] = (float)(i+j)*(i-j);

    // Create build panels
    PlotPanel panel1 = new PlotPanel();
    PlotPanel panel2 = new PlotPanel();
    JPanel topLeftPanel = new JPanel();
    JPanel topRightPanel = new JPanel();
    topLeftPanel.setLayout(new GridLayout(2,2));
    topRightPanel.setLayout(new GridLayout(2,4));

    //Create and set views
    final ContoursView cv1 = panel1.addContours(array);
    cv1.setLineStyleNegative(ContoursView.Line.DASH);
    cv1.setLineColor(Color.BLACK);
    cv1.setContours(50);

    final PixelsView pv2 = panel2.addPixels(array);
    final ContoursView cv2 = panel2.addContours(array);

    String[] cc = {"JET", "HUE", "GRAY", "SOLID"};
    String[] pc = {"JET", "HUE", "GRAY"};
    String[] yesno = {"Yes", "No"};
    String[] fc    = {"+-","++","--"};

    /* Interactive components */
    final JComboBox combocc = new JComboBox(cc);
    final JComboBox combopc = new JComboBox(pc);
    final JTextField numcontours = new JTextField(4);
    final JComboBox comboneg = new JComboBox(yesno);
    final JComboBox function = new JComboBox(fc);

    combocc.setSelectedItem("JET");
    combopc.setSelectedItem("GRAY");
    combopc.setSelectedItem("+-");

    function.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent action){
        if (function.getSelectedItem()=="+-") {
          float[][] array = new float[50][50];
          for (int i=0; i<array.length; ++i)
            for (int j=0; j<array[0].length; ++j)
              array[i][j] = (float)(i+j)*(i-j);
          pv2.set(array);
        } else if(function.getSelectedItem()=="++") {
          float[][] array = new float[50][50];
          for (int i=0; i<array.length; ++i)
            for (int j=0; j<array[0].length; ++j)
              array[i][j] = (float)(i+j)*(i+j);
          pv2.set(array);
        }
      }
    });
    combocc.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent action) {
        if (combocc.getSelectedItem()=="JET") {
          cv2.setColorModel(ColorMap.JET);
        } else if (combocc.getSelectedItem()=="HUE") {
          cv2.setColorModel(ColorMap.HUE);
        } else if (combocc.getSelectedItem()=="GRAY") {
          cv2.setColorModel(ColorMap.GRAY);
        } else {
          cv2.setLineColor(Color.BLACK);
        }
      }
    });

    combopc.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent action) {
        if (combopc.getSelectedItem()=="JET") {
          pv2.setColorModel(ColorMap.JET);
        } else if (combopc.getSelectedItem()=="HUE") {
          pv2.setColorModel(ColorMap.HUE);
        } else if (combopc.getSelectedItem()=="GRAY") {
          pv2.setColorModel(ColorMap.GRAY);
        }
      }
    });

    comboneg.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent action) {
        if(comboneg.getSelectedItem()=="Yes") {
          cv1.setLineStyleNegative(ContoursView.Line.DASH);
        } else {
          cv1.setLineStyleNegative(ContoursView.Line.SOLID);
        }
      }
    });

    numcontours.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent event) {
        int value = Integer.parseInt(numcontours.getText());
        cv1.setContours(value);
        numcontours.setText(Integer.toString(value));
      }
    });

    JSplitPane jsp = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,topLeftPanel,topRightPanel);
    jsp.setResizeWeight(0.45);

    topRightPanel.add(new Label("Contours colors:",Label.RIGHT));
    topRightPanel.add(combocc);
    topRightPanel.add(new Label("Pixels colors:",Label.RIGHT));
    topRightPanel.add(combopc);
    topRightPanel.add(new Label("Function:",Label.RIGHT));
    topRightPanel.add(function);

    topLeftPanel.add(new Label("Desired number of contours:",Label.RIGHT));
    topLeftPanel.add(numcontours);
    topLeftPanel.add(new Label("Show negatives:",Label.RIGHT));
    topLeftPanel.add(comboneg);

    panel2.addColorBar();
    PlotFrame frame = new PlotFrame(panel1,panel2,PlotFrame.Split.HORIZONTAL);
    frame.setTitle("Example Contour Plot");
    frame.setDefaultCloseOperation(PlotFrame.EXIT_ON_CLOSE);
    frame.add(jsp,BorderLayout.SOUTH);
    frame.pack();
    frame.setVisible(true);
  }
}