/**
 * 
 */
package net.i2geo.onto.updates.actions;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.i2geo.onto.updates.OntoUpdateException;

import org.jdom.Element;
import org.jdom.Namespace;
import org.semanticweb.owl.model.AddAxiom;
import org.semanticweb.owl.model.OWLAxiom;
import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLClassAssertionAxiom;
import org.semanticweb.owl.model.OWLConstant;
import org.semanticweb.owl.model.OWLDataFactory;
import org.semanticweb.owl.model.OWLDataPropertyExpression;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLOntology;
import org.semanticweb.owl.model.OWLOntologyChange;
import org.semanticweb.owl.model.OWLOntologyChangeException;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.util.OWLEntityRemover;
import org.semanticweb.owl.util.OWLEntityRenamer;

/**
 * @author afaulhab
 *
 */
public class OntoClassChangeAction
    extends OntologyActionBase
{
    /**
     * New name of the Element
     */
    private String newClass = null;
    
    
    /* (non-Javadoc)
     * @see net.i2geo.onto.updates.actions.OntologyActionBase#perform()
     */
    public void perform()
        throws OWLOntologyChangeException
    {
        if (newClass == null) {
            throw new OWLOntologyChangeException(null,
                    new OntoUpdateException("No new name given for the name change!"
                            , null))
            {
            };
        }
        
        // helper instance
        OntologyHelper helper = OntologyHelper.getInstance();
        OWLOntologyManager manager = helper.getManager();
        OWLDataFactory factory = helper.getFactory();
        OWLOntology ont;


        // get the ontology
        try {
            ont = helper.getOWLOntology();
        }
        catch (Exception ex) {
            throw new OWLOntologyChangeException(null, ex)
            {
            };
        }

        // get the original individual
        OWLIndividual individual = factory.getOWLIndividual(URI.create(helper.getBase()
                + this.getElementId()));

        // create class assertion of individual
        OWLClass owlClass = factory.getOWLClass(URI.create(helper.getBase()
                + this.newClass));

        OWLAxiom axiom = factory.getOWLClassAssertionAxiom(individual, owlClass);
        
        List<OWLOntologyChange> changeList;
        
        try {
            // change the class and get List what had to be changed
            changeList = manager.applyChange(new AddAxiom(ont, axiom));
        } catch (Exception ex) {
            // TODO: error report
            return;
        }
        
        // TODO: add changes to session changeList
    }

    /**
     * @return the newName
     */
    public String getNewClass()
    {
        return newClass;
    }
    /**
     * @param newName the newName to set
     */
    public void setNewClass(String newClass)
    {
        this.newClass = newClass;
    }

}
