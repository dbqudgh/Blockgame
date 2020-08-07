
//자바 이벤트 핸들링
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class BloockGame {

	
	static class MyFrame extends JFrame{
		
		static int BALL_SIZE = 20;
		
		static int BLOCK_ROWS = 5;
		
		static int BLOCK_COLUMNS = 10;
		static int BLOCK_WIDTH = 40;
		
		static int BLOCK_HEIGHT = 20;
		static int BLOCK_GAP = 3;
		
		static int BAR_WIDTH = 80;
		static int BAR_HEIGHT = 20;
		
		static int CANVAS_WIDTH = 400+(BLOCK_GAP*(BLOCK_COLUMNS-1));
		static int  CANVAS_HEIGHT = 600;

		static int score = 0;
		
		static int ballSpeed = 5;
		static int dir = 0; //0: 오른쪽위, 1:오른쪽 아래, 2:왼쪽위, 3: 왼쪽 아래
		
		static Timer timer = null;
		
		static Bar bar = new Bar();
		
		static Ball  ball = new Ball();
		
		static Block[][] blocks = new Block[BLOCK_ROWS][BLOCK_COLUMNS];
		static MyPanel myPanel = null;
		
		
		static class Bar{
			
			int x =  CANVAS_WIDTH/2-50;
			int y = CANVAS_HEIGHT-100;
			int width = BAR_WIDTH;
			int height = BAR_HEIGHT;
			
			
		}
		
		static class Ball{
			
			int x = CANVAS_WIDTH/2-20;
			int y = CANVAS_HEIGHT/2;
			int width = BALL_SIZE;
			int height = BALL_SIZE;
			
		}
		
		static class Block{
			
			int x = 0;
			int y = 0;
			int width = BLOCK_WIDTH;
			int height = BLOCK_HEIGHT;
			int color=0;
			
			boolean isHidden = false;
			
			
		}
		
		static class MyPanel extends JPanel{
			
			public MyPanel() {
			
				this.setSize(CANVAS_WIDTH,CANVAS_HEIGHT);
				this.setBackground(Color.BLACK);
				
			}
			
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				Graphics2D g2d = (Graphics2D) g;
				drawUI(g2d);
			}
			
			private void drawUI(Graphics2D g2d) {
				
				for(int i = 0; i<BLOCK_ROWS; i++) {
					for(int j = 0; j<BLOCK_COLUMNS;j++) {
						if(blocks[i][j].isHidden) {
							continue;
						}
						if(blocks[i][j].color==0) {
							g2d.setColor(Color.WHITE);
						}else if(blocks[i][j].color==1) {
							g2d.setColor(Color.YELLOW);
						}else if(blocks[i][j].color==2) {
							g2d.setColor(Color.BLUE);
						}else if(blocks[i][j].color==3) {
							g2d.setColor(Color.PINK);
						}else if(blocks[i][j].color==4) {
							g2d.setColor(Color.RED);
						}
						g2d.fillRect(blocks[i][j].x,blocks[i][j].y,blocks[i][j].width,blocks[i][j].height);
						
					}
					
					g2d.setColor(Color.WHITE);
					g2d.fillOval(ball.x, ball.y, BALL_SIZE, BALL_SIZE);
					
					g2d.setColor(Color.WHITE);
					g2d.fillRect(bar.x, bar.y, bar.width, bar.height);
				}
				
			}
			
			
			
			
			
		}
		
		
		public MyFrame(String Title) {
			
			super(Title);
			
			this.setVisible(true);
			this.setSize(CANVAS_WIDTH,CANVAS_HEIGHT);
			this.setLocation(400,300);
			this.setLayout(new BorderLayout());
			
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			myPanel = new MyPanel();
			this.add("Center",myPanel);
			
			initData();
			setKeyListener();
			startTimer();
			
		}
		
		
		public void initData() {
			
			for(int i = 0; i<BLOCK_ROWS;i++) {
				for(int j = 0; j<BLOCK_COLUMNS;j++) {
					
					blocks[i][j]= new Block();
					blocks[i][j].x=BLOCK_WIDTH*j+BLOCK_GAP*j;
					blocks[i][j].y=100+i*BLOCK_HEIGHT+BLOCK_GAP*i;
					blocks[i][j].width=BLOCK_WIDTH;
					blocks[i][j].height=BLOCK_HEIGHT;
					blocks[i][j].color=i;
					blocks[i][j].isHidden = false;
					
							
				}
			}
			
			
		}
		
		
		public void setKeyListener() {
			
		}
		public void startTimer() {
			
		}
	
		
	}
	
	
	
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		new MyFrame("BlockGame");
		
		
	}

}

