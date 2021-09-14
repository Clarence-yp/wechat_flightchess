package com.wechat.flight_chess.service;


import com.wechat.flight_chess.entity.Result;

import java.util.List;

/**
 * ��Ϸ����ӿ�
 *
 */
public interface GameService {
	
	/**
	 * ��ʼ��Ϸ
	 */
	public void startGame() ;

	/**
	 * ������
	 * @return
	 */
	public int throwDice();

	/**
	 * �����ƶ�
	 * @param color
	 * @param chess
	 * @return
	 */
	public List<Result> makeMove(String color, int chess);
	
	/**
	 * �ж������ɫ�ӽ��
	 * @param flag
	 * @param diceNumber
	 */
	public List<Result> judgePlayerThrowResult(int flag , int diceNumber) ;
	
	/**
	 * �жϵ�����ɫ�ӽ��
	 * @param flag
	 * @param diceNumber
	 */
	public List<Result> judgeCompThrowResult(int flag ,int diceNumber) ;
	
	/**
	 * �ж���Ӯ���
	 * 
	 * @return
	 * -1��û���κ�һ��Ӯ��
	 * 0����Ӯ��
	 * 1���Ʒ�Ӯ
	 * 2���̷�Ӯ
	 * 3���췽Ӯ
	 */
	public int  judgeIsWin(int flag) ;
	
	/**
	 * ���¿�ʼ��Ϸ
	 */
	public void restartGame() ;

}
