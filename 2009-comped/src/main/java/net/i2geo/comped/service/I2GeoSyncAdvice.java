package net.i2geo.comped.service;

import net.i2geo.changeCoder.ChangeRequest;
import net.i2geo.comped.model.CompetencyProcess;
import net.i2geo.comped.model.ConcreteCompetency;
import net.i2geo.comped.model.Identifiable;
import net.i2geo.comped.model.Name;
import net.i2geo.comped.model.AbstractTopic;
import net.i2geo.comped.model.ConcreteTopic;
import net.i2geo.comped.model.Thing;
import net.i2geo.comped.protocol.ProtocolBuilder;
import net.i2geo.comped.protocol.impl.ProtocolBuilderImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;


import java.lang.reflect.Method;

/**
 * This advice is responsible for syncing changes on ontology items with the
 * I2Geo Ontology Server.
 *
 * @author Martin Homik
 */
public class I2GeoSyncAdvice implements MethodBeforeAdvice, AfterReturningAdvice {

	private final Log log = LogFactory.getLog(I2GeoSyncAdvice.class);

    private Object object = null;
    private boolean isNew = false;
    
    private String destinationURL = null;
    private String responseURL = null;
   
    private String searchI2GURL;
    
    // the parameter is injected by Spring. See applicationContext-service.xml
    public void setDestinationURL(String destinationURL) {
    	// log.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + destinationURL);
    	this.destinationURL = destinationURL;
    }

    
    // the parameter is injected by Spring. See applicationContext-service.xml
    public void setResponseURL(String responseURL) {
    	this.responseURL = responseURL;
    	// log.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + responseURL);
    }
    
    public void setSearchI2GURL(String searchI2GURL) {
    	this.searchI2GURL = searchI2GURL;
    }


    /**
     *
     * @param method the name of the method executed
     * @param args the arguments to the method
     * @param target the target class
     * @throws Throwable thrown when first argument is null or not a known object
     */
    public void before(Method method, Object[] args, Object target) throws Throwable {
   		log.debug("Advising I2GeoSync (before): ");
   		log.debug("  " + target.getClass().getName());
   		log.debug("  " + method.getName());

   		if (target instanceof NameManager) {
   			log.debug("No synch of name manager saves.");
   			return;
   		}

    	if (method.getName().equals("save")) {
    		beforeSave(args[0], target);
    	}
    	
    	if (method.getName().equals("detach")) {
    		beforeRemove(args[0], target);
    	}
    	
    }
    
    private void beforeSave(Object o, Object target) {
   		log.debug("* Pre-Processing before saving *");
   		
      	String sessionID      = "3487653846";
      	
      	if (destinationURL == null) {
      		return;
      	}

		ProtocolBuilder pBuilder = new ProtocolBuilderImpl(sessionID, responseURL);
		
    	Identifiable i  = (Identifiable) o;
    	isNew = (i.getId() == null);


    	// TODO: At this position, a factory is very useful!!!!
    	if (!isNew) {

    		if (i instanceof ConcreteCompetency) {
    			ConcreteCompetencyManager manager = (ConcreteCompetencyManager) target;
    			this.object = manager.get(i.getId());
    			
    			pBuilder.update((ConcreteCompetency) this.object);

    		}

    		else if (i instanceof CompetencyProcess) {
    			CompetencyProcessManager manager = (CompetencyProcessManager) target;
    			this.object = manager.get(i.getId());

    			pBuilder.update((CompetencyProcess) this.object);
    		}

    		else if (i instanceof ConcreteTopic) {
    			ConcreteTopicManager manager = (ConcreteTopicManager) target;
    			this.object = manager.get(i.getId());
    			
    			pBuilder.update((ConcreteTopic) this.object);

    		}

    		else if (i instanceof AbstractTopic) {
    			AbstractTopicManager manager = (AbstractTopicManager) target;
    			this.object = manager.get(i.getId());
    			
    			pBuilder.update((AbstractTopic) this.object);
    		}
    		
    		else return;
    		
    	} else {
    		if (i instanceof ConcreteCompetency) {
    			this.object = o;
    			pBuilder.add((ConcreteCompetency) this.object);

    		}

    		else if (i instanceof CompetencyProcess) {
    			this.object = o;
    			pBuilder.add((CompetencyProcess) this.object);
    		}

    		else if (i instanceof ConcreteTopic) {
    			this.object = o;
    			pBuilder.add((ConcreteTopic) this.object);
    		}

    		else if (i instanceof AbstractTopic) {
    			this.object = o;
    			pBuilder.add((AbstractTopic) this.object);
    		}
    		
    		// Othewise don't do anything and simply return;
	    		else return;
    		
    	}   	

    	// send the message
      	try {
      		
      		
      		String xml = pBuilder.getXML();
			log.debug(xml);
			
			ChangeRequest myChangeReq = new ChangeRequest(xml, destinationURL);
      		boolean result = myChangeReq.sendMessage();
	      	if (!result) {
	      		log.debug("Failure SEND to ontoUpdate.");
	      		return;
	      	}
	      	
	    	log.debug("SUCCESSUFUL SEND to ontoUpdate.");

	    	syncSearchI2G(xml);

      	} catch (Exception ex) {
      		log.debug("EXCEPTION. " + ex);
	    }

    }


