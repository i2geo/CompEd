package net.i2geo.comped.protocol.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import net.i2geo.comped.model.AbstractTopic;
import net.i2geo.comped.model.CompetencyProcess;
import net.i2geo.comped.model.ConcreteCompetency;
import net.i2geo.comped.model.ConcreteTopic;
import net.i2geo.comped.model.Name;
import net.i2geo.comped.model.Thing;
import net.i2geo.comped.model.Topic;
import net.i2geo.comped.protocol.ProtocolBuilder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.util.List;


public class ProtocolBuilderImpl implements ProtocolBuilder {
	
    @SuppressWarnings("unused")
	private final Log log = LogFactory.getLog(ProtocolBuilderImpl.class);
    
    // the main document
	Document doc = null;
	// the document's root
	Element root = null;

	// namespaces
	protected final Namespace gsNS   = Namespace.getNamespace("gs", "http://www.inter2geo.eu/2008/ontology/GeoSkills#");
	protected final Namespace rdfNS  = Namespace.getNamespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
	protected final Namespace rdfsNS = Namespace.getNamespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
	protected final Namespace owlNS  = Namespace.getNamespace("owl", "http://www.w3.org/2002/07/owl#");
	protected final Namespace thisNS = Namespace.getNamespace("http://www.inter2geo.eu/2008/ontology/ontoUpdates.owl#");

	protected Element additions;
	protected Element updates;
	protected Element deletions;
	
	public ProtocolBuilderImpl(String name, String serverResponse) {
		
		root  = new Element("ontoUpdates");
		root.addNamespaceDeclaration(gsNS);
		root.addNamespaceDeclaration(rdfNS);
		root.addNamespaceDeclaration(rdfsNS);
		root.addNamespaceDeclaration(owlNS);
		root.setNamespace(thisNS);
	
		doc = new Document(root);
		Element session = new Element("Session", thisNS);
		Element n = new Element("Name", thisNS);
		n.setText(name);
		Element s = new Element("ServerResponse", thisNS);
		s.setText(serverResponse);
		
		session.addContent(n);
		session.addContent(s);

		root.addContent(session);
		
		additions = new Element("Additions", thisNS);
		updates =   new Element("Updates", thisNS);
		deletions = new Element("Deletions", thisNS);
		
		root.addContent(additions);
		root.addContent(updates);
		root.addContent(deletions);	
	}
	
	/****************************************************************
	 * CREATE JDOM NAME/LABLE ELEMENTS
	 ****************************************************************/
	
	private Element createName(Name name) {
		Element e = null;
		
		if (name.getType().equals(Name.TYPE_COMMON)) {
			e = new Element("commonName",gsNS);
		}

		if (name.getType().equals(Name.TYPE_UNCOMMON)) {
			e = new Element("uncommonName", gsNS);
		}

		if (name.getType().equals(Name.TYPE_RARE)) {
			e = new Element("rareName", gsNS);
		}

		if (name.getType().equals(Name.TYPE_FALSEFRIEND)) {
			e = new Element("falseFriendName", gsNS);
		}

		e.setAttribute("lang", name.getLocale(), Namespace.XML_NAMESPACE);
		e.setAttribute("default", Boolean.toString(name.isDefName()));
		e.setText(name.getName());
		
		return e;		
	}

	private Element createLabel(Name name) {
		Element e = null;
		
		e = new Element("label",rdfsNS);

		e.setAttribute("lang", name.getLocale(), Namespace.XML_NAMESPACE);
		e.setText(name.getName());
		
		return e;		
	}
	
	/****************************************************************
	 * CREATE JDOM THING ELEMENTS
	 ****************************************************************/
	
	// instead of sending the user name, we send the user id.
	// This is needed for the comped-maven-parser, as it does not
	// know anything about the user name. Hence, a correct join point
	// can be only via an ID 
	private Element createCreator(User user) {
		Element e = null;
		
		e = new Element("creator", gsNS); 
		e.setText(user.getId().toString());
		
		return e;
	}

	
	
	private Element createCreated(Date date) {
		Element e = null;
		
		e = new Element("created", gsNS); 
		e.setText(calculateDate(date));
		
		return e;
	}

	
	
	private Element createModified(Date date) {
		Element e = null;
		
		e = new Element("modified", gsNS); 
		e.setText(calculateDate(date));
		
		return e;
	}
	
