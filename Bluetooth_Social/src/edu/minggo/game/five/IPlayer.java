package edu.minggo.game.five;

import java.util.List;

/**
 * ��ҽӿ�
 * @author minggo
 * @created 2013-2-27����07:05:12
 */
public interface IPlayer {
	//��һ�����ӣ���������Ѿ��µ����Ӽ���
	public void run(List<Point> enemyPoints, Point point);

	public boolean hasWin();
	
	public void setChessboard(IChessboard chessboard);
	
	public List<Point> getMyPoints();
}
