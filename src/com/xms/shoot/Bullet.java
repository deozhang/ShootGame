package com.xms.shoot;
/**子弹:属于飞行物*/
public class Bullet extends FlyObject {
	private int speed;//子弹速度
	//构造器中的参数,x,y代表英雄机的坐标
	public Bullet(int x,int y){
		speed=3;
		this.x=x;
		this.y=y;
		image=ShootGame.bullet;
		width=image.getWidth();
		height=image.getHeight();
		
		
	}
	@Override
	public void move() {
		// TODO Auto-generated method stub
		y-=speed;
		
	}
	@Override
	public boolean isOutBound() {
		
		return y<-height;
	}

}
