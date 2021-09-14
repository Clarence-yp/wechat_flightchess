package com.wechat.flight_chess.service.impl;


import com.wechat.flight_chess.data.GameData;
import com.wechat.flight_chess.entity.*;
import com.wechat.flight_chess.service.GameService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ��Ϸ����ӿڵ�ʵ��
 *
 */
@Service("gameService")
public class GameServiceImpl implements GameService {

	private GameData gameData;

	public GameServiceImpl() {
		this.gameData = new GameData();
	}

	/**
	 * ��ʼ���ϼ�
	 */
	private void initHome(){
		Plane[] redP = gameData.getPlayers()[0].getPlanes() ;
		Cell[] redH = gameData.getRedHome() ;

		Plane[] orangeP = gameData.getPlayers()[1].getPlanes() ;
		Cell[] orangeH = gameData.getOrangeHome() ;

		Plane[] greenP = gameData.getPlayers()[2].getPlanes() ;
		Cell[] greenH = gameData.getGreenHome() ;

		Plane[] blueP = gameData.getPlayers()[3].getPlanes() ;
		Cell[] blueH = gameData.getBlueHome() ;
		}

	/**
	 * ��ʼ�����
	 */
	private void initPlayers() {
		gameData.getPlayers()[0] = new Role(true, Plane.TYPE_BLUE, this.gameData,this)  ;
		gameData.getPlayers()[1] = new Role(true, Plane.TYPE_ORANGE, this.gameData, this) ;
		gameData.getPlayers()[2] = new Role(true, Plane.TYPE_GREEN, this.gameData, this) ;
		gameData.getPlayers()[3] = new Role(true, Plane.TYPE_RED, this.gameData, this) ;
	}

	@Override
	public void startGame() {
		gameData.setStart(true);
		//��ʼ�����
		this.initPlayers();
		//��ʼ�������ϼ�
		this.initHome() ;
		gameData.setClick(false);
		gameData.setCurrentRole(0);
		gameData.setNumberOfCurrentPlane(-1);
	}

	/**
	 * �������ӽ���ǰ���ж��ƶ��ĸ�����
	 * @return
	 */
	@Override
	public int throwDice() {
		Dice dice=this.gameData.getDice();
		//�ж���Ϸ�Ƿ�ʼ
		if (this.gameData.isStart()) {
			//�ж��Ƿ�Ͷ��������
			if (!this.gameData.isClick()) {
				int temp;
				// �����������
				Random random = new Random();
				// ��ʾ���յ���
				temp = random.nextInt(6);
				dice.setNumber(temp + 1);

				this.gameData.setClick(true);
			}
		}

		return dice.getNumber();
	}

	public List<Result> makeMove(String color, int chess) {
		List<Result> resultList=new ArrayList<>();
		int flag = gameData.getCurrentRole();
		if (flag==0){
			gameData.setNumberOfCurrentPlane(chess);
			//�ж���Ϸ�Ƿ�ʼ
			if (gameData.isStart()) {
				//�ж��Ƿ�Ͷ��������
				if (gameData.isClick()) {
					resultList.addAll(judgePlayerThrowResult(flag, gameData.getDice().getNumber()));
					gameData.setClick(false);
				}
				flag = gameData.getCurrentRole() % 4;
				flag++;
				if (flag == 4) {
					flag = 0;
				}
				gameData.setCurrentRole(flag);
			}
		}
		while (flag!=0){
			int diceNumber=throwDice();
			List<Result> compList=new ArrayList<>();
			if (gameData.isClick()) {
				compList=judgeCompThrowResult(flag, diceNumber);
				gameData.setClick(false);
			}
			flag = gameData.getCurrentRole() % 4;
			flag++;
			if (flag == 4) {
				flag = 0;
			}
			gameData.setCurrentRole(flag);
			resultList.addAll(compList);
			flag = gameData.getCurrentRole();
		}
		return resultList;
	}

	/**
	 * @return the gameData
	 */
	public GameData getGameData() {
		return gameData;
	}

	/**
	 * ��������Ӳ��ƶ�
	 * @param flag ���
	 * @param diceNumber ������
	 */
	@Override
	public List<Result> judgePlayerThrowResult(int flag, int diceNumber) {
		List<Result> resultList=new ArrayList<>();
		if (diceNumber == 6) {
			// ��ø�������зɻ�
			Plane[] planes = this.getGameData().getPlayers()[flag].getPlanes();
			if (this.gameData.getNumberOfCurrentPlane() != -1) {
				// �ƶ�ѡ�еķɻ�
				resultList = this.getGameData().getPlayers()[flag].movePlane(this.getGameData().getNumberOfCurrentPlane(), diceNumber);
				// ���õ�ǰѡ�зɻ����Ϊ-1
				this.gameData.setNumberOfCurrentPlane(-1);
			}
			//����һ��
			this.gameData.setCurrentRole(flag + 3);
		} else {
			//�ڼҵķɻ���
			int tempNum = 0;
			// ��ø�������зɻ�
			Plane[] planes = this.getGameData().getPlayers()[flag].getPlanes();

			for (Plane p : planes) {
				if (p.getLoc() != -2 && p.getLoc() != -3) {}
				else {
					tempNum++;
				}
			}
			//�����ƶ�
			if (tempNum == 4){
				Result result=new Result();
				result.setColor(this.getGameData().getPlayers()[flag].getRoleColor());
				result.setChess(this.gameData.getNumberOfCurrentPlane());
				resultList.add(result);
				return resultList;
			}
			// �����ѡ����һ��Ҫ�ƶ��ķɻ�ʱ
			if (this.gameData.getNumberOfCurrentPlane() != -1) {
				// �ƶ�ѡ�еķɻ�
				resultList = this.getGameData().getPlayers()[flag].movePlane(this.getGameData().getNumberOfCurrentPlane(), diceNumber);
				// ���õ�ǰѡ�зɻ����Ϊ-1
				this.gameData.setNumberOfCurrentPlane(-1);
			}
		}
		return resultList;
	}

