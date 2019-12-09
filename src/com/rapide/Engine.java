package com.rapide;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	int n;
//	ActionEvent e;
	
	public Engine() {
		this.scoreBoard = new JPanel();
		this.labelScore = new JLabel("Score: ");
		this.score = new JLabel("0");
		this.scoreBoard.add(this.labelScore);
		this.scoreBoard.add(this.score);
	}
	
	public void logicPlay() {
		playing = true;
		int[] arr_r = new int[5];
		int[] arr_u = new int[5];
		RapideFrame fr = new RapideFrame();
		
		while(playing) {
			n = 5;
			while(n>0) {
				int i = 0;
				
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
			
//			synchronized (fr) {
//				System.out.println("masuk wait");
//				try {
//					fr.wait();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				int x;
//				x = fr.getK();
//				if(x == 4) {
//					fr.notify();
//					System.out.println("masuk notify");
//				}
//				}
			
			try {
				System.out.println("masuk sleep");
				Thread.sleep(20000);
			}catch(InterruptedException e) {
				System.out.println("Interrupt");
			}
			
			arr_u = fr.getNote();
			System.out.println("panggil getnote");
			
			for(int j = 0; j < 5; j++) {
				if(arr_u[j] == arr_r[j]) {
					playing = true;
				}else {
					playing = false;
				}	
			}
			
		}
		}
	
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
	
	public void updateScore(int value)
	{
		score.setText(Integer.valueOf(value).toString());
	}
	
}