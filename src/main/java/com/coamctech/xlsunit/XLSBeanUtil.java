package com.coamctech.xlsunit;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.converters.NumberConverter;


/**
 * poi 从excel 格式读出的int是double类型，日期是long类型
 * 
 * @author Administrator
 *
 */
public class XLSBeanUtil {
	static BeanUtilsBean bean = null;
	static PropertyUtilsBean pb = new PropertyUtilsBean();
	static {
		MyIntegerConvert integerConv = new MyIntegerConvert();
		MyLongConvert longConv = new MyLongConvert();
		MyDateConvert dateConv = new MyDateConvert();
		MySqlDateConvert sqlDateConv = new MySqlDateConvert();
		MyBigDecimalConvert bigDecimalConv = new MyBigDecimalConvert();
		
		ConvertUtilsBean cub = new ConvertUtilsBean();
		
		cub.deregister(Integer.class);
		cub.register(integerConv, Integer.class);
		cub.deregister(Integer.TYPE);
		cub.register(integerConv, Integer.TYPE);
		
		
		cub.deregister(Long.class);
		cub.register(longConv, Long.class);
		cub.deregister(Long.TYPE);
		cub.register(longConv, Long.TYPE);

		cub.deregister(Date.class);
		cub.register(dateConv, Date.class);

		cub.deregister(java.sql.Date.class);
		cub.register(sqlDateConv, java.sql.Date.class);
		
		cub.deregister(BigDecimal.class);
		cub.register(bigDecimalConv, BigDecimal.class);

		System.out.println(cub.lookup(Integer.class));
		// System.out.println(cub.lookup(String.class));

		bean = new BeanUtilsBean(cub, pb);
	}

	public static synchronized void copyPropesrties(Map source, Object dest)
			throws IllegalAccessException, InvocationTargetException {

		bean.copyProperties(dest, source);
	}
	
	public static synchronized Object getProperty( Object dest, String p)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		return pb.getProperty(dest, p);
	}
	
	
	public static synchronized void setProperty( Object dest, String p,Object value)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		 pb.setProperty(dest, p, value);
	}
	
	private static Date format(String value){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			return sdf.parse(value.toString());
		} catch (ParseException e) {
			throw new RuntimeException("parse date error:" + value);
		}
	}
	
	private static Date format(Number value){
		//错误实现，excel 返回并不是毫秒。。。。。,excel 里不能有时间类型的数据，只能是字符串
		return new Date(value.longValue()); 
	}

	static class MyIntegerConvert extends NumberConverter {

		public MyIntegerConvert() {
			super(false);
		}

		public MyIntegerConvert(Object defaultValue) {
			super(false, defaultValue);
		}

		protected Class getDefaultType() {
			return Integer.class;
		}

		@Override
		public Object convert(Class clazz, Object value) {
			if(value==null){
				return null;
			}else if(value instanceof String){
				String str = (String)value;
				if(str.length()==0){
					return null;
				}
			}
			return new BigDecimal(value.toString()).intValue();
		}
	}
	
	
	static class MyLongConvert extends NumberConverter {

		public MyLongConvert() {
			super(false);
		}

		public MyLongConvert(Object defaultValue) {
			super(false, defaultValue);
		}

		protected Class getDefaultType() {
			return Long.class;
		}

		@Override
		public Object convert(Class clazz, Object value) {
			if(value==null){
				return null;
			}else if(value instanceof String){
				String str = (String)value;
				if(str.length()==0){
					return null;
				}
			}
			return new BigDecimal(value.toString()).longValue();
		}
	}

	static class MyDateConvert extends NumberConverter {

		public MyDateConvert() {
			super(false);
		}

		public MyDateConvert(Object defaultValue) {
			super(false, defaultValue);
		}

		protected Class getDefaultType() {
			return Date.class;
		}

		@Override
		public Object convert(Class clazz, Object value) {
			if(value==null){
				return null;
			}else 
			if(value instanceof String){
				return XLSBeanUtil.format((String)value);
			}else if(value instanceof Number){
				return XLSBeanUtil.format((Number)value);
			}else if(value instanceof Date){
				return value;
			}
			else {
				throw new UnsupportedOperationException("不支持的日期转化"+value);
			}
		}
	}
	
	
	static class MySqlDateConvert extends NumberConverter {

		public MySqlDateConvert() {
			super(false);
		}

		public MySqlDateConvert(Object defaultValue) {
			super(false, defaultValue);
		}

		protected Class getDefaultType() {
			return java.sql.Date.class;
		}

		@Override
		public Object convert(Class clazz, Object value) {
			if(value==null){
				return null;
			}else 
			if(value instanceof String){
				Date date =  XLSBeanUtil.format((String)value);
				return new java.sql.Date(date.getTime());
			}else if(value instanceof Number){
				return XLSBeanUtil.format((Number)value);
			}else if(value instanceof java.sql.Date){
				return value;
			}
			else{
				throw new UnsupportedOperationException();
			}
			
		}
	}
	
	
	static class MySqlTimestampConvert extends NumberConverter {

		public MySqlTimestampConvert() {
			super(false);
		}

		public MySqlTimestampConvert(Object defaultValue) {
			super(false, defaultValue);
		}

		protected Class getDefaultType() {
			return Timestamp.class;
		}

		@Override
		public Object convert(Class clazz, Object value) {
			if(value==null){
				return null;
			}else 
			if(value instanceof String){
				Date date =  XLSBeanUtil.format((String)value);
				return new Timestamp(date.getTime());
			}else if(value instanceof Number){
				return XLSBeanUtil.format((Number)value);
			}else if(value instanceof Timestamp){
				return value;
			}
			else{
				throw new UnsupportedOperationException();
			}
			
		
		}
	}
	
	static class MyBigDecimalConvert extends NumberConverter {

		public MyBigDecimalConvert() {
			super(false);
		}

		public MyBigDecimalConvert(Object defaultValue) {
			super(false, defaultValue);
		}

		protected Class getDefaultType() {
			return BigDecimal.class;
		}

		@Override
		public Object convert(Class clazz, Object value) {
			if(value==null){
				return null;
			}else 
			if(value instanceof String){
				return new BigDecimal((String)value);
			}else if(value instanceof BigDecimal){
				return value;
			}else if(value instanceof Number){
				return  new BigDecimal(value.toString());
			}
			else{
				throw new UnsupportedOperationException();
			}
			
		
		}
	}
	

//	public static void main(String[] args) throws IllegalAccessException, InvocationTargetException {
//		User user = new User();
//		Map map = new HashMap();
//		map.put("name", "lijz");
//		map.put("password", "lijz");
//		map.put("salt", "lijz");
//		map.put("sex", 12.0);
//		map.put("date", "2016-03-29 14:20:28");
//		map.put("salary", 22.333);
//		map.put("email", "lijz");
//		// {password=123ok, salt=小贷公司??, sex=12.0, name=小贷公司专项审批人, id=null,
//		// salary=1.442, email=ccccc}
//
//		// BeanUtils.copyProperties(user, map);
//		XLSBeanUtil.copyPropesrties(map, user);
//		System.out.println(user.getDate());
//	}
}
