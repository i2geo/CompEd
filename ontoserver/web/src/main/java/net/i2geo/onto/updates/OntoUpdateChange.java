package net.i2geo.onto.updates;

import org.semanticweb.owl.model.*;
import org.jdom.input.SAXBuilder;
import org.jdom.*;

import java.net.URL;
import java.net.URI;
import java.util.*;

import net.i2geo.onto.GeoSkillsAccess;
import net.i2geo.onto.updates.actions.OntologyActionBase;

/** Object to describe the update.
 */
public class OntoUpdateChange extends OntologyActionBase {

    private static final Namespace ontoUpdateNS = Namespace.getNamespace("http://www.inter2geo.eu/2008/ontology/ontoUpdates.owl#"),
        rdfNS = Namespace.getNamespace("rdf","http://www.w3.org/1999/02/22-rdf-syntax-ns#");
    private static String base = GeoSkillsAccess.ontBaseU + "#";
    private List<OWLOntologyChange> owlChange;
    private URL xmlChangeUrl;
    private OWLOntologyManager manager;
    private OWLDataFactory factory;

    private List<OWLAxiom> additionAxioms, deletionsAxioms, updatesAxioms;


    public OntoUpdateChange(List<OWLOntologyChange> owlChange, OWLOntologyManager manager) {
        this.owlChange = owlChange;
        this.xmlChangeUrl = null;
        this.manager = manager;
        this.factory = manager.getOWLDataFactory();
    }

    public OntoUpdateChange(URL xmlChangeUrl, OWLOntologyManager manager) {
        this.xmlChangeUrl = xmlChangeUrl;
        this.owlChange = null;
        this.manager = manager;
        this.factory = manager.getOWLDataFactory();
    }


//    public void operate(OWLOntology ont) throws OWLOntologyChangeException {
    public void operate() throws OWLOntologyChangeException {
        
        // check for existing ontologies in the manager
        if (manager.getOntologies().isEmpty())
            throw new OntoUpdateException("No Ontology given!", new Exception());
        
        // get the ontology contained in the manager
        OWLOntology ont = manager.getOntologies().iterator().next();
        
        if(additionAxioms==null)
            parseOWLchange();
        for(OWLAxiom axiom:additionAxioms) {
            manager.applyChange(new AddAxiom(ont,axiom));
        }
    }

    private void parseOWLchange() {

        try {
            // make it jdom
            Document changeDoc = new SAXBuilder().build(xmlChangeUrl);
            this.parseOWLchange(changeDoc);
            
        } catch (Exception e) {
            throw new OntoUpdateException("can't parse onto-update",e);
        }
    }
    
    private void parseOWLchange(String XMLstring) {
        try {
            // make it jdom
            Document changeDoc = new SAXBuilder().build(XMLstring);
            this.parseOWLchange(changeDoc);
            
        } catch (Exception e) {
            throw new OntoUpdateException("can't parse onto-update",e);
        }
    }
    
    private void parseOWLchange(Document changeDoc) {

        try {
            List additionsElements =
                changeDoc.getRootElement().getChild("Additions",ontoUpdateNS).getChildren();
            additionAxioms = new ArrayList<OWLAxiom>(10*additionsElements.size());
            for(Object e:additionsElements) {
                Element elt = (Element) e;
                String id = elt.getAttributeValue("ID",rdfNS);
                OWLIndividual i = factory.getOWLIndividual(URI.create(base + id));

                // TODO: treat null-cases
                //OWLIndividual individual = new OWLIndividual(id);
                for(Object f : elt.getChildren()) {
                    Element child = (Element) f;
                    OWLAxiom axiom = convertChangeToAxiomAssertion(i,child);
                    additionAxioms.add(axiom);
                }
            }
        } catch (Exception e) {
            throw new OntoUpdateException("can't parse onto-update",e);
        }

    }

    private OWLAxiom convertChangeToAxiomAssertion(OWLIndividual subject, Element elt) {
        if(elt.getAttribute("lang", Namespace.XML_NAMESPACE)!=null) {
            return factory.getOWLDataPropertyAssertionAxiom(subject,
                    factory.getOWLDataProperty(URI.create(base+elt.getName())),
                    getI18nTextConstant(elt));
        }
        return null;
    }

    private OWLConstant getI18nTextConstant(Element elt) {
        OWLConstant constant = factory.getOWLUntypedConstant(
                elt.getAttributeValue("lang", Namespace.XML_NAMESPACE),
                elt.getText()
        );
        return constant;
    }


    public String getActionId()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getInitiatorID()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public URL getResultNotificationURL()
    {
        // TODO Auto-generated method stub
        return null;
    }
    

    public void perform() throws OWLOntologyChangeException
    {
        operate();
    }
}
