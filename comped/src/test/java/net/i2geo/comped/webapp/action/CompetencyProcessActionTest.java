/**
 * 
 */
package net.i2geo.comped.webapp.action;

import java.util.Date;
import java.util.Locale;

import net.i2geo.comped.model.CompetencyProcess;
import net.i2geo.comped.model.Thing;
import net.i2geo.comped.service.CompetencyProcessManager;
import net.i2geo.comped.service.ConcreteCompetencyManager;
import net.i2geo.comped.service.NameManager;
import net.i2geo.comped.service.ThingManager;

import org.springframework.mock.web.MockHttpServletRequest;

import com.opensymphony.xwork2.ActionSupport;

import org.apache.struts2.ServletActionContext;
import org.appfuse.service.UserManager;
import org.appfuse.webapp.action.BaseActionTestCase;

/**
 * @author Martin Homik
 *
 */
public class CompetencyProcessActionTest extends BaseActionTestCase {
    
	private CompetencyProcessAction action;
	private Long id = null;
	
    @Override
    protected void onSetUpBeforeTransaction() throws Exception {
        super.onSetUpBeforeTransaction();
    	log.debug("Adding provisory object ...");
        action = new CompetencyProcessAction();
        CompetencyProcessManager cpm = 
        	(CompetencyProcessManager) applicationContext.getBean("competencyProcessManager");
        cpm.setLocale(Locale.ENGLISH);
        action.setCompetencyProcessManager(cpm);

        ThingManager thingManager = 
        	(ThingManager) applicationContext.getBean("thingManager");
        action.setThingManager(thingManager);

        NameManager nameManager = 
        	(NameManager) applicationContext.getBean("nameManager");
        action.setNameManager(nameManager);

        ConcreteCompetencyManager ccm = 
        	(ConcreteCompetencyManager) applicationContext.getBean("concreteCompetencyManager");
        ccm.setLocale(Locale.ENGLISH);
        action.setConcreteCompetencyManager(ccm);

        UserManager userManager = (UserManager) applicationContext.getBean("userManager");

        // add a test competency process to the database
        // note: error will occur if one of the fields is unique
        //       for each transaction a new competency is created and saved.
        CompetencyProcess competency = new CompetencyProcess();
        competency.setName("Labertasche");
        competency.setUri("http://www.inter2geo.eu/2008/ontology/GeoSkills#Labertasche");
        
        // set Thing properties
        Date date = new Date();
        competency.setCreated(date);
        competency.setModified(date);
        competency.setStatus(Thing.STATUS_PUBLISHED);
        competency.setCommentStatus(Thing.COMMENTS_CLOSED);
        competency.setCommentDays(0);
        competency.setCreator(userManager.getUser("-1"));
        competency = cpm.save(competency);
        id = competency.getId();
    }
    
    @Override
    protected void onTearDownAfterTransaction() throws Exception {
    	super.onTearDownAfterTransaction();
        if (id != null) {
        	log.debug("Deleting provisory object: " + id);
        	CompetencyProcess competency = new CompetencyProcess();
        	competency.setId(id);
        	action.setCompetency(competency);
        	action.delete();
        	id=null;
        }
    }

    public void testSearch() throws Exception {
    	log.debug("testing search ...");
        assertEquals(action.list(), ActionSupport.SUCCESS);
        // assertEquals(0, action.getCompetencies().size());
        assertTrue(action.getCompetencies().size() >= 1);
        log.debug("Number of competencies: " + action.getCompetencies().size());
    }
    
    
    // edit an existing competency
    public void testEdit() throws Exception {
        log.debug("testing edit...");
        action.setId(180L);
        // since not yet set in CompetencyAction the returned competency is null
        assertNull(action.getCompetency());
        // retrieves or creates new competency; loads into CompetencyAction
        assertEquals("success", action.edit());
        // gets competency loaded into CompetencyAction
        assertNotNull(action.getCompetency());
        // no errors should occur while processing this action
        assertFalse(action.hasActionErrors());
    }

    // show an existing competency
    public void testShow() throws Exception {
        log.debug("testing show...");
        action.setId(180L);
        // since not yet set in CompetencyAction the returned competency is null
        assertNull(action.getCompetency());
        // retrieves or creates new competency; loads into CompetencyAction
        assertEquals("success", action.show());
        // gets competency loaded into CompetencyAction
        assertNotNull(action.getCompetency());
        // no errors should occur while processing this action
        assertFalse(action.hasActionErrors());
    }
    
    public void testSave() throws Exception {
        log.debug("testing save...");
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletActionContext.setRequest(request);
        action.setId(180L);
        assertEquals("success", action.edit());
        assertNotNull(action.getCompetency());
        
        // set nameable explicitly
        action.setNameable(action.getCompetency());
        
        // update name and save
        action.getCompetency().setName("Updated name.");
        // should be "input", because field errors are expected
        assertEquals("input", action.save());
        assertEquals("Updated name.", action.getCompetency().getName());
        assertFalse(action.hasActionErrors());
        
        // we expect field errors
        assertTrue(action.hasFieldErrors());
        // due to field errors, no messages are expected
        assertNull(request.getSession().getAttribute("messages"));
    }
    

    public void testRemove() throws Exception {
        log.debug("testing remove...");
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletActionContext.setRequest(request);
        action.setDelete("");
        CompetencyProcess competency = new CompetencyProcess();
        competency.setId(183L);
        action.setCompetency(competency);
        assertEquals("delete", action.delete());
        assertNotNull(request.getSession().getAttribute("messages"));
    } 
}
