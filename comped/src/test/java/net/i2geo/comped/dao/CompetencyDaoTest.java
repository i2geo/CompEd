/**
 * 
 */
package net.i2geo.comped.dao;


import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.appfuse.dao.BaseDaoTestCase;
import org.appfuse.dao.UserDao;

import net.i2geo.comped.model.Competency;
import net.i2geo.comped.model.Name;
import net.i2geo.comped.model.Thing;
import net.i2geo.comped.util.NameUtil;

import org.springframework.dao.DataAccessException;

/**
 * @author Martin Homik
 *
 */
public class CompetencyDaoTest extends BaseDaoTestCase {

	private CompetencyDao competencyDao = null;
	private UserDao       userDao       = null;
	
    public void setCompetencyDao(CompetencyDao competencyDao) {
        this.competencyDao = competencyDao;
    }
	
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void testFindAll() throws Exception {
    	List<Competency> competencies = competencyDao.getAll();
    	assertTrue(competencies.size() >= 1);
    	log.debug("Number of competencies: " + competencies.size());
    }
    
    // Find an occurrence 
    public void testFindCompetencyByUri() throws Exception {
        Competency competency = competencyDao.findByUri("%dentify_similar_triangles%");
        assertNotNull(competency);
        assertEquals(new Long(165), competency.getId());
    }
    
    public void testFindComposes() throws Exception {
    	Competency c = competencyDao.get(161L);
    	List<Competency> composes = c.getComposes();
    	assertNotNull(composes);
    	assertEquals(3, composes.size());
    }

    public void testFindComposedBy() throws Exception {
    	Competency c = competencyDao.get(162L);
    	List<Competency> composedBy = c.getComposedBy();
    	assertNotNull(composedBy);
    	assertEquals(2, composedBy.size());
    }
    
    public void testFindSubsumes() throws Exception {
    	Competency c = competencyDao.get(161L);
    	List<Competency> subsumes = c.getSubsumes();
    	assertNotNull(subsumes);
    	assertEquals(3, subsumes.size());
    }

    public void testFindSubsumedBy() throws Exception {
    	Competency c = competencyDao.get(162L);
    	List<Competency> subsumedBy = c.getSubsumedBy();
    	assertNotNull(subsumedBy);
    	assertEquals(2, subsumedBy.size());
    }

    public void testFindSimilarTo() throws Exception {
    	Competency c = competencyDao.get(161L);
    	Set<Competency> similarTo = c.getSimilarTo();
    	assertNotNull(similarTo);
    	assertEquals(3, similarTo.size());
    }

    public void testFindInverseSimilarTo() throws Exception {
    	Competency c = competencyDao.get(162L);
    	Set<Competency> inverseSimilarTo = c.getInverseSimilarTo();
    	assertNotNull(inverseSimilarTo);
    	assertEquals(1, inverseSimilarTo.size());
    }
    
    // Sometimes, a similarity is indicated only in one direction.
    public void testFindAllSimilarities() throws Exception {
    	Competency c = competencyDao.get(161L);
    	Set<Competency> similarTo = competencyDao.findAllSimilarities(c);
    	assertNotNull(similarTo);
    	assertEquals(4, similarTo.size());
    }

    // TODO
    public void testFindTopics() throws Exception {
    	
    }

    // TODO
    public void testGetNames() throws Exception {
    	Competency c = competencyDao.get(164L);
    	List<Name> names = competencyDao.getNames(c); 
    	assertNotNull(names);
    	assertTrue(names.size() >=1);
    	log.debug("==================== Number if Names (through DAO): " + names.size());
    }

    // TODO
    public void testFindNames() throws Exception {
    	
    }

    // TODO
    public void testFindDescriptions() throws Exception {
    	
    }
    
    public void testAddAndRemoveCompetency() throws Exception {
        Competency competency = new Competency();

        String testDescription = "Invincible description";
        
        // set competency properties
        competency.setName("Healthy Competency");
        competency.setDescription(testDescription);

        // set Thing properties
        Date date = new Date();
        competency.setCreated(date);
        competency.setModified(date);
    	competency.setStatus(Thing.STATUS_PUBLISHED);
    	competency.setCommentStatus(Thing.COMMENTS_OPENED);
    	competency.setCommentDays(0);
    	competency.setCreator(userDao.get(-1L));
        log.info(competency);
        
        competency = competencyDao.save(competency);
        flush();

        competency = competencyDao.get(competency.getId());

        assertEquals(testDescription, competency.getDescription());
        assertNotNull(competency.getId());

        log.debug("removing competency ...");

        competencyDao.remove(competency.getId());
        flush();

        try {
            competencyDao.get(competency.getId());
            fail("Competency found in database");
        } catch (DataAccessException dae) {
            log.debug("Expected exception: " + dae.getMessage());
            assertNotNull(dae);
        }
    }
    
