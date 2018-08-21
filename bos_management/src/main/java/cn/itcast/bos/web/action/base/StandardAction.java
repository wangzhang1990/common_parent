package cn.itcast.bos.web.action.base;

import java.util.HashMap;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class StandardAction extends ActionSupport implements ModelDriven<Standard> {

	private Standard standard = new Standard();
	
	@Autowired
	private StandardService standardService;

	@Override
	public Standard getModel() {
		// TODO Auto-generated method stub
		return standard;
	}

	
	// 添加操作
	@Action(value = "standard_save", results = {
			@Result(name = "success",type = "redirect", location = "/pages/base/standard.html") })
	public String save() {
		System.out.println("添加收派标准....");
		// standardService.save(standard);
		standardService.save(standard);
		return SUCCESS;
	}
	
	private int page;
	private int rows;
	
	public void setPage(int page) {
		this.page = page;
	}


	public void setRows(int rows) {
		this.rows = rows;
	}


	@Action(value = "standard_pageQuery", results = { @Result(name = "success", type = "json")})
	public String pageQuery() {
		PageRequest pageRequest = new PageRequest(page - 1, rows);
		Page<Standard> pageData = standardService.findPageData(pageRequest);
		
		HashMap<String, Object> resultMap = new HashMap<>();
		resultMap.put("total", pageData.getTotalElements());
		resultMap.put("rows", pageData.getContent());
		
		ActionContext.getContext().getValueStack().push(resultMap);
		
		return SUCCESS;
	}
	
	@Action(value = "standard_findAll", results = { @Result(name = "success", type = "json")})
	public String findAll() {
		List<Standard> standards = standardService.findAll();
		ActionContext.getContext().getValueStack().push(standards);
		return SUCCESS;
	}
}
