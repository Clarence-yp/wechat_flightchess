package com.wechat.flight_chess.entity;

import com.wechat.flight_chess.data.GameData;
import com.wechat.flight_chess.service.impl.GameServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 
 */
public class Role {

	/**
	 * ��ɫ���
	 */
	private boolean isPlayer;

	/**
	 * ��ɫ�������ɫ
	 */
	private String roleColor;

	/**
	 * �ý�ɫӵ�еķɻ�
	 */
	private Plane[] planes = new Plane[4];

	/**
	 * �ý�ɫӵ�е�·��
	 */
	private List<Cell> line;

	/**
	 * ��ҵ�ɫ��
	 */
	private Dice dice;

	/**
	 * ��Ϸ����
	 */
	private GameData gameData;

	public GameData getGameData() {
		return gameData;
	}

	public void setGameData(GameData gameData) {
		this.gameData = gameData;
	}

	public GameServiceImpl getGameServicepl() {
		return gameServicepl;
	}

	public void setGameServicepl(GameServiceImpl gameServicepl) {
		this.gameServicepl = gameServicepl;
	}

	private GameServiceImpl gameServicepl;

	public Role(boolean isPlayer, String roleColor, GameData gameData, GameServiceImpl gameServicepl) {

		this.setGameData(gameData);
		this.setPlanes(gameData.getPlane(roleColor));
		this.setLine(gameData.getLine(roleColor));
		this.setPlayer(isPlayer);
		this.setRoleColor(roleColor);
		this.setGameServicepl(gameServicepl);

	}

	/**
	 * �ƶ��ɻ�
	 * 
	 * @param planeIndex
	 * @param diceNumber
	 */
	public List<Result> movePlane(int planeIndex, int diceNumber) {
		List<Result> resultList=new ArrayList<>();
		Result result=new Result();
		resultList.add(result);
		List<Integer> moveSteps = new ArrayList<>();
		// ���Ҫ�ƶ��ķɻ�
		Plane plane = this.getPlanes()[planeIndex];

		// ����÷ɻ����ϼ�
		if (plane.getLoc() == -2) {
			// ���
			this.launchPlane(planeIndex);
			moveSteps.add(LocConvert.locConvert(plane));
		} else {
			// ��ø÷ɻ�����·�ϵ�λ��
			int increment = plane.getLoc();
			if (increment != 56) {
				// ����ƶ���ﲻ���յ�
				if (plane.getLoc() + diceNumber < 55) {
					// ֱ���ƶ�
					for (int i = 0; i < diceNumber; i++) {
						// �ɻ�λ�ü�һ
						plane.setLoc(plane.getLoc() + 1);
						//���ø�������б�
						moveSteps.add(LocConvert.locConvert(plane));
					}
					//�ж��Ƿ���Ҫ��Ծ
					int temp = this.judgeIsJump(plane) ;
					if(temp>0){
						plane.setLoc(plane.getLoc() + temp);
						moveSteps.add(LocConvert.locConvert(plane));
					}
					Result goHome =bombPlane(this.judgeIsBomb(plane)) ;
					if (goHome!=null){
						resultList.add(goHome);
					}
				// ����ƶ��󳬹����յ�
				} else if (plane.getLoc() + diceNumber > 55) {

					// ����ǰ���ľ���
					int forwards = 55 - plane.getLoc();
					// ������˵ľ���
					int rears = diceNumber - forwards;

					// ǰ��
					for (int i = 0; i < forwards; i++) {
						plane.setLoc(plane.getLoc() + 1);
						moveSteps.add(LocConvert.locConvert(plane));
					}

					// ����
					for (int i = 0; i < rears; i++) {
						plane.setLoc(plane.getLoc() - 1);
						moveSteps.add(LocConvert.locConvert(plane));
					}
				//�ƶ���պõ��յ�
				} else if (plane.getLoc() + diceNumber == 55) {
					for (int i = 0; i < diceNumber; i++) {
						plane.setLoc(plane.getLoc() + 1);
						moveSteps.add(LocConvert.locConvert(plane));
					}
					plane.setLoc(-3);
					//�ж��Ƿ�ʤ��
					int temp = this.getGameServicepl().judgeIsWin(this.gameData.getCurrentRole());
					if (temp!=-1){
						result.setPlayerWin(temp);
					}
				}
			}
		}
		result.setColor(this.roleColor);
		result.setChess(planeIndex);
		result.setMoveStep(moveSteps);
		return resultList;
	}

	/**
	 * �����ɻ�
	 * @param planeIndex
	 */
	public void launchPlane(int planeIndex) {
		this.planes[planeIndex].setLoc(-1);
	}


