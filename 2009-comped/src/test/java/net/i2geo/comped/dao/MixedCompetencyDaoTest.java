/**
 * 
 */
package net.i2geo.comped.dao;


import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.appfuse.dao.BaseDaoTestCase;
import org.appfuse.dao.UserDao;

import net.i2geo.comped.model.Competency;
import net.i2geo.comped.model.CompetencyProcess;
import net.i2geo.comped.model.ConcreteCompetency;
import net.i2geo.comped.model.Thing;

import org.springframework.dao.DataAccessException;

/**
 * This class tests the mixture of persisting sub classes of Competency. 
 * 
 * @author Martin Homik
 *
 */
public class MixedCompetencyDaoTest extends BaseDaoTestCase {

	// competency DAOs
	private ConcreteCompetencyDao concreteCompetencyDao = null;
	private CompetencyProcessDao  competencyProcessDao  = null;
	private CompetencyDao         competencyDao         = null;
	// user DAO
	private UserDao               userDao               = null;

	/************************************
	 * Set DAOS
	 ************************************/
    public void setConcreteCompetencyDao(ConcreteCompetencyDao concreteCompetencyDao) {
        this.concreteCompetencyDao = concreteCompetencyDao;
    }

    public void setCompetencyProcessDao(CompetencyProcessDao competencyProcessDao) {
		this.competencyProcessDao = competencyProcessDao;
	}
    
    public void setCompetencyDao(CompetencyDao competencyDao) {
		this.competencyDao = competencyDao;
	}
    
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

	/************************************
	 * Tests
	 ************************************/
    
    // Type mismatch: cannot convert from List<ConcreteCompetency> to List<Competency>
	// List<Competency> c2 = concreteCompetencyDao.getAll();

    // Type mismatch: cannot convert from List<Competency> to List<ConcreteCompetency>
	// List<Competency> c2 = concreteCompetencyDao.getAll();

    // No type mismatch:
    // Competency competency = concreteCompetencyDao.get(160L);
    // Competency competency = competencyProcessDao.get(180L);

    public void testFindAll() throws Exception {
    	List<Competency> c2 = competencyDao.getAll();
    	for (Competency c : c2) {
    		if (c instanceof ConcreteCompetency) {
    			ConcreteCompetency cc = (ConcreteCompetency) c;
    			log.debug("Concrete competency: " + cc);
    			continue;
    		}
    		if (c instanceof CompetencyProcess) {
    			CompetencyProcess cp = (CompetencyProcess) c;
    			log.debug("Competency process: " + cp);
    			continue;
    		}
    		
			log.debug("General competency: " + c);
    		
    	}
    	
    	List<ConcreteCompetency> competencies = concreteCompetencyDao.getAll();
    	assertNotNull(competencies);
    	assertTrue(competencies.size() >= 1);
    	assertEquals(6, competencies.size());
    	log.debug("Number of competencies: " + competencies.size());
    }

