package com.xms.shoot;
/**奖励*/
public interface Award {
	public static final  int LIFE=1;//1代表加生命
	public static final  int DOUBLE_FIRE=0;//0代表设置单火力
	public abstract int getType();
	

}
