package net.i2geo.comped.parser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;

import net.i2geo.onto.parse.GeoSkillsParseListener;

public class GeoSkillsParserSample implements GeoSkillsParseListener {

    /**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());
	
    protected final String checkName = "cumulative frequency diagram";
    int count = 0;
    
    protected final ErrorReporter error = new ErrorReporter();
    protected final StatisticsReporter statistics = new StatisticsReporter();
    
	private Document doc = null;

	private long thingCounter = 1;
	private long nameCounter = 1;
	private long descriptionCounter = 1;

	private Map entryPoints = new HashMap();

	// stores all competency items
	Map<String, String> ccURI2ID = new HashMap<String, String>();
	// stores only competency types; needed for a proper reference
	Map<String, String> cpURI2ID = new HashMap<String, String>();
	// stores only topic types; needed for a proper reference
	Map<String, String> tgURI2ID = new HashMap<String, String>();
	// stores all toipc items
	Map<String, String> tiURI2ID = new HashMap<String, String>();

	Map<String, Set<String>> ccParents     = new HashMap<String, Set<String>>();
	Map<String, Set<String>> cpParents     = new HashMap<String, Set<String>>();
	Map<String, Set<String>> tgParents     = new HashMap<String, Set<String>>();
	Map<String, Set<String>> tiParents     = new HashMap<String, Set<String>>();
	Map<String, Set<String>> cURI2topics   = new HashMap<String, Set<String>>();
	Map<String, Set<String>> cpTaxonomy    = new HashMap<String, Set<String>>();
	Map<String, Set<String>> tgTaxonomy    = new HashMap<String, Set<String>>();
	Map<String, NamesMap> reps             = new HashMap<String, NamesMap>();

	Map<String, String> tURI2ID = new HashMap<String, String>();
	Map<String, Set<String>> tURI2subsumes = new HashMap<String, Set<String>>();

	private String date;

	private Set<String> excludes = new HashSet<String>();
	
	public GeoSkillsParserSample() {
		Element root = new Element("dataset");
		doc = new Document(root);
		createDocSkeleton();
		date = calculateDate(new Date());
		initExcludes();
	}

	public GeoSkillsParserSample(Document doc) {
		this.doc = doc;
		createDocSkeleton();
		date = calculateDate(new Date());
		initExcludes();
	}
	
	public void initExcludes() {
		excludes.add("http://www.w3.org/2002/07/owl#Nothing");
	}

	public Document getDocument() {
		return doc;
		
	}
	
	private String calculateDate(Date date) {
		if (date == null) {
			return null;
		}
		
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		String year = Integer.toString(c.get(Calendar.YEAR));
		String month = Integer.toString(c.get(Calendar.MONTH));
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

	
	public static Element createTable(String name) {
		Element e = new Element("table");
		e.setAttribute("name", name);
		return e;
	}

	public static Element createColumn(String text) {
		Element e = new Element("column");
		e.setText(text);

		return e;
	}

	private static Element createRow() {
		Element e = new Element("row");
		return e;
	}

	private static Element createValue(String description, String value) {
		Element e = new Element("value");

		e.setAttribute("description", description);
		e.setText(value);

		return e;
	}

	private Element createThingTable() {
		Element e = createTable("thing");

		e = e.addContent(createColumn("id"));
		e = e.addContent(createColumn("date_created"));
		e = e.addContent(createColumn("date_modified"));
		e = e.addContent(createColumn("status"));
		e = e.addContent(createColumn("comment_status"));
		e = e.addContent(createColumn("comment_days"));
		e = e.addContent(createColumn("creator_id"));
		e = e.addContent(createColumn("uri"));

		entryPoints.put("thing", e);

		return e;
	}

	private Element createCompetencyTable() {
		Element e = createTable("competency");

		e = e.addContent(createColumn("id"));
		e = e.addContent(createColumn("description"));

		entryPoints.put("competency", e);

		return e;
	}

	private Element createConcreteCompetencyTable() {
		Element e = createTable("c_concrete");

		e = e.addContent(createColumn("id"));

		entryPoints.put("c_concrete", e);
		
		return e;
	}

	private Element createCompetencyProcessTable() {
		Element e = createTable("c_process");

		e = e.addContent(createColumn("id"));

		entryPoints.put("c_process", e);
		
		return e;
	}	
	
	private Element createTopicTable() {
		Element e = createTable("topic");

		e = e.addContent(createColumn("id"));

		entryPoints.put("topic", e);

		return e;
	}

	private Element createAbstractTopicTable() {
		Element e = createTable("t_group");
		
		e = e.addContent(createColumn("id"));
		e = e.addContent(createColumn("type"));
		
		entryPoints.put("t_group", e);
		
		return e;
	}

	private Element createConcreteTopicTable() {
		Element e = createTable("t_item");
		
		e = e.addContent(createColumn("id"));
		
		entryPoints.put("t_item", e);
	
		return e;
	}

	private Element createNameTable() {
		Element e = createTable("name");

		e = e.addContent(createColumn("id"));
		e = e.addContent(createColumn("name"));
		e = e.addContent(createColumn("locale"));
		e = e.addContent(createColumn("type"));
		e = e.addContent(createColumn("isDefName"));

		entryPoints.put("name", e);

		return e;
	}

	private Element createDescriptionTable() {
		Element e = createTable("description");

		e = e.addContent(createColumn("id"));
		e = e.addContent(createColumn("description"));
		e = e.addContent(createColumn("locale"));

		entryPoints.put("description", e);

		return e;
	}

	private Element createRelationTable(String tableName, String c1Name,
			String c2Name) {
		Element e = createTable(tableName);
		e.addContent(createColumn(c1Name));
		e.addContent(createColumn(c2Name));

		entryPoints.put(tableName, e);

		return e;
	}

	private Element createCompetencyNamesTable() {
		return createRelationTable("c_names", "competency_id", "names_id");
	}

	private Element createCompetencyTopicsTable() {
		return createRelationTable("c_topics", "competency_id", "topics_id");
	}

	private Element createTopicNameTable() {
		return createRelationTable("topic_names", "topic_id", "names_id");
	}

	private Element createCompetencyDescriptionTable() {
		return createRelationTable("c_description", "competency_id", "descriptions_id");
	}
	
	private Element createCompetencyCompositionTable() {
		return createRelationTable("c_composition", "composedBy_id", "composes_id");		
	}

	private Element createCompetencySubsumptionTable() {
		return createRelationTable("c_subsumption", "subsumedBy_id", "subsumes_id");		
	}
	
	private Element createCompetencySimilarityTable() {
		return createRelationTable("c_similarity", "inverseSimilarTo_id", "similarTo_id");		
	}

	private Element createCompetencyProcessTaxonomyTable() {
		return createRelationTable("cp_subprocesses", "child_id", "parent_id");		
	}

	private Element createAbstractTopicTaxonomyTable() {
		return createRelationTable("tg_subgroups", "child_id", "parent_id");		
	}

	private Element createProcessToConcreteTable() {
		return createRelationTable("ccs_cp", "cc_id", "cp_id");
	}

	private Element createAbstractTopicToTopicTable() {
		return createRelationTable("tis_tg", "ti_id", "tg_id");
	}

	private Element createDocSkeleton() {
		Element root = doc.getRootElement();
		root.addContent(createThingTable());
		root.addContent(createCompetencyTable());
		root.addContent(createConcreteCompetencyTable());
		root.addContent(createCompetencyProcessTable());
		root.addContent(createTopicTable());
		root.addContent(createAbstractTopicTable());
		root.addContent(createConcreteTopicTable());
		root.addContent(createNameTable());
		root.addContent(createDescriptionTable());
		root.addContent(createCompetencyNamesTable());
		root.addContent(createCompetencyTopicsTable());
		root.addContent(createCompetencyDescriptionTable());
		root.addContent(createCompetencyCompositionTable());
		root.addContent(createCompetencySubsumptionTable());
		root.addContent(createCompetencySimilarityTable());
		root.addContent(createCompetencyProcessTaxonomyTable());
		root.addContent(createTopicNameTable());
		root.addContent(createAbstractTopicTaxonomyTable());
		root.addContent(createProcessToConcreteTable());
		root.addContent(createAbstractTopicToTopicTable());
		return root;
	}

	private Element createValue(Element row, String name, String value,
			String defaultValue) {
		
		if (value != null && value != "") {
			row.addContent(createValue(name, value));
		} else {
			row.addContent(createValue(name, defaultValue));
		}

		return row;
	}

	private Element createThing(String creator, String created,
			String modified, String status, String commentStatus,
			String commentDays, String uri) {

		Element row = createRow();

		row.addContent(createValue("id", Long.toString(thingCounter)));
		thingCounter++;

		createValue(row, "date_created", created, date);
		createValue(row, "date_modified", modified, date);
		createValue(row, "status", status, "PUBLIC");
		createValue(row, "comment_status", commentStatus, "OPENED");
		createValue(row, "comment_days", commentDays, "0");
		createValue(row, "creator_id", creator, "1");
		createValue(row, "uri", uri, "http://www.i2geo.net/ontology.owl#");

		return row;
	}

	private Element createTopic(String id) {

		Element row = createRow();

		row.addContent(createValue("id", id));

		return row;
	}

	private Element createAbstractTopic(String id, String type) {

		Element row = createRow();

		row.addContent(createValue("id", id));
		row.addContent(createValue("type", type));

		return row;
	}
	
	private Element createConcreteTopic(String id) {

		Element row = createRow();

		row.addContent(createValue("id", id));

		return row;
	}

	
	private Element createCompetency(String id,
			String description) {

		Element row = createRow();

		row.addContent(createValue("id", id));

		createValue(row, "description", description, "");

		return row;
	}

	private Element createConcreteCompetency(String id) {

		Element row = createRow();

		row.addContent(createValue("id", id));

		return row;
	}

	private Element createCompetencyProcess(String id) {

		Element row = createRow();

		row.addContent(createValue("id", id));

		return row;
	}
	
	public Element createName(String name, String locale, String type, String isDefName, String uri) {
		Element row = createRow();

		row.addContent(createValue("id", Long.toString(nameCounter)));
		nameCounter++;
		
		if (name.isEmpty()) {
			error.addEmptyName(uri);
		}
		
		String tempLocale = "en";
		
		// check if language is one of the supported locales
		Locale[] locales = Locale.getAvailableLocales();
		if (locale != null && !locale.equals("")) {
			boolean correct = false;
			for (int i = 0; i < locales.length; i++) {
				if (locales[i].getLanguage().equals(locale)) {
					tempLocale=locale;
					correct = true;
					break;
				} 
			}
			if (!correct) {
				error.addFalseLocale(uri);
			}
		} else {
			error.addNoLocale(uri);
		}
		
		createValue(row, "name", name, "default name");
		createValue(row, "locale", tempLocale, "en");
		createValue(row, "type", type, "COMMON");
		createValue(row, "isDefName", isDefName, "0");

		return row;
	}

	public Element createDescription(String description, String locale) {
		Element row = createRow();
		
		row.addContent(createValue("id", Long.toString(descriptionCounter)));
		descriptionCounter++;
		
		createValue(row, "description", description, "default description");
		createValue(row, "locale", locale, "en");

		return row;
	}

	public List<Element> createNames(NamesMap namesMap, String uri) {
		List<Element> names = new ArrayList<Element>();

		for (Iterator<String> it = namesMap.getLanguages(); it.hasNext();) {
			String lang = it.next();
			for (Iterator<NameWithFrequency> i = namesMap.getNames(lang); i
					.hasNext();) {
				NameWithFrequency nf = i.next();
				
				// get db representation for default value
				boolean isDefault = nf.getFrequency()==1;
				String isDefaultValue = (isDefault) ? "1" : "0";
								
				Element e = createName(nf.getName(), lang, null, isDefaultValue, uri);
				names.add(e);
				
				if (nf.getName().equals(checkName)) {
					count++;
					System.out.println("***** " +  count + " " + uri);
				}
				
			}
		}

		return names;
	}

	private List<Element> createRelationCompetencyNames(String id, List<Element> nameElements) {
		List<Element> nameMappings = new ArrayList<Element>();

		for (Element element : nameElements) {
			String nameId = getId(element);
			if (id != null && id != "" && nameId != null && nameId != "") {
				Element row = createRow();

				createValue(row, "competency_id", id, null);
				createValue(row, "names_id", nameId, null);

				nameMappings.add(row);
			}
		}
		return nameMappings;

	}

	private List<Element> createRelationCompetencyTopics(String uri, Set<String> topicURIs) {
		List<Element> relations = new ArrayList<Element>();
		String id = null;
		
		if (ccURI2ID.containsKey(uri.toLowerCase())) {
			id = ccURI2ID.get(uri.toLowerCase());
		} else {
			id = cpURI2ID.get(uri.toLowerCase());
		}

		for (String topicURI : topicURIs) {
			String strippedURI = null;
			
			Element row = createRow();
			if (topicURI.toLowerCase().endsWith("_r")) {
				strippedURI = topicURI.substring(0, topicURI.length()-2);
			} else {
				strippedURI = topicURI;
			}
			
			if (tURI2ID.containsKey(strippedURI.toLowerCase())) {
				String topicId = tURI2ID.get(strippedURI.toLowerCase());

				createValue(row, "competency_id", id, null);
				createValue(row, "topics_id", topicId, null);
			
				relations.add(row);
			} else {
				error.addNoId(topicURI);
			}
		}
		
		return relations;
	}
	
	private List<Element> createRelationTopicNames(String id, List<Element> nameElements) {
		List<Element> nameMappings = new ArrayList<Element>();

		for (Element element : nameElements) {
			String nameId = getId(element);
			if (id != null && id != "" && nameId != null && nameId != "") {
				Element row = createRow();

				createValue(row, "topic_id", id, null);
				createValue(row, "names_id", nameId, null);

				nameMappings.add(row);
			}
		}
		return nameMappings;

	}

	private List<Element> createRelationTopicSubsumtion(String uri, Set<String> parentURIs) {
		List<Element> relations = new ArrayList<Element>();
		return relations;
	}

	/*
	private List<Element> createRelationCompetencySubsumtion(String uri, Set<String> parentURIs) {
		List<Element> relations = new ArrayList<Element>();
		
		String id = cURI2ID.get(uri.toLowerCase());

		for (String parentURI : parentURIs) {

			String parentId = cURI2ID.get(parentURI.toLowerCase());
			if (!cpURI2ID.containsKey(parentURI.toLowerCase())) {
				Element row = createRow();
			
				createValue(row, "subsumedBy_id", parentId, null);
				createValue(row, "subsumes_id", id, null);
			
				relations.add(row);
			}
		}
		
		return relations;
	}
	*/
	