	private void syncSearchI2G(String xml) throws Exception {
		boolean result;
		SearchI2GRequest searchI2GRequest = new SearchI2GRequest(xml, searchI2GURL);
		result = searchI2GRequest.sendMessage();
		if (!result) {
			log.debug("Failure SEND to searchI2G.");
			return;
		}

		log.debug("SUCCESSUFUL SEND to searchI2G.");
	}

    private void beforeRemove(Object o, Object target) {
   		log.debug("* Pre-Processing before saving *");

      	String sessionID      = "3487653846";
      	      	
		ProtocolBuilder pBuilder = new ProtocolBuilderImpl(sessionID, responseURL);
		
		if (o != null) {
			this.object = (Thing) o;
		}
		
		pBuilder.delete((Thing) o);

    	// send the message
      	try {
      		String xml = pBuilder.getXML();
			log.debug(xml);
			
			ChangeRequest myChangeReq = new ChangeRequest(xml, destinationURL);
      		boolean result = myChangeReq.sendMessage();
	      	if (!result) {
	      		log.debug("Failure SEND to ontoUpdate.");
	      	}

      		log.debug("SUCCESSUFUL SEND to ontoUpdate.");

	    	syncSearchI2G(xml);
	      	
      	} catch (Exception ex) {
      		log.debug("EXCEPTION." + ex);
	    }
    }
    
    /**
     * After returning, ....
     * @param returnValue the ... object
     * @param method the name of the method executed
     * @param args the arguments to the method
     * @param target the target class
     * @throws Throwable thrown when first argument is null or not a known object
     */
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
    	if (method.getName().equals("save")) { 
    		afterSave(args[0], target);
    	}

    	if (method.getName().equals("remove")) { 
    		afterRemove(args[0], target);
    	}

    	if (log.isDebugEnabled()) {
    		log.debug(
    				"Advising I2GeoSync (after): \n" +
    				"  " + target.getClass().getName() + "\n" +
    				"  " + method.getName() + "\n" + 
    				"  old object is: " + this.object);
    	} else {
        	log.debug("DISABLED");
    	}
    }

    private void afterSave(Object o, Object target) {
    	if (log.isDebugEnabled()) {
    		log.debug("* Post-Processing after saving *");
    	}

    	if (o instanceof Name) {
    		//
    	}
    	
    	if (o instanceof ConcreteCompetency) {
    		//
    	}
    	
    	if (o instanceof CompetencyProcess) {
    		//
    	}
    	
    	if (o instanceof ConcreteTopic) {
    		//
    	}
    	
    	if (o instanceof AbstractTopic) {
    		//
    	}
    }

    private void afterRemove(Object o, Object target) {
    	if (log.isDebugEnabled()) {
    		log.debug("* Post-Processing after saving *");
    	}

    	if (o instanceof Name) {
    		//
    	}
    	
    	if (o instanceof ConcreteCompetency) {
    		//
    	}
    	
    	if (o instanceof CompetencyProcess) {
    		//
    	}
    	
    	if (o instanceof ConcreteTopic) {
    		//
    	}
    	
    	if (o instanceof AbstractTopic) {
    		//
    	}
    }
    
}
