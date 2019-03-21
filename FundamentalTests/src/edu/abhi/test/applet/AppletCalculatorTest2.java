package edu.abhi.test.applet;

import java.applet.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
public class AppletCalculatorTest2 extends Applet implements ActionListener, TextListener

{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6401905598951217108L;
	String s,s1,s2,s3,s4;
    Button b1,b2,b3,b4,b5,b6,b7,b8,b9,b0;
    Button add,sub,eq,cl,mul,div;
    TextField t1;
    int a,b,c;

    public void init()
    {
        t1=new TextField(10);
        b1=new Button("1");
        b2=new Button("2");
        b3=new Button("3");
        b4=new Button("4");
        b5=new Button("5");
        b6=new Button("6");
        b7=new Button("7");
        b8=new Button("8");
        b9=new Button("9");
        b0=new Button("0");
        add=new Button("+");
        sub=new Button("-");
        mul=new Button("*");
        div=new Button("/");
        eq=new Button("=");
        cl=new Button("Clear");



        GridLayout gb=new GridLayout(4,5);
        setLayout(gb);


        add(t1);
        add(b1);
        add(b2);
        add(b3);
        add(b4);
        add(b5);
        add(b6);
        add(b7);
        add(b8);
        add(b9);
        add(b0);
        add(add);
        add(sub);
        add(mul);
        add(div);
        add(eq);
        add(cl);

        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);
        b4.addActionListener(this);
        b5.addActionListener(this);
        b6.addActionListener(this);
        b7.addActionListener(this);
        b8.addActionListener(this);
        b9.addActionListener(this);
        b0.addActionListener(this);
        add.addActionListener(this);
        sub.addActionListener(this);
        mul.addActionListener(this);
        div.addActionListener(this);
        eq.addActionListener(this);
        cl.addActionListener(this);
        paint();
        //t1.addTextListener(this);
    }
    public void paint()
    {
        setBackground(Color.green);
    }

    public void actionPerformed(ActionEvent e)
    {
        s=e.getActionCommand();
        if(s.equals("0")||s.equals("1")||s.equals("2")||
                s.equals("3")||s.equals("4")||s.equals("5")||s.equals("6")||s.equals("7")||s.equals("8")||
                s.equals("9")||s.equals("0"))
        {
            s1=t1.getText()+s;
            t1.setText(s1);
        }
        if(s.equals("+"))
        {
            s2=t1.getText();
            t1.setText("");
            s3="+";
        }
        if(s.equals("-"))
        {
            s2=t1.getText();
            t1.setText("");
            s3="-";
        }
        if(s.equals("*"))
        {
            s2=t1.getText();
            t1.setText("");
            s3="*";
        }
        if(s.equals("*"))
        {
            s2=t1.getText();
            t1.setText("");
            s3="*";
        }
        if(s.equals("="))
        {
            s4=t1.getText();
            a=Integer.parseInt(s2);
            b=Integer.parseInt(s4);
            if(s3.equals("+"))
                c=a+b;
            if(s3.equals("-"))
                c=a-b;

            t1.setText(String.valueOf(c));
        }
        if(s.equals("Clear"))
        {
            t1.setText("");
        }
    }
    public void textValueChanged(TextEvent e)
    {

    }
}