	private List<Element> createRelationProcessToCompetency(String uri, Set<String> parentURIs) {
		List<Element> relations = new ArrayList<Element>();
		
		String id = ccURI2ID.get(uri.toLowerCase());

		for (String parentURI : parentURIs) {

			String parentId = cpURI2ID.get(parentURI.toLowerCase());
			if (cpURI2ID.containsKey(parentURI.toLowerCase())) {
				Element row = createRow();
			
				createValue(row, "cc_id", id, null);
				createValue(row, "cp_id", parentId, null);
			
				relations.add(row);
			}
		}
		
		return relations;
	}

	private List<Element> createRelationAbstractTopicToTopic(String uri, Set<String> parentURIs) {
		List<Element> relations = new ArrayList<Element>();
		
		String id = tiURI2ID.get(uri.toLowerCase());

		for (String parentURI : parentURIs) {

			String parentId = tgURI2ID.get(parentURI.toLowerCase());
			if (tgURI2ID.containsKey(parentURI.toLowerCase())) {
				Element row = createRow();
			
				createValue(row, "ti_id", id, null);
				createValue(row, "tg_id", parentId, null);
			
				relations.add(row);
			}
		}
		
		return relations;
	}

	private List<Element> createRelationCompetencyProcessTaxonomy(String uri, Set<String> parentURIs) {
		List<Element> relations = new ArrayList<Element>();

		String id = cpURI2ID.get(uri.toLowerCase());

		for (String parentURI : parentURIs) {

			Element row = createRow();
			String parentId = cpURI2ID.get(parentURI.toLowerCase());
			
			createValue(row, "child_id", id, null);
			createValue(row, "parent_id", parentId, null);
			
			relations.add(row);
		}

		return relations;
	}

