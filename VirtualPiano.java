package com;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class VirtualPiano {

public static void main(String[] args){
    JPanel panel = new JPanel(null);
    JFrame mainFrame = new JFrame();
    PianoLayout pianoLayout = new PianoLayout();
    mainFrame.add(panel);
    panel.add(pianoLayout);
    mainFrame.setSize(500,500);
    mainFrame.setVisible(true);
	}
}