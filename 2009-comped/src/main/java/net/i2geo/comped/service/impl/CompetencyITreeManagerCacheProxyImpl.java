/**
 * @author Martin Homik
 */
package net.i2geo.comped.service.impl;


import java.util.Locale;

import com.jenkov.prizetags.tree.itf.ITree;

import net.i2geo.comped.model.CompetencyProcess;
import net.i2geo.comped.model.ConcreteCompetency;
import net.i2geo.comped.service.CompetencyProcessManager;
import net.i2geo.comped.service.CompetencyITreeManager;

/**
 * @author Martin Homik
 *
 */
public class CompetencyITreeManagerCacheProxyImpl implements CompetencyITreeManager {
	
	private CompetencyITreeManager iTreeManager;

	private ITree iTree;
	
	public CompetencyITreeManagerCacheProxyImpl(CompetencyProcessManager cpm) {
		iTreeManager = new CompetencyITreeManagerImpl(cpm);
	}

	
	public void setLocale(Locale locale) {
		iTreeManager.setLocale(locale);
	}

	
        
    /**
     * Creates a full competency process tree without individuals.
     * @return the competency process tree
     */
    public ITree createTree() {

    	if (iTree == null) {
    		iTree = iTreeManager.createTree(false);
    	}
    	
    	return iTree;
    }
    
    
    /**
     * Creates the complete competency process tree that might include individuals.
     * @param individuals true if to include concrete competencies
     * @return competency process tree
     */
    public ITree createTree(boolean individuals) {
    	if (iTree == null) {
    		iTree = iTreeManager.createTree(individuals);
    	}
    	
    	return iTree;
    }
    

    /**
     * Create a competency tree for a particular competency process.
     * 
     * @param cp a competency process
     * @return competency process tree with respect to a a particular competency process
     */
    public ITree createTree(CompetencyProcess cp) {
    	if (iTree == null) {
    		iTree = iTreeManager.createTree(cp);
    	}
    	
    	return iTree;
    }
    
    /**
     * Interface method to create a cometency tree with respect to a competency process
     * that might inlcude individuals.
     * 
     * @param cp the competency process
     * @return the tree representation
     */
    public ITree createTree(CompetencyProcess cp, boolean individuals, boolean siblings) {
    	
    	if (iTree == null) {
    		iTree = iTreeManager.createTree(cp, individuals, siblings);
    	}
    	
    	return iTree;
    }
    

    /**
     * Create a competency tree for a concrete competency and no individuals.
     * 
     * @param cc a concrete competency
     * @return competency process tree with respect to a a particular concrete competency
     */
    public ITree createTree(ConcreteCompetency cc) {
    	if (iTree == null) {
    		iTree = iTreeManager.createTree(cc);
    	}
    	
    	return iTree;
    }

    
    /**
     * Create a competency tree for a concrete competency that might include individuals.
     * 
     * @param cc a concrete competency
     * @param individuals a trigger to include individuals
     * @return competency process tree with respect to a a particular concrete competency
     */
    public ITree createTree(ConcreteCompetency cc, boolean individuals, boolean siblings) {
    	if (iTree == null) {
    		iTree = iTreeManager.createTree(cc, individuals, siblings);
    	}
    	
    	return iTree;
    }


    /**
     * Create a competency sub tree for a competency process without inclusion of 
     * concrete competencies.
     * 
     * @param cp a competency process
     * @return competency process tree with respect to a a particular competency process
     */
    public ITree createSubTree(CompetencyProcess cp) {
    	if (iTree == null) {
    		iTree = iTreeManager.createTree(cp);
    	}
    	
    	return iTree;
    }
        
    /**
     * Interface that creates a sub tree representation with respect to given
     * competency process.
	 *
     * @param cp the competency process
     * @return the tree representation
     * 
     */
    public ITree createSubTree(CompetencyProcess cp, boolean individuals) {
    	if (iTree == null) {
    		iTree = iTreeManager.createSubTree(cp, individuals);
    	}
    	
    	return iTree;
    }


    /**
     * Create a competency super tree for a competency process without inclusion
     * of concrete competencies.
     * 
     * @param cp a competency process
     * @return competency process tree with respect to a a particular competency process
     */
    public ITree createSuperTree(CompetencyProcess cp) {
    	if (iTree == null) {
    		iTree = iTreeManager.createSuperTree(cp);
    	}
    	
    	return iTree;
    }
    
    /**
     * Interface that creates a super tree representation with respect to given
     * competency process.
	 *
     * @param cp the competency process
     * @return the tree representation
     * 
     */
    public ITree createSuperTree(CompetencyProcess cp, boolean individuals) {
    	if (iTree == null) {
    		iTree = iTreeManager.createSuperTree(cp, individuals);
    	}
    	
    	return iTree;
    }
    
}
