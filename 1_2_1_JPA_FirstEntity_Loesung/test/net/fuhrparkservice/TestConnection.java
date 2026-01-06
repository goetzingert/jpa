package net.fuhrparkservice;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;

import jakarta.persistence.Query;

import net.rentacar.model.VehicleType;

import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.Test;

public class TestConnection extends AbstractJPATestCase {
	
	@Override
	public void setUp() throws Exception {
		
		manager.persist(new VehicleType("1", "VW","Golf",120,200));
		manager.flush();
		manager.clear();
	}
	
	@Test public void testFind()
	{
		assertNotNull(super.manager.find(VehicleType.class, "1").getId());
		manager.getTransaction().commit();
		manager.close();
		manager = managerFactory.createEntityManager();
		manager.getTransaction().begin();
		super.manager.find(VehicleType.class, "1");
		Query createQuery = super.manager.createQuery("Select f FROM VehicleType f WHERE f.id = '1'");
		createQuery.setHint("org.hibernate.cacheable", true);
		createQuery.setHint("org.hibernate.cacheMode", "NORMAL");
		assertNotNull(createQuery.getResultList());
		manager.persist(new VehicleType(UUID.randomUUID().toString(), "ad", "dfdf", 100, 200));
		assertNotNull(createQuery.getResultList());
		Session unwrap = manager.unwrap(Session.class);
		Statistics statistics = unwrap.getSessionFactory().getStatistics();
		long secondLevelCacheHitCount = statistics.getSecondLevelCacheHitCount();
		System.out.println(secondLevelCacheHitCount);
		long queryCacheHitCount = statistics.getQueryCacheHitCount();
		System.out.println(queryCacheHitCount);
	}
	
	

}