	private List<Element> createRelationAbstractTopicTaxonomy(String uri, Set<String> parentURIs) {
		List<Element> relations = new ArrayList<Element>();

		String id = tgURI2ID.get(uri.toLowerCase());

		for (String parentURI : parentURIs) {

			Element row = createRow();
			String parentId = tgURI2ID.get(parentURI.toLowerCase());
			
			createValue(row, "child_id", id, null);
			createValue(row, "parent_id", parentId, null);
			
			relations.add(row);
		}

		return relations;
	}

	private String getId(Element thing) {
		thing.getChildren();
		for (Iterator<Element> iterator = thing.getChildren().iterator(); iterator
				.hasNext();) {
			Element e = (Element) iterator.next();
			String value = e.getAttributeValue("description");

			if (value != null && "id".equals(value)) {
				return e.getText();
			}
		}
		return null;
	}

	/************************************************************
	 * Post processing
	 ************************************************************/
	
	public void postProcess() {
		
		createRootCompetency();
		
		statistics.addMessage("Number of competency processes:" + cpURI2ID.size());
		statistics.addMessage("Number of concrete competencies:" + ccURI2ID.size());
		statistics.addMessage("Number of representatives:" + reps.size());
		statistics.addMessage("Number of pure abstract topics:" + (tgURI2ID.size() - reps.size()) );
		statistics.addMessage("Number of topic items:" + tiURI2ID.size());
		statistics.addMessage("");
		statistics.addMessage("Name: " + checkName + "is issued " + count + "times.");
		
		linkCC2CP();
		createCPTaxonomy();
		createAbstractTopicTaxonomy();
		linkCT2AT();
		linkC2T();
		handleRepresentatives();
		
		error.report();
		statistics.report();
		
	}

