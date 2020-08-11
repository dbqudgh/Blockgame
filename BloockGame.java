import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class BlockGame {

	static class MyFrame extends JFrame{
		static int BALL_WIDTH=20;
		static int BALL_HEIGHT=20;
		static int BLOCK_ROWS=5;
		static int BLOCK_COLUMNS=10;
		static int BLOCK_WIDTH=40;
		static int BLOCK_HEIGHT=20;
		static int BLOCK_GAP=3;
		static int BAR_WIDTH=80;
		static int BAR_HEIGHT=20;
		static int CANVAS_WIDTH=400+(BLOCK_GAP*BLOCK_COLUMNS)-BLOCK_GAP;
		static int CANVAS_HEIGHT=600;
		
		static MyPanel myPanel=null;
		static int score=0;
		static Timer timer=null;
		static Block[][] blocks=new Block[BLOCK_ROWS][BLOCK_COLUMNS];
		static Bar bar=new Bar();
		static Ball ball=new Ball();
		static int barXTarget=bar.x;
		static int dir=0;//0:upright 1:downright 2:upleft 3:downleft
		static int ballSpeed=5;
		
		
		static class Ball{
			int x=CANVAS_WIDTH/2-BALL_WIDTH/2;
			int y=CANVAS_HEIGHT/2 -BALL_HEIGHT/2;
			int width=BALL_WIDTH;
			int height=BALL_HEIGHT;
			Point getCenter() {
				return new Point(x+(BALL_WIDTH/2),y+(BALL_HEIGHT/2));
				
			}
			Point getBottomCenter() {
				return new Point(x+(BALL_WIDTH/2),y+(BALL_HEIGHT));
			}
			Point getTopCenter() {
				return new Point(x+(BALL_WIDTH/2),y);
			}
			Point getLeftCenter() {
				return new Point(x,y+(BALL_HEIGHT/2));
			}
			
			Point getRightCenter() {
				return new Point(x+(BALL_WIDTH),y+(BALL_HEIGHT/2));
			}
		}
		
		static class Bar{
			int x=CANVAS_WIDTH/2-BAR_WIDTH/2;
			int y=CANVAS_HEIGHT-100;
			int width=BAR_WIDTH;
			int height=BAR_HEIGHT;
		}
		static class Block{
			int x=0;
			int y=0;
			int width=BLOCK_WIDTH;
			int height=BLOCK_HEIGHT;
			int color=0;//0:white 1:yellow 2:blue 3:pink 4:red
			boolean isHidden=false;//충돌하면 사라짐
		}

		static class MyPanel extends JPanel{
			public MyPanel() {
				this.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
				this.setBackground(Color.BLACK);
			}
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				Graphics2D g2d=(Graphics2D)g;
				
				drawUI(g2d);
			}
			private void drawUI(Graphics2D g2d) {
				for(int i=0; i<BLOCK_ROWS; i++) {
					for(int j=0; j<BLOCK_COLUMNS; j++) {
						if(blocks[i][j].isHidden) {
							continue;
						}
						if(blocks[i][j].color==0) {
							g2d.setColor(Color.WHITE);
						}
						else if(blocks[i][j].color==1) {
							g2d.setColor(Color.YELLOW);
						}
						else if(blocks[i][j].color==2) {
							g2d.setColor(Color.BLUE);
						}
						else if(blocks[i][j].color==3) {
							g2d.setColor(Color.MAGENTA);
						}
						else if(blocks[i][j].color==4) {
							g2d.setColor(Color.RED);
						}
						g2d.fillRect(blocks[i][j].x, blocks[i][j].y, blocks[i][j].width, blocks[i][j].height);
					}
					
					g2d.setColor(Color.WHITE);
					g2d.setFont(new Font("TimesRoman", Font.BOLD, 20));
					g2d.drawString("score: "+score, CANVAS_WIDTH/2-30, 20);
					
					g2d.setColor(Color.WHITE);
					g2d.fillOval(ball.x, ball.y, BALL_WIDTH, BALL_HEIGHT);
					
					g2d.setColor(Color.WHITE);
					g2d.fillRect(bar.x, bar.y, bar.width, bar.height);
					
				}
			}
		}
		
		public MyFrame(String title){
			super(title);
			this.setVisible(true);
			this.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
			this.setLocation(400,300);
			this.setLayout(new BorderLayout());
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			initData();
			myPanel=new MyPanel();
			this.add("Center", myPanel);
			setKeyListener();
			startTimer();
		}
		public void initData() {
			for(int i=0; i<BLOCK_ROWS; i++) {
				for(int j=0; j<BLOCK_COLUMNS; j++) {
					blocks[i][j]=new Block();
					blocks[i][j].x=BLOCK_WIDTH*j+BLOCK_GAP*j;
					blocks[i][j].y=100+i*BLOCK_HEIGHT+BLOCK_GAP*i;
					blocks[i][j].width=BLOCK_WIDTH;
					blocks[i][j].height=BLOCK_HEIGHT;
					blocks[i][j].color=4-i;
					blocks[i][j].isHidden=false;
				}
			}
		}
		public void setKeyListener() {
			this.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode()==KeyEvent.VK_LEFT) {
						System.out.println("Pressed Left Key");
						barXTarget -=20;
						if(bar.x<barXTarget) {
							barXTarget=bar.x;
						}
					}
					else if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
						System.out.println("Pressed Right Key");
						barXTarget +=20;
						if(bar.x>barXTarget) {
							barXTarget=bar.x;
						}
					}
				}
			});
		}
		public void startTimer() {
			timer =new Timer(20, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					movement();
					checkCollision();
					checkCollisionBlock();
					myPanel.repaint();
				}
			});
			timer.start();
		}
		public void movement() {
			if(bar.x<barXTarget) {
				bar.x+=5;
			}else if(bar.x>barXTarget) {
				bar.x-=5;
			}
			
			if(dir==0) {//upright
				ball.x+=ballSpeed;
				ball.y-=ballSpeed;
			}else if(dir==1) { //downright
				ball.x+=ballSpeed;
				ball.y+=ballSpeed;
			}else if(dir==2) { //up left
				ball.x-=ballSpeed;
				ball.y-=ballSpeed;
			}else if(dir==3) { //down left
				ball.x-=ballSpeed;
				ball.y+=ballSpeed;
			}
		}
		public boolean duplRect(Rectangle rect1, Rectangle rect2) {
			return rect1.intersects(rect2);
		}
		public void checkCollision() {
			if(dir==0) {//upright
				if(ball.y<0) {
					dir=1;
				}
				if(ball.x>CANVAS_WIDTH-BALL_WIDTH) {
					dir=2;
				}
				//wall
				
				//bar-none
				
			}else if(dir==1) { //downright
				if(ball.y>CANVAS_HEIGHT-BALL_HEIGHT-20) {
					//dir=0;
				System.exit(0);
				}
				if(ball.x>CANVAS_WIDTH-BALL_WIDTH) {
					dir=3;
				}
				
				if(ball.getBottomCenter().y>=bar.y) {
					if(duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height), new Rectangle(bar.x, bar.y, bar.width, bar.height))) {
						dir=0;
					}
				}
			}else if(dir==2) { //up left
				if(ball.y<0) {
					dir=3;
				}
				if(ball.x<0) {
					dir=0;
				}
				
				//bar-none
			}else if(dir==3) { //down left
				if(ball.y>CANVAS_HEIGHT-BALL_HEIGHT-20) {
					//dir=2;
					System.exit(0);
					
				}
				if(ball.x<0) {
					dir=1;
				}
				if(ball.getBottomCenter().y>=bar.y) {
					if(duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height), new Rectangle(bar.x, bar.y, bar.width, bar.height))) {
						dir=2;
					}
				}
			}
		}
		public void checkCollisionBlock() {
			for(int i=0; i<BLOCK_ROWS; i++) {
				for(int j=0; j<BLOCK_COLUMNS; j++) {
					Block block=blocks[i][j];
					if(block.isHidden==false) {
						if(dir==0) {
							if(duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height), new Rectangle(block.x, block.y, block.width, block.height))) {
								if(ball.x>block.x+2 && ball.getRightCenter().x<=block.x+block.width-2) {
									dir=1;
								}else {
									dir=2;
								}
								block.isHidden=true;
								if(block.color==0) {
									score+=10;
								}else if(block.color==1) {
									score+=20;
								}else if(block.color==2) {
									score+=30;
								}else if(block.color==3) {
									score+=40;
								}else if(block.color==4) {
									score+=50;
								}
							}
							
						}else if(dir==1) {
							if(duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height), new Rectangle(block.x, block.y, block.width, block.height))) {
								if(ball.x>block.x+2 && ball.getRightCenter().x<=block.x+block.width-2) {
									dir=0;
								}else {
									dir=3;
								}
								block.isHidden=true;
								if(block.color==0) {
									score+=10;
								}else if(block.color==1) {
									score+=20;
								}else if(block.color==2) {
									score+=30;
								}else if(block.color==3) {
									score+=40;
								}else if(block.color==4) {
									score+=50;
								}
								
							}
							
						}else if(dir==2) {
							if(duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height), new Rectangle(block.x, block.y, block.width, block.height))) {
								if(ball.x>block.x+2 && ball.getRightCenter().x<=block.x+block.width-2) {
									dir=3;
								}else {
									dir=0;
								}
								block.isHidden=true;
								if(block.color==0) {
									score+=10;
								}else if(block.color==1) {
									score+=20;
								}else if(block.color==2) {
									score+=30;
								}else if(block.color==3) {
									score+=40;
								}else if(block.color==4) {
									score+=50;
								}
							}
						}else if(dir==3) {
							if(duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height), new Rectangle(block.x, block.y, block.width, block.height))) {
								if(ball.x>block.x+2 && ball.getRightCenter().x<=block.x+block.width-2) {
									dir=2;
								}else {
									dir=1;
								}
								block.isHidden=true;
								if(block.color==0) {
									score+=10;
								}else if(block.color==1) {
									score+=20;
								}else if(block.color==2) {
									score+=30;
								}else if(block.color==3) {
									score+=40;
								}else if(block.color==4) {
									score+=50;
								}
							}
						}
					}
				}
			}
		}
	}
	public static void main(String[] args) {
		new MyFrame("Block Game");
	}

}

