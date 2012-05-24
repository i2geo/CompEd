package net.i2geo.comped.plugin;

/*
 * Thing.java
 *
 * Created on 13. August 2008, 09:29
 *
 * Creator: Martin Homik
 */

import java.io.FileOutputStream;
import java.io.IOException;

import java.io.File;
import java.net.URL;

import net.i2geo.comped.parser.GeoSkillsParserSample;
import net.i2geo.onto.tasks.GeoSkillsParser;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Goal which prints the projects build directory.
 *      at phase process-sources
 *
 * @goal gs2comped
 * 
 */
public class GeoSkills2SampleMojo extends AbstractMojo {

    // private static final String ontBaseU = "http://www.inter2geo.eu/2008/ontology/GeoSkills";
    // private static final String geoSkillsDevUrl= "http://i2geo.net/ontologies/dev/GeoSkills.owl";

    /**
     * Location of the source template.
     * @parameter
     * @required
     */
    private File srcTemplate;

    /**
     * Location of the destination file.
     * @parameter
     * @required
     */
    private File destFile;

    /**
     * Location of the ontology.
     * @parameter expression="http://i2geo.net/ontologies/dev/GeoSkills.owl"
     */
    private URL ontologyURL;

    public void execute() throws MojoExecutionException {
		getLog().info("Reading in template file from: \n       " + srcTemplate);
		getLog().info("Writing in destination file to: \n       " + destFile);
		
		GeoSkillsParserSample listener = null;
		
		// take template and extend by ontology items
		if (srcTemplate.exists()) {
			try {
				SAXBuilder builder = new SAXBuilder();
				Document doc = builder.build(srcTemplate);
				listener = new GeoSkillsParserSample(doc);

			} catch (Exception e) {
				getLog().info("Cannot create destination file based on source template. Creating plain document.");
				listener = new GeoSkillsParserSample();			
			}
		} else {
			// create document without prepared template
			listener = new GeoSkillsParserSample();			
		}
       	
		try {
			
			getLog().info("Reading ontology from: \n       " + ontologyURL.toString());
			
			GeoSkillsParser gsParser = new GeoSkillsParser();
			gsParser.setUrlToOntology(ontologyURL.toString());
		
			gsParser.runParser(listener);
			listener.postProcess();
			
			Document destDoc = listener.getDocument();
			printToFile(destFile, destDoc);			
			
		} catch (Exception e) {
			getLog().error("... Error while populating database", e);
		}

		getLog().info("... population successful");
	}

	private void printToFile(File file, Document doc) {
		
		if (file != null & file.exists()) {
			file.delete();
		}

		try {
			FileOutputStream out = new java.io.FileOutputStream(file);
			XMLOutputter serializer = new XMLOutputter(Format.getPrettyFormat());
			serializer.output(doc, out);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    
}
