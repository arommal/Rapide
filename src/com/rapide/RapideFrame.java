package com.rapide;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.*;

public class RapideFrame extends BaseListener {

	protected String[] extendedNote = {"C","C#","D","D#","E","F","F#","G","G#","A","A#","B","C'"};
	public ArrayList<Integer> note;
	JButton[] key = new JButton[14];
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
		JLayeredPane layer =  new JLayeredPane();
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("bg1.jpg"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		Image bg = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		ImagePanel bg_panel = new ImagePanel(bg, width, height);
		layer.add(bg_panel, -1, -1);
		
		rapideFrame.add(layer);
		
        int keyIndex = 0;
        
        for (int i = 0; i < 8; i++) {
            key[keyIndex] = new JButton(extendedNote[keyIndex]);
            key[keyIndex].addActionListener(this);
            key[keyIndex].setBackground(Color.white);
            key[keyIndex].setLocation((160+i*60), height-(height-125));
            key[keyIndex].setSize(60, 250);
        	layer.add(key[keyIndex], 0, -1);
        	keyIndex++;
        	
            if(i%7 != 2 && i%7 != 6 && keyIndex < 13) {
            	key[keyIndex] = new JButton(extendedNote[keyIndex]);
            	key[keyIndex].addActionListener(this);
            	key[keyIndex].setBackground(Color.black);
            	key[keyIndex].setLocation(200+(i*60), height-(height-125));
            	key[keyIndex].setSize(40, 140);
            	layer.add(key[keyIndex], 1, -1);
            	keyIndex++;
            }
        }
        
        rapideFrame.setTitle("Rapide");
        rapideFrame.setSize(width, height);
        rapideFrame.setLocationRelativeTo(null);
        rapideFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        rapideFrame.setResizable(false);
        rapideFrame.setVisible(true);
        
        engine = new Engine(this);
        engine.logicPlay(key);
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
            k++;
        }
        
        if(k%5==0) {
        	listening.setActive(false);
        	m = 0;
        	k = 0;
        }
	}

	public void paintComponent(Graphics g) {
		Image img = Toolkit.getDefaultToolkit().getImage("bg1.jpg");
		g.drawImage(img, 0, 0, null);
	}
	private void addNote(int key){
		note.add(key);
	}
	
	public ArrayList<Integer> getList(){
		return note;
	}

}