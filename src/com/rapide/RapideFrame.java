package com.rapide;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.*;

public class RapideFrame extends JFrame implements ActionListener{

	protected String[] extendedNote = {"C","C#","D","D#","E","F","F#","G","G#","A","A#","B","CC"};
	int[] Note = new int[5];
	Engine engine;
	int k = 0;
	
	public void setGUI(int width, int height) {
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("bg1.jpg"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		Image bg = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		ImageIcon bg_image = new ImageIcon(bg);
		
        setTitle("Rapide");
        setSize(width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setContentPane(new JLabel(bg_image));
        setLayout(new FlowLayout());
        
        JPanel keyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        
        JLayeredPane layer =  new JLayeredPane();
        JButton[] key = new JButton[13];
        		
       
        for (int i = 0; i < 13; i++) {
            key[i] = new JButton(extendedNote[i]);
            key[i].addActionListener(this);
            
            switch(i) {
	            case 0: case 2: case 4: case 5: case 7: case 9: case 11: case 12:
	            	key[i].setBackground(Color.white);
	            	key[i].setLocation(i*40, width/2);
	            	key[i].setPreferredSize(new Dimension(40, 150));
	            	layer.add(key[i], 0);
	            	break;
	            case 1: case 3: case 6: case 8: case 10:
	            	key[i].setBackground(Color.black);
	            	key[i].setLocation(25 + i*40, width/2);
	            	key[i].setPreferredSize(new Dimension(30, 90));
	            	layer.add(key[i], 1);
	            	break;
            }
            keyPanel.add(key[i]);
        }
        
        add(keyPanel);
        
        engine = new Engine();
        setResizable(false);
        setVisible(true);
        engine.logicPlay();
    }
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		int finalNote = 0;
//		int[] arr = new int[5];
		
        Object source = e.getSource();
        if (source instanceof JButton) {
            JButton but = (JButton) source;
            System.out.print(but.getText());

            finalNote = (5*12) + Arrays.asList(extendedNote).indexOf(but.getText());
//            finalNote = (5 * 12) + extendedNote.indexOf(but.getText());

            System.out.print(" (" + finalNote + ")\n");
        }
        
        engine.playNote(finalNote);
//        setNote(arr);
//        System.out.println("setnote");
	}

	public void setNote(int[] note) {
		Note = note;
		setK();
		System.out.println("suda di set");
	}
	
	public int[] getNote() {	
		return Note;
	}
	
	public void setK() {
		k = k+1;
		System.out.println("Masuk setk");
	}
	public int getK() {
		System.out.println("masuk getk");
		return k;
	}
}
