package com.xms.shoot;

import java.util.Random;

/**敌机:飞行物    分数*/
public class Airplane extends FlyObject implements Enemy {
	private int speed;//下落速度
	
	public Airplane(){
		speed=2;
		image=ShootGame.airplane;
		width=image.getWidth();
		height=image.getHeight();
		y=-height;
		Random rd=new Random();
		x=rd.nextInt(ShootGame.WIDTH-width);
	}
	@Override
	public void move() {
		y+=speed;
	}
	/**打一个敌机得1分*/
	@Override
	public int getScore() {
		return 1;
	}
	@Override
	public boolean isOutBound() {
		
		return y>ShootGame.HEIGHT;
	}

}
