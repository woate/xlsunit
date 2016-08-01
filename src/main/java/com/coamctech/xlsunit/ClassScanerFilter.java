package com.coamctech.xlsunit;
/**
	 * 类查找过滤器
	 * 
	 * @author liucheng
	 *
	 */
	public interface ClassScanerFilter {
		/**
		 * 如果接受返回真，反之返回假
		 * 
		 * @param clazz
		 *            当前处理类
		 * @return 真，通过
		 */
		public boolean accept(Class<?> clazz);
	}