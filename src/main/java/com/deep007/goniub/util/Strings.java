package com.deep007.goniub.util;

import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Strings {

	public static int alphabetNumber(String str) {
		int chineseNumber = chineseNumber(str);
		return str.length() - chineseNumber;
	}

	/**
	 * 中文字的个数
	 * 
	 * @param str
	 * @return
	 */
	public static int chineseNumber(String str) {
		int chineseNumber = 0;
		Matcher matcher = Pattern.compile("[\\u4e00-\\u9fa5]").matcher(str);
		while (matcher.find()) {
			chineseNumber += matcher.group().length();
		}
		return chineseNumber;
	}

	public static boolean hasChineseNumber(String str) {
		return Pattern.compile("[零一二三四五六七八九十壹贰叁肆伍陆柒捌玖]").matcher(str).find();
	}

	public static boolean statrWithAlphabet(String text) {
		if (text == null || text.isEmpty()) {
			return false;
		}
		String first = text.substring(0, 1);
		return Pattern.compile("[0-9a-zA-Z]+").matcher(first).find();
	}

	public static boolean endsWithAlphabet(String text) {
		if (text == null || text.isEmpty()) {
			return false;
		}
		String end = text.substring(text.length() - 1, text.length());
		return Pattern.compile("[0-9a-zA-Z]+").matcher(end).find();
	}

	public static boolean hasAlphabet(String text) {
		return Pattern.compile("[0-9a-zA-Z]+").matcher(text).find();
	}

	public static boolean hasSymbol(String text) {
		return Pattern.compile("\\pP").matcher(text).find();
	}

	public static boolean isAlphabet(String text) {
		return text.matches("^[0-9a-zA-Z]+$");
	}

	/**
	 * unicode转中文
	 * 
	 * @param str
	 * @return
	 * @author yutao
	 * @date 2017年1月24日上午10:33:25
	 */
	public static String unicodeToString(String str) {

		Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");

		Matcher matcher = pattern.matcher(str);

		char ch;

		while (matcher.find()) {

			ch = (char) Integer.parseInt(matcher.group(2), 16);

			str = str.replace(matcher.group(1), ch + "");

		}
		return str;

	}

	public static String getDomain(String urlOrDomain) {
		String domain = null;
		if (urlOrDomain.trim().startsWith("http") || urlOrDomain.contains("/")) {
			java.net.URL url;
			try {
				url = new java.net.URL(urlOrDomain);
				domain = url.getHost().toLowerCase();// 获取主机名
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			domain = urlOrDomain;
		}
		Matcher matcher = Pattern.compile("(\\w+.\\w+)$").matcher(domain);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	/**
	 * 两个大数相减，默认没有符号位，且都为正数
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public static String bigDigitalSub(String a, String b) {
		// 翻转字符串并转化成数组
		char[] aArray = new StringBuilder(a).reverse().toString().toCharArray();
		char[] bArray = new StringBuilder(b).reverse().toString().toCharArray();
		int aLength = aArray.length;
		int bLength = bArray.length;
		// 找到最大的位数，两个整数的差的位数小于等于两个整数中的最大位数
		int maxLength = aLength > bLength ? aLength : bLength;
		int[] result = new int[maxLength];
		// 判断结果符号
		char sign = '+';
		if (aLength < bLength)
			sign = '-';
		else if (aLength == bLength) {
			int i = maxLength - 1;
			while (i > 0 && aArray[i] == bArray[i])
				i--;
			if (aArray[i] < bArray[i])
				sign = '-';
		}
		// 开始计算结果集
		for (int i = 0; i < maxLength; i++) {
			int aInt = i < aLength ? aArray[i] - '0' : 0;
			int bInt = i < bLength ? bArray[i] - '0' : 0;
			if (sign == '-')
				result[i] = bInt - aInt;
			else
				result[i] = aInt - bInt;
		}
		// 处理结果集，如果结果集中的某一位小于0，则向高位借位，然后将本位加10
		for (int i = 0; i < maxLength - 1; i++) {
			if (result[i] < 0) {
				result[i + 1] -= 1;
				result[i] += 10;
			}
		}

		// 处理结果集，转化成真正结果
		StringBuffer realResult = new StringBuffer();
		if (sign == '-')
			realResult.append('-');
		boolean isBeginning = true;
		for (int i = maxLength - 1; i >= 0; i--) {
			if (result[i] == 0 && isBeginning)
				continue;
			else
				isBeginning = false;
			realResult.append(result[i]);
		}
		if (realResult.toString().equals(""))
			realResult.append('0');
		return realResult.toString();
	}

	/**
	 * 采用动态规划的方法解决
	 *
	 * @param source
	 * @param target
	 * @return
	 */
	public static int editDistance(String source, String target) {
		char[] sources = source.toCharArray();
		char[] targets = target.toCharArray();
		int sourceLen = sources.length;
		int targetLen = targets.length;
		int[][] d = new int[sourceLen + 1][targetLen + 1];
		for (int i = 0; i <= sourceLen; i++) {
			d[i][0] = i;
		}
		for (int i = 0; i <= targetLen; i++) {
			d[0][i] = i;
		}

		for (int i = 1; i <= sourceLen; i++) {
			for (int j = 1; j <= targetLen; j++) {
				if (sources[i - 1] == targets[j - 1]) {
					d[i][j] = d[i - 1][j - 1];
				} else {
					// 插入
					int insert = d[i][j - 1] + 1;
					// 删除
					int delete = d[i - 1][j] + 1;
					// 替换
					int replace = d[i - 1][j - 1] + 1;
					d[i][j] = Math.min(insert, delete) > Math.min(delete, replace) ? Math.min(delete, replace)
							: Math.min(insert, delete);
				}
			}
		}
		return d[sourceLen][targetLen];
	}

	public static Date formatDate(String str, String format) {
		if (format == null || format.isEmpty()) {
			return null;
		}
		try {
			return new SimpleDateFormat(format).parse(str);
		} catch (ParseException e) {
		}
		return null;
	}

	public static String getMD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
			byte[] bs = md5.digest(inStr.getBytes());
			StringBuilder sb = new StringBuilder(40);
			for (byte x : bs) {
				if ((x & 0xff) >> 4 == 0) {
					sb.append("0").append(Integer.toHexString(x & 0xff));
				} else {
					sb.append(Integer.toHexString(x & 0xff));
				}
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return inStr;
		}
	}

	public static boolean hasChinese(String str) {
		return Pattern.compile("[\\u4e00-\\u9fa5]").matcher(str).find();
	}
	
	public static boolean isChinese(String str) {
		return str.matches("[\\u4e00-\\u9fa5]+");
	}

	public static boolean hasNumber(String str) {
		return Pattern.compile("\\d+").matcher(str).find();
	}

	public static boolean hasLetter(String str) {
		return Pattern.compile("[a-zA-Z]+").matcher(str).find();
	}

	public static boolean hasNocrediteNumber(String text) {
		// (2018)苏0205民初6040号
		Pattern pattern = Pattern
				.compile("[\\(（][\\d]{4}[）\\)][\\u4e00-\\u9fa5]{1,3}[\\d]{4,5}[\\u4e00-\\u9fa5]{1,3}[\\d]{4,5}号");
		return pattern.matcher(text).find();
	}

	public static String getPersonName(String name) throws Exception {
		if (!isValidString(name)) {
			throw new Exception("字符串无效:" + name);
		}
		if (name.contains("*")) {
			throw new Exception("名字不能有*:" + name);
		}
		StringBuilder sb = new StringBuilder();
		Matcher matcher = Pattern.compile("[\u4e00-\u9fa5a-zA-Z]").matcher(name);
		while (matcher.find()) {
			sb.append(matcher.group());
		}
		return sb.toString();
	}

	public static String filterNoNumber(String number) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < number.length(); i++) {
			if (number.charAt(i) == '*' || (number.charAt(i) >= '0' && number.charAt(i) <= '9')) {
				sb.append(number.charAt(i));
			}
		}
		return sb.toString();
	}

	public static String getNumber(String number) throws Exception {
		if (!isValidString(number)) {
			throw new Exception("字符串无效:" + number);
		}
		return filterNoNumber(number);
	}

	public static String getCardIdNumber(String number) throws Exception {
		if (!isValidString(number)) {
			throw new Exception("字符串无效:" + number);
		}
		Matcher matcher = Pattern.compile("\\d[\\d*a-zA-Z]{17}").matcher(number);
		if (matcher.find()) {
			return matcher.group(0);
		}
		throw new Exception("找不到身份证号:" + number);
	}

	public static String getTelephoneNumber(String number) throws Exception {
		if (!isValidString(number)) {
			throw new Exception("字符串无效:" + number);
		}
		number = filterNoNumber(number);
		if (number.startsWith("+86")) {
			number = number.substring(3);
		} else if (number.startsWith("86")) {
			number = number.substring(2);
		}
		Matcher matcher = Pattern.compile("[\\d*\\-]{7,13}").matcher(number);
		if (matcher.find()) {
			number = matcher.group(0);
			if (number.length() > 11) {
				return number.substring(number.length() - 11);
			}
			return number;
		}
		throw new Exception("找不到手机号:" + number);
	}

	public static String getTelephoneNumberNoStar(String number) throws Exception {
		if (!isValidString(number)) {
			throw new Exception("字符串无效:" + number);
		}
		number = filterNoNumber(number);
		if (number.startsWith("+86")) {
			number = number.substring(3);
		} else if (number.startsWith("86")) {
			number = number.substring(2);
		}
		Matcher matcher = Pattern.compile("[\\d]{7,11}").matcher(number);
		if (matcher.find()) {
			number = matcher.group(0);
			if (number.length() > 11) {
				return number.substring(number.length() - 11);
			}
			return number;
		}
		throw new Exception("找不到手机号:" + number);
	}

	public static String mergeText(String a, String b) {
		if (a == null) {
			return b;
		}
		if (b == null) {
			return a;
		}
		if (!isSameText(a, b)) {
			return a;
		}
		String n = "";
		for (int i = 0; i < a.length(); i++) {
			if (a.charAt(i) != '*') {
				n += a.charAt(i);
			} else if (b.length() > i) {
				n += b.charAt(i);
			}
		}
		return n;
	}

	public static boolean isSameText(String cardIdA, String cardIdB) {
		if (cardIdA != null && cardIdB != null && cardIdA.length() == cardIdB.length()) {
			for (int i = 0; i < cardIdA.length(); i++) {
				if (cardIdA.charAt(i) == '*' || cardIdB.charAt(i) == '*') {
					continue;
				}
				if (cardIdA.charAt(i) != cardIdB.charAt(i)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * @param str
	 * @return starts ends of array
	 */
	public static String[] extractDesensitizationInfo(String str) {
		String[] result = new String[2];
		if (!str.startsWith("*")) {
			String temp = "";
			for (int i = 0; i < str.length(); i++) {
				char ch = str.charAt(i);
				if (ch != '*') {
					temp += ch;
				} else {
					result[0] = temp;
					break;
				}
				if (temp.length() >= 5) {
					result[0] = temp;
					break;
				}
			}
		}
		if (!str.endsWith("*")) {
			String temp = "";
			int i = str.length() - 1;
			for (; i < str.length(); i--) {
				char ch = str.charAt(i);
				if (ch != '*') {
					temp = ch + temp;
				} else {
					result[1] = temp;
					break;
				}
				if (temp.length() >= 5) {
					result[1] = temp;
					break;
				}
			}
		}
		return result;
	}

	public static boolean isValidString(String str) {
		if (str == null || str.trim().isEmpty()) {
			return false;
		}
		return !str.contains("正在加载..");
	}

	public static boolean isValidTelephone(String str) {
		if (str == null || str.trim().isEmpty()) {
			return false;
		}
		Matcher matcher = Pattern.compile("[\u4e00-\u9fa5]").matcher(str);
		return !matcher.find();
	}

	public static boolean isSamePrefix(String a, String b, int num) {
		for (int i = 0; i < num; i++) {
			if (a.charAt(i) != b.charAt(i)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 区县名字简易化
	 *
	 * @param village
	 * @return
	 */
	public static String district2SimpleText(String district) {
		if (district.endsWith("市") || district.endsWith("区")) {
			return district.substring(0, district.length() - 1);
		}
		return district;
	}

	/**
	 * 乡镇名字简易化
	 *
	 * @param village
	 * @return
	 */
	public static String village2SimpleText(String village) {
		if (village.endsWith("镇") || village.endsWith("乡") || village.endsWith("村") || village.endsWith("组")) {
			return village.substring(0, village.length() - 1);
		}
		if (village.endsWith("街道") || village.endsWith("集团") || village.endsWith("林场") || village.endsWith("大队")
				|| village.endsWith("基地")) {
			return village.substring(0, village.length() - 2);
		}
		return village;
	}

	/**
	 * 公司名字简易化
	 *
	 * @param companyName
	 * @return
	 */
	public static String companyName2SimpleText(String companyName) {
		if (companyName.contains("有限公司")) {
			companyName = companyName.substring(0, companyName.indexOf("有限公司") - 2);
		}
		if (companyName.contains("有限责任公司")) {
			companyName = companyName.substring(0, companyName.indexOf("有限责任公司") - 2);
		}
		return companyName;
	}

	/**
	 * 去除括号里面的
	 *
	 * @param companyName
	 * @return
	 */
	public static String removeParenthesis(String addressText) {
		int startIndex = addressText.indexOf("(");
		int endIndex = addressText.indexOf(")");
		while (endIndex > startIndex && startIndex > 0) {
			addressText = addressText.substring(0, startIndex) + addressText.substring(endIndex + 1);
			addressText.indexOf("(");
			endIndex = addressText.indexOf(")");
		}
		startIndex = addressText.indexOf("（");
		endIndex = addressText.indexOf("）");
		while (endIndex > startIndex && startIndex > 0) {
			addressText = addressText.substring(0, startIndex) + addressText.substring(endIndex + 1);
			addressText.indexOf("（");
			endIndex = addressText.indexOf("）");
		}
		addressText = addressText.replaceAll("\\(", "").replaceAll("\\)", "");
		addressText = addressText.replaceAll("（", "").replaceAll("）", "");
		return addressText;
	}
}
