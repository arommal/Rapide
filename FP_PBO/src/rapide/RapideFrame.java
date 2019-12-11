package rapide;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.*;

public class RapideFrame extends BaseListener{

	protected String[] extendedNote = {"C","C#","D","D#","E","F","F#","G","G#","A","A#","B","CC"};
	int[] Note = new int[6];
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
        
        rapideFrame.add(keyPanel);
        
        engine = new Engine();
        rapideFrame.setResizable(false);
        rapideFrame.setVisible(true);
        
        engine.logicPlay();
    }
	
	protected void doPerformAction(ActionEvent e) {
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
            
            engine.playNote(finalNote);
            k++;
            setNote(finalNote, m);
            m++;
        }

        setK(k);
        
        if(k%5==0) {
        	listening.setActive(false);
        	m = 0;
        	k = 0;
        	for(int z = 0; z < 5; z++) {
				System.out.println(Note[z]);
        	}
        	
        }
        
//        setNote(arr);
//        System.out.println("setnote");
	}

	public void setNote(int note, int id) {
		this.Note[id] = note;
//		System.out.print(note);
//        System.out.print(" " + id);
//        System.out.print(" " + Note[id]);
//		System.out.println("suda di set");
	}
	
	public int getNote(int id) {	
		return this.Note[id];
	}
	
	public void setK(int k) {
		this.k = k;
		System.out.println("Masuk setk" + k);
	}
	
	public int getK() {
		return k;
	}

}