package com.xpy;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.*;

public class SnakeBorder extends JPanel implements Runnable, KeyListener {
	private static final long serialVersionUID = 1L;
	private Snake[] images = new Snake[151];
	int score;// 用于统计分数，每增加一节，则加1
	Image grey;
	Image head;
	Image body;
	Image food;
	private int location = 29;
	ArrayList<Snake> snake;
	Snake snakeHead;
	Snake snakeFood;
	Snake snakeBody;
	boolean supe;
	long time;// 时间控制,默认为0.5s移一格,每增加一格长度，time加快0.03s直到time=0.2s为止
	boolean gameover;

	public SnakeBorder() {
		snake = new ArrayList<Snake>();
		init();
	}

	// 初始化函数
	public void init() {
		score = 1;
		time = 500;
		supe = false;
		gameover = false;
		snake.clear();
		grey = new ImageIcon("pictures/grey.png").getImage();
		head = new ImageIcon("pictures/head_right.png").getImage();
		body = new ImageIcon("pictures/body.png").getImage();
		food = new ImageIcon("pictures/food.png").getImage();
		for (int i = 0; i < 151; i++)
			images[i] = new Snake();
		int k = 1;
		for (int i = 30; i < 605; i = i + 60) {
			for (int j = 40; j < 940; j = j + 60) {
				images[k].x = j;
				images[k].y = i;
				images[k].dir = 4;
				k++;
			}
		}
		// 将贪吃蛇第一次出现控制在2~6列，1~5行之内
		while ((location % 15 >= 6) || (location % 15 < 2) || location > 90) {
			location = (int) (Math.random() * 150);
		}
		// 获取贪吃蛇位置
		snakeHead = new Snake();
		snakeHead.x = images[location].x;
		snakeHead.y = images[location].y;
		snakeHead.dir = images[location].dir;
		snakeHead.image = head;

		snake.add(snakeHead);
		// 食物出现
		produceFood();
	}

	public void paint(Graphics g) {
		super.paint(g);
		for (int i = 1; i < 151; i++) {
			g.drawImage(grey, images[i].x, images[i].y, null);
		}
		for (int i = 0; i < snake.size(); i++) {
			g.drawImage(snake.get(i).image, snake.get(i).x, snake.get(i).y,
					null);
		}
		g.drawImage(snakeFood.image, snakeFood.x, snakeFood.y, null);
	}

	private void produceFood() {
		// 食物不能出现四个角落
		boolean judge = false;
		while (location == 1 || location == 15 || location == 135
				|| location == 150 || !judge) {
			location = (int) (Math.random() * 149 + 1);
			for (int i = 0; i < snake.size(); i++) {
				if ((images[location].x == snake.get(i).x)
						&& (images[location].y == snake.get(i).y)) {
					judge = false;
					break;
				} else
					judge = true;
			}
		}
		snakeFood = new Snake();
		snakeFood.x = images[location].x;
		snakeFood.y = images[location].y;
		snakeFood.image = food;
	}

	// 吃到食物
	private void eatFood() {
		int x = snake.get(0).x;
		int y = snake.get(0).y;
		if ((x < snakeFood.x + 20) && x > snakeFood.x - 20
				&& (y < snakeFood.y + 20) && (y > snakeFood.y - 20)) {
			snakeBody = new Snake();
			if (snake.get(snake.size() - 1).dir == 1) {
				snakeBody.x = snake.get(snake.size() - 1).x;
				snakeBody.y = snake.get(snake.size() - 1).y + 60;
			} else if (snake.get(snake.size() - 1).dir == 2) {
				snakeBody.x = snake.get(snake.size() - 1).x;
				snakeBody.y = snake.get(snake.size() - 1).y - 60;
			} else if (snake.get(snake.size() - 1).dir == 3) {
				snakeBody.x = snake.get(snake.size() - 1).x + 60;
				snakeBody.y = snake.get(snake.size() - 1).y;
			} else if (snake.get(snake.size() - 1).dir == 4) {
				snakeBody.x = snake.get(snake.size() - 1).x - 60;
				snakeBody.y = snake.get(snake.size() - 1).y;
			}
			snakeBody.image = body;
			snakeBody.dir = snake.get(snake.size() - 1).dir;
			snake.add(snakeBody);
			score++;// 分数加1
			if (time >= 200)
				time = time - 30;// 每增加一节，时间加快0.03s
			produceFood();
			repaint();
		}
	}

	private void moved() {
		for (int k = 0; k < snake.size(); k++) {
			// 向上
			if (snake.get(k).dir == 1) {
				snake.get(k).y = snake.get(k).y - 60;
			} else if (snake.get(k).dir == 2) {// 向下
				snake.get(k).y = snake.get(k).y + 60;
			} else if (snake.get(k).dir == 3) {// 向左
				snake.get(k).x = snake.get(k).x - 60;
			} else if (snake.get(k).dir == 4) {// 右
				snake.get(k).x = snake.get(k).x + 60;

			}
		}
		eatFood();// 判断是否吃到食物
		// 判断方向
		for (int i = snake.size() - 1; i > 0; i--) {
			if (snake.get(i).dir != snake.get(i - 1).dir)
				snake.get(i).dir = snake.get(i - 1).dir;
		}
	}

	private void isGameOver() {
		if ((snake.get(0).x > 880) || (snake.get(0).x < 40)
				|| (snake.get(0).y < 30) || (snake.get(0).y > 570)) {// 头部碰到四壁
			gameover = true;
		} else {
			for (int i = 1; i < snake.size(); i++) {
				if ((snake.get(0).x == snake.get(i).x)
						&& (snake.get(0).y == snake.get(i).y)) {// 头部碰到自己的身体
					gameover = true;
					break;
				}
			}
		}
		if (gameover) {
			int choice = JOptionPane.showConfirmDialog(SnakeBorder.this,
					"你输了！是否再来一局？", null, JOptionPane.YES_NO_OPTION);
			if (choice == JOptionPane.YES_OPTION) {
				init();// 初始化
				resume();// 线程开始
				repaint();// 重新绘制
			} else
				System.exit(0);// 退出
		}
	}

	@Override
	public void run() {

		while (true) {
			try {
				Thread.sleep(time);
				// Thread.sleep(100);
				synchronized (this) {
					while (!supe) {
						wait();
					}
					moved();// 移动
					isGameOver();
					if (!gameover)
						repaint();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 唤醒线程
	synchronized void resume() {
		supe = true;
		notify();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// 向下
		if (e.getKeyCode() == KeyEvent.VK_DOWN && snake.get(0).dir != 1) {
			snake.get(0).dir = 2;
			snake.get(0).image = new ImageIcon("pictures/head_down.png")
					.getImage();
		}
		// 上
		if (e.getKeyCode() == KeyEvent.VK_UP && snake.get(0).dir != 2) {
			snake.get(0).dir = 1;
			snake.get(0).image = new ImageIcon("pictures/head_up.png")
					.getImage();
		}
		// 左
		if (e.getKeyCode() == KeyEvent.VK_LEFT && snake.get(0).dir != 4) {
			snake.get(0).dir = 3;
			snake.get(0).image = new ImageIcon("pictures/head_left.png")
					.getImage();
		}
		// 右
		if (e.getKeyCode() == KeyEvent.VK_RIGHT && snake.get(0).dir != 3) {
			snake.get(0).dir = 4;
			snake.get(0).image = new ImageIcon("pictures/head_right.png")
					.getImage();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
