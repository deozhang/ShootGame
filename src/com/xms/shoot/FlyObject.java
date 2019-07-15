package com.xms.shoot;

import java.awt.image.BufferedImage;
/**��������*/
public abstract class FlyObject {
	protected int width;//ͼƬ���
	protected int height;//ͼƬ�ĸ߶�
	protected int x;//������ĺ�����
	protected int y;//�������������
	protected BufferedImage image;//ͼƬ���͵ı���
	public abstract void move();//�˶���ʽ
	//�ж��ӵ��Ƿ���з�����
	public boolean shootBy(Bullet b) {
		int x=b.x;
		int y=b.y;
		boolean b1=x>this.x-b.width&&x<this.x+width&&
					y>this.y-b.height&&y<this.y+height;
		return b1;
	}
	public abstract boolean isOutBound();
		
	
}
