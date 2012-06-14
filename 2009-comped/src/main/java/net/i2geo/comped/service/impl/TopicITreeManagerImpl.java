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

import net.i2geo.comped.model.AbstractTopic;
import net.i2geo.comped.model.ConcreteTopic;
import net.i2geo.comped.model.Topic;
import net.i2geo.comped.model.compare.DefaultCommonNamesByLanguageTopicComparator;
import net.i2geo.comped.service.AbstractTopicManager;
import net.i2geo.comped.service.TopicITreeManager;

/**
 * @author Martin Homik
 *
 */
public class TopicITreeManagerImpl implements TopicITreeManager {

	private Locale locale;
	private AbstractTopicManager atm;
	
	public TopicITreeManagerImpl(AbstractTopicManager atm) {
		this.atm = atm;
	}

	
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	private boolean  isSetLocale() {
		return (locale != null);
	}
	
	/**
     * Get all root nodes of a abstract topic hierarchy.
     */
    protected List<AbstractTopic> getRootNodes() {
    	List<AbstractTopic> rootNodes = new ArrayList<AbstractTopic>();

    	for (AbstractTopic abstractTopic : atm.getAll()) {
			if (abstractTopic.getParent().isEmpty()) {
				rootNodes.add(abstractTopic);
			}
		}
    	
    	return rootNodes;
    }
        
    /**
     * Creates a full abstract topic tree without individuals.
     * @return the abstract topic tree
     */
    public ITree createTree() {
    	return createTree(false);
    }
    
    /**
     * Creates the complete abstract topic tree that might include individuals.
     * @param individuals true if to include concrete topics
     * @return abstract topic tree
     */
    public ITree createTree(boolean individuals) {
    	ITree tree = new Tree();
    	
    	List<AbstractTopic> roots = getRootNodes();
    	
    	if (roots.size() == 1) {
    		ITreeNode root = createSubTreeNode(roots.get(0), individuals);
    		tree.setRoot(root);
    	} else {
    		ITreeNode root = new TreeNode();
    		root.setId("-1");
    		root.setName("root");
    		for (AbstractTopic abstractTopic : roots) {
				ITreeNode node = createSubTreeNode(abstractTopic, individuals);
				root.addChild(node);
			}
    		tree.setRoot(root);
    	}
    	
    	return tree;
    }

    /**
     * Interface method to create a topic tree with respect to a topic group.
     * 
     * @param tg the topic group
     * @return the tree representation
     */
    public ITree createTree(AbstractTopic tg, boolean individuals) {
    	
    	ITree subTree         = createSubTree(tg, individuals);
    	ITreeNode subTreeRoot = subTree.getRoot();
    	
    	// if tg is already root, return the sub tree only
    	if (tg.getParent().isEmpty()) {
    		return subTree;
    	}
    	
    	ITree superTree       = createSuperTree(tg, individuals);
    	
    	ITreeNode node = superTree.findNode(tg.getId().toString());
    	ITreeNode pNode = node.getParent();
    	pNode.removeChild(node);
    	pNode.addChild(subTreeRoot);
    	return superTree;
    }
    

    /**
     * Interface method to create a topic tree with respect to a concrete topic.
     * 
     * @param tg the topic group
     * @return the tree representation
     */
    public ITree createTree(ConcreteTopic ti, boolean individuals) {
    	List<AbstractTopic> processes = ti.getAts();
    	if (processes.isEmpty()) {
    		return new Tree();
    	} else {
    		AbstractTopic parent = processes.get(0);
    		ITree tree = createTree(parent, individuals);
    		ITreeNode pNode = tree.findNode(parent.getId().toString());
    		ITreeNode tiNode = createITreeNode(ti);
    		pNode.addChild(tiNode);
    		
    		return tree;
    	}
    }

    /**
     * Create a topic tree for a particular abstract topic.
     * 
     * @param cp a abstract topic
     * @return abstract topic tree with respect to a a particular abstract topic
     */
    public ITree createTree(AbstractTopic at) {
    	return createTree(at, false, true);
    }
    
