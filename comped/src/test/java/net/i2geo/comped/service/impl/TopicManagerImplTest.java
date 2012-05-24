/**
 * 
 */
package net.i2geo.comped.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.appfuse.service.impl.BaseManagerMockTestCase;
import org.jmock.Expectations;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import net.i2geo.comped.dao.TopicDao;
import net.i2geo.comped.model.Topic;

/**
 * @author Martin Homik
 *
 */
public class TopicManagerImplTest extends BaseManagerMockTestCase {


    private TopicDao dao = null;
	private TopicManagerImpl manager = null;
	
	
	@Before
    public void setUp() throws Exception {
        dao = context.mock(TopicDao.class);
        manager = new TopicManagerImpl(dao);
    }
	
	@After
    public void tearDown() throws Exception {
        manager = null;
    }

	@Test
    public void testGetTopic() {
        log.debug("testing getTopic");

        final Long id = 777777L;
        final Topic topic = new Topic();

        // set expected behavior on dao
        context.checking(new Expectations() {{
            one(dao).get(with(equal(id)));
            will(returnValue(topic));
        }});

        Topic result = manager.get(id);
        assertSame(topic, result);
    }
    
	@Test
    public void testGetCompetencies() {
        log.debug("testing getTopics");

        final List<Topic> topics = new ArrayList<Topic>();

        // set expected behavior on dao
        context.checking(new Expectations() {{
            one(dao).getAll();
            will(returnValue(topics));
        }});
        
        List<Topic> result = manager.getAll();
        assertSame(topics, result);
    }
	
	
	@Test
    public void testFindByUri() {
        log.debug("testing findByUri");

        final Topic topic = new Topic();
        final String uri = "%Graphical_display_of_data_r%";
                
        // set expected behavior on dao
        context.checking(new Expectations() {{
        	one(dao).findByUri(with(equal(uri)));
        	will(returnValue(topic));
        }});

        Topic result = manager.findByUri(uri);
        assertNotNull(topic);
        assertSame(topic, result);
    }
	
	
	@Test
    public void testSaveTopic() {
        log.debug("testing saveTopic");

        final Topic topic = new Topic();

        // set expected behavior on dao
        context.checking(new Expectations() {{
            one(dao).save(with(same(topic)));
        }});
        
        manager.prepare(topic);
        manager.save(topic);
    }

	@Test
    public void testRemoveTopic() {
        log.debug("testing removeTopic");

        final Long id = 777777L;

        // set expected behavior on dao
        context.checking(new Expectations(){{
        	one(dao).remove(with(equal(id)));
        }});

        manager.remove(id);
    }    

	@Test
	public void testDetach() {
		// TODO
	}

	@Test
    public void testFindByLastCreated() {
        log.debug("testing findByLastCreated");

        final int number = 5;
        final List<Topic> topics = new ArrayList<Topic>();
                        
        // set expected behavior on dao
        context.checking(new Expectations() {{
        	one(dao).findByLastCreated(with(equal(number)));
        	will(returnValue(topics));
        }});

        List<Topic> result = manager.findByLastCreated(number);
        assertNotNull(topics);
        assertSame(topics, result);
    }

	@Test
    public void testFindByLastModified() {
        log.debug("testing findByLastModified");

        final int number = 5;
        final List<Topic> topics = new ArrayList<Topic>();
                        
        // set expected behavior on dao
        context.checking(new Expectations() {{
        	one(dao).findByLastModified(with(equal(number)));
        	will(returnValue(topics));
        }});

        List<Topic> result = manager.findByLastModified(number);
        assertNotNull(topics);
        assertSame(topics, result);
    }
}
