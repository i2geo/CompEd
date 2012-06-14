/**
 * 
 */
package net.i2geo.comped.dao;

import java.util.List;

import org.appfuse.dao.BaseDaoTestCase;
import org.junit.After;
import org.junit.Before;

import net.i2geo.comped.model.Competency;
import net.i2geo.comped.model.Name;
import net.i2geo.comped.dao.NameDao;
import org.springframework.dao.DataAccessException;

/**
 * @author Martin Homik
 *
 */
public class NameDaoTest extends BaseDaoTestCase {

	private NameDao       nameDao       = null;
	private CompetencyDao competencyDao = null;
	private Competency    competency    = null;
		
    public void setNameDao(NameDao nameDao) {
        this.nameDao = nameDao;
    }
    
    public void setCompetencyDao(CompetencyDao competencyDao) {
		this.competencyDao = competencyDao;
	}
    
	@Before
    public void onSetUp() throws Exception {
		competency = competencyDao.get(160L);
	}
	
	@After
    public void onTearDown() throws Exception {
		competency = null;
	}
    
	public void testFindByNameExact() throws Exception {
		List<Name> names = nameDao.findByNameExact("magnitude");
		assertNotNull(names);
		assertTrue(names.size() > 0);
		assertEquals(1, names.size());
	}
	
	public void testFindByNameLike() throws Exception {
		List<Name> names = nameDao.findByNameLike("%agnit%");
		assertNotNull(names);
		assertTrue(names.size() > 0);
		assertEquals(2, names.size());
	}
	
	public void testGetByLocale() throws Exception {
		List<Name> names = nameDao.getByLocale("de");
		assertNotNull(names);
		assertTrue(names.size() > 0);
		assertEquals(14, names.size());		
	}
	
	public void testFindByNameAndLocaleExact() throws Exception {
		List<Name> names = nameDao.findByNameAndLocaleExact("magnitude", "en");
		assertNotNull(names);
		assertTrue(names.size() > 0);
		assertEquals(1, names.size());
	}
	
	public void testFindByNameAndLocaleLike() throws Exception {
		List<Name> names = nameDao.findByNameAndLocaleLike("%agnit%", "en");
		assertNotNull(names);
		assertTrue(names.size() > 0);
		assertEquals(1, names.size());
	}
	
	public void testFindByType() throws Exception {
		List<Name> names = nameDao.findByType("COMMON");
		assertNotNull(names);
		assertTrue(names.size() > 0);
		assertEquals(16, names.size());

		names = nameDao.findByType("RARE");
		assertNotNull(names);
		assertTrue(names.size() > 0);
		assertEquals(6, names.size());

		names = nameDao.findByType("FALSEFRIEND");
		assertNotNull(names);
		assertTrue(names.size() > 0);
		assertEquals(3, names.size());
	}

	public void testFindByTypeAndLocale() throws Exception {
		List<Name> names = nameDao.findByTypeAndLocale("COMMON", "de");
		assertNotNull(names);
		assertTrue(names.size() > 0);
		assertEquals(7, names.size());		
	}

	public void testFindByThing() throws Exception {
		List<Name> names = nameDao.findByThing(competency);
		assertNotNull(names);
		assertTrue(names.size() > 0);
		assertEquals(4, names.size());				
	}
	
	public void testFindByThingAndType() throws Exception {
		List<Name> names = nameDao.findByThingAndType(competency, "COMMON");
		assertNotNull(names);
		assertTrue(names.size() > 0);
		assertEquals(3, names.size());		
		
	}
	
	public void testFindByThingAndTypeAndLocale() throws Exception {
		List<Name> names = nameDao.findByThingAndTypeAndLocale(competency, "COMMON", "de");
		assertNotNull(names);
		assertTrue(names.size() > 0);
		assertEquals(1, names.size());		
		
	}
	
	public void testFindDefaultByThingAndTypeAndLocale() throws Exception {
		List<Name> names = nameDao.findDefaultByThingAndTypeAndLocale(competency, "COMMON", "de");
		assertNotNull(names);
		assertTrue(names.size() > 0);
		assertEquals(1, names.size());		
		
	}
	
    
    public void testAddAndRemoveName() throws Exception {
        Name name = new Name();

    	name.setName("derivative");
    	name.setType("COMMON");
    	name.setLocale("en");
    	name.setDefName(true);
    	
        name = nameDao.save(name);
        flush();

        name = nameDao.get(name.getId());

        assertNotNull(name.getId());

        log.debug("removing name ...");

        nameDao.remove(name.getId());
        flush();

        try {
            nameDao.get(name.getId());
            fail("Name found in database");
        } catch (DataAccessException dae) {
            log.debug("Expected exception: " + dae.getMessage());
            assertNotNull(dae);
        }
    }
}
