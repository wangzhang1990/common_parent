package cn.itcast.bos.dao.base;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.domain.base.Standard;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class StandardRepositoryTest {
	
		@Autowired
		private StandardRepository standardRepository;
		
		@Test
		@Transactional
		@Rollback(false)
		public void updateMin() {
			standardRepository.updateMinLength(11, 2);
		}
		
		@Test
		public void testQuery() {
			Standard findByName = standardRepository.findByName("1-10公斤");
			System.out.println(findByName.getMaxWeight());
		}
		
		@Test
		public void testQueryLike() {
			List<Standard> findByNameLike = standardRepository.findByNameLike("%公斤");
			System.out.println(findByNameLike);
		}
		
		@Test
		public void testQueryMax() {
			Standard queryMaxWeight = standardRepository.queryMaxWeight(20);
			System.out.println(queryMaxWeight);
		}
}
