package net.i2geo.comped.protocol;


import net.i2geo.comped.dao.AbstractTopicDao;
import net.i2geo.comped.dao.CompetencyProcessDao;
import net.i2geo.comped.dao.ConcreteCompetencyDao;
import net.i2geo.comped.dao.ConcreteTopicDao;
import net.i2geo.comped.model.AbstractTopic;
import net.i2geo.comped.model.CompetencyProcess;
import net.i2geo.comped.model.ConcreteCompetency;
import net.i2geo.comped.model.ConcreteTopic;
import net.i2geo.comped.model.Name;
import net.i2geo.comped.model.Topic;
import net.i2geo.comped.protocol.impl.ProtocolBuilderImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.dao.BaseDaoTestCase;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.junit.Test;


public class ProtocolBuilderTest extends BaseDaoTestCase {

    /**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());
	

    ConcreteCompetencyDao concreteCompetencyDao;
    CompetencyProcessDao competencyProcessDao;

    ConcreteTopicDao concreteTopicDao;
    AbstractTopicDao abstractTopicDao;

    public void setConcreteCompetencyDao(ConcreteCompetencyDao concreteCompetencyDao) {
        this.concreteCompetencyDao = concreteCompetencyDao;
    }

    public void setCompetencyProcessDao(CompetencyProcessDao competencyProcessDao) {
        this.competencyProcessDao = competencyProcessDao;
    }

    public void setConcreteTopicDao(ConcreteTopicDao concreteTopicDao) {
        this.concreteTopicDao = concreteTopicDao;
    }

    public void setAbstractTopicDao(AbstractTopicDao abstractTopicDao) {
        this.abstractTopicDao = abstractTopicDao;
    }
    

    @Test
	public void testCreateDoc() throws Exception {
		ProtocolBuilder pBuilder = new ProtocolBuilderImpl("Test", "http://www.activemath.org/");
		String doc = pBuilder.getXML();
		
		log.debug(doc);
	}

    /*
     * TODO: probably delete
	@Test
	public void testCreateName() throws Exception {
		ProtocolBuilder pBuilder = new ProtocolBuilderImpl("Test", "http://www.activemath.org/");
		Name name = new Name();
		name.setName("Test");
		name.setType(Name.TYPE_COMMON);
		name.setLocale("en");
		name.setDefName(false);
		
		Element e = pBuilder.createName(name);
		
		assertNotNull(e);
		
		XMLOutputter serializer = new XMLOutputter(Format.getPrettyFormat());
		serializer.output(e, System.out);

	}
	*/
    
	@Test
	public void testAddConcreteCompetency() throws Exception {

    	ConcreteCompetency c = concreteCompetencyDao.get(164L);

		ProtocolBuilder pBuilder = new ProtocolBuilderImpl("Test", "http://www.activemath.org/");
		pBuilder.add(c);
		
		String doc = pBuilder.getXML();
		
		log.debug(doc);
	}

	@Test
	public void testAddCompetencyProcess() throws Exception {

    	CompetencyProcess c = competencyProcessDao.get(180L);

		ProtocolBuilder pBuilder = new ProtocolBuilderImpl("Test", "http://www.activemath.org/");
		pBuilder.add(c);
		
		String doc = pBuilder.getXML();
		
		log.debug(doc);
	}

	@Test
	public void testAddConcreteTopic() throws Exception {

    	ConcreteTopic t = concreteTopicDao.get(172L);

		ProtocolBuilder pBuilder = new ProtocolBuilderImpl("Test", "http://www.activemath.org/");
		pBuilder.add(t);
		
		String doc = pBuilder.getXML();
		
		log.debug(doc);
	}

	@Test
	public void testAddAbstractTopic() throws Exception {

    	AbstractTopic t = abstractTopicDao.get(171L);

		ProtocolBuilder pBuilder = new ProtocolBuilderImpl("Test", "http://www.activemath.org/");
		pBuilder.add(t);
		
		String doc = pBuilder.getXML();

		log.debug(doc);
	}

	@Test
	public void testDeleteCompetency() throws Exception {

    	ConcreteCompetency c = concreteCompetencyDao.get(164L);

		ProtocolBuilder pBuilder = new ProtocolBuilderImpl("Test", "http://www.activemath.org/");
		pBuilder.delete(c);
		
		String doc = pBuilder.getXML();
		
		log.debug(doc);
	}

	@Test
	public void testDeleteTopic() throws Exception {

    	ConcreteTopic t = concreteTopicDao.get(172L);

		ProtocolBuilder pBuilder = new ProtocolBuilderImpl("Test", "http://www.activemath.org/");
		pBuilder.delete(t);
		
		String doc = pBuilder.getXML();
		
		log.debug(doc);
	}

	/*
	 * TODO enable test method
	@Test
	public void testDeleteTopicFromCompetency() throws Exception {

    	ConcreteCompetency c = concreteCompetencyDao.get(164L);
    	
    	
		ProtocolBuilder pBuilder = new ProtocolBuilderImpl("Test", "http://www.activemath.org/");

    	for (Topic t : c.getTopics()) {
    		pBuilder.deleteTopicFromCompetency(c, t);
    	}

		String doc = pBuilder.getXML();
		
		log.debug(doc);
	}
	*/
}
