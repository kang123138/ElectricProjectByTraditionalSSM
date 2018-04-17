package com.atguigu.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyDateUtil {

	public static void main(String[] args) {

	}

	// 使用Calendar进行日期的计算
	public static Date getMyDate(int i) {
		// Calendar的实例创建特殊，调用方法
		Calendar c = Calendar.getInstance();

		// 给当前日期的天数+1
		c.add(Calendar.DATE, i);

		// 取出时间
		return c.getTime();
	}

	public static String getMyDateString() {
		// 使用SimpleDateFormat按照某种格式创建sdf对象
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		// 调用format方法按照上面的格式格式化当前的时间
		String format = sdf.format(new Date());
		return format;
	}

}