    /**
     * Interface method to create a cometency tree with respect to a abstract topic
     * that might inlcude individuals.
     * 
     * @param cp the abstract topic
     * @return the tree representation
     */
    public ITree createTree(AbstractTopic at, boolean individuals, boolean siblings) {
    	
    	ITree subTree         = createSubTree(at, individuals);
    	ITreeNode subTreeRoot = subTree.getRoot();
    	
    	// if at is already root, return the sub tree only
    	if (at.getParent().isEmpty()) {
    		return subTree;
    	}
    	
    	ITree superTree       = createSuperTree(at, individuals);
    	
    	ITreeNode node = superTree.findNode(at.getId().toString());
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
     * Create a topic tree for a concrete topic and no individuals.
     * 
     * @param cc a concrete topic
     * @return abstract topic tree with respect to a a particular concrete topic
     */
    public ITree createTree(ConcreteTopic ct) {
    	return createTree(ct, false, false);
    }

    
    /**
     * Create a topic tree for a concrete topic that might include individuals.
     * 
     * @param cc a concrete topic
     * @param individuals a trigger to include individuals
     * @return abstract topic tree with respect to a a particular concrete topic
     */
    public ITree createTree(ConcreteTopic ct, boolean individuals, boolean siblings) {
    	List<AbstractTopic> abstractTopics = ct.getAts();
    	if (abstractTopics.isEmpty()) {
    		return new Tree();
    	} else {
    		AbstractTopic parent = abstractTopics.get(0);
    		ITree tree = createTree(parent, individuals, siblings);
    		ITreeNode pNode = tree.findNode(parent.getId().toString());
    		ITreeNode ccNode = createITreeNode(ct);
    		pNode.addChild(ccNode);
    		
    		return tree;
    	}
    }


    /**
     * Create a topic sub tree for a abstract topic without inclusion of 
     * concrete topics.
     * 
     * @param at a abstract topic
     * @return abstract topic tree with respect to a a particular abstract topic
     */
    public ITree createSubTree(AbstractTopic at) {
    	return createSubTree(at, false);
    }
    
    /**
     * Interface that creates a sub tree representation with respect to given
     * abstract topic.
	 *
     * @param at the abstract topic
     * @return the tree representation
     * 
     */
    public ITree createSubTree(AbstractTopic at, boolean individuals) {
    	ITree tree = new Tree();
    	
    	ITreeNode rootNode = createSubTreeNode(at, individuals);
    	tree.setRoot(rootNode);
    	return tree;
    }
    
    /**
     * Recursive method to create a sub tree representation of a abstract topic.
     * 
     * @param at
     * @return
     */
    private ITreeNode createSubTreeNode(AbstractTopic at, boolean individuals) {
    	ITreeNode node = createITreeNode(at);
    	
    	if (individuals) {
    		node = addIndividuals(node, at);
    	}
    	
    	List<AbstractTopic> children = at.getChildren();
    	Collections.sort(children, 
    			new DefaultCommonNamesByLanguageTopicComparator(locale.getLanguage()));
    	
    	if (!children.isEmpty()) {
    		for (AbstractTopic abstractTopic : children) {
    			ITreeNode iTreeChild = createSubTreeNode(abstractTopic, individuals);
				node.addChild(iTreeChild);
    		}
    	}
    	
    	return node;
    }

    /**
     * Create a topic super tree for a abstract topic without inclusion
     * of concrete topics.
     * 
     * @param at a abstract topic
     * @return abstract topic tree with respect to a a particular abstract topic
     */
    public ITree createSuperTree(AbstractTopic at) {
    	return createSuperTree(at, false);
    }
    
    /**
     * Interface that creates a super tree representation with respect to given
     * abstract topic.
	 *
     * @param at the abstract topic
     * @return the tree representation
     * 
     */
    public ITree createSuperTree(AbstractTopic at, boolean individuals) {
    	ITree tree = new Tree();
    	
    	List<ITreeNode> roots = new ArrayList<ITreeNode>();
    	roots = createSuperTreeNodes(at, null, new ArrayList<ITreeNode>(), individuals);
    	
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
     * Recursive method to create a super tree representation of a abstract topic.
     */
    private List<ITreeNode> createSuperTreeNodes(
    		AbstractTopic current, ITreeNode child, List<ITreeNode> roots, boolean individuals) {
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
    		List<AbstractTopic> parents = current.getParent();
    		for (AbstractTopic at : parents) {
    			roots = createSuperTreeNodes(at, currentNode, roots, individuals);		
    		}
    	}
    	
    	return roots;
    	
    }
    
    /**
     * Add concrete topics to a abstract topic tree node.
     * @param node The node that represents the abstract topic
     * @param at The abstract topic.
     * @return tree node of a abstract topic with concrete topics included
     */
    private ITreeNode addIndividuals(ITreeNode node, AbstractTopic at) {
    	if (at.getItems().size() > 0) {
    		
    		List<ConcreteTopic> sorted = at.getItems();
    		Collections.sort(sorted, 
    				new DefaultCommonNamesByLanguageTopicComparator(locale.getLanguage()));
    		
    		for (ConcreteTopic ct : sorted) {
				ITreeNode ctNode = createITreeNode(ct);
				node.addChild(ctNode);
			}
    		return node;
    	} else {
    		return node;
    	}
    }
    
    /**
     * Create a an ITree representation of a abstract topic.
     * 
     * @param c
     * @return
     */
    // TODO
    private ITreeNode createITreeNode(Topic t) {
    	ITreeNode node = new TreeNode();
    	
    	node.setId(t.getId().toString());

    	if (isSetLocale()) {
    		// change name only when the locale is actually set
    		t.setName(atm.getName(t, this.locale.getLanguage()));
    	}
    	
    	node.setName(t.getName());
    	node.setObject(t);

    	if (t instanceof AbstractTopic) {
    		AbstractTopic at = (AbstractTopic) t;
    		if (at.getType().equals(Topic.TYPE_REPRESENTATIVE)) {
        		node.setType(Topic.TYPE_REPRESENTATIVE);
    		} else {
    			node.setType(Topic.TYPE_PURE);
    		}
    	}
    	
    	if (t instanceof ConcreteTopic) {
    		node.setType(Topic.TYPE_ITEM);
    	}

    	return node;
    }

}
