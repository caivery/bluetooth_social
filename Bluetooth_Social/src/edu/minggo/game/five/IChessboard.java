package edu.minggo.game.five;

import java.util.List;
/**
 * ���̽ӿ�
 * @author minggo
 * @created 2013-2-27����07:04:36
 */
public interface IChessboard {
	//ȡ��������������
	public int getMaxX();
	//���������
	public int getMaxY();
	//ȡ�õ�ǰ���пհ׵㣬��Щ��ſ�������
	public List<Point> getFreePoints();
}
