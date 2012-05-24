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
import net.i2geo.comped.model.CompetencyProcess;
import net.i2geo.comped.model.Name;
import net.i2geo.comped.model.Thing;
import net.i2geo.comped.util.NameUtil;

import org.springframework.dao.DataAccessException;

/**
 * @author Martin Homik
 *
 */
public class CompetencyProcessDaoTest extends BaseDaoTestCase {

	private CompetencyProcessDao competencyProcessDao = null;
	private UserDao               userDao               = null;

    public void setCompetencyProcessDao(CompetencyProcessDao competencyProcessDao) {
        this.competencyProcessDao = competencyProcessDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
    
    public void testFindAll() throws Exception {
    	List<CompetencyProcess> competencies = competencyProcessDao.getAll();
    	assertNotNull(competencies);
    	assertTrue(competencies.size() >= 1);
    	assertEquals(4, competencies.size());
    	log.debug("Number of competencies: " + competencies.size());
    }
    
    
    public void testAddAndRemoveCompetencyProcess() throws Exception {
        CompetencyProcess competencyProcess = new CompetencyProcess();

        String testDescription = "Invincible description";
        
        // set competency properties
        competencyProcess.setName("Healthy CompetencyProcess");
        competencyProcess.setDescription(testDescription);

        // set Thing properties
        Date date = new Date();
        competencyProcess.setCreated(date);
        competencyProcess.setModified(date);
    	competencyProcess.setStatus(Thing.STATUS_PUBLISHED);
    	competencyProcess.setCommentStatus(Thing.COMMENTS_OPENED);
    	competencyProcess.setCommentDays(0);
    	competencyProcess.setCreator(userDao.get(-1L));
        log.info(competencyProcess);
        
        competencyProcess = competencyProcessDao.save(competencyProcess);
        flush();

        competencyProcess = competencyProcessDao.get(competencyProcess.getId());

        assertEquals(testDescription, competencyProcess.getDescription());
        assertNotNull(competencyProcess.getId());

        log.debug("removing competency ...");

        competencyProcessDao.remove(competencyProcess.getId());
        flush();

        try {
            competencyProcessDao.get(competencyProcess.getId());
            fail("CompetencyProcess found in database");
        } catch (DataAccessException dae) {
            log.debug("Expected exception: " + dae.getMessage());
            assertNotNull(dae);
        }
    }
        
    // Find an occurrence 
    public void testFindCompetencyByUri() throws Exception {
        CompetencyProcess competency = competencyProcessDao.findByUri("%model_similar_triangles%");
        assertNotNull(competency);
        assertEquals(new Long(182), competency.getId());
    }
    
    public void testFindComposes() throws Exception {
    	CompetencyProcess c = competencyProcessDao.get(183L);
    	List<Competency> composes = c.getComposes();
    	assertNotNull(composes);
    	assertEquals(3, composes.size());
    }

    public void testFindComposedBy() throws Exception {
    	CompetencyProcess c = competencyProcessDao.get(180L);
    	List<Competency> composedBy = c.getComposedBy();
    	assertNotNull(composedBy);
    	assertEquals(0, composedBy.size());
    }

    public void testFindSubsumes() throws Exception {
    	CompetencyProcess c = competencyProcessDao.get(181L);
    	List<Competency> subsumes = c.getSubsumes();
    	assertNotNull(subsumes);
    	assertEquals(3, subsumes.size());
    }

    public void testFindSubsumedBy() throws Exception {
    	CompetencyProcess c = competencyProcessDao.get(180L);
    	List<Competency> subsumedBy = c.getSubsumedBy();
    	assertNotNull(subsumedBy);
    	assertEquals(0, subsumedBy.size());
    }

    public void testFindSimilarTo() throws Exception {
    	CompetencyProcess c = competencyProcessDao.get(182L);
    	Set<Competency> similarTo = c.getSimilarTo();
    	assertNotNull(similarTo);
    	assertEquals(1, similarTo.size());
    }

    public void testFindInverseSimilarTo() throws Exception {
    	CompetencyProcess c = competencyProcessDao.get(183L);
    	Set<Competency> inverseSimilarTo = c.getInverseSimilarTo();
    	assertNotNull(inverseSimilarTo);
    	assertEquals(1, inverseSimilarTo.size());
    }
    
    // Sometimes, a similarity is indicated only in one direction.
    public void testFindAllSimilarities() throws Exception {
    	CompetencyProcess c = competencyProcessDao.get(182L);
    	Set<Competency> similarTo = competencyProcessDao.findAllSimilarities(c);
    	assertNotNull(similarTo);
    	assertEquals(1, similarTo.size());
    }

    // TODO
    public void testFindTopics() throws Exception {
    	
    }

    // TODO
    public void testGetNames() throws Exception {
    	CompetencyProcess c = competencyProcessDao.get(180L);
    	List<Name> names = competencyProcessDao.getNames(c); 
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

    public void testFindByNameExact() throws Exception {
    	String language = "de";
    	String token = "Der 180 Kompetenzprozess";
    	
    	List<CompetencyProcess> list = competencyProcessDao.findByNameExact(token, language);

    	assertNotNull(list);
    	assertTrue(list.size() > 0);
    	assertEquals(1, list.size());
    }

    public void testFindByNameLike() throws Exception {
    	String language = "de";
    	String token = "%180%";
    	
    	List<CompetencyProcess> list = competencyProcessDao.findByNameLike(token, language);

    	assertNotNull(list);
    	assertTrue(list.size() > 0);
    	assertEquals(1, list.size());
    }
    
    public void testAddAndRemoveCompetency() throws Exception {
        CompetencyProcess competency = new CompetencyProcess();

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
        
        competency = competencyProcessDao.save(competency);
        flush();

        competency = competencyProcessDao.get(competency.getId());

        assertEquals(testDescription, competency.getDescription());
        assertNotNull(competency.getId());

        log.debug("removing competency ...");

        competencyProcessDao.remove(competency.getId());
        flush();

        try {
            competencyProcessDao.get(competency.getId());
            fail("Competency found in database");
        } catch (DataAccessException dae) {
            log.debug("Expected exception: " + dae.getMessage());
            assertNotNull(dae);
        }
    }
    
    public void testAddAndRemoveComposition() throws Exception {
    	log.debug("Testing composition ...");
    	
        Date date = new Date();
 
    	CompetencyProcess c1 = new CompetencyProcess();
    	c1.setName("CompositionTestSuper");
    	c1.setCreated(date);
        c1.setModified(date);
    	c1.setStatus(Thing.STATUS_PUBLISHED);
    	c1.setCommentStatus(Thing.COMMENTS_OPENED);
    	c1.setCommentDays(0);
    	c1.setCreator(userDao.get(-1L));

        CompetencyProcess c2 = new CompetencyProcess();
    	c2.setName("CompositionTestSub1");
    	c2.setCreated(date);
        c2.setModified(date);
    	c2.setStatus(Thing.STATUS_PUBLISHED);
    	c2.setCommentStatus(Thing.COMMENTS_OPENED);
    	c2.setCommentDays(0);
    	c2.setCreator(userDao.get(-1L));
    	
        CompetencyProcess c3 = new CompetencyProcess();
    	c3.setName("CompositionTestSub2");
    	c3.setCreated(date);
        c3.setModified(date);
    	c3.setStatus(Thing.STATUS_PUBLISHED);
    	c3.setCommentStatus(Thing.COMMENTS_OPENED);
    	c3.setCommentDays(0);
    	c3.setCreator(userDao.get(-1L));

    	CompetencyProcess c4 = new CompetencyProcess();
    	c4.setName("CompositionTestSub3");
    	c4.setCreated(date);
        c4.setModified(date);
    	c4.setStatus(Thing.STATUS_PUBLISHED);
    	c4.setCommentStatus(Thing.COMMENTS_OPENED);
    	c4.setCommentDays(0);
    	c4.setCreator(userDao.get(-1L));

    	c2 = competencyProcessDao.save(c2);
    	c3 = competencyProcessDao.save(c3);
    	c4 = competencyProcessDao.save(c4);

    	List<Competency> cList = new ArrayList<Competency>();
    	cList.add(c2);
    	cList.add(c3);
    	cList.add(c4);

    	c1.setComposes(cList);
    	c1 = competencyProcessDao.save(c1);
    	flush();
    	
    	// Now, let's test "composes"
        c1 = competencyProcessDao.get(c1.getId());
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
        c3 = competencyProcessDao.get(c3.getId());
        
        List <Competency> c3ComposedBy= c3.getComposedBy();
        for (Iterator<Competency> iter = c3ComposedBy.iterator(); iter.hasNext();) {
        	Competency parent = iter.next();
            // remove c3 from parent composition competency
        	parent.removeComposes(c3);
        	// TODO: ugly cast!!!!
        	competencyProcessDao.save((CompetencyProcess) parent);
        }
        
        competencyProcessDao.remove(c3.getId());
        flush();
        
        try {
        	// check if c3 still exists in database
        	competencyProcessDao.get(c3.getId());
            fail("Competency found in database");
        } catch (DataAccessException dae) {
            log.debug("Expected exception: " + dae.getMessage());
            assertNotNull(dae);
        }
        
        
        try {
        	// update c1
            c1 = competencyProcessDao.get(c1.getId());
        } catch (DataAccessException dae) {
        	log.debug(dae.getMessage());
        	fail("Cascading type might include deletion");
        }
        
        // c1 should have only 2 children
        result = c1.getComposes();
        assertNotNull(result);
        assertEquals(2, result.size());

        log.debug("removing top competency ...");
        c1 = competencyProcessDao.get(c1.getId());
        c1.setComposes(null);
        competencyProcessDao.save(c1);
        flush();
        
        competencyProcessDao.remove(c1.getId());
        flush();
        
        c2 = competencyProcessDao.get(c2.getId());
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
    	Competency competency = competencyProcessDao.get(180L);
    	String  type = Name.TYPE_UNCOMMON;
    	String  locale = "en";
    	boolean isDefault = true;
    	
    	List<Name> allNames = competency.getNames();
    	assertNotNull(allNames);
    	assertEquals(2, allNames.size());
    	
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
		Competency competency = competencyProcessDao.get(180L);
    	String type = Name.TYPE_COMMON;
    	String locale = "en";
    	boolean isDefault = true;
    	
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
   		Competency competency = competencyProcessDao.get(180L);
    	String type = null;
    	String locale = "de";
    	boolean isDefault = true;
    	
    	// should have one hit
    	List<Name> names = NameUtil.findNames(competency, type, locale, isDefault);
    	assertNotNull(names);
    	assertEquals(1, names.size());
    }
    
    /**
     * Because manager tests happen outside Spring, dependency injection of other 
     * beans is not supported. It is not elegant, but I moved the test here. 
     * 
     */
    public void testFindNamesByParam4() throws Exception {
		log.debug("testing findNamesByParam4");
		Competency competency = competencyProcessDao.get(180L);
    	String type = Name.TYPE_COMMON; 
    	String locale = null;
    	boolean isDefault = true;
    	
    	// should have two hit
    	List<Name> names = NameUtil.findNames(competency, type, locale, isDefault);
    	assertNotNull(names);
    	assertTrue(names.size() > 0);
    	assertEquals(2, names.size());
    }
    
    public void testFindByLastCreated() throws Exception {
    	log.debug("testing findByLastCreated");
    	
    	int number = 5;
    	
    	List<CompetencyProcess> competencies = competencyProcessDao.findByLastCreated(number);
    	assertNotNull(competencies);
    	assertTrue(competencies.size() >1);
    	assertTrue(competencies.size() <= number);
    
    	assertTrue(181L == competencies.get(0).getId());
    }

    public void testFindByLastModified() throws Exception {
    	log.debug("testing findByLastModified");
    	
    	int number = 5;
    	
    	List<CompetencyProcess> competencies = competencyProcessDao.findByLastModified(number);
    	assertNotNull(competencies);
    	assertTrue(competencies.size() >1);
    	assertTrue(competencies.size() <= number);
    
    	assertTrue(183L == competencies.get(0).getId());
    }
    
    public void testFindConcreteCOmpetencies() throws Exception {
    	log.debug("testing findConcreteCompetencies");
    	
    	CompetencyProcess cp = competencyProcessDao.get(180L);
    	assertNotNull(cp);
    	assertNotNull(cp.getCcompetencies());
    	assertTrue(cp.getCcompetencies().size() >= 1);
    	assertEquals(2, cp.getCcompetencies().size());

    	CompetencyProcess cp2 = competencyProcessDao.get(181L);
    	assertNotNull(cp2);
    	assertNotNull(cp2.getCcompetencies());
    	assertTrue(cp2.getCcompetencies().size() >= 1);
    	assertEquals(1, cp2.getCcompetencies().size());
    }
    
    public void testTaxonomy() throws Exception {
    	log.debug("testing taxonomy");
    	
    	CompetencyProcess cp = competencyProcessDao.get(180L);
    	assertNotNull(cp);
    	assertNotNull(cp.getChildren());
    	assertTrue(cp.getChildren().size() >= 1);
    	assertEquals(2, cp.getChildren().size());

    	CompetencyProcess cp2 = competencyProcessDao.get(182L);
    	assertNotNull(cp2);
    	assertNotNull(cp2.getChildren());
    	assertTrue(cp2.getChildren().size() >= 1);
    	assertEquals(2, cp.getChildren().size());

    	assertNotNull(cp2.getParent());
    	assertTrue(cp2.getParent().size() >= 1);
    	assertEquals(1, cp2.getParent().size());
    }
}
