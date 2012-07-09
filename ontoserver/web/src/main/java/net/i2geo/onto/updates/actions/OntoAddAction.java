/**
 * This ontology action class handles additions to the ontology.
 * @author Arndt Faulhaber
 * @author Martin Homik
 * 
 */
package net.i2geo.onto.updates.actions;

import java.net.*;
import java.util.*;

import org.jdom.*;
import org.semanticweb.owl.model.*;

/**
 * @author Arndt Faulhaber
 * @author Martin Homik
 *
 */
public class OntoAddAction extends OntologyActionBase
{

    private static final Namespace rdfNS = Namespace.getNamespace(
    		"rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");

    /* (non-Javadoc)
     * @see net.i2geo.onto.updates.actions.OntologyActionBase#perform()
     * 
     */
    public void perform() throws OWLOntologyChangeException {
        // helper instance
        OntologyHelper helper = OntologyHelper.getInstance();
        OWLOntologyManager manager = helper.getManager();
        OWLDataFactory factory = helper.getFactory();
        
        // axioms are the triples: subject, predicate, object
        List<OWLAxiom> additionAxioms = new ArrayList<OWLAxiom>();
                
        URI owlElementIdUri = URI.create(helper.getBase() + getElementId());
        URI owlElementClassUri = URI.create(getElement().getNamespaceURI() + getElement().getName());

        log.debug("OWL Element ID URI: " + owlElementIdUri);
        log.debug("OWL Element Class URI: " + owlElementClassUri);
        
        // get the individual if it exists
        OWLIndividual individual = factory.getOWLIndividual(owlElementIdUri);
        // get the class of the individual
        
        OWLClass owlClass = factory.getOWLClass(owlElementClassUri); // ??? needed ???

        // create class axion from individual identifier and class name 
        OWLClassAssertionAxiom classAxiom = factory.getOWLClassAssertionAxiom(individual, owlClass);

        log.debug("ADD AXIOM (Top): " + classAxiom);
        
        // add class axiom to the list which will be passed to the manager at the end
        additionAxioms.add(classAxiom);

        // create for each property an axiom
        for (Element subElement : this.getSubelements()) {
            OWLAxiom axiom = null;

            // get the name of the property
            URI subElementUri = URI.create(subElement.getNamespaceURI() + subElement.getName()); 
            OWLDataPropertyExpression property = factory.getOWLDataProperty(subElementUri);
            
            // if property is a not a reference then the data will be set here
            OWLConstant object = null;
            
            // try to get a property of type "resource", "id", and "about"
            Attribute ref = subElement.getAttribute("resource", rdfNS);
            if (ref == null) {subElement.getAttribute("ID", rdfNS);}
            if (ref == null) {subElement.getAttribute("about", rdfNS);}
            
            // test if the property is a reference
            boolean isRef = (ref != null);

            if (isRef) {
                try {
                	// relation can be rdfs:subClassOf
                    OWLObjectProperty prop = factory.getOWLObjectProperty(subElementUri);
                    // the item to which the relation points to
                    URI refElementUri = URI.create(helper.getBase() + ref.getValue());
                    OWLIndividual obj = factory.getOWLIndividual(refElementUri);

                    axiom = factory.getOWLObjectPropertyAssertionAxiom(individual, prop, obj);
                    //TODO: Make this be an Attribute...
                }
                catch (Exception ex) {
                    System.err.println("Cannot interpret reference...");
                }
            } else {
                // Check for internationalization if is a string
                if (subElement.getAttribute("lang", Namespace.XML_NAMESPACE) != null) {
                    // get the internationalized text (value)
                    object = helper.getI18nTextConstant(subElement);
                }
                else {
                    // get the text (value)
                    object = factory.getOWLUntypedConstant(subElement.getValue());
                }
                axiom = factory.getOWLDataPropertyAssertionAxiom(individual, property, object);
            }

            if (axiom != null)
                additionAxioms.add(axiom);
        }

        //**********************************
        // apply add actions to the ontology
        //**********************************

        // get the ontology
        OWLOntology ont = manager.getOntologies().iterator().next();

        for (OWLAxiom axiom : additionAxioms) {
            manager.applyChange(new AddAxiom(ont, axiom));
        }
    }

}