    public void testAddAndRemoveComposition() throws Exception {
    	log.debug("Testing composition ...");
    	
        Date date = new Date();
 
    	Competency c1 = new Competency();
    	c1.setName("CompositionTestSuper");
    	c1.setCreated(date);
        c1.setModified(date);
    	c1.setStatus(Thing.STATUS_PUBLISHED);
    	c1.setCommentStatus(Thing.COMMENTS_OPENED);
    	c1.setCommentDays(0);
    	c1.setCreator(userDao.get(-1L));

        Competency c2 = new Competency();
    	c2.setName("CompositionTestSub1");
    	c2.setCreated(date);
        c2.setModified(date);
    	c2.setStatus(Thing.STATUS_PUBLISHED);
    	c2.setCommentStatus(Thing.COMMENTS_OPENED);
    	c2.setCommentDays(0);
    	c2.setCreator(userDao.get(-1L));
    	
        Competency c3 = new Competency();
    	c3.setName("CompositionTestSub2");
    	c3.setCreated(date);
        c3.setModified(date);
    	c3.setStatus(Thing.STATUS_PUBLISHED);
    	c3.setCommentStatus(Thing.COMMENTS_OPENED);
    	c3.setCommentDays(0);
    	c3.setCreator(userDao.get(-1L));

    	Competency c4 = new Competency();
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

    	c1.setComposes(cList);
    	c1 = competencyDao.save(c1);
    	flush();
    	
    	// Now, let's test "composes"
        c1 = competencyDao.get(c1.getId());
        assertNotNull(c1);
        List<Competency> result = c1.getComposes();
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
        	competencyDao.save(parent);
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
            c1 = competencyDao.get(c1.getId());
        } catch (DataAccessException dae) {
        	log.debug(dae.getMessage());
        	fail("Cascading type might include deletion");
        }
        
        // c1 should have only 2 children
        result = c1.getComposes();
        assertNotNull(result);
        assertEquals(2, result.size());

        log.debug("removing top competency ...");
        c1 = competencyDao.get(c1.getId());
        c1.setComposes(null);
        competencyDao.save(c1);
        flush();
        
        competencyDao.remove(c1.getId());
        flush();
        
        c2 = competencyDao.get(c2.getId());
        List <Competency> c2list = c2.getComposedBy();
        
        // c2 should have no composition parent any more
        assertEquals(0, c2list.size());
    }
        
    /**
     * Because manager tests happen outside Spring, dependency injection of other 
     * beans is not supported. It is not elegant, but I moved the test here. 
     * 
     */
    public void testFindNamesByParam1() throws Exception {
		log.debug("testing findNamesByParam1");
    	Competency competency = competencyDao.get(160L);
    	String  type = Name.TYPE_COMMON;
    	String  locale = "en";
    	boolean isDefault = false;
    	
    	List<Name> allNames = competency.getNames();
    	assertNotNull(allNames);
    	assertEquals(4, allNames.size());
    	
    	// should have no hits
    	List<Name> names = NameUtil.findNames(competency, type, locale, isDefault);
    	assertNotNull(names);
    	assertEquals(0, names.size());
    }
    
    /**
     * Because manager tests happen outside Spring, dependency injection of other 
     * beans is not supported. It is not elegant, but I moved the test here. 
     * 
     */
    public void testFindNamesByParam2() throws Exception {
		log.debug("testing findNamesByParam2");
		Competency competency = competencyDao.get(160L);
    	String type = Name.TYPE_COMMON;
    	String locale = "fr";
    	boolean isDefault = false;
    	
    	// should have one hit
    	List<Name> names  = NameUtil.findNames(competency, type, locale, isDefault);
    	assertNotNull(names);
    	assertTrue(names.size() > 0);
    	assertEquals(1, names.size());
    }
   		
        /**
         * Because manager tests happen outside Spring, dependency injection of other 
         * beans is not supported. It is not elegant, but I moved the test here. 
         * 
         */
    public void testFindNamesByParam3() throws Exception {
		log.debug("testing findNamesByParam3");
   		Competency competency = competencyDao.get(160L);
    	String type = Name.TYPE_RARE;
    	String locale = "fr";
    	boolean isDefault = false;
    	
    	// should have one hit
    	List<Name> names = NameUtil.findNames(competency, type, locale, isDefault);
    	assertNotNull(names);
    	assertTrue(names.size() > 0);
    	assertEquals(1, names.size());
    }
    
    /**
     * Because manager tests happen outside Spring, dependency injection of other 
     * beans is not supported. It is not elegant, but I moved the test here. 
     * 
     */
    public void testFindNamesByParam4() throws Exception {
		log.debug("testing findNamesByParam4");
		Competency competency = competencyDao.get(160L);
    	String type = null; 
    	String locale = "fr";
    	boolean isDefault = false;
    	
    	// should have two hit
    	List<Name> names = NameUtil.findNames(competency, type, locale, isDefault);
    	assertNotNull(names);
    	assertTrue(names.size() > 0);
    	assertEquals(2, names.size());
    }
    
    public void testFindByLastCreated() throws Exception {
    	log.debug("testing findByLastCreated");
    	
    	int number = 5;
    	
    	List<Competency> competencies = competencyDao.findByLastCreated(number);
    	assertNotNull(competencies);
    	assertTrue(competencies.size() >1);
    	assertTrue(competencies.size() <= number);
    
    	assertTrue(164L == competencies.get(0).getId());
    }

    public void testFindByLastModified() throws Exception {
    	log.debug("testing findByLastModified");
    	
    	int number = 5;
    	
    	List<Competency> competencies = competencyDao.findByLastModified(number);
    	assertNotNull(competencies);
    	assertTrue(competencies.size() >1);
    	assertTrue(competencies.size() <= number);
    
    	assertTrue(163L == competencies.get(0).getId());
    }
    
}