	/**
	 * �ж��Ƿ���Ծ
	 * @param plane
	 * @return
	 */
	private int judgeIsJump(Plane plane) {

		String type = plane.getPlaneType();
		List<Cell> line = gameData.getLine(type);

		if(plane.getLoc()>48){
			return 0 ;
		}

		if (plane.getLoc()!=0){
			//���Ծ
			if (line.get(plane.getLoc()-1).getCellColor().equals(plane.getPlaneType()) && plane.getLoc()==17){
				return  12;
			}
			//С��Ծ
			else if(line.get(plane.getLoc()-1).getCellColor().equals(plane.getPlaneType())){
				return 4 ;
			}
		}
		return 0 ;
	}

	/**
	 * �ж��Ƿ��зɻ�Ҫ��ը
	 * @param plane
	 * @return
	 */
	private List<Plane> judgeIsBomb(Plane plane){
		
		List<Plane> planeList = new ArrayList<Plane>() ;
		
		GameData data = this.gameData ;
		Plane[] redPs = data.getPlane(Plane.TYPE_RED) ;
		Plane[] orangePs = data.getPlane(Plane.TYPE_ORANGE) ;
		Plane[] greenPs = data.getPlane(Plane.TYPE_GREEN) ;
		Plane[] bluePs = data.getPlane(Plane.TYPE_BLUE) ;

		int loc=LocConvert.locConvert(plane);

		for(int i = 0 ;i<4;i++){
			if(LocConvert.locConvert(redPs[i]) == loc && (!redPs[i].getPlaneType().equals(plane.getPlaneType())))
				planeList.add(redPs[i]) ;
		}
		if(planeList.size()!=0)
			return planeList ;

		for(int i = 0 ;i<4;i++) {
			if (LocConvert.locConvert(orangePs[i]) == loc && (!orangePs[i].getPlaneType().equals(plane.getPlaneType())))
				planeList.add(orangePs[i]);
		}
		if(planeList.size()!=0)
			return planeList ;
		
		for(int i = 0 ;i<4;i++) {
			if (LocConvert.locConvert(greenPs[i]) == loc && (!greenPs[i].getPlaneType().equals(plane.getPlaneType())))
				planeList.add(greenPs[i]);
		}
		if(planeList.size()!=0)
			return planeList ;
		
		for(int i = 0 ;i<4;i++) {
			if (LocConvert.locConvert(bluePs[i]) == loc && (!bluePs[i].getPlaneType().equals(plane.getPlaneType())))
				planeList.add(bluePs[i]);
		}
		if(planeList.size()!=0)
			return planeList ;
		return null ;
	}
	
	/**
	 * ʵ�к�ը
	 * @param
	 */
	private Result bombPlane(List<Plane> planeList){
		if(planeList==null) {
			return null;
		} else{
			Result a=new Result();
			for(Plane plane : planeList){
				List<Integer> moveSteps = new ArrayList<>();
				this.initPlane(plane) ;
				moveSteps.add(LocConvert.locConvert(plane));

				a.setColor(plane.getPlaneType());
				a.setChess(plane.getIndex());
				a.setMoveStep(moveSteps);

				return a;
			}
		}
		return null;
	}

	
	private void initPlane(Plane plane){
		plane.setAtHome(true);
		plane.setLoc(-2) ;
		
	}
	
	/**
	 * @return the isPlayer
	 */
	public boolean isPlayer() {
		return isPlayer;
	}

	/**
	 * @param isPlayer
	 *            the isPlayer to set
	 */
	public void setPlayer(boolean isPlayer) {
		this.isPlayer = isPlayer;
	}

	/**
	 * @return the roleColor
	 */
	public String getRoleColor() {
		return roleColor;
	}

	/**
	 * @param roleColor
	 *            the roleColor to set
	 */
	public void setRoleColor(String roleColor) {
		this.roleColor = roleColor;
	}

	/**
	 * @return the planes
	 */
	public Plane[] getPlanes() {
		return planes;
	}

	/**
	 * @param planes
	 *            the planes to set
	 */
	public void setPlanes(Plane[] planes) {
		this.planes = planes;
	}

	/**
	 * @return the line
	 */
	public List<Cell> getLine() {
		return line;
	}

	/**
	 * @param line
	 *            the line to set
	 */
	public void setLine(List<Cell> line) {
		this.line = line;
	}

	/**
	 * @return the dice
	 */
	public Dice getDice() {
		return dice;
	}

	/**
	 * @param dice
	 *            the dice to set
	 */
	public void setDice(Dice dice) {
		this.dice = dice;
	}

}
