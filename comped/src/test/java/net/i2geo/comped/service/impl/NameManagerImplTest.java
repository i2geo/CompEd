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

import net.i2geo.comped.dao.NameDao;
import net.i2geo.comped.model.Competency;
import net.i2geo.comped.model.ConcreteCompetency;
import net.i2geo.comped.model.Name;
/**
 * @author Martin Homik
 *
 */
public class NameManagerImplTest extends BaseManagerMockTestCase {

    private NameDao dao = null;
	private NameManagerImpl manager = null;
	
	@Before
    public void setUp() throws Exception {
        dao = context.mock(NameDao.class);
        manager = new NameManagerImpl(dao);
	}
	
	@After
    public void tearDown() throws Exception {
        manager = null;
    }

	@Test
    public void testGetName() {
        log.debug("testing getName");

        final Long id = 777777L;
        final Name name = new Name();

        // set expected behavior on dao
        context.checking(new Expectations() {{
            one(dao).get(with(equal(id)));
            will(returnValue(name));
        }});

        Name result = manager.get(id);
        assertSame(name, result);
    }
    
	@Test
    public void testGetCompetencies() {
        log.debug("testing getCompetencies");

        final List<Name> names = new ArrayList<Name>();

        // set expected behavior on dao
        context.checking(new Expectations() {{
            one(dao).getAll();
            will(returnValue(names));
        }});
        
        List<Name> result = manager.getAll();
        assertSame(names, result);
    }

	@Test
    public void testFindByNameExact() {
        log.debug("testing findByNameExact");

        final List<Name> names = new ArrayList<Name>();
        final String name = "Name 2";

        // set expected behavior on dao
        context.checking(new Expectations() {{
        	one(dao).findByNameExact(with(equal(name)));
        	will(returnValue(names));
        }});

        List<Name> result = manager.findByNameExact(name);
       	assertSame(names, result);
    }

	@Test
    public void testFindByNameLike() {
        log.debug("testing findByNameLike");

        final List<Name> names = new ArrayList<Name>();
        final String name = "Name 2";

        // set expected behavior on dao
        context.checking(new Expectations() {{
        	one(dao).findByNameLike(with(equal(name)));
        	will(returnValue(names));
        }});

        List<Name> result = manager.findByNameLike(name);
       	assertSame(names, result);
    }
	

	@Test
    public void testGetByLocale() {
        log.debug("testing getByLocale");

        final List<Name> names = new ArrayList<Name>();
        final String locale = "en";

        // set expected behavior on dao
        context.checking(new Expectations() {{
        	one(dao).getByLocale(with(equal(locale)));
        	will(returnValue(names));
        }});

        List<Name> result = manager.getByLocale(locale);
       	assertSame(names, result);
    }

	@Test
    public void testFindByNameAndLocaleExact() {
        log.debug("testing findByNameAndLocaleExact");

        final List<Name> names = new ArrayList<Name>();
        final String name   = "Name 2";
        final String locale = "en";

        // set expected behavior on dao
        context.checking(new Expectations() {{
        	one(dao).findByNameAndLocaleExact(
        			with(equal(name)),
        			with(equal(locale)));
        	will(returnValue(names));
        }});

        List<Name> result = manager.findByNameAndLocaleExact(name, locale);
       	assertSame(names, result);
    }

	@Test
    public void testFindByNameAndLocaleLike() {
        log.debug("testing findByNameAndLocaleLike");

        final List<Name> names = new ArrayList<Name>();
        final String name   = "Name 2";
        final String locale = "en";

        // set expected behavior on dao
        context.checking(new Expectations() {{
        	one(dao).findByNameAndLocaleLike(
        			with(equal(name)),
        			with(equal(locale)));
        	will(returnValue(names));
        }});

        List<Name> result = manager.findByNameAndLocaleLike(name, locale);
       	assertSame(names, result);
    }
	
	@Test
    public void testFindByType() {
        log.debug("testing findByType");

        final List<Name> names = new ArrayList<Name>();
        final String type  = "common";

        // set expected behavior on dao
        context.checking(new Expectations() {{
        	one(dao).findByType(
        			with(equal(type)));
        	will(returnValue(names));
        }});

        List<Name> result = manager.findByType(type);
       	assertSame(names, result);
    }

	@Test
    public void testFindByTypeAndLocale() {
        log.debug("testing findByTypeAndLocale");

        final List<Name> names = new ArrayList<Name>();
        final String type   = "common";
        final String locale = "en";

        // set expected behavior on dao
        context.checking(new Expectations() {{
        	one(dao).findByTypeAndLocale(
        			with(equal(type)),
        			with(equal(locale)));
        	will(returnValue(names));
        }});

        List<Name> result = manager.findByTypeAndLocale(type, locale);
       	assertSame(names, result);
    }

	@Test
    public void testFindByThing() {
        log.debug("testing findByThing");

        final List<Name> names = new ArrayList<Name>();
        final Competency thing   = new ConcreteCompetency();

        // set expected behavior on dao
        context.checking(new Expectations() {{
        	one(dao).findByThing(
        			with(equal(thing)));
        	will(returnValue(names));
        }});

        List<Name> result = manager.findByThing(thing);
       	assertSame(names, result);
    }

	@Test
    public void testFindByThingAndType() {
        log.debug("testing findByThingAndType");

        final List<Name> names   = new ArrayList<Name>();
        final Competency thing   = new ConcreteCompetency();
        final String     type    = "common";

        // set expected behavior on dao
        context.checking(new Expectations() {{
        	one(dao).findByThingAndType(
        			with(equal(thing)),
        			with(equal(type)));
        	will(returnValue(names));
        }});

        List<Name> result = manager.findByThingAndType(thing, type);
       	assertSame(names, result);
    }

	@Test
    public void testFindByThingAndTypeAndLocale() {
        log.debug("testing findByThingAndTypeAndLocale");

        final List<Name> names   = new ArrayList<Name>();
        final Competency thing   = new ConcreteCompetency();
        final String     type    = "common";
        final String     locale  = "en";

        // set expected behavior on dao
        context.checking(new Expectations() {{
        	one(dao).findByThingAndTypeAndLocale(
        			with(equal(thing)),
        			with(equal(type)),
        			with(equal(locale)));
        	will(returnValue(names));
        }});

        List<Name> result = manager.findByThingAndTypeAndLocale(thing, type, locale);
       	assertSame(names, result);
    }

	@Test
    public void testFindDefaultByThingAndTypeAndLocale() {
        log.debug("testing findDefaultByThingAndTypeAndLocale");

        final List<Name> names   = new ArrayList<Name>();
        final Competency thing   = new ConcreteCompetency();
        final String     type    = Name.TYPE_COMMON;
        final String     locale  = "en";

        // set expected behavior on dao
        context.checking(new Expectations() {{
        	one(dao).findDefaultByThingAndTypeAndLocale(
        			with(equal(thing)),
        			with(equal(type)),
        			with(equal(locale)));
        	will(returnValue(names));
        }});

        Name result = manager.findDefaultByThingAndTypeAndLocale(thing, type, locale);
       	assertNull(result);
    }
	
	@Test
    public void testSaveName() {
        log.debug("testing saveComptency");

        final Name name = new Name();

        // set expected behavior on dao
        context.checking(new Expectations() {{
            one(dao).save(with(same(name)));
        }});
        
        manager.save(name);
    }

	@Test
    public void testRemoveName() {
        log.debug("testing removeName");

        final Long id = 777777L;

        // set expected behavior on dao
        context.checking(new Expectations(){{
        	one(dao).remove(with(equal(id)));
        }});

        manager.remove(id);
    }
	
}
