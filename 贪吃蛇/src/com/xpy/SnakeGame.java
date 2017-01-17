package com.xpy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.swing.*;

public class SnakeGame extends JFrame implements Runnable {
	private static final long serialVersionUID = -6497735513679547494L;
	SnakeBorder panel;
	JPanel jp;
	JLabel lab;//��ʾ
	JLabel labScore;//��ǰ����
	JLabel labRecorde;//��ǰ��Ѽ�¼
	JButton btnStart;
	JButton btnRestart;
    private int bestRecorde=0;
	public SnakeGame() {
		super("Sanke Game");
		setSize(1000, 780);
		saveData();
		labScore=new JLabel("��ǰ������0");
		labRecorde=new JLabel("   ��ǰ��Ѽ�¼��"+bestRecorde);
		panel=new SnakeBorder();
		int x=(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		int y=(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		setLocation((x-getWidth())/2, (y-getHeight())/2);
		lab=new JLabel("��ʾ���������������������Ʒ���     ");
		
		jp=new JPanel();
		btnStart=new JButton("�����ʼ");
		btnStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource()==btnStart&&!panel.supe){
					btnStart.setText("�����ͣ");
					panel.resume();
				}
				else if(e.getSource()==btnStart&&panel.supe){
					btnStart.setText("�������");
					panel.supe=false;
				}
			}
		});
		btnRestart=new JButton("���¿�ʼ");
		btnRestart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getSource()==btnRestart){
					panel.init();
					panel.resume();
				}
				
			}
		});
		jp.setSize(1000, 25);
		jp.setBackground(Color.cyan);
		jp.add(lab);
		jp.add(btnStart);
		jp.add(btnRestart);
		jp.add(labScore);
		jp.add(labRecorde);
		add(jp, BorderLayout.NORTH);
		add(panel);
		
		btnStart.addKeyListener(panel);//����ע��
		btnRestart.addKeyListener(panel);//����ע��
		panel.addKeyListener(panel);
		addKeyListener(panel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		Thread t=new Thread(panel);
		t.start();
		
		Thread s=new Thread(this);
		s.start();
	}

	public static void main(String[] args) {
		new SnakeGame();
	}
//����ͳ�Ʒ���
	@Override
	public void run() {
		while(true){
			labScore.setText("��ǰ������"+panel.score);
			if(bestRecorde<panel.score)
				saveData();
		}
		
	}
	
	public void saveData(){
		
		try{
			   BufferedReader in=new BufferedReader(new InputStreamReader(new FileInputStream("Data.txt")));
			    String str="";
			    str=in.readLine();
			    bestRecorde=Integer.parseInt(str);
			   if(bestRecorde<=panel.score){
				   FileOutputStream fos=new FileOutputStream("Data.txt");
				   PrintWriter pw=new PrintWriter(fos);
			       pw.write(""+panel.score);
				  pw.flush();
				  pw.close();
			   }
			   in.close();
			   }catch(Exception e){//���û�и��ļ��򴴽����ļ�
				   try {
					FileOutputStream fos1=new FileOutputStream("Data.txt");
					PrintWriter pw1=new PrintWriter(fos1);
					pw1.write(""+bestRecorde);
					pw1.flush();
				    pw1.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}//true������׷������
			   }
	}

}
