package cn.itcast.bos.web.action.common;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;

import org.springframework.data.domain.Page;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Area;

public class BaseAction<T> extends ActionSupport implements ModelDriven<T> {
	
	protected T model;
	@Override
	public T getModel() {
		// TODO Auto-generated method stub
		return model;
	}
	
	public BaseAction() {
		Type genericSuperclass = this.getClass().getGenericSuperclass(); //1
		ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass; //2
		Type[] actualTypeArguments = parameterizedType.getActualTypeArguments(); //3
		Class<T> modelClass = (Class<T>) actualTypeArguments[0]; //4
		
		try {
			model = modelClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("模型构造失败...BaseAction");
		}
	}
	
	protected int page;
	
	protected int rows;
	
	
	public void setPage(int page) {
		this.page = page;
	}


	public void setRows(int rows) {
		this.rows = rows;
	}
	
	protected void pushPageDateToValueStack(Page<Area> pageData) {
		
		
		HashMap<Object, Object> resultMap = new HashMap<>();
		resultMap.put("total", pageData.getTotalElements());
		resultMap.put("rows", pageData.getContent());
		
		ActionContext.getContext().getValueStack().push(resultMap);
	}
}
