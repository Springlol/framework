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
import java.lang.reflect.Method;
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
		//解析请求
		if(!req.getRequestURI().endsWith(".action")){
			chain.doFilter(request,response);
		}
		//获取action name
		String actionName = getRequestActionName(req);
		Map<String, Object> context = ActionMapping.getContext();
		Action action = (Action)context.get(actionName);
		try {
			//执行actionProxy
			Object proxy = getProxy(action);
			String resultCode = doAction(action,proxy, req);
			Result result  = (Result)action.getResult().get(resultCode);
			toLocation(result,proxy,req,resp);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getRequestActionName(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		String actionName = requestURI.substring(requestURI.lastIndexOf("/") + 1, requestURI.lastIndexOf(".action"));
		return actionName;
	}

	private Object getProxy(Action action) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
		String classes = action.getClasses();
		Class aClass = Class.forName(classes);
		Object actionProxy = aClass.newInstance();
		return actionProxy;
	}

	private String doAction(Action action,Object actionProxy,HttpServletRequest request) throws Exception {
		Class aClass = actionProxy.getClass();
		Map<String,String> params = request.getParameterMap();
		for (String fileName : params.keySet()){
			Field field = aClass.getDeclaredField(fileName);
			if (field != null) {
				field.setAccessible(true);
				field.set(actionProxy,params.get(fileName));
			}
		}
		Method method = aClass.getDeclaredMethod(action.getMethod());
		String ret = (String)method.invoke(actionProxy);
		return ret;
	}
	//只执行dispatcher和redirect类型
	private void toLocation(Result result,Object proxy,HttpServletRequest req,HttpServletResponse resp) throws IOException, IllegalAccessException, ServletException {
		String type = result.getType();
		if("dispatcher".equals(type)){
			req.getRequestDispatcher(result.getLocation()).forward(req,resp);
		}else{
			Map<String,String> map = req.getParameterMap();
			Field[] fields = proxy.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				map.put(field.getName(),field.get(proxy).toString());
			}
			resp.sendRedirect(result.getLocation());
		}

	}
}
