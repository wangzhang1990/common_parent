package cn.itcast.bos.dao.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.base.Courier;

public interface CourierRepository extends JpaRepository<Courier, Integer>, JpaSpecificationExecutor<Courier> {
	
	@Query("update Courier set deltag = '1' where id = ?")
	@Modifying
	void updateDelTag(Integer valueOf);
	
	@Query("update Courier set deltag = null where id = ?")
	@Modifying
	void restoreDelTag(Integer valueOf);
	
}
