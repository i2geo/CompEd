/**
 * 
 */
package net.i2geo.onto.updates.actions;

import java.io.*;
import java.net.*;

import net.i2geo.onto.GeoSkillsAccess;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;
import org.jdom.Namespace;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.io.WriterOutputTarget;
import org.semanticweb.owl.io.OWLOntologyInputSource;
import org.semanticweb.owl.model.*;

/**
 * @author afaulhab
 *
 */
public class OntologyHelper {

    /**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());

	private final String owlFileName = "data/GeoSkills.owl";
    private static OWLOntologyManager manager = null;
    private static OWLOntology ontology = null;
    private static OWLDataFactory factory = null;

    private OntologyHelper() {
        // The ontology file
        final File owlFile = new File(owlFileName);

        log.debug("Ontology is at: " + owlFile.toURI());
        
        manager = OWLManager.createOWLOntologyManager();

        // load the ontology
        try {

            final URI uri = owlFile.toURI();

            OWLOntologyInputSource source = new OWLOntologyInputSource() {
                public boolean isReaderAvailable() {
                    return true;
                }

                public Reader getReader() {
                    try {
                        return new InputStreamReader(new FileInputStream(owlFile),"utf-8");
                    } catch(IOException ex) {
                        throw new IllegalStateException("Can't open GeoSkills URL.",ex);
                    }
                }

                public boolean isInputStreamAvailable() {
                    return false;
                }

                public InputStream getInputStream() {
                    return null;
                }

                public URI getPhysicalURI() {
                    return uri;
                }
            };
            ontology = manager.loadOntology(source);
        }
        catch (Exception ex) {
            System.err.println("Fatal error: cannot load Ontology");
        }

        factory = manager.getOWLDataFactory();
    }

    private static OntologyHelper instance = new OntologyHelper();

    public static OntologyHelper getInstance()
    {
        return instance;
    }

    public OWLOntologyManager getOWLManager()
        throws OWLOntologyCreationException, MalformedURLException
    {
        return manager;
    }

    public OWLOntology getOWLOntology()
    {
        return ontology;
    }

    public OWLConstant getI18nTextConstant(Element elt)
    {
        String langVal = elt.getAttributeValue("lang",
                Namespace.XML_NAMESPACE);
        
        OWLConstant constant = factory.getOWLUntypedConstant(elt.getText(), langVal);
        return constant;
    }

    private static String base = GeoSkillsAccess.ontBaseU + "#";

    public String getBase()
    {
        return base;
    }

    /**
     * @return the factory
     */
    public OWLDataFactory getFactory()
    {
        return factory;
    }

    /**
     * @return the manager
     */
    public OWLOntologyManager getManager()
    {
        return manager;
    }

    /**
     * Write changed ontology
     */
    public void writeOntology()
    {
        try {
            Writer out = new OutputStreamWriter(new FileOutputStream(owlFileName), "utf-8");

            manager.saveOntology(ontology, new WriterOutputTarget(out));
            
            out.flush();
            out.close();
        }
        catch (Exception ex) {
            System.err.println("Fatal error: Cannot write Ontology");
            ex.printStackTrace();
        }
    }

}
