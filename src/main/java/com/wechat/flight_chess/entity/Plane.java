package com.wechat.flight_chess.entity;

import com.wechat.flight_chess.data.GameData;

/**
 * ��Ϸ�ɻ���
 */
public class Plane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * �ɻ�����
	 * blue��ɫ�ɻ�
	 * red��ɫ�ɻ�
	 * green��ɫ�ɻ�
	 * orange��ɫ�ɻ�
	 */
	private final String planeType ;

	/**
	 * �ǲ�����ҿ��Ƶ�����
	 */
	private boolean isPlayer ;
	
	/**
	 * �ɻ��Ƿ��ڼ�
	 */
	private boolean isAtHome = true ;

	/**
	 * �ɻ��ں����ϵ�λ��
	 */
	private int loc = -2 ;
	
	/**
	 * �ɻ����ϼҵ����
	 */
	private int index = -1 ;

	public static final String TYPE_RED = "red" ;
	
	public static final String TYPE_BLUE = "blue" ;
	
	public static final String TYPE_GREEN = "green" ;
	
	public static final String TYPE_ORANGE = "orange" ;
	
	private GameData data ;
	
	
	public Plane(String type ,boolean isPlayer,int index,GameData data){
		this.setData(data) ;
		this.setIndex(index) ;
		this.planeType = type ;
		this.setPlayer(isPlayer) ;
		
	}

	/**
	 * @return the loc
	 */
	public int getLoc() {
		return loc;
	}

	/**
	 * @param loc the loc to set
	 */
	public void setLoc(int loc) {
		this.loc = loc;
	}

	/**
	 * @return the planeType
	 */
	public String getPlaneType() {
		return planeType;
	}

	/**
	 * @return the isPlayer
	 */
	public boolean isPlayer() {
		return isPlayer;
	}

	/**
	 * @param isPlayer the isPlayer to set
	 */
	public void setPlayer(boolean isPlayer) {
		this.isPlayer = isPlayer;
	}

	/**
	 * @return the isAtHome
	 */
	public boolean isAtHome() {
		return isAtHome;
	}

	/**
	 * @param isAtHome the isAtHome to set
	 */
	public void setAtHome(boolean isAtHome) {
		this.isAtHome = isAtHome;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the data
	 */
	public GameData getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(GameData data) {
		this.data = data;
	}
	
}
