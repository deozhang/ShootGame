package com.xms.shoot;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ShootGame extends JPanel{
	//	JFrame  ����        JPanel  ����
	public static final int WIDTH=380;
	public static final int HEIGHT=650;
	public static BufferedImage background;
	public static BufferedImage bee;
	public static BufferedImage hero0;
	public static BufferedImage hero1;
	public static BufferedImage airplane;
	public static BufferedImage bullet;
	public static BufferedImage start;
	public static BufferedImage pasue;
	public static BufferedImage gameover;
	private Hero hero=new Hero();//Ӣ�ۻ�
	private Bullet[] bullets={};//�ӵ����͵�����
	private FlyObject[] flyobject={};//������:С�۷�  �л�
	private int score;
	public static int state;
	public static final int START=0;
	public static final int RUNNING=1;
	public static final int PAUSE=2;
	public static final int END=3;
	//ͼƬ,��Ƶ,..��Ҫ��̬�������
	static{
		try {
			background=ImageIO.read
			(ShootGame.class.getResource("background.png"));
			bee=ImageIO.read(ShootGame.class.getResource("bee.png"));
			hero0=ImageIO.read(ShootGame.class.getResource("hero0.png"));
			hero1=ImageIO.read(ShootGame.class.getResource("hero1.png"));
			airplane=ImageIO.read(ShootGame.class.getResource("airplane.png"));
			bullet=ImageIO.read(ShootGame.class.getResource("bullet.png"));
			start=ImageIO.read(ShootGame.class.getResource("start.png"));
			pasue=ImageIO.read(ShootGame.class.getResource("pasue.png"));
			gameover=ImageIO.read(ShootGame.class.getResource("gameover.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//�������
	public static void main(String[] args) {
		JFrame jf=new JFrame("�ɻ���ս");//����
		ShootGame sg=new ShootGame();
		jf.add(sg);//��������ӵ�������
		jf.setSize(ShootGame.WIDTH, ShootGame.HEIGHT);//���ÿ���Ŀ��
		jf.setLocationRelativeTo(null);//���ÿ����λ��
		jf.setResizable(false);//�����ô������
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//���ùرմ��ڵķ���
		jf.setVisible(true);//��ʾ �������paint����
		sg.start();
	}
	public void start(){
		//�������¼� 
		//����
		MouseAdapter i=new MouseAdapter() {
			public void mouseMoved(MouseEvent e){
				if(state==RUNNING){
					int x=e.getX();
					int y=e.getY();
					hero.moveto(x,y); 
				}
				
			}
			//���
			public void mouseClicked(MouseEvent e){
				switch (state) {
				case START:
					state=RUNNING;
					break;
				case END://��������״̬����
					hero=new Hero();
					flyobject =new FlyObject[0];
					bullets =new Bullet[0];
					score=0;
					state=START;
					break;

				default:
					break;
				}
			}
			//����
			public void mouseExited(MouseEvent e){
				if(state!=END&&state!=START){
					state=PAUSE;
				}
			}
			//����
			public void mouseEntered(MouseEvent e){
				if(state==PAUSE){
					state=RUNNING;
				}
			}
		};
		//����ǰ��������������������
		this.addMouseListener(i);
		//����ǰ���������껬������
		this.addMouseMotionListener(i);
		//
	//��ʱ�� �÷�������ӵ��볡
		int intval=10;//����
		Timer timer =new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(state==RUNNING){
				//�÷������볡
				enterAction();
				//�÷������ƶ�
				stepAction();
				//���ӵ�����
				shootAction();
				//����ӵ��Ƿ�򵽷�����
				checkAction();
				//Խ����
				checkOutofAction();
				//�ж�Ӣ���Ƿ�ײ��������
				checkHeroAction();
				//�ж���Ϸ��������Ϸ״̬
				checkGameover();
				}
				//�ػ�
				repaint();
			}
		},intval,intval);
		
	}
	/**�ж���Ϸ����*/
	public void checkGameover() {
		if(isover()){
			state=END;
		}
	}
	private boolean isover() {
		if(hero.getLife()<=0){
			return true;
		}else{
			return false;
		}
	}
	/**������:�۷�͵л���ײӢ�ۻ���Ѫ,��������*/
	public void checkHeroAction() {
		for(int i=0;i<flyobject.length;i++){
			int index=-1;
			FlyObject f=flyobject[i];
			if(hero.hit(f)){
		
				if(hero.getLife()>0){
					hero.subLife();	
					hero.setDoubleFire(0);
					index=i;
				if(index!=-1){
					FlyObject temp1=flyobject[index];
					flyobject[index]=flyobject[flyobject.length-1];
					flyobject[flyobject.length-1]=temp1;
					flyobject=Arrays.copyOf(flyobject, flyobject.length-1);
				}
			}
			}
			
		}
		
	}
	/**�ӵ�������,Խ������*/
	public void checkOutofAction() {
		//�����۷�͵л�
		for(int i=0;i<flyobject.length;i++){
			FlyObject f=flyobject[i];
			if(f.isOutBound()){
				FlyObject temp=flyobject[i];
				flyobject[i]=flyobject[flyobject.length-1];
				flyobject[flyobject.length-1]=temp;
				flyobject=Arrays.copyOf(flyobject, flyobject.length-1);
				
			}
		}
		//�����ӵ�
		for(int i=0;i<bullets.length;i++){
			Bullet b=bullets[i];
			if(b.isOutBound()){
				Bullet temp=bullets[i];
				bullets[i]=bullets[bullets.length-1];
				bullets[bullets.length-1]=temp;
				bullets=Arrays.copyOf(bullets, bullets.length-1);
			}
		}
		
	}

	/**�ж��ӵ��Ƿ���з�����*/
	public void checkAction() {
		for(int i=0;i<bullets.length;i++){
			Bullet b=bullets[i];
			//�ж��ӵ��Ƿ�ͷ�������ײ
			boolean flag=check(b);/////////?
			if(flag){
				int index=i;
				Bullet temp=bullets[index];
				bullets[index]=bullets[bullets.length-1];
				bullets[bullets.length-1]=temp;
				bullets=Arrays.copyOf(bullets, bullets.length-1);
				
			}
		}
		
	}
	/**�ж��ӵ��Ƿ�ͷ�������ײ*/
	public boolean check(Bullet b) {
		int indexfly=-1;
		for(int i=0;i<flyobject.length;i++){
			FlyObject fly=flyobject[i];
			if(fly.shootBy(b)){
				//��¼�����������
				indexfly=i;
				break;
			}
		}
		//����
		 if(indexfly!=-1){
			FlyObject f=flyobject[indexfly]; 
			FlyObject flying=flyobject[indexfly]; 
			flyobject[indexfly]=flyobject[flyobject.length-1];
			flyobject[flyobject.length-1]=flying;
			flyobject=Arrays.copyOf(flyobject, flyobject.length-1);
			if(f instanceof Airplane){
				Airplane airp=(Airplane)f;
				int sc=airp.getScore();
				score+=sc;
			}else{
				Bee bee=(Bee)f;
				int flag=bee.getType();
				if(flag==0){
				//**********************************
					hero.setDoubleFire(1);
				}else{
//					if(hero.getLife()){*******************
//					}
					int life=hero.getLife();
					life++;
					hero.setLife(life);
					//hero.setLife(hero.getLife()+1);
				}
			}
			return true;
		 }else{
			return false;
		 }
		 
	}
	/**�ӵ�����*/
	private int shootindex=0; 
	public void shootAction() {
		if(shootindex++%30==0){
			Bullet[] bs=hero.shoot();
			//����
			bullets=Arrays.copyOf(bullets, bs.length+bullets.length);
			System.arraycopy(bs, 0, bullets, bullets.length-bs.length, bs.length);
			System.out.println("0000");
		}
	}

	/**�������볡�ķ���*/
	private int index=0;
	public void enterAction() {
		//����������
		if(index++%20==0){
			FlyObject o=nextOne();
			//����������
			flyobject=Arrays.copyOf(flyobject, flyobject.length+1);
			//�Ѳ���������ŵ����ݺ���������һλ
			flyobject[flyobject.length-1]=o;
			
			
		}
		
	}
	/**��������,�������С�۷�͵л�*/
	public FlyObject nextOne() {
		Random rd=new Random();
		int i=rd.nextInt(10);
		if(i==0){
			return new Bee();
		}else{
			return new Airplane();
		}
	}
	/**�������ƶ�*/
	public void stepAction() {
		for(int i=0;i<flyobject.length;i++){
			flyobject[i].move();
		}
		hero.move();
		for(int i=0;i<bullets.length;i++){
			bullets[i].move();
		}
	}
	//�滭����
	public void paint(Graphics g){
		g.drawImage(background, 0, 0, null);
		g.setFont(new Font("����",25,15));
		g.drawString("����:"+score, 15, 30);
		g.drawString("����ֵ:"+hero.getLife(), 15, 45);
		//��Ӣ�ۻ�
		paintHero(g);
		//���ӵ�
		paintBullet(g);
		//�л���С�۷�
		paintFlyObject(g);
		//�ж���Ϸ״̬
		paintstate(g); 
		
	}
	/**����Ϸ״̬��ͼƬ*/
	public void paintstate(Graphics g) {
		switch (state) {
		case START:
			g.drawImage(start, 0, 0, null);
			break;
		case PAUSE:
			g.drawImage(pasue, 0, 0, null);
			break;
		case END:
			g.drawImage(gameover, 0, 0, null);
			break;

		default:
			break;
		}
	}
	/**��Ӣ�ۻ��ķ���*/
	public void paintHero(Graphics g){
		
		g.drawImage(hero.image, hero.x, hero.y, null);
	}
	/**���ӵ�*/
	public void paintBullet(Graphics g){
		for(int i=0;i<bullets.length;i++){
			g.drawImage(bullets[i].image, bullets[i].x, bullets[i].y, null);
		}
	}
	/**С�۷�͵л�*/
	public void paintFlyObject(Graphics g){
		for(int i=0;i<flyobject.length;i++){
			FlyObject f=flyobject[i];
			if(f instanceof Airplane){
				Airplane a=(Airplane)f;
				g.drawImage(a.image, a.x, a.y, null);
			}else if(f instanceof Bee){
				Bee b=(Bee)f;
				g.drawImage(b.image, b.x, b.y, null);
			}
		}
	}

}






































































