package myPiano;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.midi.*;
import javax.swing.*;
import java.util.*;

public class Piano extends Applet implements Runnable, ActionListener {
	private Receiver out;
	private String error = null;
	private int currentNote = -1, keyboardStart = 35;
	private JTextField output = null;
	private Thread playing = null;
	private boolean isApplet = true;
	private JFrame extraFrame = null;
	private Piano extraPiano;

	private static int[] WHITE = { 0, 2, 4, 5, 7, 9, 11 };
	private static int KEY_WIDTH = 20, BLACK_WIDTH = 15,
	                   BLACK_START = KEY_WIDTH-BLACK_WIDTH/2;
	private static String BLACK_STRING = "WERTYUIOP[]\\",
	                      WHITE_STRING = "ASDFGHJKL"+((char)KeyEvent.VK_SEMICOLON)+((char)KeyEvent.VK_QUOTE)+"\n";
	
	public void init() {
		try {
			Synthesizer synth = MidiSystem.getSynthesizer();
			if(!synth.isOpen())
				synth.open();
			out = synth.getReceiver();
		} catch(Exception z) {
			z.printStackTrace();
			error = z.toString();
		}
		setBackground(Color.white);
		addMouseListener(
			new MouseAdapter() {
				public void mousePressed(MouseEvent me) {
					pressed(me);
				}
				public void mouseReleased(MouseEvent me) {
					released(me);
				}
			}
		);
		addMouseMotionListener(
			new MouseMotionAdapter() {
				public void mouseDragged(MouseEvent me) {
					dragged(me);
				}
			}
		);
		addKeyListener(
			new KeyAdapter() {
				public void keyPressed(KeyEvent ke) {
					pressed(ke);
				}
				public void keyReleased(KeyEvent ke) {
					released(ke);
				}
			}
		);
		addFocusListener(
			new FocusListener() {
				public void focusGained(FocusEvent fe) {
					repaint();
				}
				public void focusLost(FocusEvent fe) {
					repaint();
				}
			}
		);
	}
	
	private String noteName(int n) {
		String[] names = {"c","cis","d","es","e","f","fis","g","gis","a","bes","b"};
		String name = names[n%12];
		while(n >= 60) {
			n -= 12;
			name += "'";
		}
		while(n < 60-12) {
			n += 12;
			name += ",";
		}
		return name;
	}

	private class Note {
		public int pitch = -1;
		public int duration = 0;
		public boolean startChord, endChord;
	}

	public static int WHOLE_NOTE_LENGTH = 2000;
	
	private Note parseNote(String name) {
		int loc = 0;
		Note note = new Note();
		
		if(name.startsWith("<", loc)) {
			note.startChord = true;
			loc++;
		}
		
		if(loc < name.length() && name.charAt(loc) >= 'a' && name.charAt(loc) <= 'g') {
			note.pitch = white((name.charAt(loc) + 7 - 'c') % 7) + (60-12);
			loc++;
			
			if(name.startsWith("is", loc)) {
				note.pitch++;
				loc += 2;
			} else if(name.startsWith("es", loc)) {
				note.pitch--;
				loc += 2;
			} else if(name.startsWith("s", loc)) {
				note.pitch--;
				loc += 1;
			}
			
			while(name.startsWith("'", loc)) {
				note.pitch += 12;
				loc++;
			}
			while(name.startsWith(",", loc)) {
				note.pitch -= 12;
				loc++;
			}
			note.duration = -1;
		} else if(name.startsWith("r", loc) || name.startsWith("R", loc) || name.startsWith("s", loc)) {
			note.pitch = -1;
			loc++;
			note.duration = -1;
		}
		
		if(name.indexOf(">") >= 0) {
			note.endChord = true;
			loc = name.indexOf(">")+1;
		}
		
		if(loc < name.length() && Character.isDigit(name.charAt(loc))) {
			StringBuffer num = new StringBuffer();
			while(loc < name.length() && Character.isDigit(name.charAt(loc))) {
				num.append(name.charAt(loc));
				loc++;
			}
			int mainDuration = WHOLE_NOTE_LENGTH / Integer.parseInt(num.toString());
			note.duration = mainDuration;
			
			while(name.startsWith(".", loc)) {
				mainDuration /= 2;
				note.duration += mainDuration;
				loc++;
			}
		}

		return note;
	}
	