    /**
     * Mixed scenario in which a CompetencyProcess composes three concrete competencies.
     * Stick to correct competency types.
     * 
     * @throws Exception
     */
    public void testAddAndRemoveComposition() throws Exception {
    	log.debug("Testing composition ...");
    	
        Date date = new Date();
 
    	CompetencyProcess process = new CompetencyProcess();
    	process.setName("CompositionTestSuper");
    	process.setCreated(date);
        process.setModified(date);
    	process.setStatus(Thing.STATUS_PUBLISHED);
    	process.setCommentStatus(Thing.COMMENTS_OPENED);
    	process.setCommentDays(0);
    	process.setCreator(userDao.get(-1L));

        ConcreteCompetency c2 = new ConcreteCompetency();
    	c2.setName("CompositionTestSub1");
    	c2.setCreated(date);
        c2.setModified(date);
    	c2.setStatus(Thing.STATUS_PUBLISHED);
    	c2.setCommentStatus(Thing.COMMENTS_OPENED);
    	c2.setCommentDays(0);
    	c2.setCreator(userDao.get(-1L));
    	
        ConcreteCompetency c3 = new ConcreteCompetency();
    	c3.setName("CompositionTestSub2");
    	c3.setCreated(date);
        c3.setModified(date);
    	c3.setStatus(Thing.STATUS_PUBLISHED);
    	c3.setCommentStatus(Thing.COMMENTS_OPENED);
    	c3.setCommentDays(0);
    	c3.setCreator(userDao.get(-1L));

    	ConcreteCompetency c4 = new ConcreteCompetency();
    	c4.setName("CompositionTestSub3");
    	c4.setCreated(date);
        c4.setModified(date);
    	c4.setStatus(Thing.STATUS_PUBLISHED);
    	c4.setCommentStatus(Thing.COMMENTS_OPENED);
    	c4.setCommentDays(0);
    	c4.setCreator(userDao.get(-1L));

    	c2 = concreteCompetencyDao.save(c2);
    	c3 = concreteCompetencyDao.save(c3);
    	c4 = concreteCompetencyDao.save(c4);

    	List<Competency> cList = new ArrayList<Competency>();
    	cList.add(c2);
    	cList.add(c3);
    	cList.add(c4);

    	process.setComposes(cList);
    	process = competencyProcessDao.save(process);
    	flush();
    	
    	// Now, let's test "composes"
        process = competencyProcessDao.get(process.getId());
        assertNotNull(process);
        List<Competency> result = process.getComposes();
        assertNotNull(result);
        assertEquals(3, result.size());
        
        // get first competency in result set and test "composedBy" 
        // this could be successful because of an eager fetch strategy
        // maybe a better way is to go through the dao.
        log.debug("testing composedBy ...");
        Competency c = result.get(1);
        cList = c.getComposedBy();
        assertNotNull(cList);
        assertEquals(1, cList.size());

        
        log.debug("removing sub competency 3 ...");

        // first update c3
        c3 = concreteCompetencyDao.get(c3.getId());
        
        List <Competency> c3ComposedBy= c3.getComposedBy();
        for (Iterator<Competency> iter = c3ComposedBy.iterator(); iter.hasNext();) {
        	Competency parent = iter.next();
            // remove c3 from parent composition competency
        	parent.removeComposes(c3);
        	// TODO: ugly cast!!!!
        	if (parent instanceof ConcreteCompetency){
        		concreteCompetencyDao.save((ConcreteCompetency) parent);
        	}
        	if (parent instanceof CompetencyProcess) {
        		competencyProcessDao.save((CompetencyProcess) parent);
        	}
        }
        
        concreteCompetencyDao.remove(c3.getId());
        flush();
    
        try {
        	// check if c3 still exists in database
        	concreteCompetencyDao.get(c3.getId());
            fail("Competency found in database");
        } catch (DataAccessException dae) {
            log.debug("Expected exception: " + dae.getMessage());
            assertNotNull(dae);
        }
        
        try {
        	// update c1
            process = competencyProcessDao.get(process.getId());
        } catch (DataAccessException dae) {
        	log.debug(dae.getMessage());
        	fail("Cascading type might include deletion");
        }

        // c1 should have only 2 children
        result = process.getComposes();
        assertNotNull(result);
        assertEquals(2, result.size());

        log.debug("removing top competency ...");
        process = competencyProcessDao.get(process.getId());
        process.setComposes(null);
        competencyProcessDao.save(process);
        flush();

        competencyProcessDao.remove(process.getId());
        flush();
        
        c2 = concreteCompetencyDao.get(c2.getId());
        List<Competency> c2list = c2.getComposedBy();
        
        // c2 should have no composition parent any more
        assertEquals(0, c2list.size());
    }