	/**
	 * 
	 */
	private void handleRepresentatives() {
		for (String rep : reps.keySet()) {
			String atUri = rep.substring(0, rep.length()-2);
			String id = tgURI2ID.get(atUri.toLowerCase());
			// System.out.println("REP (POSTPROCESS): " + rep);
			// System.out.println("REP-ID (POSTPROCESS): " + id);
			
			Element e = (Element) entryPoints.get("t_group");
			
			
			for (Object o : e.getChildren("row")) {
			
				Element e_id = null;
				Element e_type = null;
				Element row = (Element) o;

				for (Object o2 : row.getChildren("value")) {
					Element value = (Element) o2;
					if (value.getAttributeValue("description") != null
							&& value.getAttributeValue("description").equals("id")) {
						e_id = value;
					}

					if (value.getAttributeValue("description") != null
							&& value.getAttributeValue("description").equals("type")) {
						e_type = value;
					}
				}
				
				if (e_id != null && e_id.getText().equals(id)) {
					e_type.setText("REPRESENTATIVE");
				}
				
			}
		}
	}
	
	/**
	 * 
	 */
	private void linkC2T() {
		// handle mappings from competency to topics
		tURI2ID = new HashMap<String, String>(tgURI2ID);
		tURI2ID.putAll(tiURI2ID);
		
		for (String uri  : cURI2topics.keySet()) {
			List<Element> topics = createRelationCompetencyTopics(uri, cURI2topics.get(uri));
			((Element) entryPoints.get("c_topics")).addContent(topics);
		}
		
		statistics.addMessage("Number of competencies handled for adding topics: " +
				((Element) entryPoints.get("c_topics")).getChildren().size());
	}

