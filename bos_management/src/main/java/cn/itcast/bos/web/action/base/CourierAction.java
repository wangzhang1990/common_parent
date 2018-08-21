package cn.itcast.bos.web.action.base;

import java.util.ArrayList;
import java.util.HashMap;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;

@Controller
@ParentPackage("json-default")
@Scope("prototype")
@Namespace("/")
public class CourierAction extends ActionSupport implements ModelDriven<Courier> {
	private Courier courier = new Courier();

	@Override
	public Courier getModel() {
		return courier;
	}
	
	@Autowired
	private CourierService courierService;
	
	@Action(value = "courier_save"
			, results = { @Result(name = "success", location = "/pages/base/courier.html", type = "redirect")})
	public String save() {
		courierService.save(courier);
		return SUCCESS;
	}
	
	private String ids;
	
	public void setIds(String ids) {
		this.ids = ids;
	}

	@Action(value = "courier_delBatch"
			, results = { @Result(name = "success", location = "/pages/base/courier.html", type = "redirect")})
	public String delBatch() {
		String[] idArray = ids.split("-");
		courierService.delBatch(idArray);
		return SUCCESS;
	}
	
	@Action(value = "courier_resBatch"
			, results = { @Result(name = "success", location = "/pages/base/courier.html", type = "redirect")})
	public String resBatch() {
		String[] idArray = ids.split("-");
		courierService.resBatch(idArray);
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

	@Action(value = "courier_pageQuery"
			, results = { @Result(name = "success", type = "json")})
	public String pageQuery() {
		Pageable pageable = new PageRequest(page - 1, rows);
		
		//根据条件查询，构造specification对象
		Specification<Courier> specification = new Specification<Courier>() {
			
			@Override
			/**
			 * 如果方法返回null，表示无条件查询
			 * root就是当前查询的对象表，也就是Root里泛型指定的那个类
			 * root 对象用来获取条件表达式的字段，比如获取" where name=？，age=？"中的"name"和"age"
			 * CriteriaQuery 对象，可以构造简单查询条件，提供了一个where方法用来构造条件（暂解）
			 * CriteriaBuilder 对象cb是用来构造Predicate对象的。有点类似于Hibernate中的Restrictions
			 * 
			 */
			public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				
				ArrayList<Predicate> predicates = new ArrayList<>();
				
				//单表查询，只涉及到当前类，也就是Courier类
				if (StringUtils.isNotBlank(courier.getCourierNum())) {
					//第一个参数就是条件中的属性名，，第二个参数是这个属性的值
					Predicate p1 = cb.equal(root.get("courierNum").as(String.class), courier.getCourierNum());
					predicates.add(p1);
				}
				
				if (StringUtils.isNotBlank(courier.getCompany())) {
					Predicate p2 = cb.like(root.get("company").as(String.class), "%" + courier.getCompany() + "%");
					predicates.add(p2);
				}
				
				if (StringUtils.isNotBlank(courier.getType())) {
					Predicate p3 = cb.equal(root.get("type").as(String.class), courier.getType());
					predicates.add(p3);
				}
				
				//多表查询，涉及到了Standard表，需要使用Root(也即Courier)关联Standard,从而获得Standard的root对象
				//join方法关联的时候，第一个参数写的是该类在当前root对象里的属性名，所以是小写的standard
				//JoinType使用的是javax.persistence.criteria.JoinType;
				Join<Object, Object> standardRoot = root.join("standard", JoinType.INNER);
				if (courier.getStandard() != null && StringUtils.isNotBlank(courier.getStandard().getName())) {
					Predicate p4 = cb.like(standardRoot.get("name").as(String.class), "%" + courier.getStandard().getName() + "%");
					predicates.add(p4);
				}
				
				//返回上面这些条件的一个集合体。and里面传Predicate的数组。
				//由于不能确保每一个Predicate都不是null，所以我们采用集合的形式对他们进行存储，最后在转成数组
				return cb.and(predicates.toArray(new Predicate[0]));
			}
			
		};
		
		
		Page<Courier> pageData = courierService.findPageData(specification, pageable);
		HashMap<Object, Object> map = new HashMap<>();
		map.put("total", pageData.getTotalElements());
		map.put("rows", pageData.getContent());
		
		
		ActionContext.getContext().getValueStack().push(map);
		return SUCCESS;
	}
	
	
}
