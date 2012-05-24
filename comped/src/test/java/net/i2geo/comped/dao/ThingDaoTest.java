/**
 * 
 */
package net.i2geo.comped.dao;

import java.util.List;

import org.appfuse.dao.BaseDaoTestCase;
import org.appfuse.dao.UserDao;
import org.appfuse.model.User;
import org.junit.After;
import org.junit.Before;

import net.i2geo.comped.model.AbstractTopic;
import net.i2geo.comped.model.Thing;
import net.i2geo.comped.dao.ThingDao;

/**
 * @author Martin Homik
 *
 */
public class ThingDaoTest extends BaseDaoTestCase {

	private ThingDao thingDao = null;
	private UserDao  userDao  = null;
	private User     user1    = null;
	private User     user2    = null;
	
    public void setThingDao(ThingDao thingDao) {
        this.thingDao = thingDao;
    }
    
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

	@Before
    public void onSetUp() throws Exception {
		user1 = userDao.get(-1L);
		user2 = userDao.get(-2L);
	}
	
	@After
    public void onTearDown() throws Exception {
		user1=null;
		user2=null;
	}
		
    public void testFindByCreator() throws Exception {
    	List<Thing> things1 = thingDao.findByCreator(user2);
    	assertNotNull(things1);
    	assertTrue(things1.size() > 0);
    	assertEquals(3, things1.size());

    	List<Thing> things2 = thingDao.findByCreator(user1);

    	assertNotNull(things2);
    	assertTrue(things2.size() > 0);
    	assertEquals(13, things2.size());
    }

    // Find an occurrence 
    public void testFindThingByUri() throws Exception {
        List<Thing> things = thingDao.findByUri("%Graphical_display_of_data_r%");
        assertNotNull(things);
        assertTrue(things.size() > 0);
        Thing thing = things.get(0);
        assertEquals(new Long(171), thing.getId());
        
        assertTrue(thing instanceof AbstractTopic);
    }

    /*  
    public void testAddAndRemoveThing() throws Exception {
        Thing thing = new Thing();

        Date date = new Date();
    	thing.setCreated(date);
    	thing.setModified(date);
    	thing.setStatus(Thing.STATUS_PUBLISHED);
    	thing.setCommentStatus(Thing.COMMENTS_OPENED);
    	thing.setCommentDays(0);
    	thing.setCreator(userDao.get(-1L));
    	
        thing = thingDao.save(thing);
        flush();

        thing = thingDao.get(thing.getId());

        assertNotNull(thing.getId());
        // assertNotNull(thing.getCreated());
        // assertNotNull(thing.getModified());

        log.debug("removing thing ...");

        thingDao.remove(thing.getId());
        flush();

        try {
            thingDao.get(thing.getId());
            fail("Thing found in database");
        } catch (DataAccessException dae) {
            log.debug("Expected exception: " + dae.getMessage());
            assertNotNull(dae);
        }
    }
    */
}
