package cn.itcast.bos.web.action.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
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

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;
import cn.itcast.bos.utils.PinYin4jUtils;
import cn.itcast.bos.web.action.common.BaseAction;

@Controller
@ParentPackage("json-default")
@Namespace("/")
@Scope("prototype")
public class AreaAction extends BaseAction<Area> {
	
	//private Area area = new Area();
	
	@Autowired
	private AreaService areaService;
	
	private File file;
	private String fileContentType;
	private String fileFileName;

	public void setFile(File file) {
		this.file = file;
	}
	
	
	public void setFileContentType(String fileContentType) {
		this.fileContentType = fileContentType;
	}


	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}


	@Action("area_batchImport")
	public String batchImport() {
		ArrayList<Area> areas = new ArrayList<>();
		try {
			HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
			HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
			for (Row row : sheet) {
				if (row.getRowNum() == 0) {
					continue;
				}
				Area area = new Area();
				area.setId(row.getCell(0).getStringCellValue());
				area.setProvince(row.getCell(1).getStringCellValue());
				area.setCity(row.getCell(2).getStringCellValue());
				area.setDistrict(row.getCell(3).getStringCellValue());
				area.setPostcode(row.getCell(4).getStringCellValue());
				
				//生成城市编码和区域简码
				String city = area.getCity();
				String province = area.getProvince();
				String district = area.getDistrict();
				city = city.substring(0, city.length() - 1);
				province = province.substring(0, province.length() - 1);
				district = district.substring(0, district.length() - 1);
				
				String[] headByString = PinYin4jUtils.getHeadByString(province + city + district);
				StringBuffer stringBuffer = new StringBuffer();
				for (String str : headByString) {
					stringBuffer.append(str);
				}
				area.setShortcode(stringBuffer.toString());
				area.setCitycode(PinYin4jUtils.hanziToPinyin(city, ""));
				
				areas.add(area);
				areaService.saveBatch(areas);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return NONE;
	}
	
	
	@Action(value = "area_pageQuery", results = { @Result(name = "success", type = "json") })
	public String pageQuery() {
		
		
		Specification<Area> specification = new Specification<Area>() {

			@Override
			public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				ArrayList<Predicate> predicates = new ArrayList<>();
				
				if (StringUtils.isNotBlank(model.getCity())) {
					Predicate p1 = cb.like(root.get("city").as(String.class), "%" + model.getCity() + "%");
					predicates.add(p1);
				}
				if (StringUtils.isNotBlank(model.getProvince())) {
					Predicate p2 = cb.like(root.get("province").as(String.class), "%" + model.getProvince() + "%");
					predicates.add(p2);
				}
				if (StringUtils.isNotBlank(model.getDistrict())) {
					Predicate p3 = cb.like(root.get("district").as(String.class), "%" + model.getDistrict() + "%");
					predicates.add(p3);
				}
				return cb.and(predicates.toArray(new Predicate[0]));
			}
		};
		
		Pageable pageable = new PageRequest(page - 1, rows);
		Page<Area> pageData = areaService.findPageData(specification, pageable);
		
		pushPageDateToValueStack(pageData);
		return SUCCESS;
	}


	/*@Override
	public Area getModel() {
		// TODO Auto-generated method stub
		return area;
	}*/
}
