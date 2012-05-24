/**
 * 
 */
package net.i2geo.comped.dao;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.appfuse.dao.BaseDaoTestCase;
import org.appfuse.dao.UserDao;
import org.appfuse.model.User;

import net.i2geo.comped.model.Competency;
import net.i2geo.comped.model.ConcreteCompetency;
import net.i2geo.comped.model.Name;
import net.i2geo.comped.model.Thing;
import net.i2geo.comped.model.Topic;
import net.i2geo.comped.model.AbstractTopic;
import net.i2geo.comped.model.ConcreteTopic;
import net.i2geo.comped.util.NameUtil;

import org.springframework.dao.DataAccessException;

/**
 * @author Martin Homik
 *
 */
public class ConcreteCompetencyDaoTest extends BaseDaoTestCase {

	private ConcreteCompetencyDao concreteCompetencyDao = null;
	private UserDao               userDao               = null;
	private ConcreteTopicDao          tiDao                 = null;
	private AbstractTopicDao         tgDao                 = null;

    public void setConcreteCompetencyDao(ConcreteCompetencyDao concreteCompetencyDao) {
        this.concreteCompetencyDao = concreteCompetencyDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setConcreteTopicDao(ConcreteTopicDao tiDao) {
		this.tiDao = tiDao;
	}

    public void setAbstractTopicDao(AbstractTopicDao tgDao) {
		this.tgDao = tgDao;
	}

    public void testFindAll() throws Exception {
    	List<ConcreteCompetency> competencies = concreteCompetencyDao.getAll();
    	assertNotNull(competencies);
    	assertTrue(competencies.size() >= 1);
    	assertEquals(6, competencies.size());
    	log.debug("Number of competencies: " + competencies.size());
    }
    
    // Find an occurrence 
    public void testFindCompetencyByUri() throws Exception {
        ConcreteCompetency competency = concreteCompetencyDao.findByUri("%dentify_similar_triangles%");
        assertNotNull(competency);
        assertEquals(new Long(165), competency.getId());
    }
    
    public void testFindComposes() throws Exception {
    	ConcreteCompetency c = concreteCompetencyDao.get(161L);
    	List<Competency> composes = c.getComposes();
    	assertNotNull(composes);
    	assertEquals(3, composes.size());
    }

    // composed by on concrete competency and one competency process
    public void testFindComposedBy() throws Exception {
    	ConcreteCompetency c = concreteCompetencyDao.get(162L);
    	List<Competency> composedBy = c.getComposedBy();
    	assertNotNull(composedBy);
    	assertEquals(2, composedBy.size());
    }

    public void testFindSubsumes() throws Exception {
    	ConcreteCompetency c = concreteCompetencyDao.get(161L);
    	List<Competency> subsumes = c.getSubsumes();
    	assertNotNull(subsumes);
    	assertEquals(3, subsumes.size());
    }

    // subsumed by on concrete competency and one competency process
    public void testFindSubsumedBy() throws Exception {
    	ConcreteCompetency c = concreteCompetencyDao.get(162L);
    	List<Competency> subsumedBy = c.getSubsumedBy();
    	assertNotNull(subsumedBy);
    	assertEquals(2, subsumedBy.size());
    }

    public void testFindSimilarTo() throws Exception {
    	ConcreteCompetency c = concreteCompetencyDao.get(161L);
    	Set<Competency> similarTo = c.getSimilarTo();
    	assertNotNull(similarTo);
    	assertEquals(3, similarTo.size());
    }

    public void testFindInverseSimilarTo() throws Exception {
    	ConcreteCompetency c = concreteCompetencyDao.get(162L);
    	Set<Competency> inverseSimilarTo = c.getInverseSimilarTo();
    	assertNotNull(inverseSimilarTo);
    	assertEquals(1, inverseSimilarTo.size());
    }
    
    // Sometimes, a similarity is indicated only in one direction.
    public void testFindAllSimilarities() throws Exception {
    	ConcreteCompetency c = concreteCompetencyDao.get(161L);
    	Set<Competency> similarTo = concreteCompetencyDao.findAllSimilarities(c);
    	assertNotNull(similarTo);
    	assertEquals(4, similarTo.size());
    }

    // TODO
    public void testFindTopics() throws Exception {
    	
    }

    // TODO
    public void testGetNames() throws Exception {
    	ConcreteCompetency c = concreteCompetencyDao.get(164L);
    	List<Name> names = concreteCompetencyDao.getNames(c); 
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
    	String token = "Groesse";
    	
    	List<ConcreteCompetency> list = concreteCompetencyDao.findByNameExact(token, language);

    	assertNotNull(list);
    	assertTrue(list.size() > 0);
    	assertEquals(1, list.size());
    }

    public void testFindByNameLike() throws Exception {
    	String language = "de";
    	String token = "%oly%";
    	
    	List<ConcreteCompetency> list = concreteCompetencyDao.findByNameLike(token, language);

    	assertNotNull(list);
    	assertTrue(list.size() > 0);
    	assertEquals(1, list.size());
    }
    
    public void testAddAndRemoveCompetency() throws Exception {
        ConcreteCompetency competency = new ConcreteCompetency();

        String testDescription = "Healthy description";
        
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
        
        competency = concreteCompetencyDao.save(competency);
        flush();

        competency = concreteCompetencyDao.get(competency.getId());

        assertEquals(testDescription, competency.getDescription());
        assertNotNull(competency.getId());

        log.debug("removing competency ...");

        concreteCompetencyDao.remove(competency.getId());
        flush();

        try {
            concreteCompetencyDao.get(competency.getId());
            fail("Competency found in database");
        } catch (DataAccessException dae) {
            log.debug("Expected exception: " + dae.getMessage());
            assertNotNull(dae);
        }
    }
    
    public void testAddAndRemoveComposition() throws Exception {
    	log.debug("Testing composition ...");
    	
        Date date = new Date();
 
    	ConcreteCompetency c1 = new ConcreteCompetency();
    	c1.setName("CompositionTestSuper");
    	c1.setCreated(date);
        c1.setModified(date);
    	c1.setStatus(Thing.STATUS_PUBLISHED);
    	c1.setCommentStatus(Thing.COMMENTS_OPENED);
    	c1.setCommentDays(0);
    	c1.setCreator(userDao.get(-1L));

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

    	c1.setComposes(cList);
    	c1 = concreteCompetencyDao.save(c1);
    	flush();
    	
    	// Now, let's test "composes"
        c1 = concreteCompetencyDao.get(c1.getId());
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
        c3 = concreteCompetencyDao.get(c3.getId());
        
        List <Competency> c3ComposedBy= c3.getComposedBy();
        for (Iterator<Competency> iter = c3ComposedBy.iterator(); iter.hasNext();) {
        	Competency parent = iter.next();
            // remove c3 from parent composition competency
        	parent.removeComposes(c3);
        	// TODO: ugly cast!!!!
        	concreteCompetencyDao.save((ConcreteCompetency) parent);
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
            c1 = concreteCompetencyDao.get(c1.getId());
        } catch (DataAccessException dae) {
        	log.debug(dae.getMessage());
        	fail("Cascading type might include deletion");
        }
        
        // c1 should have only 2 children
        result = c1.getComposes();
        assertNotNull(result);
        assertEquals(2, result.size());

        log.debug("removing top competency ...");
        c1 = concreteCompetencyDao.get(c1.getId());
        c1.setComposes(null);
        concreteCompetencyDao.save(c1);
        flush();
        
        concreteCompetencyDao.remove(c1.getId());
        flush();
        
        c2 = concreteCompetencyDao.get(c2.getId());
        List <Competency> c2list = c2.getComposedBy();
        
        // c2 should have no composition parent any more
        assertEquals(0, c2list.size());
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
    
    /**
     * Because manager tests happen outside Spring, dependency injection of other 
     * beans is not supported. It is not elegant, but I moved the test here. 
     * 
     */
    public void testFindNamesByParam1() throws Exception {
		log.debug("testing findNamesByParam1");
    	Competency competency = concreteCompetencyDao.get(160L);
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
		Competency competency = concreteCompetencyDao.get(160L);
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
   		Competency competency = concreteCompetencyDao.get(160L);
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
		Competency competency = concreteCompetencyDao.get(160L);
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
    	
    	List<ConcreteCompetency> competencies = concreteCompetencyDao.findByLastCreated(number);
    	assertNotNull(competencies);
    	assertTrue(competencies.size() >1);
    	assertTrue(competencies.size() <= number);
    
    	assertTrue(164L == competencies.get(0).getId());
    }

    public void testFindByLastModified() throws Exception {
    	log.debug("testing findByLastModified");
    	
    	int number = 5;
    	
    	List<ConcreteCompetency> competencies = concreteCompetencyDao.findByLastModified(number);
    	assertNotNull(competencies);
    	assertTrue(competencies.size() >1);
    	assertTrue(competencies.size() <= number);
    
    	assertTrue(163L == competencies.get(0).getId());
    }
    
    public void testAddAndRemoveConcreteCompetency() throws Exception {
        ConcreteCompetency concreteCompetency = new ConcreteCompetency();

        String testDescription = "Invincible description.";
        
        // set competency properties
        concreteCompetency.setName("Healthy ConcreteCompetency");
        concreteCompetency.setDescription(testDescription);

        // set Thing properties
        Date date = new Date();
        concreteCompetency.setCreated(date);
        concreteCompetency.setModified(date);
    	concreteCompetency.setStatus(Thing.STATUS_PUBLISHED);
    	concreteCompetency.setCommentStatus(Thing.COMMENTS_OPENED);
    	concreteCompetency.setCommentDays(0);
    	concreteCompetency.setCreator(userDao.get(-1L));
        log.info(concreteCompetency);
        
        concreteCompetency = concreteCompetencyDao.save(concreteCompetency);
        flush();

        concreteCompetency = concreteCompetencyDao.get(concreteCompetency.getId());

        assertEquals(testDescription, concreteCompetency.getDescription());
        assertNotNull(concreteCompetency.getId());

        log.debug("removing competency ...");

        concreteCompetencyDao.remove(concreteCompetency.getId());
        flush();

        try {
            concreteCompetencyDao.get(concreteCompetency.getId());
            fail("ConcreteCompetency found in database");
        } catch (DataAccessException dae) {
            log.debug("Expected exception: " + dae.getMessage());
            assertNotNull(dae);
        }
    }
    
    public void testFindCompetencyProcesses() throws Exception {
    	log.debug("testing findCompetencyProcesses");
    	
    	ConcreteCompetency cc = concreteCompetencyDao.get(162L);
    	assertNotNull(cc);
    	assertNotNull(cc.getProcesses());
    	assertTrue(cc.getProcesses().size() >= 1);
    	assertEquals(2, cc.getProcesses().size());

    	ConcreteCompetency cc2 = concreteCompetencyDao.get(163L);
    	assertNotNull(cc2);
    	assertNotNull(cc2.getProcesses());
    	assertTrue(cc2.getProcesses().size() >= 1);
    	assertEquals(1, cc2.getProcesses().size());
    }
    
    public void testTopic() throws Exception {
    	log.debug("testing addition of topics");
    	
    	User user = userDao.get(-1L);
    	
    	ConcreteTopic ti = new ConcreteTopic();
    	ti.setCreated(new Date());
    	ti.setModified(new Date());
    	ti.setCreator(user);
    	ti.setUri("http://www.activemath.org/ontology.owl#testme1");
    	ti.setCommentStatus(Thing.COMMENTS_OPENED);
    	ti.setCommentDays(0);
    	ti.setStatus(Thing.STATUS_PUBLISHED);
    	
    	AbstractTopic tg = new AbstractTopic();
    	tg.setCreated(new Date());
    	tg.setModified(new Date());
    	tg.setCreator(user);
    	tg.setUri("http://www.activemath.org/ontology.owl#testme2");
    	tg.setCommentStatus(Thing.COMMENTS_OPENED);
    	tg.setCommentDays(0);
    	tg.setStatus(Thing.STATUS_PUBLISHED);

    	ti = tiDao.save(ti);
    	flush();
    	
    	tg = tgDao.save(tg);
    	
    	ConcreteCompetency cc2 = concreteCompetencyDao.get(163L);
    	
    	Set<Topic> topics = new HashSet<Topic>();
    	topics.add(ti);
    	topics.add(tg);
    	
    	cc2.setTopics(topics);
    	
    	concreteCompetencyDao.save(cc2);
        flush();
    }
}
