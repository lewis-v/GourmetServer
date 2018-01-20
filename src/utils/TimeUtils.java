package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {
	static DateFormat  dataYear = DateFormat.getDateInstance();
	static SimpleDateFormat dataMonth = new SimpleDateFormat("MM-dd HH:mm");
	static SimpleDateFormat dataDay = new SimpleDateFormat("HH:mm");
	
	public static String getTime(String time){
		String result;
		long now = System.currentTimeMillis();
		long get = Long.parseLong(time);
		long sub = now - get;
		if (sub <= 1000 * 60){//1��������
			result = "�ո�";
		}else if (sub < 60*60*1000) {//1Сʱ����
			result = sub/1000/60 + "����ǰ";
		}else if (sub <= 12*60*60*1000){//12Сʱ����
			result = sub/60/60/1000 +"Сʱǰ";
		}else{//����12Сʱ
			Calendar calendarNow = Calendar.getInstance();
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(get);
			if (calendar.get(Calendar.YEAR) != calendarNow.get(Calendar.YEAR)){//1����ǰ
				result = dataYear.format(new Date(get));
			}else if(calendarNow.get(Calendar.DATE) - calendar.get(Calendar.DATE) == 1){//�����
				result = "����\t\t" + dataDay.format(new Date(get));
			}else {//��ʾ����ʱ��
				result = dataMonth.format(new Date(get));
			}
		}
		
		return result;
	}
	
}