	/**
	 * ��������ƶ�
	 * @param flag
	 * @param diceNumber
	 */
	@Override
	public List<Result> judgeCompThrowResult(int flag, int diceNumber) {
		List<Result> resultList=new ArrayList<>();
		Result result=new Result();
		resultList.add(result);
		List<Integer> moveSteps = new ArrayList<>();
		Plane[] planes = this.getGameData().getPlayers()[flag].getPlanes();
		if (diceNumber == 6) {
			if (this.getOutPlane(flag) == 0) {
				int temp = 0;
				for (int i = 0; i < 4; i++) {
					if (planes[i].getLoc() == -2) {
						temp = i;
						break;
					}
				}
				//����һ��
				this.gameData.setCurrentRole(flag + 3);
				this.gameData.getPlayers()[flag].launchPlane(temp);
				moveSteps.add(LocConvert.locConvert(this.gameData.getPlayers()[flag].getPlanes()[temp]));
				result.setColor(this.gameData.getPlayers()[flag].getRoleColor());
				result.setChess(this.gameData.getPlayers()[flag].getPlanes()[temp].getIndex());
				result.setMoveStep(moveSteps);
			} else {

				int index = this.isGetDes(planes, diceNumber);
				if (index != -1) {
					resultList = this.getGameData().getPlayers()[flag].movePlane(index, diceNumber);
				}else if (this.isDoubleMoving(planes, diceNumber) != -1) {
					resultList = this.getGameData().getPlayers()[flag].movePlane(this.isDoubleMoving(planes, diceNumber), diceNumber);
				}else if (this.getPlaneIndexAtHome(planes) != -1){
					resultList = this.getGameData().getPlayers()[flag].movePlane(this.getPlaneIndexAtHome(planes), diceNumber);
				}else {
					resultList = this.getGameData().getPlayers()[flag].movePlane(this.getNearDes(planes), diceNumber);
				}
			}
		} else {
			if (this.getOutPlane(flag) != 0) {
				int index = this.isGetDes(planes, diceNumber);
				if (index != -1) {
					resultList = this.getGameData().getPlayers()[flag].movePlane(index, diceNumber);
				}
				else if (this.isDoubleMoving(planes, diceNumber) != -1) {
					resultList = this.getGameData().getPlayers()[flag].movePlane(this.isDoubleMoving(planes, diceNumber), diceNumber);
				} else {
					resultList = this.getGameData().getPlayers()[flag].movePlane(this.getNearDes(planes), diceNumber);
				}
			}else {
				result.setColor(this.getGameData().getPlayers()[flag].getRoleColor());
				result.setChess(this.gameData.getNumberOfCurrentPlane());
				return resultList;
			}
		}
		return resultList;
	}

	/**
	 * �����ɵķɻ���
	 * 
	 * @param flag
	 * @return
	 */
	private int getOutPlane(int flag) {
		int temp = 0;

		Plane[] planes = this.getGameData().getPlayers()[flag].getPlanes();
		for (Plane p : planes) {
			if (p.getLoc() > -2)
				temp++;
		}
		return temp;
	}

	/**
	 * �ж���û�зɻ��ƶ��󵽴�Ŀ�ĵ�
	 * 
	 * @param planes
	 * @param diceNumber
	 * @return
	 */
	private int isGetDes(Plane[] planes, int diceNumber) {
		for (int i = 0; i < 4; i++)
			if (planes[i].getLoc() + diceNumber == 56)
				return i;
		return -1;
	}

	/**
	 * �鿴�Ƿ��зɻ����������ƶ�����
	 * 
	 * @param planes
	 * @param diceNumber
	 * @return
	 */
	private int isDoubleMoving(Plane[] planes, int diceNumber) {

		List<Cell> path = this.getGameData().getLine(planes[0].getPlaneType());
		for (int i = 0; i < 4; i++){
			if (planes[i].getLoc() > -2 && (planes[i].getLoc() + diceNumber) <= 56){
				if (planes[i].getPlaneType().equals(path.get(planes[i].getLoc() + diceNumber).getCellColor())){
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * ���һ��
	 * 
	 * @param planes
	 * @return
	 */
	private int getNearDes(Plane[] planes) {
		int temp = 0;
		Random random = new Random();
		while (true) {
			temp = random.nextInt(4);
			if (planes[temp].getLoc() > -2)
				return temp;
		}
	}

	/**
	 * ����ϼҵķɻ����
	 * 
	 * @param planes
	 * @return
	 */
	private int getPlaneIndexAtHome(Plane[] planes) {
		for (int i = 0; i < 4; i++)
			if (planes[i].getLoc() == -2)
				return i;
		return -1;
	}

	@Override
	public int judgeIsWin(int flag) {

		Plane[] planes = this.getGameData().getPlayers()[flag].getPlanes();

		int temp = 0;
		for (int i = 0; i < 4; i++) {
			if (planes[i].getLoc() == -3)
				temp++;
		}
		if (temp == 4) {
			return flag;
		}
		return -1;
	}

	@Override
	public void restartGame() {

	}


}