	public void noteOn(int n) {
		if(currentNote==n)
			return;
		noteOff(currentNote);
		if(n < 0 || n > 127) return;
		try {
			ShortMessage sm = new ShortMessage();
			sm.setMessage(ShortMessage.NOTE_ON,0,n,64);
			out.send(sm,-1);
		} catch(Exception z) {
			throw new RuntimeException(z);
		}
		currentNote = n;
		output(0, noteName(n)+" ");
		repaint();
	}

	public void noteOff(int n) {
		if(n < 0 || n > 127) return;
		try {
			ShortMessage sm = new ShortMessage();
			sm.setMessage(ShortMessage.NOTE_OFF,0,n,64);
			out.send(sm,-1);
		} catch(Exception z) {
			throw new RuntimeException(z);
		}
		currentNote = -1;
		repaint();
	}

	public void setPedal(int v) {
		try {
			ShortMessage sm = new ShortMessage();
			sm.setMessage(ShortMessage.CONTROL_CHANGE,0,64,v);
			out.send(sm,-1);
		} catch(Exception z) {
			throw new RuntimeException(z);
		}
	}

	public Dimension getPreferredSize() {
		return new Dimension(KEY_WIDTH*35,100);
	}

	private int getMin() {
		return Math.max(0,35-getSize().width/KEY_WIDTH/2);
	}
	
	private int white(int k) {
		return WHITE[k%7] + (k/7)*12;
	}

	private int invwhite(int n) {
		int oct = (n/12)*7, note = n%12;
		for(int i = 0; i < WHITE.length; i++)
			if(WHITE[i]==note)
				return i + oct;
		return -1;
	}
	
	public void paint(Graphics g) {
		int min = getMin();
		int max = getSize().width/KEY_WIDTH;
		
		g.setColor(Color.cyan);
		g.fillRect((35-min)*KEY_WIDTH,0,KEY_WIDTH,getSize().height);
		g.setColor(Color.orange);
		if(hasFocus())
			g.fillRect((keyboardStart-min)*KEY_WIDTH,0,KEY_WIDTH,getSize().height);
		g.setColor(Color.lightGray);
		g.fillRect((invwhite(currentNote)-min)*KEY_WIDTH,0,
		           KEY_WIDTH,getSize().height);
		g.setColor(Color.black);
		
		int i;
		for(i = 0; i < max; i++) {
			if(white(i)>127) break;
			g.drawLine((i+1)*KEY_WIDTH,0,(i+1)*KEY_WIDTH,getSize().height);
			if(white(i+min)+1 != white(i+min+1)) {
				if(white(i+min)+1 == currentNote)
					g.setColor(Color.gray);
				g.fillRect(i*KEY_WIDTH+BLACK_START,0,BLACK_WIDTH,getSize().height/2);
				g.setColor(Color.black);
			}
		}
		g.drawRect(0,0,i*KEY_WIDTH,getSize().height-1);
	}

	public int getNote(int x, int y) {
		int min = getMin();
		int xwhite = x/KEY_WIDTH+min,
		    xblack = (x-KEY_WIDTH/2)/KEY_WIDTH+min;
		
		if(y < getSize().height/2 && white(xblack)+1 != white(xblack+1))
			return white(xblack)+1;
		else
			return white(xwhite);
	}
	
	public void pressed(MouseEvent me) {
		int x = me.getX(), y = me.getY();
		 
		int note = getNote(x,y);
		if(me.isAltDown()) {
			keyboardStart = x/KEY_WIDTH+getMin();
			repaint();
		} else {
			noteOn(note);
		}
	}

	public void dragged(MouseEvent me) {
		int x = me.getX(), y = me.getY();
		
		int note = getNote(x,y);
		if(me.isAltDown()) {
			keyboardStart = x/KEY_WIDTH+getMin();
			repaint();
		} else if(note != currentNote) {
			noteOn(note);
		}
	}

	public void released(MouseEvent me) {
		noteOff(currentNote);
	}

	public int getNote(int keyCode) {
		if(keyCode==KeyEvent.VK_Q) {
			return white(keyboardStart)-1;
		} else if(keyCode<65536) {
			char ch = (char)keyCode;
			int w = WHITE_STRING.indexOf(ch), b = BLACK_STRING.indexOf(ch);
			if(w != -1)
				return white(w+keyboardStart);
			else if(b != -1 && white(b+keyboardStart)+1 != white(b+keyboardStart+1))
				return white(b+keyboardStart)+1;
		}
		return -1;
	}
	
