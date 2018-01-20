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
		if (sub <= 1000 * 60){//1分钟以内
			result = "刚刚";
		}else if (sub < 60*60*1000) {//1小时以内
			result = sub/1000/60 + "分钟前";
		}else if (sub <= 12*60*60*1000){//12小时以内
			result = sub/60/60/1000 +"小时前";
		}else{//大于12小时
			Calendar calendarNow = Calendar.getInstance();
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(get);
			if (calendar.get(Calendar.YEAR) != calendarNow.get(Calendar.YEAR)){//1年以前
				result = dataYear.format(new Date(get));
			}else if(calendarNow.get(Calendar.DATE) - calendar.get(Calendar.DATE) == 1){//昨天的
				result = "昨天\t\t" + dataDay.format(new Date(get));
			}else {//显示日期时间
				result = dataMonth.format(new Date(get));
			}
		}
		
		return result;
	}
	
}