	/**
	 * Mixed scenario in which a CompetencyProcess composes three concrete competencies.
	 * This scenario is the same, but for persistence it does not use any specific
	 * DAO classes. It simply uses the CompetencyDao class. What if one sub class adds a
	 * new property?
	 * 
	 * @throws Exception
	 */
	public void testAddAndRemoveCompositionGeneralTypes() throws Exception {
		log.debug("Testing general composition ...");
	
		int sizeProcess  = competencyProcessDao.getAll().size();
		int sizeConcrete = concreteCompetencyDao.getAll().size();
		
	    Date date = new Date();
	
		Competency process = new CompetencyProcess();
		process.setName("CompositionTestSuper");
		process.setCreated(date);
	    process.setModified(date);
		process.setStatus(Thing.STATUS_PUBLISHED);
		process.setCommentStatus(Thing.COMMENTS_OPENED);
		process.setCommentDays(0);
		process.setCreator(userDao.get(-1L));
	
	    Competency c2 = new ConcreteCompetency();
		c2.setName("CompositionTestSub1");
		c2.setCreated(date);
	    c2.setModified(date);
		c2.setStatus(Thing.STATUS_PUBLISHED);
		c2.setCommentStatus(Thing.COMMENTS_OPENED);
		c2.setCommentDays(0);
		c2.setCreator(userDao.get(-1L));
		
	    Competency c3 = new ConcreteCompetency();
		c3.setName("CompositionTestSub2");
		c3.setCreated(date);
	    c3.setModified(date);
		c3.setStatus(Thing.STATUS_PUBLISHED);
		c3.setCommentStatus(Thing.COMMENTS_OPENED);
		c3.setCommentDays(0);
		c3.setCreator(userDao.get(-1L));
	
		Competency c4 = new ConcreteCompetency();
		c4.setName("CompositionTestSub3");
		c4.setCreated(date);
	    c4.setModified(date);
		c4.setStatus(Thing.STATUS_PUBLISHED);
		c4.setCommentStatus(Thing.COMMENTS_OPENED);
		c4.setCommentDays(0);
		c4.setCreator(userDao.get(-1L));
	
		c2 = competencyDao.save(c2);
		c3 = competencyDao.save(c3);
		c4 = competencyDao.save(c4);
	
		List<Competency> cList = new ArrayList<Competency>();
		cList.add(c2);
		cList.add(c3);
		cList.add(c4);
	
		process.setComposes(cList);
		process = competencyDao.save(process);
		flush();
		
		// Let's test size of processes and individuals
		 int newSizeProcess  = competencyProcessDao.getAll().size();
		 int newSizeConcrete = concreteCompetencyDao.getAll().size();
		 
		 assertTrue(newSizeProcess > sizeProcess);
		 assertTrue(newSizeConcrete > sizeConcrete);
		 
		 assertEquals(sizeProcess+1, newSizeProcess);
		 assertEquals(sizeConcrete+3, newSizeConcrete);
		 
		// Now, let's test "composes"	    
		process = competencyDao.get(process.getId());
	    assertNotNull(process);
	    List<Competency> result = process.getComposes();
	    assertNotNull(result);
	    assertEquals(3, result.size());
	    
	    // get first competency in result set and test "composedBy" 
	    // this could be successful because of an eager fetch strategy
	    // maybe a better way is to go through the dao.
	    log.debug("testing composedBy ...");
	    Competency c = result.get(1);
	    cList = c.getComposedBy();
	    assertNotNull(cList);
	    assertEquals(1, cList.size());
	
	    
	    log.debug("removing sub competency 3 ...");
	
	    // first update c3
	    c3 = competencyDao.get(c3.getId());
	    
	    List <Competency> c3ComposedBy= c3.getComposedBy();
	    for (Iterator<Competency> iter = c3ComposedBy.iterator(); iter.hasNext();) {
	    	Competency parent = iter.next();
	        // remove c3 from parent composition competency
	    	parent.removeComposes(c3);
	    	// TODO: ugly cast!!!!
	    	competencyDao.save(parent);
	    	/*
	    	if (parent instanceof ConcreteCompetency){
	    		competencyDao.save((ConcreteCompetency) parent);
	    	}
	    	if (parent instanceof CompetencyProcess) {
	    		competencyProcessDao.save((CompetencyProcess) parent);
	    	}
	    	*/
	    }
	    
	    competencyDao.remove(c3.getId());
	    flush();
	
	    try {
	    	// check if c3 still exists in database
	    	competencyDao.get(c3.getId());
	        fail("Competency found in database");
	    } catch (DataAccessException dae) {
	        log.debug("Expected exception: " + dae.getMessage());
	        assertNotNull(dae);
	    }
	    
	    try {
	    	// update c1
	        process = competencyDao.get(process.getId());
	    } catch (DataAccessException dae) {
	    	log.debug(dae.getMessage());
	    	fail("Cascading type might include deletion");
	    }
	
	    // c1 should have only 2 children
	    result = process.getComposes();
	    assertNotNull(result);
	    assertEquals(2, result.size());
	
	    log.debug("removing top competency ...");
	    process = competencyDao.get(process.getId());
	    process.setComposes(null);
	    competencyDao.save(process);
	    flush();
	
	    competencyDao.remove(process.getId());
	    flush();
	    
	    c2 = competencyDao.get(c2.getId());
	    List<Competency> c2list = c2.getComposedBy();
	    
	    // c2 should have no composition parent any more
	    assertEquals(0, c2list.size());
	}
        
}
