package com.xms.shoot;

import java.util.Random;

/**С�۷�:���ڷ�����  ����*/
public class Bee extends FlyObject implements Award{
	private int xspeed;//x�����ٶ�
	private int yspeed;//y�����ٶ�
	private int awardType;//��������
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
