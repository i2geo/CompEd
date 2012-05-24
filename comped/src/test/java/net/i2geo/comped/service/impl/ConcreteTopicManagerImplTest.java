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

import net.i2geo.comped.dao.ConcreteTopicDao;
import net.i2geo.comped.model.Name;
import net.i2geo.comped.model.ConcreteTopic;

/**
 * @author Martin Homik
 *
 */
public class ConcreteTopicManagerImplTest extends BaseManagerMockTestCase {

    private ConcreteTopicDao dao = null;
	private ConcreteTopicManagerImpl manager = null;
	
	@Before
    public void setUp() throws Exception {
        dao = context.mock(ConcreteTopicDao.class);
        manager = new ConcreteTopicManagerImpl(dao);
        manager.setLocale(Locale.ENGLISH);
	}
	
	@After
    public void tearDown() throws Exception {
        manager = null;
    }

	@Test
    public void testGetTopic() {
        log.debug("testing getTopic");

        final Long id = 777777L;
        final ConcreteTopic topic = new ConcreteTopic();

        // set expected behavior on dao
        context.checking(new Expectations() {{
            one(dao).get(with(equal(id)));
            will(returnValue(topic));
        }});

        ConcreteTopic result = manager.get(id);
        assertSame(topic, result);
    }
    
	@Test
    public void testGetCompetencies() {
        log.debug("testing getTopics");

        final List<ConcreteTopic> topics = new ArrayList<ConcreteTopic>();

        // set expected behavior on dao
        context.checking(new Expectations() {{
            one(dao).getAll();
            will(returnValue(topics));
        }});
        
        List<ConcreteTopic> result = manager.getAll();
        assertSame(topics, result);
    }
	
	
	@Test
    public void testFindByUri() {
        log.debug("testing findByUri");

        final ConcreteTopic topic = new ConcreteTopic();
        final String uri = "%Graphical_display_of_data_r%";
                
        // set expected behavior on dao
        context.checking(new Expectations() {{
        	one(dao).findByUri(with(equal(uri)));
        	will(returnValue(topic));
        }});

        ConcreteTopic result = manager.findByUri(uri);
        assertNotNull(topic);
        assertSame(topic, result);
    }
		
	@Test
    public void testSaveTopic() {
        log.debug("testing saveTopic");

        final ConcreteTopic topic = new ConcreteTopic();

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
        final List<ConcreteTopic> topics = new ArrayList<ConcreteTopic>();
                        
        // set expected behavior on dao
        context.checking(new Expectations() {{
        	one(dao).findByLastCreated(with(equal(number)));
        	will(returnValue(topics));
        }});

        List<ConcreteTopic> result = manager.findByLastCreated(number);
        assertNotNull(topics);
        assertSame(topics, result);
    }

	@Test
    public void testFindByLastModified() {
        log.debug("testing findByLastModified");

        final int number = 5;
        final List<ConcreteTopic> topics = new ArrayList<ConcreteTopic>();
                        
        // set expected behavior on dao
        context.checking(new Expectations() {{
        	one(dao).findByLastModified(with(equal(number)));
        	will(returnValue(topics));
        }});

        List<ConcreteTopic> result = manager.findByLastModified(number);
        assertNotNull(topics);
        assertSame(topics, result);
    }

	@Test
	public void testFindByNameExact() {
		log.debug("testing findByNameExact");

		final String token = "Name 1";
		final String language = "en";
		
		final ConcreteTopic c = new ConcreteTopic();
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
		
		final List<ConcreteTopic> cList = new ArrayList<ConcreteTopic>();
		cList.add(c);
		
        // set expected behavior on dao
        context.checking(new Expectations() {{
        	one(dao).findByNameExact(
        			with(equal(token)),
        			with(equal(language)));
        	will(returnValue(cList));
        }});

        List<ConcreteTopic> result = manager.findByNameExact(token, language);
        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertEquals(1, result.size());
        assertEquals(token, result.get(0).getName());
	}
}
