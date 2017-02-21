package io.ibole.infrastructure.support.tool.codegen.util;

public class StringUtils {

	public static final String CAMEL_TYPE_METHOD = "M";
	public static String toCamelString(String src) {
		if (src != null && !src.trim().equals("")) {
			String first = src.substring(0, 1);
			return first.toUpperCase() + src.substring(1);
		}
		return "";
	}

	/**
	 * emp_info -->EmpInfo
	 * @param src
	 * @param type 如果生成方法名请传入该参数 CAMEL_TYPE_METHOD emp_info -->empInfo
	 * @return
	 */
	public static String omitUnderLineToCamelStr(String src,String... type) {
		if (src.indexOf("_") < 0) {
			if(type.length > 0 && type[0].equals(CAMEL_TYPE_METHOD)){
				return src.toLowerCase();
			}
			return toCamelString(src);
		}
		StringBuffer sb = new StringBuffer();
		if (src != null && !src.trim().equals("")) {
			String[] temp = src.split("_");
			for (int i = 0; i < temp.length; i++) {
				if (!temp[i].trim().equals("")) {
					if(type.length > 0 && type[0].equals(CAMEL_TYPE_METHOD) && i == 0){
							sb.append(temp[i]);
					}else{
						sb.append(toCamelString(temp[i]));
					}
				}
			}
			return sb.toString();
		}
		return "";
	}

	
	public static void main(String[] args) {
		String src = "t_user_releas";
		System.out.println(omitUnderLineToCamelStr(src));
	}
}
