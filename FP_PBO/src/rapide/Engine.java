package rapide;

import java.awt.event.*;
import java.util.*;

import javax.sound.midi.*;
import javax.swing.*;

public class Engine extends RapideFrame implements ActionListener {

	Random a = new Random();
	private int init = 5 * 12;
	private int rand, fin;
	JPanel scoreBoard;
	JLabel score, labelScore;
	private boolean playing = false;
	private boolean score_val = true;
	int n;
	ActionEvent e;
	
	public Engine() {
		this.scoreBoard = new JPanel();
		this.labelScore = new JLabel("Score: ");
		this.score = new JLabel("0");
		this.scoreBoard.add(this.labelScore);
		this.scoreBoard.add(this.score);
	}
	
	public void logicPlay() {
		playing = true;
		int[] arr_r = new int[6];
		int[] arr_u = new int[6];
		RapideFrame fr = new RapideFrame();
		Thread tr = new Thread();
		
		while(playing) {
			fr.listening.setActive(true);
			fr.m = 0;
			
			n = 5;
			int i = 0;
			while(n>0) {
				
				
				try {
					Thread.sleep(500);
				}catch(InterruptedException e) {
					System.out.println("Interrupt");
					playing = false;
				}
				
				rand = a.nextInt(12);
				fin = init + rand;
				arr_r[i] = fin;
				
				playNote(fin);
		
				i++;
				n--;
			}
			
			fr.st = STATE.WAIT;
			System.out.println("jadi wait");
			
			
				while((fr.st==STATE.WAIT)) {
					try {
						System.out.println("masuk synchro");	
						tr.sleep(20000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//tr.wait();
					System.out.println("suda tr.sleep");

					fr.st = STATE.CONTINUE;
					}
				
//				arr_u = fr.getNote();
				
				for(int z = 0; z < 5; z++) {
					System.out.println(fr.getNote(z));
					System.out.println(arr_r[z]);
					
//					if(fr.Note[z] == arr_r[z]) {
//						score_val = true;
//						System.out.println("true");
//					}else {
//						score_val = false;
//						System.out.println("false");
//						break;
					}
				}
				
				if(score_val == false) {
					System.out.println("gameover");
					playing = false;
				}else {
					System.out.println("level up");
					playing = true;
				}
			}
			
//		}
	
	public void playNote(int Note) {
		// TODO Auto-generated method stub
		try {
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();
            Sequence sequence = new Sequence(Sequence.PPQ,4);
            Track track = sequence.createTrack();

//            MidiEvent event = null;

            ShortMessage first = new ShortMessage();
            first.setMessage(192,1,1,0);
            MidiEvent setInstrument = new MidiEvent(first, 1);
            track.add(setInstrument);

            ShortMessage a = new ShortMessage();
            a.setMessage(144,1,Note,100);
            MidiEvent noteOn = new MidiEvent(a, 1);
            track.add(noteOn);

            ShortMessage b = new ShortMessage();
            b.setMessage(128,1,Note,100);
            MidiEvent noteOff = new MidiEvent(b, 16);
            track.add(noteOff);

            sequencer.setSequence(sequence);
            sequencer.start();
            
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
		
		System.out.print(" (" + Note + ")\n");
    }
	
//	public void updateScore(int value)
//	{
//		score.setText(Integer.valueOf(value).toString());
//	}
//	
}