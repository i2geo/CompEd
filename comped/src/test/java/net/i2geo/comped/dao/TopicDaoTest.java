package net.i2geo.comped.dao;

import java.util.Date;
import java.util.List;

import net.i2geo.comped.model.Thing;
import net.i2geo.comped.model.Topic;

import org.appfuse.dao.BaseDaoTestCase;
import org.appfuse.dao.UserDao;
import org.springframework.dao.DataAccessException;

public class TopicDaoTest extends BaseDaoTestCase {
	
	private TopicDao topicDao;
	private UserDao userDao;
	
	public void setTopicDao(TopicDao topicDao) {
		this.topicDao = topicDao;
	}
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
    // Find an occurrence 
    public void testFindCompetencyByUri() throws Exception {
        Topic topic = topicDao.findByUri("%Graphical_display_of_data_r%");
        assertNotNull(topic);
        assertEquals(new Long(171), topic.getId());
    }
        
    public void testAddAndRemoveTopic() throws Exception {
        Topic topic = new Topic();

        // set Thing properties
        Date date = new Date();
        topic.setCreated(date);
        topic.setModified(date);
    	topic.setStatus(Thing.STATUS_PUBLISHED);
    	topic.setCommentStatus(Thing.COMMENTS_OPENED);
    	topic.setCommentDays(0);
    	topic.setCreator(userDao.get(-1L));

    	topic.setName("Test topic");	
    	topic.setUri("http://www.somedomain.de/testUri");
    	
    	
        topic = topicDao.save(topic);
        flush();

        topic = topicDao.get(topic.getId());

        assertNotNull(topic.getId());

        log.debug("removing name ...");

        topicDao.remove(topic.getId());
        flush();

        try {
            topicDao.get(topic.getId());
            fail("Topic found in database");
        } catch (DataAccessException dae) {
            log.debug("Expected exception: " + dae.getMessage());
            assertNotNull(dae);
        }
    }
    
