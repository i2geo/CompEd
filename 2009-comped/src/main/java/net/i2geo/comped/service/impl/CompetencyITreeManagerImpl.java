/**
 * @author Martin Homik
 */
package net.i2geo.comped.service.impl;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.jenkov.prizetags.tree.impl.Tree;
import com.jenkov.prizetags.tree.impl.TreeNode;
import com.jenkov.prizetags.tree.itf.ITree;
import com.jenkov.prizetags.tree.itf.ITreeNode;

import net.i2geo.comped.model.Competency;
import net.i2geo.comped.model.CompetencyProcess;
import net.i2geo.comped.model.ConcreteCompetency;
import net.i2geo.comped.model.compare.DefaultCommonNamesByLanguageCompetencyComparator;
import net.i2geo.comped.service.CompetencyProcessManager;
import net.i2geo.comped.service.CompetencyITreeManager;

/**
 * @author Martin Homik
 *
 */
public class CompetencyITreeManagerImpl implements CompetencyITreeManager {

	private Locale locale;
	private CompetencyProcessManager cpm;
	
	public CompetencyITreeManagerImpl(CompetencyProcessManager cpm) {
		this.cpm = cpm;
	}

	
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	private boolean  isSetLocale() {
		return (locale != null);
	}
	
	/**
     * Get all root nodes of a competency process hierarchy.
     */
    protected List<CompetencyProcess> getRootNodes() {
    	List<CompetencyProcess> rootNodes = new ArrayList<CompetencyProcess>();

    	for (CompetencyProcess competencyProcess : cpm.getAll()) {
			if (competencyProcess.getParent().isEmpty()) {
				rootNodes.add(competencyProcess);
			}
		}
    	
    	return rootNodes;
    }
        
    /**
     * Creates a full competency process tree without individuals.
     * @return the competency process tree
     */
    public ITree createTree() {
    	return createTree(false);
    }
    
    /**
     * Creates the complete competency process tree that might include individuals.
     * @param individuals true if to include concrete competencies
     * @return competency process tree
     */
    public ITree createTree(boolean individuals) {
    	ITree tree = new Tree();
    	
    	List<CompetencyProcess> roots = getRootNodes();
    	
    	if (roots.size() == 1) {
    		ITreeNode root = createSubTreeNode(roots.get(0), individuals);
    		tree.setRoot(root);
    	} else {
    		ITreeNode root = new TreeNode();
    		root.setId("-1");
    		root.setName("root");
    		for (CompetencyProcess competencyProcess : roots) {
				ITreeNode node = createSubTreeNode(competencyProcess, individuals);
				root.addChild(node);
			}
    		tree.setRoot(root);
    	}
    	
    	return tree;
    }

    /**
     * Create a competency tree for a particular competency process.
     * 
     * @param cp a competency process
     * @return competency process tree with respect to a a particular competency process
     */
    public ITree createTree(CompetencyProcess cp) {
    	return createTree(cp, false, true);
    }
    
    /**
     * Interface method to create a cometency tree with respect to a competency process
     * that might inlcude individuals.
     * 
     * @param cp the competency process
     * @return the tree representation
     */
    public ITree createTree(CompetencyProcess cp, boolean individuals, boolean siblings) {
    	
    	ITree subTree         = createSubTree(cp, individuals);
    	ITreeNode subTreeRoot = subTree.getRoot();
    	
    	// if cp is already root, return the sub tree only
    	if (cp.getParent().isEmpty()) {
    		return subTree;
    	}
    	
    	ITree superTree       = createSuperTree(cp, individuals);
    	
    	ITreeNode node = superTree.findNode(cp.getId().toString());
    	ITreeNode pNode = node.getParent();
    	pNode.removeChild(node);
    	pNode.addChild(subTreeRoot);
    	
    	/*
    	if (!siblings) {
    		ITreeNode parentOfCP = subTreeRoot.getParent();
    		List<ITreeNode> ggg = parentOfCP.getChildren();
    		for (ITreeNode n : ggg) {
    			if (!n.equals(subTreeRoot)) {
    				parentOfCP.removeChild(n);
    			}
    		}
    	}
    	*/
    	
    	return superTree;
    }

    /**
     * Create a competency tree for a concrete competency and no individuals.
     * 
     * @param cc a concrete competency
     * @return competency process tree with respect to a a particular concrete competency
     */
    public ITree createTree(ConcreteCompetency cc) {
    	return createTree(cc, false, false);
    }

    
    /**
     * Create a competency tree for a concrete competency that might include individuals.
     * 
     * @param cc a concrete competency
     * @param individuals a trigger to include individuals
     * @return competency process tree with respect to a a particular concrete competency
     */
    public ITree createTree(ConcreteCompetency cc, boolean individuals, boolean siblings) {
    	List<CompetencyProcess> processes = cc.getProcesses();
    	if (processes.isEmpty()) {
    		return new Tree();
    	} else {
    		CompetencyProcess parent = processes.get(0);
    		ITree tree = createTree(parent, individuals, siblings);
    		ITreeNode pNode = tree.findNode(parent.getId().toString());
    		ITreeNode ccNode = createITreeNode(cc);
    		pNode.addChild(ccNode);
    		
    		return tree;
    	}
    }