	/**
	 * 
	 */
	private void linkCT2AT() {
		for (String uri  : tiParents.keySet()) {
			List<Element> subsumptions = createRelationAbstractTopicToTopic(uri, tiParents.get(uri));
			((Element) entryPoints.get("tis_tg")).addContent(subsumptions);
		}
		
		statistics.addMessage("Number of individuals added to topic groups: " +
				((Element) entryPoints.get("tis_tg")).getChildren().size());
	}

	/**
	 * 
	 */
	private void createAbstractTopicTaxonomy() {
		for (String uri  : tgTaxonomy.keySet()) {
			if (!uri.toLowerCase().endsWith("#topic")) {
				List<Element> taxonomy = createRelationAbstractTopicTaxonomy(uri, tgTaxonomy.get(uri));
				((Element) entryPoints.get("tg_subgroups")).addContent(taxonomy);
			}
		}
		
		statistics.addMessage("Number of topic groups handled for subgrouping: " +
				((Element) entryPoints.get("tg_subgroups")).getChildren().size());
	}

	/**
	 * 
	 */
	private void createCPTaxonomy() {
		for (String uri  : cpTaxonomy.keySet()) {
			if (!uri.toLowerCase().endsWith("#competency")) {
				List<Element> taxonomy = createRelationCompetencyProcessTaxonomy(uri, cpTaxonomy.get(uri));
				((Element) entryPoints.get("cp_subprocesses")).addContent(taxonomy);
			}
		}
		
		statistics.addMessage("Number of competency processes handled for subclassing: " +
				((Element) entryPoints.get("cp_subprocesses")).getChildren().size());
	}

	/**
	 * 
	 */
	private void linkCC2CP() {
		for (String uri  : ccParents.keySet()) {
			List<Element> subsumptions = createRelationProcessToCompetency(uri, ccParents.get(uri));
			((Element) entryPoints.get("ccs_cp")).addContent(subsumptions);
		}
		statistics.addMessage("Number of individuals added to competency classes: " +
				((Element) entryPoints.get("ccs_cp")).getChildren().size());
	}

