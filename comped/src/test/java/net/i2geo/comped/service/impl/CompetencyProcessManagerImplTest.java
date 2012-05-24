/**
 * 
 */
package net.i2geo.comped.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.appfuse.service.impl.BaseManagerMockTestCase;
import org.jmock.Expectations;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import net.i2geo.comped.dao.CompetencyProcessDao;
import net.i2geo.comped.model.Competency;
import net.i2geo.comped.model.CompetencyProcess;
import net.i2geo.comped.model.Name;
/**
 * @author Martin Homik
 *
 */
public class CompetencyProcessManagerImplTest extends BaseManagerMockTestCase {

    private CompetencyProcessDao dao = null;
	private CompetencyProcessManagerImpl manager = null;
	
	@Before
    public void setUp() throws Exception {
        dao = context.mock(CompetencyProcessDao.class);
        manager = new CompetencyProcessManagerImpl(dao);
        manager.setLocale(Locale.ENGLISH);
    }
	
	@After
    public void tearDown() throws Exception {
        manager = null;
    }

	@Test
    public void testGetCompetency() {
        log.debug("testing getCompetency");

        final Long id = 777777L;
        final Competency competency = new CompetencyProcess();

        // set expected behavior on dao
        context.checking(new Expectations() {{
            one(dao).get(with(equal(id)));
            will(returnValue(competency));
        }});

        Competency result = manager.get(id);
        assertSame(competency, result);
    }

	@Test
    public void testGetCompetencyByLocale() {
        log.debug("testing getCompetency by locale");

        final Long id = 777777L;
        final String locale = "en";
        final String name = "no_real_item";
        final Competency competency = new CompetencyProcess();
        competency.setUri("http://www.activemath.org/ontology.owl#" + name);

        // set expected behavior on dao
        context.checking(new Expectations() {{
            one(dao).get(with(equal(id)));
            will(returnValue(competency));
        }});

        Competency result = manager.get(id, locale);
        assertTrue(result.getName().endsWith(name));
    }

	@Test
    public void testGetCompetencies() {
        log.debug("testing getCompetencies");

        final List<Competency> competencies = new ArrayList<Competency>();

        // set expected behavior on dao
        context.checking(new Expectations() {{
            one(dao).getAll();
            will(returnValue(competencies));
        }});
        
        List<CompetencyProcess> result = manager.getAll();
        assertSame(competencies, result);
    }
	
	@Test
    public void testFindByUri() {
        log.debug("testing findByUri");

        final Competency competency = new CompetencyProcess();
        final String uri = "%dentify_similar_triangles%";
                
        // set expected behavior on dao
        context.checking(new Expectations() {{
        	one(dao).findByUri(with(equal(uri)));
        	will(returnValue(competency));
        }});

        Competency result = manager.findByUri(uri);
        assertNotNull(competency);
        assertSame(competency, result);
    }
	

	@Test
    public void testSaveCompetency() {
        log.debug("testing saveComptency");

        final CompetencyProcess competency = new CompetencyProcess();

        // set expected behavior on dao
        context.checking(new Expectations() {{
            one(dao).save(with(same(competency)));
        }});
        
        manager.prepare(competency);
        manager.save(competency);
    }

	@Test
    public void testRemoveCompetency() {
        log.debug("testing removeCompetency");

        final Long id = 777777L;

        // set expected behavior on dao
        context.checking(new Expectations(){{
        	one(dao).remove(with(equal(id)));
        }});

        manager.remove(id);
    }    

	@Test
    public void testFindByLastCreated() {
        log.debug("testing findByLastCreated");

        final int number = 5;
        final List<Competency> competencies = new ArrayList<Competency>();
                        
        // set expected behavior on dao
        context.checking(new Expectations() {{
        	one(dao).findByLastCreated(with(equal(number)));
        	will(returnValue(competencies));
        }});

        List<CompetencyProcess> result = manager.findByLastCreated(number);
        assertNotNull(competencies);
        assertSame(competencies, result);
    }

	@Test
    public void testFindByLastModified() {
        log.debug("testing findByLastModified");

        final int number = 5;
        final List<Competency> competencies = new ArrayList<Competency>();
                        
        // set expected behavior on dao
        context.checking(new Expectations() {{
        	one(dao).findByLastModified(with(equal(number)));
        	will(returnValue(competencies));
        }});

        List<CompetencyProcess> result = manager.findByLastModified(number);
        assertNotNull(competencies);
        assertSame(competencies, result);
    }

	@Test
	public void testFindByNameExact() {
		log.debug("testing findByNameExact");

		final String token = "Name 1";
		final String language = "en";
		
		final CompetencyProcess c = new CompetencyProcess();
		final Name n1 = new Name();
		final Name n2 = new Name();
		
		n1.setName("Name 1");
		n1.setType(Name.TYPE_COMMON);
		n1.setLocale("en");

		n2.setName("Name 2");
		n2.setType(Name.TYPE_COMMON);
		n2.setLocale("de");
		
		final List<Name> namesList = new ArrayList<Name>();
		namesList.add(n1);
		namesList.add(n2);
		
		c.setNames(namesList);
		
		final List<CompetencyProcess> cList = new ArrayList<CompetencyProcess>();
		cList.add(c);
		
        // set expected behavior on dao
        context.checking(new Expectations() {{
        	one(dao).findByNameExact(
        			with(equal(token)),
        			with(equal(language)));
        	will(returnValue(cList));
        }});

        List<CompetencyProcess> result = manager.findByNameExact(token, language);
        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals(1, result.size());
        assertEquals(token, result.get(0).getName());
	}

	@Test
	public void testDetach() {
		// TODO
	}
	
    /*
    public void testDetach() throws Exception {
    	log.debug("Testing that bloody 183!!!!");
    	CompetencyProcess c = competencyProcessDao.get(182L);
    	Competency parent = competencyProcessDao.get(183L);
    	
    	competencyProcessDao.detach(c);
    	
    	try {
            c = competencyProcessDao.get(c.getId());
            fail("Competency found in database");
        } catch (DataAccessException dae) {
            log.debug("Expected exception: " + dae.getMessage());
            assertNotNull(dae);
        }
    		
    	assertEquals(2, parent.getComposes().size());
    	
    }
    */

}
