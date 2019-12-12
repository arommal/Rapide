package rapide;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.*;

public class RapideFrame extends BaseListener implements MouseListener {

	protected String[] extendedNote = {"C","C#","D","D#","E","F","F#","G","G#","A","A#","B","C'"};
	public ArrayList<Integer> note;
	JButton[] key = new JButton[14];
	Engine engine;
	int k;
	boolean m;
	BaseListener listening;
	
	public enum STATE{
		WAIT,
		CONTINUE
	}
	
	STATE st;
	JLayeredPane layer =  new JLayeredPane();
	
	public void setGUI(int width, int height) {
		JFrame rapideFrame = new JFrame();
		
		st = STATE.WAIT;
		m = false;
		
		setMain();
		
		if(m == true) {
		
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
 //           x++;
        }
        
        if(k%5==0) {
        	listening.setActive(false);
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
	
	public void setBgOver() {
		
		BufferedImage image1 = null;
		try {
			image1 = ImageIO.read(new File("gameover.jpg"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		Image bg1 = image1.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
		ImagePanel bg1_panel = new ImagePanel(bg1, 800, 600);
		layer.add(bg1_panel, 2, -1);
	}
	
	public int setLvl(int score) {
		String[] options = {"GO ON", "EXIT GAME"};
		int ret = JOptionPane.showOptionDialog(null, "SCORE: " + score,
                "Click a button",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
		
		return ret;
	}
	
	public void setMain() {
		BufferedImage image2 = null;
		try {
			image2 = ImageIO.read(new File("menu.jpg"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		Image bg2 = image2.getScaledInstance(800, 600, Image.SCALE_SMOOTH);
		ImagePanel bg2_panel = new ImagePanel(bg2, 800, 600);
		layer.add(bg2_panel, -1, -1);
		
		JButton btn = new JButton("Play");
		btn.addMouseListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		st = STATE.CONTINUE;
		m = true;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}