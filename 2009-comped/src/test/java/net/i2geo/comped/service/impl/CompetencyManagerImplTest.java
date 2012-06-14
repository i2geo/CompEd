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

import net.i2geo.comped.dao.CompetencyDao;
import net.i2geo.comped.model.Competency;
import net.i2geo.comped.model.ConcreteCompetency;
/**
 * @author Martin Homik
 *
 */
public class CompetencyManagerImplTest extends BaseManagerMockTestCase {

    private CompetencyDao dao = null;
	private CompetencyManagerImpl manager = null;
	
	@Before
    public void setUp() throws Exception {
        dao = context.mock(CompetencyDao.class);
        manager = new CompetencyManagerImpl(dao);
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
        final Competency competency = new ConcreteCompetency();

        // set expected behavior on dao
        context.checking(new Expectations() {{
            one(dao).get(with(equal(id)));
            will(returnValue(competency));
        }});

        Competency result = manager.get(id);
        assertSame(competency, result);
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
        
        List<Competency> result = manager.getAll();
        assertSame(competencies, result);
    }

	@Test
    public void testFindByUri() {
        log.debug("testing findByUri");

        final Competency competency = new ConcreteCompetency();
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

        final Competency competency = new ConcreteCompetency();

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

        List<Competency> result = manager.findByLastCreated(number);
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

        List<Competency> result = manager.findByLastModified(number);
        assertNotNull(competencies);
        assertSame(competencies, result);
    }
	
	@Test
	public void testDetach() {
		// TODO
	}
	
    /*    
    public void testDetach() throws Exception {
    	log.debug("Testing that bloody 163!!!!");
    	Competency c = competencyDao.get(163L);
    	Competency parent = competencyDao.get(161L);
    	
    	competencyDao.detach(c);
    	
    	try {
            c = competencyDao.get(c.getId());
            fail("Competency found in database");
        } catch (DataAccessException dae) {
            log.debug("Expected exception: " + dae.getMessage());
            assertNotNull(dae);
        }
    		
    	assertEquals(2, parent.getSubsumes().size());
    	assertEquals(2, parent.getComposes().size());
    	
    }
    */

}
