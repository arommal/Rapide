package com.rapide;

import java.awt.Color;
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
	boolean playing = false;
	private boolean score_val = true;
	RapideFrame fr;
	int n;
	ActionEvent e;
	
	
	public Engine(RapideFrame f) {
		this.scoreBoard = new JPanel();
		this.labelScore = new JLabel("Score: ");
		this.score = new JLabel("0");
		this.scoreBoard.add(this.labelScore);
		this.scoreBoard.add(this.score);
		this.fr = f;
	}
	
	public void logicPlay(JButton[] key) {
		playing = true;
		int[] arr_r = new int[6];
		ArrayList<Integer> temp;
		fr.key = new JButton[14];
		
		Thread tr = new Thread();
		
		while(playing) {
			fr.note = new ArrayList<Integer>();
			fr.listening.setActive(true);
			fr.m = 0;
			
			n = 5;
			int i = 0;
			
			while(n>0) {
				try {
					Thread.sleep(250);
				}catch(InterruptedException e) {
					System.out.println("Interrupt");
					playing = false;
				}
				
				rand = a.nextInt(12);
				fin = init + rand;
				arr_r[i] = fin;
				
				playNote(fin, key);
		
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

				fr.st = STATE.CONTINUE;
			}
				
			temp = fr.getList();
				
			for(int z = 0; z < 5; z++){
				System.out.println("in " + temp.get(z));
				System.out.println("rand " + arr_r[z]);
				
				if(temp.get(z) == arr_r[z]) {
					score_val = true;
					System.out.println("true");
				}else {
					score_val = false;
					System.out.println("false");
					break;
				}
			}
				
			if(score_val == false) {
				System.out.println("GAME OVER");
				playing = false;
			}else {
				System.out.println("LEVEL UP");
				playing = true;
			}
		}
	}
	
	public void playNote(int note, JButton[] key) {
		
		try {
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();
            Sequence sequence = new Sequence(Sequence.PPQ,4);
            Track track = sequence.createTrack();

//            MidiEvent event = null;

            ShortMessage first = new ShortMessage();
            first.setMessage(192, 1, 1, 0);
            MidiEvent setInstrument = new MidiEvent(first, 1);
            track.add(setInstrument);

            ShortMessage a = new ShortMessage();
            a.setMessage(144, 1, note, 100);
            MidiEvent noteOn = new MidiEvent(a, 1);
            track.add(noteOn);

            ShortMessage b = new ShortMessage();
            b.setMessage(128, 1, note, 100);
            MidiEvent noteOff = new MidiEvent(b, 16);
            track.add(noteOff);

            sequencer.setSequence(sequence);
            sequencer.start();
            
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
		
		System.out.print(" (" + note + ")\n");
		
		int index = note - (5*12);
		
		if(key[index].getBackground() == Color.white) {
			key[index].setBackground(Color.decode("#FADF63"));
			try {
				Thread.sleep(250);
			}catch(InterruptedException e) {}
			
			key[index].setBackground(Color.WHITE);
		}else {
			key[index].setBackground(Color.decode("#FADF63"));
			try {
				Thread.sleep(250);
			}catch(InterruptedException e) {}
			key[index].setBackground(Color.BLACK);
		}
		
    }
	
	public void playNote(int note) {
		// TODO Auto-generated method stub
		try {
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.open();
            Sequence sequence = new Sequence(Sequence.PPQ,4);
            Track track = sequence.createTrack();

//            MidiEvent event = null;

            ShortMessage first = new ShortMessage();
            first.setMessage(192, 1, 1, 0);
            MidiEvent setInstrument = new MidiEvent(first, 1);
            track.add(setInstrument);

            ShortMessage a = new ShortMessage();
            a.setMessage(144, 1, note, 100);
            MidiEvent noteOn = new MidiEvent(a, 1);
            track.add(noteOn);

            ShortMessage b = new ShortMessage();
            b.setMessage(128, 1, note, 100);
            MidiEvent noteOff = new MidiEvent(b, 16);
            track.add(noteOff);

            sequencer.setSequence(sequence);
            sequencer.start();
            
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
		
		System.out.print(" (" + note + ")\n");
	
    }
	
}