	/**
	 * Creates a root competency if it doesn't exist yet.
	 */
	private void createRootCompetency() {
		String rootUri = "http://www.inter2geo.eu/2008/ontology/GeoSkills#Competency";

		if (!ccURI2ID.containsKey(rootUri.toLowerCase()) && 
				!cpURI2ID.containsKey(rootUri.toLowerCase())) { 
			
			System.out.println("Creating root competency. Necessary.");
			
			// add root competency class
			Element thing       = createThing(null, null, null, null, null, null, rootUri);
			Element competency  = createCompetency(getId(thing), null);
			Element pcompetency = createCompetencyProcess(getId(thing));

			Element name = createName("Competency", "en", "COMMON", "1", rootUri);
			List<Element> nameElements = new ArrayList<Element>();
			nameElements.add(name);
			
			List<Element> nameMappings = createRelationCompetencyNames(
					getId(thing), nameElements);

			cpURI2ID.put(rootUri.toLowerCase(), getId(thing));
			// cURI2ID.put(rootUri.toLowerCase(), getId(thing));

			((Element) entryPoints.get("thing")).addContent(thing);
			((Element) entryPoints.get("competency")).addContent(competency);
			((Element) entryPoints.get("c_process")).addContent(pcompetency);
			((Element) entryPoints.get("name")).addContent(nameElements);
			((Element) entryPoints.get("c_names")).addContent(nameMappings);

			System.out.println("Rood node id is: " + getId(thing));
		} else {
			System.out.println("No need to create a competency root node. ");
		}
	}

	
	/************************************************************
	 * Interface methods
	 ************************************************************/

	public void thereIsItem(String uri, NodeType type) {
		//System.out.println("    Item uri: " + uri);		
	}

	public void topicType(String uri, Set<String> parentTypeURIs, NamesMap namesMap) {
		// System.out.println("    Topic Type uri: " + uri);
		// System.out.println("       Topic Type uri parents: " + parentTypeURIs);
		
		if (excludes.contains(uri)) { return; }
		
		Element thing       = createThing(null, null, null, null, null, null, uri);
		Element topic       = createTopic(getId(thing));
		Element tGroup      = createAbstractTopic(getId(thing), "PURE");
		
		// store all topic types only
		if (tgURI2ID.containsKey(uri.toLowerCase())) {
			error.addDuplicates(uri);
		} else {
			tgURI2ID.put(uri.toLowerCase(), getId(thing));
		}
		
		if (parentTypeURIs != null && !parentTypeURIs.isEmpty()) {
			tgParents.put(uri.toLowerCase(), parentTypeURIs);
		}
		
		// caring only about sub classing
		if (parentTypeURIs != null && !parentTypeURIs.isEmpty()) {
			tgTaxonomy.put(uri.toLowerCase(), parentTypeURIs);
		}

		List<Element> nameElements = createNames(namesMap, uri);

		List<Element> nameMappings = createRelationTopicNames(getId(thing),
				nameElements);
	
		((Element) entryPoints.get("thing")).addContent(thing);
		((Element) entryPoints.get("topic")).addContent(topic);
		((Element) entryPoints.get("t_group")).addContent(tGroup);
		((Element) entryPoints.get("name")).addContent(nameElements);
		((Element) entryPoints.get("topic_names")).addContent(nameMappings);

	}