	public void pressed(KeyEvent ke) {
		if(ke.getKeyCode() == KeyEvent.VK_UP && isApplet) {
			if(extraFrame == null) {
				Piano p = new Piano();
				p.isApplet = false;
				p.output = new JTextField();
				p.output.addActionListener(p);
				p.output.setVisible(false);
				p.init();
				extraPiano = p;
				
				JFrame f = new JFrame("Piano");
				f.getContentPane().add(p,BorderLayout.CENTER);
				f.getContentPane().add(p.output,BorderLayout.SOUTH);
				f.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				f.pack();
				extraFrame = f;
			}
			extraFrame.setVisible(true);
			extraPiano.requestFocus();
		} else if(ke.getKeyCode()==KeyEvent.VK_DOWN && output != null) {
			output.setVisible(!output.isVisible());
			output.getParent().doLayout();
		} else if(ke.getKeyCode()==KeyEvent.VK_SHIFT) {
			setPedal(127);
			output(0,"<");
		} else if(ke.getKeyCode()==KeyEvent.VK_BACK_SPACE) {
			delete();
		} else if(ke.getKeyCode()==KeyEvent.VK_SPACE) {
			output(0,"r ");
		} else if(ke.getKeyCode()==KeyEvent.VK_MINUS) {
			if(keyboardStart-7 >= 0)
				keyboardStart -= 7;
			repaint();
		} else if(ke.getKeyCode()==KeyEvent.VK_PLUS || ke.getKeyCode()==KeyEvent.VK_EQUALS) {
			if(white(keyboardStart+7) <= 127)
				keyboardStart += 7;
			repaint();
		} else {
			int n = getNote(ke.getKeyCode());
			noteOn(n);
		}
	}

	public void released(KeyEvent ke) {
		if(ke.getKeyCode() == ke.VK_SHIFT) {
			setPedal(0);
			output(1,"> ");
		} else if(getNote(ke.getKeyCode()) == currentNote)
			noteOff(currentNote);
	}
	
	private void output(int delete, String insert) {
		if(output==null || playing!=null || !output.isVisible())
			return;
		int caret = output.getCaretPosition();
		String text = output.getText();
		if(delete > caret)
			delete = caret;
		output.setText(text.substring(0,caret-delete)+insert+text.substring(caret));
		output.setCaretPosition(caret - delete + insert.length());
	}
	
	private void delete() {
		if(output==null)
			return;
		int caret = output.getCaretPosition();
		String text = output.getText();
		
		if(caret==0)
			return;
		int l = text.lastIndexOf(" ", caret-2)+1;
		if(text.lastIndexOf(">", caret) > l)
			l = text.lastIndexOf("<", caret);
		output(caret-l, "");
	}

	public void run() {
		try {
			playing = Thread.currentThread();
			StringTokenizer tok = new StringTokenizer(output.getText());
			boolean inChord = false;
			int duration = WHOLE_NOTE_LENGTH/4;
			while(tok.hasMoreTokens()) {
				if(playing != Thread.currentThread())
					break;
				String i = tok.nextToken();
				Note n = parseNote(i);
				if(n.startChord) {
					inChord = true;
					setPedal(127);
				}
				if(n.endChord)
					inChord = false;
				
				if(n.pitch != -1)
					noteOn(n.pitch);
				if(!inChord && n.duration > 0)
					duration = n.duration;
				if(!inChord && n.duration != 0)
					try {Thread.sleep(duration);} catch(Exception z) {throw new RuntimeException(z);}
				if(n.endChord)
					setPedal(0);
				noteOff(n.pitch);
			}
		} finally {
			playing = null;
			setPedal(0);
		}
	}

	public void actionPerformed(ActionEvent ae) {
		new Thread(this).start();
	}
	
	public static void main(String[] args) {
		Piano p = new Piano();
		p.isApplet = false;
		p.output = new JTextField();
		p.output.addActionListener(p);
		p.output.setVisible(false);
		p.init();
		
		JFrame f = new JFrame("Piano");
		f.getContentPane().add(p,BorderLayout.CENTER);
		f.getContentPane().add(p.output,BorderLayout.SOUTH);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
		f.setVisible(true);

		p.requestFocus();
	}
}