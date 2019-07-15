package com.xms.shoot;

import java.awt.image.BufferedImage;

/**英雄机类: 飞行物*/
public class Hero extends FlyObject{
	private int life;//生命
	private int doubleFire;//双倍火力
	private BufferedImage[] images;
	
	public Hero(){
		life=3;
		doubleFire=0;//0代表单倍 1代表双倍
		images=new BufferedImage[]{ShootGame.hero0,ShootGame.hero1};
		image=ShootGame.hero0;
		width=image.getWidth();
		height=image.getHeight();
		x=150;
		y=450;
	}
	
	public int getLife() {
		return life;
	}
	public void setLife(int life) {
		this.life = life;
	}
	public int getDoubleFire() {
		return doubleFire;
	}
	public void setDoubleFire(int doubleFire) {
		this.doubleFire = doubleFire;
	}
	
	
	//图片切换
	int index=0;
	@Override
	public void move() {
		int num=index++/10%images.length;
		image=images[num];
	}
	public void moveto(int x,int y) {
		this.x=x-width/2;
		this.y=y-height/2;
		
	}
	public Bullet[] shoot() {
		int xstep=width/4;
		Bullet[] bullets;
		if(doubleFire==0){
			bullets=new Bullet[1];
			bullets[0]=new Bullet(x+xstep*2, y);
		}else{
			 bullets=new Bullet[2];
			bullets[0]=new Bullet(x+xstep, y);
			bullets[1]=new Bullet(x+xstep*3, y);
		}
		return bullets;
	}

	@Override
	public boolean isOutBound() {
		
		return false;//永远不会出界
	}
	/**判断飞行物和英雄机相撞*/
	public boolean hit(FlyObject f) {
		int herox=this.x+width/2;
		int heroy=this.y+height/2;
		int x1=f.x-width/2;
		int x2=f.x+f.width+width/2;
		int y1=f.y-height/2;
		int y2=f.y+f.height+height/2;
		
		return herox>x1&&herox<x2&&heroy>y1&&heroy<y2;
	}
	/**减少生命值*/
	public void subLife() {
		life--;
		
	}
	/**增加生命值*/
	public void sumLife() {
		life++;
		
	}
		
	
}





