	public void topicDescription(String uri, Set<String> parentTypeURIs,
			NamesMap namesMap, ModificationData md) {
		
		// System.out.println("    Topic representative uri: " + uri);
		// System.out.println("       Topic Type uri parents: " + parentTypeURIs);

		if (excludes.contains(uri)) { return; }

		if (!uri.endsWith("_r")) {
			String created  = calculateDate(md.getCreationDate());
			String modified = calculateDate(md.getModificationDate());
			String user     = md.getCreationUserName();

			Element thing       = createThing(user, created, modified, null, null, null, uri);
			Element topic = createTopic(getId(thing));
			Element tItem = createConcreteTopic(getId(thing));

			if (tiURI2ID.containsKey(uri.toLowerCase())) {
				error.addDuplicates(uri);
			} else {
				tiURI2ID.put(uri.toLowerCase(), getId(thing));
			}

			if (!parentTypeURIs.isEmpty())  {
				//tURI2subsumes.put(uri.toLowerCase(), parentTypeURIs);
				tiParents.put(uri.toLowerCase(), parentTypeURIs);
			}
		
			
			List<Element> nameElements = createNames(namesMap, uri);

			List<Element> nameMappings = createRelationTopicNames(getId(thing),
					nameElements);

			((Element) entryPoints.get("thing")).addContent(thing);
			((Element) entryPoints.get("topic")).addContent(topic);
			((Element) entryPoints.get("t_item")).addContent(tItem);
			((Element) entryPoints.get("name")).addContent(nameElements);
			((Element) entryPoints.get("topic_names")).addContent(nameMappings);
		} else {			
			
			// do only when you know that Topic Types have been already processed
			// System.out.println("REPRESENTATIVE");
			// System.out.println("REP: " + uri);
			for (String pUri : parentTypeURIs) {
				// System.out.println("  parent: " + pUri );
				
				String pID = tgURI2ID.get(uri.substring(0, uri.length()-2).toLowerCase());
				List<Element> nameElements = createNames(namesMap, pUri);

				List<Element> nameMappings = 
					createRelationTopicNames(pID, nameElements);

				((Element) entryPoints.get("name")).addContent(nameElements);
				((Element) entryPoints.get("topic_names")).addContent(nameMappings);
			}
			reps.put(uri, namesMap);
		}
	}

	public void competencyType(String uri, Set<String> parentTypeURIs, NamesMap namesMap) {

		if (excludes.contains(uri)) { return; }

		Element thing       = createThing(null, null, null, null, null, null, uri);
		Element competency  = createCompetency(getId(thing), null);
		Element pcompetency = createCompetencyProcess(getId(thing));
		
		// store all competency types only
		if (cpURI2ID.containsKey(uri.toLowerCase())) {
			error.addDuplicates(uri);
		} else {
			cpURI2ID.put(uri.toLowerCase(), getId(thing));
		}
		
		if (parentTypeURIs != null && !parentTypeURIs.isEmpty()) {
			cpParents.put(uri.toLowerCase(), parentTypeURIs);
		}
		
		// caring only about sub classing
		if (parentTypeURIs != null && !parentTypeURIs.isEmpty()) {
			cpTaxonomy.put(uri.toLowerCase(), parentTypeURIs);
		}

		List<Element> nameElements = createNames(namesMap, uri);

		List<Element> nameMappings = createRelationCompetencyNames(
				getId(thing), nameElements);

		((Element) entryPoints.get("thing")).addContent(thing);
		((Element) entryPoints.get("competency")).addContent(competency);
		((Element) entryPoints.get("c_process")).addContent(pcompetency);
		((Element) entryPoints.get("name")).addContent(nameElements);
		((Element) entryPoints.get("c_names")).addContent(nameMappings);

	}

	
	public void competencyDescription(String uri, Set<String> parentTypeURIs,
			Set<String> topicURIs, NamesMap namesMap, ModificationData md) {

		if (excludes.contains(uri)) { return; }
		
		String created  = calculateDate(md.getCreationDate());
		String modified = calculateDate(md.getModificationDate());
		String user     = md.getCreationUserName();

		
		Element thing       = createThing(user, created, modified, null, null, null, uri);
		Element competency  = createCompetency(getId(thing), null);
		Element ccompetency = createConcreteCompetency(getId(thing));


		if (ccURI2ID.containsKey(uri.toLowerCase())) {
			error.addDuplicates(uri);
		} else {
			ccURI2ID.put(uri.toLowerCase(), getId(thing));
		}

		if (parentTypeURIs != null && !parentTypeURIs.isEmpty()) {
			ccParents.put(uri.toLowerCase(), parentTypeURIs);
		}

		if (topicURIs != null && !topicURIs.isEmpty()) {
			cURI2topics.put(uri.toLowerCase(), topicURIs);
		}
		
		List<Element> nameElements = createNames(namesMap, uri);

		List<Element> nameMappings = createRelationCompetencyNames(
				getId(thing), nameElements);

		
		((Element) entryPoints.get("thing")).addContent(thing);
		((Element) entryPoints.get("competency")).addContent(competency);
		((Element) entryPoints.get("c_concrete")).addContent(ccompetency);
		((Element) entryPoints.get("name")).addContent(nameElements);
		((Element) entryPoints.get("c_names")).addContent(nameMappings);
	}

}
