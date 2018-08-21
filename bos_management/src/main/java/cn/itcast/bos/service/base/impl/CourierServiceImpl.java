package cn.itcast.bos.service.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.base.CourierRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;

@Service
@Transactional
public class CourierServiceImpl implements CourierService {
	
	@Autowired
	private CourierRepository courierRepository;
	
	@Override
	public void save(Courier courier) {
		// TODO Auto-generated method stub
		courierRepository.save(courier);
	}
	
	@Override
	public Page<Courier> findPageData(Specification<Courier> specification, Pageable pageable) {
		Page<Courier> pageData = courierRepository.findAll(specification, pageable);
		return pageData;
	}

	@Override
	public void delBatch(String[] idArray) {
		// TODO Auto-generated method stub
		for (String idStr : idArray) {
			courierRepository.updateDelTag(Integer.valueOf(idStr));
		}
	}

	@Override
	public void resBatch(String[] idArray) {
		// TODO Auto-generated method stub
		for (String idStr : idArray) {
			courierRepository.restoreDelTag(Integer.valueOf(idStr));
		}
	}
}
