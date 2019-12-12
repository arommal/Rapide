package com.rapide;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.*;

public class RapideFrame extends BaseListener {

	protected String[] extendedNote = {"C","C#","D","D#","E","F","F#","G","G#","A","A#","B","CC"};
	public ArrayList<Integer> note = new ArrayList<Integer>();
	Engine engine;
	int k, m;
	BaseListener listening;
	
	public enum STATE{
		WAIT,
		CONTINUE
	}
	
	STATE st;
	
	public void setGUI(int width, int height) {
		JFrame rapideFrame = new JFrame();
		JScrollPane rapidePiano = new JScrollPane();
		
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(new File("bg1.jpg"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		Image bg = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		ImageIcon bg_image = new ImageIcon(bg);
		
        rapideFrame.setTitle("Rapide");
        rapideFrame.setSize(width, height);
        rapideFrame.setLocationRelativeTo(null);
        rapideFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        rapideFrame.setLayout(new BorderLayout());
        rapideFrame.setContentPane(new JLabel(bg_image));
        rapideFrame.setLayout(new FlowLayout());
        
        JPanel keyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0,0));
        
        JLayeredPane layer =  new JLayeredPane();
        layer.setSize(1120,150);
        
        JButton[] key = new JButton[13];		
       
        for (int i = 0; i < 13; i++) {
            key[i] = new JButton(extendedNote[i]);
            key[i].addActionListener(this);
            
            switch(i) {
	            case 0: case 2: case 4: case 5: case 7: case 9: case 11: case 12:
	            	key[i].setBackground(Color.white);
	            	key[i].setLocation(i*40, width/2);
	            	key[i].setPreferredSize(new Dimension(40, 150));
	            	break;
	            case 1: case 3: case 6: case 8: case 10:
	            	key[i].setBackground(Color.black);
	            	key[i].setLocation(25 + i*40, width/2);
	            	key[i].setPreferredSize(new Dimension(30, 90));
	            	break;
            }
            keyPanel.add(key[i]);
        }
        
        rapidePiano.setViewportView(layer);
        rapideFrame.add(keyPanel);
        
        engine = new Engine(this);
        rapideFrame.setResizable(false);
        rapideFrame.setVisible(true);
        
        engine.logicPlay();
    }
	
	protected void doPerformAction(ActionEvent e) {
		// TODO Auto-generated method stub
		int finalNote = 0;
		
        Object source = e.getSource();
        if (source instanceof JButton) {
            JButton but = (JButton) source;
            System.out.print(but.getText());

            finalNote = (5*12) + Arrays.asList(extendedNote).indexOf(but.getText());

            System.out.print(" (" + finalNote + ")\n");
            
            engine.playNote(finalNote);
            addNote(finalNote);
            System.out.println(note.get(0));
            k++;
        }
        
        if(k%5==0) {
        	listening.setActive(false);
        	m = 0;
        	k = 0;
        }
	}

	private void addNote(int key){
		note.add(key);
	}
	
	public ArrayList<Integer> getList(){
		return note;
	}
}