package shootingGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class FrameMake  extends JFrame implements KeyListener, Runnable{
	Label lab1;
	//0.
	int width = 800;
	int height = 600;
	//2.
	//이미지를 불러오기위한 툴킷
	Toolkit tk = Toolkit.getDefaultToolkit();
	Image meImg = tk.getImage("1.png");
	Image Missale;
	Image EnemyImage;
	Image BackGround_img;
	
	ArrayList MissaleList = new ArrayList();
	ArrayList EnemyList = new ArrayList();
	
	
	int score;
	int x, y;
	int cnt;
	
	int[] cx = {0, 0, 0}; //구름 스크롤 속도 제어
	int bx = 0; //전체 배경 스크롤
	
	boolean keyUp = false;
	boolean keyDown = false;
	boolean keyLeft = false;
	boolean keyRight = false;
	private boolean keyBar = false;
	Thread th;
	Image buffImage;
	Graphics buffg;
	Missile ms;
	Enemy en;
	
	int e_w, e_h; // 적 이미지의 크기를 가져올수 있는 변수
	int m_w, m_h; // 포탄 이미지의 크기를 가져올수 있는 변수
	
	public FrameMake() {
		Frame f = new Frame();
		
		 setLayout(new FlowLayout());
		 
		 lab1 = new Label("score : " + score);
		
		add(lab1);
		
//		setBounds(500, 500, 1000, 800);
//		setSize(1000,1000);
		lab1.setPreferredSize(new Dimension(100, 50));
		
		setVisible(true);
		//1.
		//나중을 위한 프레임에 들어갈 컴포넌트 세팅 메소드
		init();
		
		//시작 명령 처리 부분
		start();
		//프레임의 이름을 설정
		setTitle("슈팅게임 by 박민규");
		//프레임의 크기를 위해
		setSize(width, height);
		//화면의 중앙에 프레임을 위치하기 위한 동작
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		
		int xpos = (int)(screen.getWidth() / 2 - (width / 2));
		int ypos = (int)(screen.getHeight() / 2 - (height / 2));

		//화면에 프레임을 위치시킨다.
		setLocation(xpos, ypos);
		//프레임 크기 변경하지 못하게 하기 위해
		setResizable(false);
		//프레임을 보이게 하기 위해
		setVisible(true);
		
	}

	private void init() {
		x = 100;
		y = 100;
		Missale = tk.getImage("3.png");
		EnemyImage = tk.getImage("2.png");
		BackGround_img = tk.getImage("background.png");
		
		e_w = ImageWidthValue("2.png");
		e_h = ImageHeightValue("2.png");

		m_w = ImageWidthValue("3.png");
		m_h = ImageHeightValue("3.png");
	}
	
	private int ImageHeightValue(String File) {
		int y = 0;
		try {
			File f = new File(File);
			BufferedImage hi = ImageIO.read(f);
			y = hi.getHeight();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return y;
	}

	private int ImageWidthValue(String File) {
		// 이미지의 크기를 계산하는 메소드
		int x = 0;
		try {
			File f = new File(File);
			BufferedImage hi = ImageIO.read(f);
			x = hi.getWidth();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return x;
	}

	private void start() {
		//프로그램이 정상적으로 종료되도록 만들어주는 메서드 , x(닫기) 버튼을 누르면 프로그램 종료하도록 한다.
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addKeyListener(this);
		th = new Thread(this);
		th.start();
		
	}

	//3.
	@Override
	public void paint(Graphics g) {
		//화면을 0,0 ~ width,height 만큼 꺠끗이 치운다
//		g.clearRect(0, 0, width, height);
//		//meImg 를 100,100에 생성
//		g.drawImage(meImg, 100, 100, this);
//		//meImg 를 400,100 위치에 100,20의 크기로 생성
		buffImage = createImage(width, height);
		buffg = buffImage.getGraphics();
		
		update(g);
	}
	
	@Override
	public void update (Graphics g) {
		Draw_Background();
		Draw_Char();
		Draw_Missile();
		Draw_Enemy();
		g.drawImage(buffImage, 0, 0, this);
	}
	
	
	private void Draw_Background() {
		// TODO Auto-generated method stub
		buffg.clearRect(0, 0, width, height);
		if(bx > -1000) {
			buffg.drawImage(BackGround_img, bx, 0, this);
			bx -= 10;
		} else {
			bx = 0;
		}
	}

	private void Draw_Enemy() {
		for(int i = 0; i < EnemyList.size(); i++) {
			en = (Enemy)(EnemyList.get(i));
			buffg.drawImage(EnemyImage, en.x, en.y, this);
		}
	}

	private void Draw_Missile() {
		// TODO Auto-generated method stub
		for(int i = 0; i<MissaleList.size(); i++) {
			ms = (Missile)(MissaleList.get(i));
			
			
			buffg.drawImage(Missale, ms.x + 100, ms.y+20, this);
			ms.move();
			
			if(ms.x > width){
				MissaleList.remove(i);
			}
		}
	}

	private void Draw_Char() {
		buffg.drawImage(meImg, x, y, this);
	}

	public static void main(String[] args) {
		FrameMake fm = new FrameMake();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			while(true) {
				KeyProcess();
				MissilProcess();
				EnemyProcess();
				repaint();
				Thread.sleep(20);
				cnt++;
			}
		} catch (Exception e) {
			
		}
	}

	private void EnemyProcess() {
		for(int i = 0; i < EnemyList.size(); i++) {
		en = (Enemy)(EnemyList.get(i));
		en.move();
		if(en.x < -200) {
			EnemyList.remove(i);
		}
		}
		if(cnt % 300 == 0) {
			en = new Enemy(width+100, 100);
			EnemyList.add(en);
			en = new Enemy(width+100, 200);
			EnemyList.add(en);
			en = new Enemy(width+100, 300);
			EnemyList.add(en);
			en = new Enemy(width+100, 400);
			EnemyList.add(en);
			en = new Enemy(width+100, 500);
			EnemyList.add(en);
		}
	}

	private void MissilProcess() {
		if(keyBar == true) {
			ms = new Missile(x,y);
			MissaleList.add(ms);
		}
		for(int i =0; i < MissaleList.size(); i++) {
			ms = (Missile)(MissaleList.get(i));
			ms.move();
			if(ms.x > width -20) {
				MissaleList.remove(i);
			}
			for(int j = 0; j < EnemyList.size(); j ++) {
				en = (Enemy)(EnemyList.get(j));
				if(crash(ms.x,ms.y,en.x,en.y,m_w, m_h , e_w, e_h) ) {
					MissaleList.remove(i);
					EnemyList.remove(j);
					score += 100;
					lab1.setText("score : " + score);
				}
					
			}
		}
	}

	private boolean crash(int x1, int y1, int x2, int y2, int w1, int h1, int w2, int h2) {
		boolean check = false;
		if(Math.abs((x1 + w1 /2 ) - (x2 + w2 / 2 )) < (w2 /2 + w1 /2) && Math.abs((y1 + h1 /2 ) - (y2 + h2 / 2 )) < (h2 /2 + h1 /2)){
			check = true;
		}else {
			check = false;
		}
		return check;
	}

	private void KeyProcess() {
		if(keyUp == true) {
			if(y > 35) {
				y -= 10;
			}
		}
		if(keyDown == true) {
			if(y < 565) {
				y += 10;
			}
		}
		if(keyLeft == true) {
			if(x > 5) {
				x -= 10;
			}
		}
		if(keyRight == true) {
			if(x < 695) {

				x += 10;
			}
			
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP: {
			keyUp = true;
			break;
		}
		case KeyEvent.VK_DOWN: {
			keyDown = true;
			break;
		}
		case KeyEvent.VK_LEFT: {
			keyLeft = true;
			break;
		}
		case KeyEvent.VK_RIGHT: {
			keyRight = true;
			break;
		}
		case KeyEvent.VK_SPACE: {
			keyBar = true;
			break;
		}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP: {
			keyUp = false;
			break;
		}
		case KeyEvent.VK_DOWN: {
			keyDown = false;
			break;
		}
		case KeyEvent.VK_LEFT: {
			keyLeft = false;
			break;
		}
		case KeyEvent.VK_RIGHT: {
			keyRight = false;
			break;
		}
		case KeyEvent.VK_SPACE: {
			keyBar = false;
			break;
		}
		}
	}
	
}