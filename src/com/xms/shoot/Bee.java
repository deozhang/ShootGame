package com.xms.shoot;

import java.util.Random;

/**小蜜蜂:属于飞行物  奖励*/
public class Bee extends FlyObject implements Award{
	private int xspeed;//x方向速度
	private int yspeed;//y方向速度
	private int awardType;//奖励类型
	public Bee(){
		xspeed=1;
		yspeed=2;
		image=ShootGame.bee;
		width=image.getWidth();
		height=image.getHeight();
		y=-height;
		Random rd=new Random();
		x=rd.nextInt(ShootGame.WIDTH-width);
		awardType=rd.nextInt(2);
	}
	
	@Override
	public void move() {
		x+=xspeed;
		y+=yspeed;
		if(x>ShootGame.WIDTH-width){
			xspeed=-1;
		}
		if(x<0){
			xspeed=1;
		}
		
	}
	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return awardType;
	}

	@Override
	public boolean isOutBound() {
		return y>ShootGame.HEIGHT;
	}
	

}
