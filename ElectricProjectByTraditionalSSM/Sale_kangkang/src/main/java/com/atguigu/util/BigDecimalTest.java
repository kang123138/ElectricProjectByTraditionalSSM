package com.atguigu.util;

import java.math.BigDecimal;

public class BigDecimalTest {

	public static void main(String[] args) {
		// 初始化 -- 结果：一定要用String字符串，其他都不精确
		BigDecimal b1 = new BigDecimal("0.02");
		BigDecimal b2 = new BigDecimal(0.02d);
		BigDecimal b3 = new BigDecimal(0.02f);
		System.out.println(b1);
		System.out.println(b2);
		System.out.println(b3);

		// 比较大小
		int compareTo = b2.compareTo(b3);
		System.out.println(compareTo);// -1 0 1

		// 运算
		BigDecimal b6 = new BigDecimal("6");
		BigDecimal b7 = new BigDecimal("7");
		// 1.加法
		BigDecimal add = b6.add(b7);
		System.out.println(add);
		// 2.减法
		BigDecimal subtract = b6.subtract(b7);
		System.out.println(subtract);
		// 3.乘法
		BigDecimal multiply = b6.multiply(b7);
		System.out.println(multiply);

		// 4.除法特殊，需要进行取舍
		// 取舍
		// divide(BigDecimal divisor, int scale, int roundingMode)；三个参数：BigDecimal，保留的位数，BigDecimal.ROUND_HALF_DOWN是四舍五入
		BigDecimal divide = b6.divide(b7, 4, BigDecimal.ROUND_HALF_DOWN);
		BigDecimal add2 = b2.add(b3);
		BigDecimal setScale = add2.setScale(3, BigDecimal.ROUND_HALF_DOWN);
		System.out.println(setScale);
	}

}
