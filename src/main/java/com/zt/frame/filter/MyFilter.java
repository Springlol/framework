package com.zt.frame.filter;

import com.zt.frame.entity.Action;
import com.zt.frame.entity.Result;
import com.zt.frame.util.ActionMapping;
import org.dom4j.DocumentException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zhoutao on 2016/11/28.
 */
public class MyFilter implements Filter{
	public void init(FilterConfig filterConfig) throws ServletException {
		try {
			ActionMapping.parseXML();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		//获取action
		Action action = reqToAction(req);
		if(action == null){
			chain.doFilter(request,response);
		}
		try {
			//创建actionProxy
			Object actionProxy = createActionProxy(action);
			setPropertyToProxy(req,actionProxy);
			String ret = actionExecute(actionProxy, action.getMethod());
			Result result  = (Result)action.getResult().get(ret);
			if (result != null) {
				resultExecute(result,actionProxy,req,resp);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//获取action
	private Action reqToAction(HttpServletRequest req){
		if (!req.getRequestURI().endsWith(".action")){
			return null;
		}
		String path = req.getRequestURI();
		String actionName = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf(".action"));
		Object obj = ActionMapping.getContext().get(actionName);
		return (Action)obj;
	}
	//创建action代理实例
	private Object createActionProxy(Action action) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		String classes = action.getClasses();
		Class aClass = Class.forName(classes);
		return aClass.newInstance();
	}
	//将属性设置到Action属性中
	private void setPropertyToProxy(HttpServletRequest req,Object actionProxy) throws NoSuchFieldException, IllegalAccessException, ParseException {
		Map map = req.getParameterMap();
		for(Iterator iterator = map.keySet().iterator();iterator.hasNext();){
			Object key = iterator.next();
			Class aClass = actionProxy.getClass();
			Field field = aClass.getDeclaredField(key.toString());
			if (field == null) {
				continue;
			}
			field.setAccessible(true);

			typeConventer(req, actionProxy, key, field);

			field.setAccessible(false);
		}
	}
	//部分类型转换
	private void typeConventer(HttpServletRequest req, Object actionProxy, Object key, Field field) throws IllegalAccessException, ParseException {
		Class type = field.getType();
		if(type == String.class ){
			field.set(actionProxy,req.getParameter(key.toString()));
		}else if ( type == Integer.class || type == Integer.TYPE ){
			field.set(actionProxy,Integer.parseInt(req.getParameter(key.toString())));
		}else if ( type == Double.class || type == Double.TYPE ){
			field.set(actionProxy,Double.parseDouble(req.getParameter(key.toString())));
		}else if ( type == Short.class || type == Short.TYPE){
			field.set(actionProxy,Short.parseShort(req.getParameter(key.toString())));
		} else if ( type == Date.class) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
			field.set(actionProxy, sdf.parse(req.getParameter(key.toString())));
		} else {
			field.set(actionProxy, req.getParameter(key.toString()));
		}
	}

	//执行方法
	private String actionExecute(Object actionProxy,String methodName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Class aClass = actionProxy.getClass();
		Method method = aClass.getDeclaredMethod(methodName);
		Object ret = method.invoke(actionProxy);
		return (String)ret;
	}

	//只处理dispatcher和redirect类型
	private void resultExecute(Result result,Object proxy,HttpServletRequest req,HttpServletResponse resp) throws IOException, IllegalAccessException, ServletException {
		String type = result.getType();
		if("redirect".equals(type)){
			resp.sendRedirect(result.getLocation());
		}else{
			Field[] fields = proxy.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				req.setAttribute(field.getName(),field.get(proxy));
				field.setAccessible(false);
			}
			req.getRequestDispatcher(result.getLocation()).forward(req,resp);
		}

	}



}