    /**
     * Create a competency sub tree for a competency process without inclusion of 
     * concrete competencies.
     * 
     * @param cp a competency process
     * @return competency process tree with respect to a a particular competency process
     */
    public ITree createSubTree(CompetencyProcess cp) {
    	return createSubTree(cp, false);
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
    	ITree tree = new Tree();
    	
    	ITreeNode rootNode = createSubTreeNode(cp, individuals);
    	tree.setRoot(rootNode);
    	return tree;
    }
    
    /**
     * Recursive method to create a sub tree representation of a competency process.
     * 
     * @param cp
     * @return
     */
    private ITreeNode createSubTreeNode(CompetencyProcess cp, boolean individuals) {
    	ITreeNode node = createITreeNode(cp);
    	
    	if (individuals) {
    		node = addIndividuals(node, cp);
    	}
    	
    	List<CompetencyProcess> children = cp.getChildren();
    	
    	Collections.sort(children, new DefaultCommonNamesByLanguageCompetencyComparator(locale.getLanguage()));
    	
    	if (!children.isEmpty()) {
    		for (CompetencyProcess competencyProcess : children) {
    			ITreeNode iTreeChild = createSubTreeNode(competencyProcess, individuals);
				node.addChild(iTreeChild);
    		}
    	}
    	
    	return node;
    }

    /**
     * Create a competency super tree for a competency process without inclusion
     * of concrete competencies.
     * 
     * @param cp a competency process
     * @return competency process tree with respect to a a particular competency process
     */
    public ITree createSuperTree(CompetencyProcess cp) {
    	return createSuperTree(cp, false);
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
    	ITree tree = new Tree();
    	
    	List<ITreeNode> roots = new ArrayList<ITreeNode>();
    	roots = createSuperTreeNodes(cp, null, new ArrayList<ITreeNode>(), individuals);
    	
    	if (roots.size() == 1) {
    		tree.setRoot(roots.get(0));
    	} else {
    		ITreeNode root = new TreeNode();
    		root.setId("-1");
    		root.setName("root");
    		for (ITreeNode rootNode : roots) {
				root.addChild(rootNode);
			}
    		tree.setRoot(root);
    	}
    	
    	return tree;
    }
    
    /**
     * Recursive method to create a super tree representation of a competency process.
     * 
     * @param cp
     * @return
     */
    private List<ITreeNode> createSuperTreeNodes(
    		CompetencyProcess current, ITreeNode child, List<ITreeNode> roots, boolean individuals) {
    	// create an ITreeNode from current
    	ITreeNode currentNode = createITreeNode(current);
    	
    	if (individuals) {
    		currentNode = addIndividuals(currentNode, current);
    	}
    	
    	// add child to current node
    	if (child != null) {
    		currentNode.addChild(child);
    	}
    	// get parents from current node and iterate through them
    	if (current.getParent().isEmpty()) {
    		roots.add(currentNode); 	
    	} else {
    		List<CompetencyProcess> parents = current.getParent();
    		for (CompetencyProcess cp : parents) {
    			roots = createSuperTreeNodes(cp, currentNode, roots, individuals);		
    		}
    	}
    	
    	return roots;
    	
    }
    
    /**
     * Add concrete competencies to a competency process tree node.
     * @param node The node that represents the competency process
     * @param cp The competency process.
     * @return tree node of a competency process with concrete competencies included
     */
    private ITreeNode addIndividuals(ITreeNode node, CompetencyProcess cp) {
    	if (cp.getCcompetencies().size() > 0) {
    		
    		List<ConcreteCompetency> sorted = cp.getCcompetencies();
    			
   			Collections.sort(sorted, 
   				new DefaultCommonNamesByLanguageCompetencyComparator(locale.getLanguage()));
    		
    		for (ConcreteCompetency cc : sorted) {
				ITreeNode ccNode = createITreeNode(cc);
				node.addChild(ccNode);
			}
    		return node;
    	} else {
    		return node;
    	}
    }
    
    /**
     * Create a an ITree representation of a competency process.
     * 
     * @param c
     * @return
     */
    private ITreeNode createITreeNode(Competency c) {
    	ITreeNode node = new TreeNode();
    	
    	node.setId(c.getId().toString());

    	if (isSetLocale()) {
    		// change name only when the locale is actually set
    		c.setName(cpm.getName(c, this.locale.getLanguage()));
    	}
    	
    	node.setName(c.getName());
    	node.setObject(c);

    	if (c instanceof CompetencyProcess) {
    		node.setType("process");
    	}
    	
    	if (c instanceof ConcreteCompetency) {
    		node.setType("competency");
    	}

    	return node;
    }
}
