/**
 * 
 */
package net.i2geo.comped.webapp.action;

import java.util.Date;
import java.util.Locale;

import net.i2geo.comped.model.Competency;
import net.i2geo.comped.model.Thing;
import net.i2geo.comped.service.CompetencyManager;
import net.i2geo.comped.service.NameManager;

import org.springframework.mock.web.MockHttpServletRequest;

import com.opensymphony.xwork2.ActionSupport;

import org.apache.struts2.ServletActionContext;
import org.appfuse.service.UserManager;
import org.appfuse.webapp.action.BaseActionTestCase;

/**
 * @author Martin Homik
 *
 */
public class CompetencyActionTest extends BaseActionTestCase {
    
	private CompetencyAction action;
	private Long id = null;
	
    @Override
    protected void onSetUpBeforeTransaction() throws Exception {
        super.onSetUpBeforeTransaction();
    	log.debug("Adding provisory object ...");
        action = new CompetencyAction();
        CompetencyManager competencyManager = (CompetencyManager) applicationContext.getBean("competencyManager");
        competencyManager.setLocale(Locale.ENGLISH);
        UserManager userManager = (UserManager) applicationContext.getBean("userManager");
        action.setCompetencyManager(competencyManager);

        NameManager nameManager = (NameManager) applicationContext.getBean("nameManager");
        action.setNameManager(nameManager);
        
        // add a test competency to the database
        // note: error will occur if one of the fields is unique
        //       for each transaction a new competency is created and saved.
        Competency competency = new Competency();
        competency.setName("Labertasche");
        
        // set Thing properties
        Date date = new Date();
        competency.setCreated(date);
        competency.setModified(date);
        competency.setStatus(Thing.STATUS_PUBLISHED);
        competency.setCommentStatus(Thing.COMMENTS_CLOSED);
        competency.setCommentDays(0);
        competency.setCreator(userManager.getUser("-1"));
        competency = competencyManager.save(competency);
        id = competency.getId();
    }
    
    @Override
    protected void onTearDownAfterTransaction() throws Exception {
    	super.onTearDownAfterTransaction();
        if (id != null) {
        	log.debug("Deleting provisory object: " + id);
        	Competency competency = new Competency();
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
        action.setId(160L);
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
        action.setId(160L);
        // since not yet set in CompetencyAction the returned competency is null
        assertNull(action.getCompetency());
        // retrieves or creates new competency; loads into CompetencyAction
        assertEquals("success", action.show());
        // gets competency loaded into CompetencyAction
        assertNotNull(action.getCompetency());
        // no errors should occur while processing this action
        assertFalse(action.hasActionErrors());
    }
    
    /* Skipping test as saves on Competencies should be omitted.
     * There will be a clash with I2GeoSyncAdvise
     * 
    public void testSave() throws Exception {
        log.debug("testing save...");
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletActionContext.setRequest(request);
        action.setId(160L);
        assertEquals("success", action.edit());
        assertNotNull(action.getCompetency());
        
        
        // update name and save
        action.getCompetency().setName("Updated name.");
        assertEquals("input", action.save());
        assertEquals("Updated name.", action.getCompetency().getName());
        assertFalse(action.hasActionErrors());
        assertFalse(action.hasFieldErrors());
        assertNotNull(request.getSession().getAttribute("messages"));
    }
    */

    public void testRemove() throws Exception {
        log.debug("testing remove...");
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletActionContext.setRequest(request);
        action.setDelete("");
        Competency competency = new Competency();
        competency.setId(164L);
        action.setCompetency(competency);
        assertEquals("success", action.delete());
        assertNotNull(request.getSession().getAttribute("messages"));
    } 
}
