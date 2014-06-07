package edu.minggo.game.five;

import java.util.HashMap;
import java.util.Map;
/**
 * ����AI������
 * @author minggo
 * @created 2013-2-27����07:01:56
 */
public class AiFactory {
	private final static Map<Integer,IPlayer> ais = new HashMap<Integer, IPlayer>(2);
	//��������������Խ���Ѷ�Խ��
	public static IPlayer getInstance(int level){
		IPlayer ai = ais.get(level);
		if(ai==null){
			switch (level) {
			case 1:
				ais.put(level, new ZhuBaJieAi());
				break;
			case 2:
				ais.put(level, new SunWuKongAi());
				break;
			}
		}
		return ais.get(level);
	}
}
