package net.i2geo.comped.parser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import net.i2geo.comped.parser.GeoSkillsParserSample;
import net.i2geo.onto.tasks.GeoSkillsParser;

import junit.framework.TestCase;

public class GeoSkillsParserSampleTest extends TestCase {

    /**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());

	GeoSkillsParserSample parser = null;
	
    public void setUp() throws Exception {
		parser = new GeoSkillsParserSample();
	}
	
    public void tearDown() throws Exception {
        parser = null;
    }
    
    /*
	public void testCreateTable() throws Exception {
		Element e = parser.createTable("testTable");
		
		System.out.println("table element: " + e);
		
		assertNotNull(e.getAttributeValue("name"));
		assertEquals("testTable", e.getAttributeValue("name"));
	}

	public void testCreateColumn() throws Exception {
		Element e = parser.createColumn("testColumn");
		
		System.out.println("column element: " + e);
		
		assertNotNull(e);
		assertEquals("column", e.getName());
		assertNotNull(e.getText());
		assertEquals("testColumn", e.getText());
	}
	*/
    
		
	private void printToFile(String fileName, Document doc) {
		File file = new File(fileName);
		
		if (file != null & file.exists()) {
			file.delete();
		}

		try {
			FileOutputStream out = new FileOutputStream(file);
			XMLOutputter serializer = new XMLOutputter(Format.getPrettyFormat());
			serializer.output(doc, out);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void prettyOutput(Document doc) {
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		try {
			outputter.output(doc, System.out);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testRunningParser() throws Exception {
        // Level level = Logger.getLogger("org.mindswap.pellet.ABox").getLevel();
        Logger.getLogger("org.mindswap.pellet.ABox").setLevel(Level.WARNING);
        
        new GeoSkillsParser().runParser(parser);	
		parser.postProcess();
		Document doc = parser.getDocument();
		
		printToFile("sample-data.xml", doc);
	}
}
