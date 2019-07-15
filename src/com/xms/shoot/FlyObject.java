package com.xms.shoot;

import java.awt.image.BufferedImage;
/**飞行物类*/
public abstract class FlyObject {
	protected int width;//图片宽度
	protected int height;//图片的高度
	protected int x;//飞行物的横坐标
	protected int y;//飞行物的纵坐标
	protected BufferedImage image;//图片类型的变量
	public abstract void move();//运动方式
	//判断子弹是否打中飞行物
	public boolean shootBy(Bullet b) {
		int x=b.x;
		int y=b.y;
		boolean b1=x>this.x-b.width&&x<this.x+width&&
					y>this.y-b.height&&y<this.y+height;
		return b1;
	}
	public abstract boolean isOutBound();
		
	
}
