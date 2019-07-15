package com.xms.shoot;

import java.util.Random;

/**�л�:������    ����*/
public class Airplane extends FlyObject implements Enemy {
	private int speed;//�����ٶ�
	
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
	/**��һ���л���1��*/
	@Override
	public int getScore() {
		return 1;
	}
	@Override
	public boolean isOutBound() {
		
		return y>ShootGame.HEIGHT;
	}

}
