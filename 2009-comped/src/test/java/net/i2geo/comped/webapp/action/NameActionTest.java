/**
 * 
 */
package net.i2geo.comped.webapp.action;


import net.i2geo.comped.model.Name;
import net.i2geo.comped.service.NameManager;

import org.springframework.mock.web.MockHttpServletRequest;

import com.opensymphony.xwork2.ActionSupport;

import org.apache.struts2.ServletActionContext;
import org.appfuse.webapp.action.BaseActionTestCase;

/**
 * @author Martin Homik
 *
 */
public class NameActionTest extends BaseActionTestCase {
    
	private NameAction action;
	private Long id = null;
	
    @Override
    protected void onSetUpBeforeTransaction() throws Exception {
        super.onSetUpBeforeTransaction();
    	log.debug("Adding provisory object ...");
        action = new NameAction();
        NameManager nameManager = (NameManager) applicationContext.getBean("nameManager");
        action.setNameManager(nameManager);

        // add a test name to the database
        // note: error will occur if one of the fields is unique
        //       for each transaction a new name is created and saved.
        Name name = new Name();
        name.setName("Labertasche");
        name.setLocale("de");
        name.setType(Name.TYPE_COMMON);
        name.setDefName(false);
        
        name = nameManager.save(name);
        id = name.getId();
    }
    
    @Override
    protected void onTearDownAfterTransaction() throws Exception {
    	super.onTearDownAfterTransaction();
        if (id != null) {
        	log.debug("Deleting provisory object: " + id);
        	Name name = new Name();
        	name.setId(id);
        	action.setName(name);
        	action.delete();
        	id=null;
        }
    }

    public void testSearch() throws Exception {
    	log.debug("testing search ...");
        assertEquals(action.list(), ActionSupport.SUCCESS);
        // assertEquals(0, action.getNames().size());
        assertTrue(action.getNames().size() >= 1);
    }
    
    
    // edit an existing name
    public void testEdit() throws Exception {
        log.debug("testing edit...");
        action.setId(1L);
        // since not yet set in NameAction the returned name is null
        assertNull(action.getName());
        // retrieves or creates new name; loads into NameAction
        assertEquals("success", action.edit());
        // gets name loaded into NameAction
        assertNotNull(action.getName());
        // no errors should occur while processing this action
        assertFalse(action.hasActionErrors());
    }

    // show an existing name
    public void testShow() throws Exception {
        log.debug("testing show...");
        action.setId(1L);
        // since not yet set in NameAction the returned name is null
        assertNull(action.getName());
        // retrieves or creates new name; loads into NameAction
        assertEquals("success", action.show());
        // gets name loaded into NameAction
        assertNotNull(action.getName());
        // no errors should occur while processing this action
        assertFalse(action.hasActionErrors());
    }
    
    public void testSave() throws Exception {
        log.debug("testing save...");
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletActionContext.setRequest(request);
        action.setId(1L);
        assertEquals("success", action.edit());
        assertNotNull(action.getName());
        
        
        // update name and save
        action.getName().setName("Updated name.");
        assertEquals("success", action.save());
        assertEquals("Updated name.", action.getName().getName());
        assertFalse(action.hasActionErrors());
        assertFalse(action.hasFieldErrors());
        assertNotNull(request.getSession().getAttribute("messages"));
    }
    

    public void testRemove() throws Exception {
        log.debug("testing remove...");
        MockHttpServletRequest request = new MockHttpServletRequest();
        ServletActionContext.setRequest(request);
        action.setDelete("");
        Name name = new Name();
        name.setId(20L);
        action.setName(name);
        assertEquals("success", action.delete());
        assertNotNull(request.getSession().getAttribute("messages"));
    } 
}
