package com;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;

class PianoLayout extends JScrollPane
{
public PianoLayout()
{
    initComponents();

}

private void initComponents()
{
    JLayeredPane layer = new JLayeredPane();
    //ScrollableLayeredPane layer = new ScrollableLayeredPane();
    layer.setSize(1120,150);
    JButton[] keys = new JButton[48];
    int keyIndex = 0, i;

    for(i=0;i<28;i++)
    {
        keys[keyIndex] = createWhiteKey(i);
        layer.add(keys[keyIndex], 0, -1);
        keyIndex+=1;
        if(i%7!=2 && i%7!=6)
        {
            keys[keyIndex] = createBlackKey(i);
            layer.add(keys[keyIndex], 1, -1);
            keyIndex+=1;
        }
    }
    this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    this.setViewportView(layer);
    setSize(280, 150);     
    setLocation(110,100);


}

private JButton createWhiteKey(int i)
{
    JButton whiteKey = new JButton();
    whiteKey.setBackground(Color.WHITE);
    whiteKey.setLocation(i*40,0);
    whiteKey.setSize(40, 150);
    return whiteKey;
}

private JButton createBlackKey(int i)
{
    JButton blackKey = new JButton();
    blackKey.setBackground(Color.BLACK);
    blackKey.setLocation(25 + i*40,0);
    blackKey.setSize(30, 90);
    return blackKey;
}

}