	private String calculateDate(Date date) {
		if (date == null) {
			return null;
		}
		
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		String year = Integer.toString(c.get(Calendar.YEAR));
		String month = Integer.toString(c.get(Calendar.MONTH+1)); // IG-338: GregorianCalendar starts
		String day = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
		String hour = Integer.toString(c.get(Calendar.HOUR_OF_DAY));
		String minute = Integer.toString(c.get(Calendar.MINUTE));
		String second = Integer.toString(c.get(Calendar.SECOND));
		
		month = (month.length() == 1) ? ("0" + month) : month;
		day  = (day.length() == 1) ? ("0" + day) : day;
		hour = (hour.length() == 1) ? ("0" + hour) : hour;
		minute = (minute.length() == 1) ? ("0" + minute) : minute;
		second = (second.length() == 1) ? ("0" + second) : second;

		return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
	}

	/****************************************************************
	 * CREATE HAS ELEMENTS
	 ****************************************************************/
	
	private Element createHasTopic(Topic t) {
		Element e = null;
		
		e = new Element("hasTopic", gsNS);
		e.setAttribute("resource",t.getUriSuffix(), rdfNS);
		
		return e;
	}

	private Element createHasAbstractTopicParent(AbstractTopic t) {
		Element e = null;
		
		e = new Element("subClassOf", rdfsNS);
		e.setAttribute("resource",t.getUriSuffix(), rdfNS);
		
		return e;
	}

	private Element createHasCompetencyProcessParent(CompetencyProcess cp) {
		Element e = null;
		
		e = new Element("subClassOf", rdfsNS);
		e.setAttribute("resource",cp.getUriSuffix(), rdfNS);
		
		return e;
	}
	
	private List<Element> createProcessReferences(List<CompetencyProcess> processes) {
		List<Element> types = new ArrayList<Element>();
		
		for (CompetencyProcess cp : processes) {
			Element e = new Element("type", rdfNS);
			e.setAttribute("resource",cp.getUriSuffix(), rdfNS);
			types.add(e);
		}
		
		return types;
	}

	private List<Element> createAbstractTopicReferences(List<AbstractTopic> abstractTopics) {
		List<Element> types = new ArrayList<Element>();
		
		for (AbstractTopic at : abstractTopics) {
			Element e = new Element("type", rdfNS);
			e.setAttribute("resource",at.getUriSuffix(), rdfNS);
			types.add(e);
		}
		
		return types;
	}
	
	/****************************************************************
	 * CREATE JDOM ADD ELEMENTS
	 ****************************************************************/

	
	/*
	    <owl:class rdf:about="Nancy">
	  		<gs:creator>user</gs:creator>
    		<gs:created>Mon Jan 12 14:28:51 CET 2009</gs:created>
      		<gs:modified>Mon Jan 12 14:28:51 CET 2009</gs:modified>
      		<rdfs:label xml:lang="en">Nancy</rdfs:label>
      		<rdfs:subClassOf rdf:resource="Construct" />
        </owl:class>  
	*/
  
	/**
	 * Creates a JDOM element that represents the creation of a new competency process.
	 * 
	 * @param c
	 * @return
	 */
	private Element createCompetencyProcess(CompetencyProcess c) {
		Element e = null;
		
		e = new Element("class", owlNS);
		e.setAttribute("about",c.getUriSuffix(), rdfNS);
		
		/* e.addContent(createCreator(c.getCreator()));
		e.addContent(createCreated(c.getCreated()));
		e.addContent(createModified(c.getModified())); */
		
		for (Name name: c.getNames()) {
			e.addContent(createLabel(name));
		}
				
		// add subclass information
		for (CompetencyProcess cp : c.getParent()) {
			e.addContent(createHasCompetencyProcessParent(cp));
		}
		
		return e;
	}
	
	
	
	/*
    	<gs:Competency rdf:ID="Grenoble">
      		<gs:creator>user</gs:creator>
      		<gs:created>Mon Jan 12 14:26:19 CET 2009</gs:created>
      		<gs:modified>Mon Jan 12 14:26:19 CET 2009</gs:modified>
      		<rdf:type rdf:resource="Construct" />
      		<gs:commonName xml:lang="en" default="true">Grenoble</gs:commonName>
      		<gs:hasTopic rdf:resource="Segment" />
    	</gs:Competency>
	*/
	
