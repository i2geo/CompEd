/**
 * 
 */
package net.i2geo.comped.dao;

import java.util.List;

import org.appfuse.dao.BaseDaoTestCase;
import net.i2geo.comped.model.Description;
import net.i2geo.comped.dao.DescriptionDao;
import org.springframework.dao.DataAccessException;

/**
 * @author Martin Homik
 *
 */
public class DescriptionDaoTest extends BaseDaoTestCase {

	private DescriptionDao descriptionDao               = null;
	
    public void setDescriptionDao(DescriptionDao descriptionDao) {
        this.descriptionDao = descriptionDao;
    }
    
    /*
     * Fails with Derby.
	 * org.apache.derby.client.am.SqlException: Comparisons between 'CLOB (UCS_BASIC)' and 
	 * 'CLOB (UCS_BASIC)' are not supported. Types must be comparable. String types must also 
	 * have matching collation. If collation does not match, a possible solution is to cast 
	 * operands to force them to the default collation (e.g. 
	 * SELECT tablename FROM sys.systables WHERE CAST(tablename AS VARCHAR(128)) = 'T1')
     */
	public void testFindByDescriptionExact() throws Exception {
		List<Description> descriptions = descriptionDao.findByDescriptionExact("magnitude");
		assertNotNull(descriptions);
		assertTrue(descriptions.size() > 0);
		assertEquals(1, descriptions.size());
	}
    
	public void testFindByDescriptionLike() throws Exception {
		List<Description> descriptions = descriptionDao.findByDescriptionLike("%agnit%");
		assertNotNull(descriptions);
		assertTrue(descriptions.size() > 0);
		assertEquals(2, descriptions.size());
	}
	
	public void testGetByLocale() throws Exception {
		List<Description> descriptions = descriptionDao.getByLocale("de");
		assertNotNull(descriptions);
		assertTrue(descriptions.size() > 0);
		assertEquals(4, descriptions.size());
		
	}
	
	/*
	 * Fails with Derby.
	 * org.apache.derby.client.am.SqlException: Comparisons between 'CLOB (UCS_BASIC)' and 
	 * 'CLOB (UCS_BASIC)' are not supported. Types must be comparable. String types must also 
	 * have matching collation. If collation does not match, a possible solution is to cast 
	 * operands to force them to the default collation (e.g. 
	 * SELECT tablename FROM sys.systables WHERE CAST(tablename AS VARCHAR(128)) = 'T1')
	 * 
	 */ 
	public void testFindByDescriptionAndLocaleExact() throws Exception {
		List<Description> descriptions = descriptionDao.findByDescriptionAndLocaleExact("magnitude", "en");
		assertNotNull(descriptions);
		assertTrue(descriptions.size() > 0);
		assertEquals(1, descriptions.size());
	}

	public void testFindByDescriptionAndLocaleLike() throws Exception {
		List<Description> descriptions = descriptionDao.findByDescriptionAndLocaleLike("%agnit%", "en");
		assertNotNull(descriptions);
		assertTrue(descriptions.size() > 0);
		assertEquals(1, descriptions.size());
	}

	/*
	public void testFindByThing() throws Exception {
		List<Description> descriptions = descriptionDao.findByThing(competency);
		assertNotNull(descriptions);
		assertTrue(descriptions.size() > 0);
		assertEquals(4, descriptions.size());				
	}
	
	public void testFindByThingAndLocale() throws Exception {
		List<Description> descriptions = descriptionDao.findByThingAndLocale(competency, "de");
		assertNotNull(descriptions);
		assertTrue(descriptions.size() > 0);
		assertEquals(1, descriptions.size());		
	}
	*/
	
    public void testAddAndRemoveDescription() throws Exception {
        Description description = new Description();

    	description.setDescription("derivative");
    	description.setLocale("en");
    	
        description = descriptionDao.save(description);
        flush();

        description = descriptionDao.get(description.getId());

        assertNotNull(description.getId());

        log.debug("removing description ...");

        descriptionDao.remove(description.getId());
        flush();

        try {
            descriptionDao.get(description.getId());
            fail("Description found in database");
        } catch (DataAccessException dae) {
            log.debug("Expected exception: " + dae.getMessage());
            assertNotNull(dae);
        }
    }
}
