package com.xms.shoot;
/**�ӵ�:���ڷ�����*/
public class Bullet extends FlyObject {
	private int speed;//�ӵ��ٶ�
	//�������еĲ���,x,y����Ӣ�ۻ�������
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