	/**
	 * Creates a JDOM element that represents the creation of a new concrete competency.
	 * 
	 * @param c
	 * @return
	 */
	private Element createConcreteCompetency(ConcreteCompetency c) {
		Element e = null;
		
		e = new Element("Competency", gsNS);
		e.setAttribute("ID", c.getUriSuffix(), rdfNS);
		
		e.addContent(createCreator(c.getCreator()));
		e.addContent(createCreated(c.getCreated()));
		e.addContent(createModified(c.getModified()));
		e.addContent(createProcessReferences(c.getProcesses()));
		
		for (Name name: c.getNames()) {
			e.addContent(createName(name));
		}

		for (Topic t : c.getTopics()) {
			e.addContent(createHasTopic(t));
		}
		
		return e;
	}

	/*
	 
    	<gs:Topic rdf:ID="Bretagne">
      		<gs:creator>user</gs:creator>
      		<gs:created>Mon Jan 12 14:31:17 CET 2009</gs:created>
      		<gs:modified>Mon Jan 12 14:31:17 CET 2009</gs:modified>
      		<rdf:type rdf:resource="Quadrilateral" />
      		<gs:commonName xml:lang="en" default="true">Bretagne</gs:commonName>
    	</gs:Topic>
	* 
	*/
	
	/**
	 * Creates a JDOM element that represents the creation of a new concrete topic.
	 * 
	 * @param t
	 * @return
	 */
	private Element createConcreteTopic(ConcreteTopic t) {
		Element e = null;
		
		e = new Element("Topic", gsNS);
		e.setAttribute("ID",t.getUriSuffix(), rdfNS);
		
		e.addContent(createCreator(t.getCreator()));
		e.addContent(createCreated(t.getCreated()));
		e.addContent(createModified(t.getModified()));
		e.addContent(createAbstractTopicReferences(t.getAts()));
		
		for (Name name: t.getNames()) {
			e.addContent(createName(name));
		}
		
		return e;
	}

	/*
	 	<owl:class rdf:ID="Forbach">
	      <gs:creator>user</gs:creator>
    	  <gs:created>Mon Jan 12 14:34:00 CET 2009</gs:created>
      	  <gs:modified>Mon Jan 12 14:34:00 CET 2009</gs:modified>
      	  <rdfs:label xml:lang="en">Forbach</rdfs:label>
      	  <rdfs:subClassOf rdf:resource="Quadrilateral" />
        </owl:class>
	
	*/
	
	/**
	 * Creates a JDOM element that represents the creation of a new abstract topic.
	 * 
	 * @param t
	 * @return
	 */
	private Element createAbstractTopic(AbstractTopic t) {
		Element e = null;
		
		e = new Element("class", owlNS);
		e.setAttribute("ID", t.getUriSuffix(), rdfNS);
		
		/* e.addContent(createCreator(t.getCreator()));
		e.addContent(createCreated(t.getCreated()));
		e.addContent(createModified(t.getModified())); */
		
		for (Name name: t.getNames()) {
			if (name.isDefName()) {
				e.addContent(createLabel(name));
			}
		}
		
		// add subclass information
		for (AbstractTopic at : t.getParent()) {
			e.addContent(createHasAbstractTopicParent(at));
		}

		return e;
	}

	
	/**
	 * Creates a JDOM element that represents the creation of a new abstract competency.
	 * 
	 * @param t
	 * @return
	 */
	private Element createRepresentative(AbstractTopic t) {
		Element e = null;
		
		e = new Element("Topic", gsNS);
		e.setAttribute("ID",t.getUriSuffix()+"_r", rdfNS);
		
		e.addContent(createCreator(t.getCreator()));
		e.addContent(createCreated(t.getCreated()));
		e.addContent(createModified(t.getModified()));

		ArrayList<AbstractTopic> ref = new ArrayList<AbstractTopic>();
		ref.add(t);
		e.addContent(createAbstractTopicReferences(ref));
		
		for (Name name: t.getNames()) {
			e.addContent(createName(name));
		}
		
		return e;
	}
	
	
	/****************************************************************
	 * CREATE JDOM DELETE METHODS
	 ****************************************************************/
	
	private Element createDeleteThing(Thing t) {
		Element e = null;
		
		e = new Element("Thing", gsNS);
		e.setAttribute("ID", t.getUriSuffix(), rdfNS);
		return e;
	}

	/****************************************************************
	 * HELP METHODS
	 ****************************************************************/

	
	private Document getDoc() {
		return doc;
	}

		
	
	/****************************************************************
	 * INTERFACE IMPLEMENTATIONS
	 ****************************************************************/
	
