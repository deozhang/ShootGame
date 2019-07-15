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
	//	JFrame  画框        JPanel  画板
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
	private Hero hero=new Hero();//英雄机
	private Bullet[] bullets={};//子弹类型的数组
	private FlyObject[] flyobject={};//飞行物:小蜜蜂  敌机
	private int score;
	public static int state;
	public static final int START=0;
	public static final int RUNNING=1;
	public static final int PAUSE=2;
	public static final int END=3;
	//图片,视频,..需要静态代码加载
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
	
	//程序入口
	public static void main(String[] args) {
		JFrame jf=new JFrame("飞机大战");//画框
		ShootGame sg=new ShootGame();
		jf.add(sg);//将画板添加到框体中
		jf.setSize(ShootGame.WIDTH, ShootGame.HEIGHT);//设置框体的宽高
		jf.setLocationRelativeTo(null);//设置框体的位置
		jf.setResizable(false);//不能让窗口最大化
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置关闭窗口的方法
		jf.setVisible(true);//显示 尽快调用paint方法
		sg.start();
	}
	public void start(){
		//添加鼠标事件 
		//焦点
		MouseAdapter i=new MouseAdapter() {
			public void mouseMoved(MouseEvent e){
				if(state==RUNNING){
					int x=e.getX();
					int y=e.getY();
					hero.moveto(x,y); 
				}
				
			}
			//点击
			public void mouseClicked(MouseEvent e){
				switch (state) {
				case START:
					state=RUNNING;
					break;
				case END://归零所有状态归零
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
			//划出
			public void mouseExited(MouseEvent e){
				if(state!=END&&state!=START){
					state=PAUSE;
				}
			}
			//滑入
			public void mouseEntered(MouseEvent e){
				if(state==PAUSE){
					state=RUNNING;
				}
			}
		};
		//给当前面板增加鼠标点击操作监听
		this.addMouseListener(i);
		//给当前面板增加鼠标滑动监听
		this.addMouseMotionListener(i);
		//
	//定时器 让飞行物和子弹入场
		int intval=10;//毫秒
		Timer timer =new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(state==RUNNING){
				//让飞行物入场
				enterAction();
				//让飞行物移动
				stepAction();
				//让子弹出场
				shootAction();
				//检查子弹是否打到飞行物
				checkAction();
				//越界检查
				checkOutofAction();
				//判断英雄是否撞到飞行物
				checkHeroAction();
				//判断游戏结束，游戏状态
				checkGameover();
				}
				//重绘
				repaint();
			}
		},intval,intval);
		
	}
	/**判断游戏结束*/
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
	/**飞行物:蜜蜂和敌机碰撞英雄机掉血,火力重置*/
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
	/**子弹飞行物,越界缩容*/
	public void checkOutofAction() {
		//缩容蜜蜂和敌机
		for(int i=0;i<flyobject.length;i++){
			FlyObject f=flyobject[i];
			if(f.isOutBound()){
				FlyObject temp=flyobject[i];
				flyobject[i]=flyobject[flyobject.length-1];
				flyobject[flyobject.length-1]=temp;
				flyobject=Arrays.copyOf(flyobject, flyobject.length-1);
				
			}
		}
		//缩容子弹
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

	/**判断子弹是否击中飞行物*/
	public void checkAction() {
		for(int i=0;i<bullets.length;i++){
			Bullet b=bullets[i];
			//判断子弹是否和飞行物相撞
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
	/**判断子弹是否和飞行物相撞*/
	public boolean check(Bullet b) {
		int indexfly=-1;
		for(int i=0;i<flyobject.length;i++){
			FlyObject fly=flyobject[i];
			if(fly.shootBy(b)){
				//记录飞行物的坐标
				indexfly=i;
				break;
			}
		}
		//缩容
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
	/**子弹出场*/
	private int shootindex=0; 
	public void shootAction() {
		if(shootindex++%30==0){
			Bullet[] bs=hero.shoot();
			//扩容
			bullets=Arrays.copyOf(bullets, bs.length+bullets.length);
			System.arraycopy(bs, 0, bullets, bullets.length-bs.length, bs.length);
			System.out.println("0000");
		}
	}

	/**飞行物入场的方法*/
	private int index=0;
	public void enterAction() {
		//产生飞行物
		if(index++%20==0){
			FlyObject o=nextOne();
			//把数组扩容
			flyobject=Arrays.copyOf(flyobject, flyobject.length+1);
			//把产生的数组放到扩容后数组的最后一位
			flyobject[flyobject.length-1]=o;
			
			
		}
		
	}
	/**工厂方法,随机产生小蜜蜂和敌机*/
	public FlyObject nextOne() {
		Random rd=new Random();
		int i=rd.nextInt(10);
		if(i==0){
			return new Bee();
		}else{
			return new Airplane();
		}
	}
	/**飞行物移动*/
	public void stepAction() {
		for(int i=0;i<flyobject.length;i++){
			flyobject[i].move();
		}
		hero.move();
		for(int i=0;i<bullets.length;i++){
			bullets[i].move();
		}
	}
	//绘画方法
	public void paint(Graphics g){
		g.drawImage(background, 0, 0, null);
		g.setFont(new Font("楷体",25,15));
		g.drawString("分数:"+score, 15, 30);
		g.drawString("生命值:"+hero.getLife(), 15, 45);
		//画英雄机
		paintHero(g);
		//画子弹
		paintBullet(g);
		//敌机和小蜜蜂
		paintFlyObject(g);
		//判断游戏状态
		paintstate(g); 
		
	}
	/**画游戏状态的图片*/
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
	/**画英雄机的方法*/
	public void paintHero(Graphics g){
		
		g.drawImage(hero.image, hero.x, hero.y, null);
	}
	/**画子弹*/
	public void paintBullet(Graphics g){
		for(int i=0;i<bullets.length;i++){
			g.drawImage(bullets[i].image, bullets[i].x, bullets[i].y, null);
		}
	}
	/**小蜜蜂和敌机*/
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






































































