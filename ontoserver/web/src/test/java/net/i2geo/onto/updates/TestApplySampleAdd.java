package net.i2geo.onto.updates;

import junit.framework.*;

import java.io.*;
import java.net.URI;
import java.net.URL;

import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.io.WriterOutputTarget;
import net.i2geo.onto.updates.OntoUpdateChange;

/**
 */
public class TestApplySampleAdd extends TestCase {

    public TestApplySampleAdd(String name) {
        super(name);
    }

    public void setUp() throws Exception {

    }

    public void testApplySampleAdd() throws Exception {
    	/*
    	// get new OWL Manager
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        
        // The file containing the XML-Update Request
        URL updateURL = getClass().getResource("/net/i2geo/onto/updates/sample-onto-update-add.xml");
        if(updateURL==null) updateURL = getClass().getResource("sample-onto-update-add.xml");
        if(updateURL==null) throw new IllegalStateException("Resource sample-onto-update-add.xml not found.");

        // create a new ChangeAction class
        OntoUpdateChange change = new OntoUpdateChange(
                updateURL, manager);
        
        // The ontology file
        File owlFile = new File("/home/mhomik/GeoSkills.owl");
        
        // load the ontology
        OWLOntology ont = manager.loadOntologyFromPhysicalURI(URI.create(owlFile.toURL().toExternalForm()));
        
        // perform change in ontology
        change.operate();
        
        // Write changed ontology
        Writer out = new OutputStreamWriter(new FileOutputStream(owlFile),"utf-8");
        manager.saveOntology(ont,new WriterOutputTarget(out));
        out.flush(); out.close();
        
        // should now test the effect of the addition
		*/
    }
}
