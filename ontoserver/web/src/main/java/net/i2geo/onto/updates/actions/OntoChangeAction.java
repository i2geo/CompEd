/**
 * 
 */
package net.i2geo.onto.updates.actions;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.semanticweb.owl.model.AddAxiom;
import org.semanticweb.owl.model.OWLAnnotationAxiom;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLConstant;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLDataPropertyExpression;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLObjectPropertyExpression;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyChange;
import org.semanticweb.owl.model.OWLOntologyChangeException;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.model.RemoveAxiom;

/**
 * @author Arndt Faulhaber
 * @author Martin Homik
 *
 */
public class OntoChangeAction extends OntologyActionBase {

    private static final Namespace rdfNS = Namespace.getNamespace(
    		"rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");

	/* (non-Javadoc)
     * @see net.i2geo.onto.updates.actions.OntologyActionBase#perform()
     */
    @SuppressWarnings("unchecked")
	public void perform() throws OWLOntologyChangeException {
    	log.debug("Performing ontology update for: " + getElementId());
    	
    	// helper instance
        OntologyHelper     helper  = OntologyHelper.getInstance();
        OWLDataFactory     factory = helper.getFactory();
        OWLOntologyManager manager = helper.getManager();

        OWLIndividual individual = factory.getOWLIndividual(
        		URI.create(helper.getBase() + this.getElementId()));


        // get the ontology
        OWLOntology ont;
        try {
            ont = helper.getOWLOntology();
        } catch (Exception ex) {
        	throw new OWLOntologyChangeException(null, ex){
				private static final long serialVersionUID = -5568393047392754030L;};
        }

        // ===================================== create remove axiom =========
        log.debug("Collecting deletion axioms.");

        List<OWLOntologyChange> deletionAxioms = new ArrayList<OWLOntologyChange>();
        
        /*
        OWLClass oldOwlClass = factory.getOWLClass(
        		URI.create(helper.getBase() + this.getElementClass()));

        OWLAxiom classAxiom = factory.getOWLClassAssertionAxiom(individual, oldOwlClass); 
        deletionAxioms.add(new RemoveAxiom(ont, classAxiom));
		*/

        // delete all annotations
        Set<OWLAnnotationAxiom> annotations = individual.getAnnotationAxioms(ont);
        
        for (OWLAnnotationAxiom annotAxiom : annotations) {
            deletionAxioms.add(new RemoveAxiom(ont, annotAxiom));
        }
        
        // ===================================== delete all properties =========
        Map<OWLDataPropertyExpression, Set<OWLConstant>> props
            = individual.getDataPropertyValues(ont);
        for (OWLDataPropertyExpression propExpr : props.keySet()) {
            for (OWLConstant propConst : props.get(propExpr)) {
                deletionAxioms.add(new RemoveAxiom(ont, 
                    factory.getOWLDataPropertyAssertionAxiom(
                            individual, propExpr, propConst) ));
            }
        }
        
        
        Map<OWLObjectPropertyExpression, Set<OWLIndividual>> objectProps
        = individual.getObjectPropertyValues(ont);

        for (OWLObjectPropertyExpression propExpr : objectProps.keySet()) {
        	for (OWLIndividual i : objectProps.get(propExpr)) {
        		deletionAxioms.add(new RemoveAxiom(ont, 
        				factory.getOWLObjectPropertyAssertionAxiom(
        						individual, propExpr, i) ));
        	}
        }
        
        log.debug("Applying deletion axioms.");
        manager.applyChanges(deletionAxioms);
        
        // ===================================== add new properties =========

        log.debug("Collecting update properties.");

        List<OWLAxiom> additionAxioms = new ArrayList<OWLAxiom>();

        /*
        OWLIndividual newIndividual = factory.getOWLIndividual(
        		URI.create(helper.getBase() + this.getElementId()));
        OWLClass owlClass = factory.getOWLClass( // ???needed???
                URI.create(helper.getBase() + this.getElementClass())
                ); 
		
        		

        
        OWLClassAssertionAxiom newClassAxiom =
            factory.getOWLClassAssertionAxiom(newIndividual, owlClass);
        
        additionAxioms.add(newClassAxiom);
        */
        
        for (Element subElement : this.getSubelements()) {
        	log.debug("Checking sub element: " + subElement);
        	
            OWLAxiom axiom = null;
            
            // get the name of the property
            OWLDataPropertyExpression property = factory.getOWLDataProperty(
           		URI.create(helper.getBase() + subElement.getName()));

            OWLConstant object;
            
            // try to get a property of type "resource", "id", and "about"
            Attribute ref = subElement.getAttribute("resource", rdfNS);
            if (ref == null) {subElement.getAttribute("ID", rdfNS);}
            if (ref == null) {subElement.getAttribute("about", rdfNS);}
            
            // test if the property is a reference
            boolean isRef = (ref != null);

            if (isRef) {
            	log.debug("... subElement is a reference.\n ... " + ref);
            	try {
                	// relation can be rdfs:subClassOf
                    OWLObjectProperty prop = factory.getOWLObjectProperty(
                    		URI.create(helper.getBase() + subElement.getName()));
                    // the item to which the relation points to
                    OWLIndividual obj = factory.getOWLIndividual(
                    		URI.create(helper.getBase() + ref.getValue()));

                    axiom = factory.getOWLObjectPropertyAssertionAxiom(individual, prop, obj);

                    if (axiom != null) {
                		log.debug("... adding axiom ..." + axiom);
                		additionAxioms.add(axiom);
                	}
                    
                    log.debug("... created axiom for subElement by factory: " + axiom);
                    //TODO: Make this be an Attribute...
                } catch (Exception ex) {
                	log.error("Cannot interpret reference for subElement..." + subElement 
                    		+ "\n" + ex);
                }
            } else {

            	// Check for internationalization
            	if (subElement.getAttribute("lang", Namespace.XML_NAMESPACE) != null) {
            		// get the internationalized text (value)
            		object = helper.getI18nTextConstant(subElement);
            	} else {
            		// get the text (value)
            		object = factory.getOWLUntypedConstant(subElement.getValue());
            	}

            	axiom = factory.getOWLDataPropertyAssertionAxiom(individual, property, object);
            	
            	if (axiom != null) {
            		log.debug("... adding axiom ..." + axiom);
            		additionAxioms.add(axiom);
            	}
            }
        }

        //**********************************
        // apply add actions to the ontology
        //**********************************

        log.debug("Applying addition axioms.");

        
        for (OWLAxiom axiom : additionAxioms) {
            manager.applyChange(new AddAxiom(ont, axiom));
        }
    }

}
