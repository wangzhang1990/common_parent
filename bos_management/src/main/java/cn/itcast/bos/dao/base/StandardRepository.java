package cn.itcast.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.base.Standard;

public interface StandardRepository extends JpaRepository<Standard, Integer> {
	public List<Standard> findByNameLike(String nameLike);
	public Standard findByName(String name);
	
	@Query(value = "from Standard where maxWeight = ?", nativeQuery = false)
	public Standard queryMaxWeight(Integer maxWeight);
	
	@Query(value = "update Standard set minLength = ? where id = ?")
	@Modifying
	public void updateMinLength(Integer min, Integer id);
}