	/*
	 * (non-Javadoc)
	 * @see net.i2geo.comped.protocol.ProtocolBuilder#add(net.i2geo.comped.model.ConcreteCompetency)
	 */
	public void add(ConcreteCompetency c) {
		additions.addContent(createConcreteCompetency(c));
	}

	/*
	 * (non-Javadoc)
	 * @see net.i2geo.comped.protocol.ProtocolBuilder#add(net.i2geo.comped.model.CompetencyProcess)
	 */
	public void add(CompetencyProcess c) {
		additions.addContent(createCompetencyProcess(c));
	}

	/*
	 * (non-Javadoc)
	 * @see net.i2geo.comped.protocol.ProtocolBuilder#add(net.i2geo.comped.model.ConcreteTopic)
	 */
	public void add(ConcreteTopic t) {
		additions.addContent(createConcreteTopic(t));
	}

	/*
	 * (non-Javadoc)
	 * @see net.i2geo.comped.protocol.ProtocolBuilder#add(net.i2geo.comped.model.AbstractTopic)
	 */
	public void add(AbstractTopic t) {
		if (t.getType().equals(Topic.TYPE_PURE)) {
			additions.addContent(createAbstractTopic(t));
		}
	
		if (t.getType().equals(Topic.TYPE_REPRESENTATIVE)) {
			additions.addContent(createAbstractTopic(t));
			additions.addContent(createRepresentative(t));
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.i2geo.comped.protocol.ProtocolBuilder#update(net.i2geo.comped.model.ConcreteCompetency)
	 */
	public void update(ConcreteCompetency c) {
		updates.addContent(createConcreteCompetency(c));
	}

	/*
	 * (non-Javadoc)
	 * @see net.i2geo.comped.protocol.ProtocolBuilder#update(net.i2geo.comped.model.CompetencyProcess)
	 */
	public void update(CompetencyProcess c) {
		updates.addContent(createCompetencyProcess(c));
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.i2geo.comped.protocol.ProtocolBuilder#update(net.i2geo.comped.model.ConcreteTopic)
	 */
	public void update(ConcreteTopic t) {
		updates.addContent(createConcreteTopic(t));
	}

	/*
	 * (non-Javadoc)
	 * @see net.i2geo.comped.protocol.ProtocolBuilder#update(net.i2geo.comped.model.AbstractTopic)
	 */
	public void update(AbstractTopic t) {
		
		if (t.getType().equals(Topic.TYPE_PURE)) {
			updates.addContent(createAbstractTopic(t));
		}
	
		if (t.getType().equals(Topic.TYPE_REPRESENTATIVE)) {
			updates.addContent(createAbstractTopic(t));
			updates.addContent(createRepresentative(t));
		}
	}
	/*
	 * (non-Javadoc)
	 * @see net.i2geo.comped.protocol.ProtocolBuilder#delete(net.i2geo.comped.model.ConcreteCompetency)
	 */
	public void delete(ConcreteCompetency c) {
		deletions.addContent(createDeleteThing(c));
	}

	/*
	 * (non-Javadoc)
	 * @see net.i2geo.comped.protocol.ProtocolBuilder#delete(net.i2geo.comped.model.CompetencyProcess)
	 */
	public void delete(CompetencyProcess c) {
		deletions.addContent(createDeleteThing(c));
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.i2geo.comped.protocol.ProtocolBuilder#delete(net.i2geo.comped.model.ConcreteTopic)
	 */
	public void delete(ConcreteTopic t) {
		deletions.addContent(createDeleteThing(t));
	}

	/*
	 * (non-Javadoc)
	 * @see net.i2geo.comped.protocol.ProtocolBuilder#delete(net.i2geo.comped.model.AbstractTopic)
	 */
	public void delete(AbstractTopic t) {
		deletions.addContent(createDeleteThing(t));		
		if (t.getType().equals(Topic.TYPE_REPRESENTATIVE)) {
			Thing rThing = new Thing();
			rThing.setUri(t.getUri() + "_r");
			deletions.addContent(createDeleteThing(rThing));
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.i2geo.comped.protocol.ProtocolBuilder#delete(net.i2geo.comped.model.Thing)
	 */
	public void delete(Thing t) {
		if (t instanceof AbstractTopic) {
			delete((AbstractTopic) t);
		} else {
			deletions.addContent(createDeleteThing(t));		
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.i2geo.comped.protocol.ProtocolBuilder#getXML()
	 */
	public String getXML() {
		XMLOutputter serializer = new XMLOutputter(Format.getPrettyFormat());
		return serializer.outputString(getDoc());
	}

}
