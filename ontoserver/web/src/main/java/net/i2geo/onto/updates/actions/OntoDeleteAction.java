/**
 * This ontology action class handles deletions to the ontology.
 * @author Arndt Faulhaber
 * @author Martin Homik
 * 
 */
package net.i2geo.onto.updates.actions;

import java.net.*;
import java.util.*;

import org.jdom.*;
import org.semanticweb.owl.model.*;
import org.semanticweb.owl.util.OWLEntityRemover;

/**
 * @author Arndt Faulhaber
 * @author Martin Homik
 *
 */
public class OntoDeleteAction
    extends OntologyActionBase
{
    /* (non-Javadoc)
     * @see net.i2geo.onto.updates.actions.OntologyActionBase#perform()
     */
    public void perform() throws OWLOntologyChangeException {
        // helper instance
        OntologyHelper helper      = OntologyHelper.getInstance();
        OWLDataFactory factory     = helper.getFactory();
        OWLOntologyManager manager = helper.getManager();

        // For some reason, an entity can be both, an individual and a class.
        // This is the case for competency processes. So, we need both.
        OWLIndividual individual   = factory.getOWLIndividual(
        		URI.create(helper.getBase() + this.getElementId()));
        OWLClass owlClass = factory.getOWLClass(
        		URI.create(helper.getBase() + this.getElementId()));
                
        List<OWLOntologyChange> deletionAxioms = new ArrayList<OWLOntologyChange>();

        // see whether a property should be deleted or an individual
        List<Element> subElements = this.getSubelements();

        // get the ontology
        OWLOntology ont;
        try {
            ont = helper.getOWLOntology();
        } catch (Exception ex) {
            throw new OWLOntologyChangeException(null, ex) {
				private static final long serialVersionUID = 6130861776560095334L;};
        }

        OWLEntityRemover remover = new OWLEntityRemover(manager, Collections.singleton(ont));

        if (remover == null) {
            log.error("Fatal error: no remover for Ontology...");
            return;
        }

        boolean entityDelete = (subElements.size() == 0);

        log.debug("*** Deleting entity? " + entityDelete);
        
        if (entityDelete) {
        	log.info("Deleting entity: " + individual);
        	
            // just delete the individual
            individual.accept(remover);
            // because it could be a class, add a remove on the class as well
            owlClass.accept(remover);
            
            List<OWLOntologyChange> changes = remover.getChanges();
            log.debug(manager.applyChanges(changes));
        } else {
            // delete one or more properties from individual
        	log.info("Deleting property from: " + individual);
        	
            for (Element subElement : subElements) {
                OWLAxiom axiom = null;

                // The OWL property of the individual 
                OWLDataProperty property = factory.getOWLDataProperty(
                		URI.create(helper.getBase() + subElement.getName()));
                
                // The value of the property
                // Check for internationalization
                OWLConstant value;
                if (subElement.getAttribute("lang", Namespace.XML_NAMESPACE) != null) {
                    // get the internationalized text (value)
                    value = helper.getI18nTextConstant(subElement);
                } else {
                    // get the text (value)
                    value = factory.getOWLUntypedConstant(subElement.getValue());
                }
                
                axiom = factory.getOWLDataPropertyAssertionAxiom(
                		individual, // The individual
                		property,   // name of property
                        value);     // The value of the property

                if (axiom != null) {
                    // FIXME: this simply does not work (at least in my tests)
                    // no idea why, and no idea how to find out... I'll check the
                    // mailing list...
                    deletionAxioms.add(new RemoveAxiom(ont, axiom));
                }
            }
        }

        List<OWLOntologyChange> changesDone = manager.applyChanges(deletionAxioms);

        changesDone.clear();
    }

}