    /*
    public void testCreateHierarchy() throws Exception {
    	log.debug("Testing topic hierarchy ...");

    	Topic t1, t2, t3, t4;

        Date date = new Date();
        
    	t1 = new Topic();
    	t1.setName("TopicTestSuper");
        t1.setCreated(date);
        t1.setModified(date);
    	t1.setStatus(Thing.STATUS_PUBLISHED);
    	t1.setCommentStatus(Thing.COMMENTS_OPENED);
    	t1.setCommentDays(0);
    	t1.setCreator(userDao.get(-1L));
    	    	
        t2 = new Topic();
    	t2.setName("TopicTestSub1");
        t2.setCreated(date);
        t2.setModified(date);
    	t2.setStatus(Thing.STATUS_PUBLISHED);
    	t2.setCommentStatus(Thing.COMMENTS_OPENED);
    	t2.setCommentDays(0);
    	t2.setCreator(userDao.get(-1L));
    	
        t3 = new Topic();
    	t3.setName("TopicTestSub2");
        t3.setCreated(date);
        t3.setModified(date);
    	t3.setStatus(Thing.STATUS_PUBLISHED);
    	t3.setCommentStatus(Thing.COMMENTS_OPENED);
    	t3.setCommentDays(0);
    	t3.setCreator(userDao.get(-1L));

    	t4 = new Topic();
    	t4.setName("TopicTestSub3");
        t4.setCreated(date);
        t4.setModified(date);
    	t4.setStatus(Thing.STATUS_PUBLISHED);
    	t4.setCommentStatus(Thing.COMMENTS_OPENED);
    	t4.setCommentDays(0);
    	t4.setCreator(userDao.get(-1L));
    	
    	// save topics to session
    	t2 = topicDao.save(t2);
    	t3 = topicDao.save(t3);
    	t4 = topicDao.save(t4);

    	// save session to database
    	t1 = topicDao.save(t1);
    	flush();


    	// create new list to save children topics
    	List<Topic> tList = new ArrayList<Topic>();
    	tList.add(t2);
    	tList.add(t3);
    	tList.add(t4);

    	// get persisted t1 topic; not needed
    	t1 = topicDao.get(t1.getId());
    	// set children on POJO
    	// t1.setChildren(tList);
    	// save children to session
    	t1 = topicDao.save(t1);
    	// save session to database
    	flush();
    	
        // This is for testing only.
    	System.out.println("\n*************** getChildren on t1: " + t1.getChildren().size());
    	System.out.println("\n***************  get t1 first and get children then \n" + topicDao.get(t1.getId()).getChildren().size());
    	System.out.println("\n*************** find children of t1 through DAO: " + topicDao.findChildrenOf(t1).size());
    	
    	List<Topic> dummy = topicDao.getAll();
    	for (Iterator<Topic> dummies = dummy.iterator(); dummies.hasNext();) {
			Topic topic = (Topic) dummies.next();
			System.out.println("***** " + topic.getName());
		}
    	
    	assertNotNull(dummy);
    	assertTrue(dummy.size() > 0);
    	assertEquals(6, dummy.size());
		
    	
    	// Check children for their parents via POJO
        t2 = topicDao.get(t2.getId());
        assertNotNull(t2);
        List<Topic> t2parentByPojo = t2.getParents();
    	assertNotNull(t2parentByPojo);
    	assertTrue(t2parentByPojo.size() > 0);
    	assertEquals(1, t2parentByPojo.size());
    
    	
    	// Check children for their parents via DAO
        t2 = topicDao.get(t2.getId());
        assertNotNull(t2);
        List<Topic> t2parentByDao = topicDao.findParentsOf(t2);
    	assertNotNull(t2parentByDao);
    	assertTrue(t2parentByDao.size() > 0);
    	assertEquals(1, t2parentByDao.size());
	    
        // detach t3
        log.debug("removing/detaching sub topic 3 ...");
        t3 = topicDao.get(t3.getId());
        topicDao.detach(t3);
        flush();
        
        try {
        	// check if ct still exists in database
            topicDao.get(t3.getId());
            fail("Topic found in database");
        } catch (DataAccessException dae) {
            log.debug("Expected exception: " + dae.getMessage());
            assertNotNull(dae);
        }
    	
        // t1 should have only 2 children
        t1 = topicDao.get(t1.getId());
        List<Topic> t1Children = t1.getChildren();
        assertNotNull(t1Children);
        assertTrue(t1Children.size() > 0);
        assertEquals(2, t1Children.size());

        
        // Testing removal of top topic
        log.debug("removing top topic t1 ...");
        t1 = topicDao.get(t1.getId());
        t1.setChildren(null);
        topicDao.save(t1);
        flush();

        topicDao.remove(t1.getId());
        flush();
        
        t2 = topicDao.get(t2.getId());
        List <Topic> t2parents = t2.getParents();
        
        // t2 should have no parent any more
        assertEquals(0, t2parents.size());
    }
	*/
	
    
    /*
    public void testDetach() throws Exception {
    	log.debug("Testing removal of Topic 2!!!!");
    	Topic t = topicDao.get(171L);
    	topicDao.detach(t);
    	
    	try {
            t = topicDao.get(t.getId());
            fail("Topic found in database");
        } catch (DataAccessException dae) {
            log.debug("Expected exception: " + dae.getMessage());
            assertNotNull(dae);
        }
    		
    	Topic parent = topicDao.get(170L);
    	assertEquals(0, parent.getChildren().size());    	
    }
	*/
    
    public void testFindByLastCreated() throws Exception {
    	log.debug("testing findByLastCreated");
    	
    	int number = 5;
    	
    	List<Topic> topics = topicDao.findByLastCreated(number);
    	assertNotNull(topics);
    	assertTrue(topics.size() >1);
    	assertTrue(topics.size() <= number);
    
    	assertTrue(171L == topics.get(0).getId());
    }

    public void testFindByLastModified() throws Exception {
    	log.debug("testing findByLastModified");
    	
    	int number = 5;
    	
    	List<Topic> topics = topicDao.findByLastModified(number);
    	assertNotNull(topics);
    	assertTrue(topics.size() >1);
    	assertTrue(topics.size() <= number);
    
    	assertTrue(170L == topics.get(0).getId());
    }
    
}
