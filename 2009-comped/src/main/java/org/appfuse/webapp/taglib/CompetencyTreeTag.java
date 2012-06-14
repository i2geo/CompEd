package org.appfuse.webapp.taglib;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.i2geo.comped.model.Competency;
import net.i2geo.comped.model.Thing;
import net.i2geo.comped.model.Topic;
import net.i2geo.comped.model.URIAddressable;

import com.jenkov.prizetags.tree.itf.ITree;
import com.jenkov.prizetags.tree.itf.ITreeNode;


/**
 * Tag for creating unordered lists that reflect the competency hierarchy.
 *
 * @author Martin Homik
 *
 * @jsp.tag name="cptree" bodycontent="empty"
 */
public class CompetencyTreeTag extends TagSupport {
    
	private static final long serialVersionUID = -8401450268820143210L;

    /**
     * Transient log to prevent session synchronization issues - children can use instance for logging.
     */
    protected transient final Log log = LogFactory.getLog(getClass());
    
	private String name;
    private String scope;
    private Thing selected;
    private String cssClass = "filetree";
    private boolean individuals = false;
    private boolean folded = false;
    private boolean foldChildrenOfSelected = false;
    
    private ITree treemap = null;
    
    /**
     * @param name The name to set.
     *
     * @jsp.attribute required="false" rtexprvalue="true"
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param selected The selected option.
     * @jsp.attribute required="false" rtexprvalue="true"
     */
    public void setSelected(Thing selected) {
        this.selected = selected;
    }


    /**
     * @param locale The css class option. Default is "filetree".
     * @jsp.attribute required="false" rtexprvalue="true"
     */
    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    /**
     * @param individuals The individuals option.
     * @jsp.attribute required="false" rtexprvalue="true"
     */
    public void setIndividuals(boolean individuals) {
		this.individuals = individuals;
	}

    /**
     * @param folded Will fold the tree when desired
     * @jsp.attribute required="false" rtexprvalue="true"
     */
    public void setFolded(boolean folded) {
		this.folded = folded;
	}

    /**
     * @param foldChildrenOfSelected Will fold the children of the selected item.
     * @jsp.attribute required="false" rtexprvalue="true"
     */
    public void setFoldChildrenOfSelected(boolean foldChildrenOfSelected) {
		this.foldChildrenOfSelected = foldChildrenOfSelected;
	}

	/**
     * Property used to simply stuff the list of languages into a
     * specified scope.
     *
     * @param scope
     *
     * @jsp.attribute required="false" rtexprvalue="true"
     */
    public void setToScope(String scope) {
        this.scope = scope;
    }

	/**
     * 
     * @param treemap
     *
     * @jsp.attribute required="true" rtexprvalue="true"
     */
    public void setTreemap(ITree treemap) {
    	this.treemap = treemap;
    }
    
    
    /**
     * Process the start of this tag.
     *
     * @return int status
     *
     * @exception JspException if a JSP exception has occurred
     *
     * @see javax.servlet.jsp.tagext.Tag#doStartTag()
     */
    public int doStartTag() throws JspException {
        // ExpressionEvaluator eval = new ExpressionEvaluator(this, pageContext);
        // Locale userLocale = pageContext.getRequest().getLocale();

        // I'm not sure how to use this scope information
        if (scope != null) {
            if (scope.equals("page")) {
                pageContext.setAttribute(name, treemap);
            } else if (scope.equals("request")) {
                pageContext.getRequest().setAttribute(name, treemap);
            } else if (scope.equals("session")) {
                pageContext.getSession().setAttribute(name, treemap);
            } else if (scope.equals("application")) {
                pageContext.getServletContext().setAttribute(name, treemap);
            } else {
                throw new JspException("Attribute 'scope' must be: page, request, session or application");
            }
        } else {
        
            StringBuffer sb = new StringBuffer();
            
            // sb.append("============ I GOT IT================\n");
            // sb.append("\n treemap: " + treemap);
            
            // sb.append("<ul id=\"" + name +"\" class=\"" + cssClass +"\">\n");
            
            if (treemap == null || treemap.getRoot() == null) {
            	sb.append("WARNING: could not calclulate tree.");
            	log.error("WARNING: could not calclulate tree.");
            	
            } else {
            
            	sb.append("<ul id=\"itree\" class=\"" + cssClass +"\">\n");
            	createRootTree(sb, treemap);
            	sb.append("</ul>\n");
            
            	try {
            		pageContext.getOut().write(sb.toString());
            	} catch (IOException io) {
            		throw new JspException(io);
            	}
            }
        }
        
        return super.doStartTag();
    }

    private void createRootTree(StringBuffer sb, ITree tree) {
    	log.debug("Tag: Tree: " + tree);
    	log.debug("Tag: Tree root: " + tree.getRoot());
    	log.debug("Tag: Tree root object: " + tree.getRoot().getObject());
    	
    	createSubTree(sb, tree.getRoot());    	
    }
    
    @SuppressWarnings("unchecked")
	private void createSubTree(StringBuffer sb, ITreeNode node) {
    	if (node.hasChildren()) {
    		printItem(sb, node);
    		sb.append("<ul>\n");
    		for (Iterator<ITreeNode> iterator = node.getChildren().iterator(); iterator.hasNext();) {
    			ITreeNode subNode =  iterator.next();
				createSubTree(sb, subNode);
			}
    		sb.append("</ul></li>\n");

    	} else {
    		printItem(sb, node);
    	}
    }
    
    private void printItem(StringBuffer sb, ITreeNode node) {

    	if (node.getType().equals("process")) {
			printItem(sb, node, "folder");
		}
		
		if (node.getType().equals("competency") && individuals ) {
			printItem(sb, node, "file");
		}

		// TODO: should be 'file'
    	if (node.getType().equals(Topic.TYPE_REPRESENTATIVE)) {
			printItem(sb, node, "folder");
		}

    	if (node.getType().equals(Topic.TYPE_PURE)) {
			printItem(sb, node, "folder");
		}
		
		if (node.getType().equals(Topic.TYPE_ITEM) && individuals ) {
			printItem(sb, node, "file");
		}
		
    }
        
    private void printItem(StringBuffer sb, ITreeNode node, String itemClass) {
    	
    	URIAddressable o = (URIAddressable) node.getObject();

    	String match = "ontology.owl#";
    	int uriIndex = o.getUri().lastIndexOf(match);
    	String uri= o.getUri().substring(uriIndex + match.length());
    	
    	String action = "";


    	if (node.getType().equals("process")) {action = "showProcess.html";}
    	if (node.getType().equals("competency")) {action = "showCompetency.html";}
    	if (node.getType().equals(Topic.TYPE_PURE)) {action = "show.html";}
    	if (node.getType().equals(Topic.TYPE_REPRESENTATIVE)) {action = "show.html";}
    	if (node.getType().equals(Topic.TYPE_ITEM)) {action = "show.html";}

    	String ahref =
			"<a href=\"" + action + "?uri=" + uri + "\"/>";
    	
    	String listItemCssClass = "";
    	if (node.getType().equals("process") && folded) {listItemCssClass = " class=\"closed\" ";}
    	if (node.getType().equals(Topic.TYPE_REPRESENTATIVE) && folded) {listItemCssClass = " class=\"closed\" ";}
    	if (node.getType().equals(Topic.TYPE_PURE) && folded) {listItemCssClass = " class=\"closed\" ";}
    	

    	if (isSelected(node)) {
			sb.append("<li" + listItemCssClass + "><span class=\"" 
					+ itemClass 
					+ "\"><em>" 
					+ ahref
					+ node.getName() 
					+ "</a></em></span>\n");
		} else {
			sb.append("<li" + listItemCssClass + "><span class=\"" 
					+ itemClass 
					+ "\">" 
					+ ahref
					+ node.getName() 
					+ "</a></span>\n");
		}
    }

    private boolean isRoot(ITreeNode node) {
    	Object o = node.getObject();
    	
    	if (o instanceof Competency) {
    		Competency c = (Competency) o;
    		if (c.getUriSuffix().equals("Competency")) {return true;}
    	}
    	if (o instanceof Topic) {
    		Topic t = (Topic) o;
    		if (t.getUriSuffix().equals("Topic")) {return true;}
    	}
    	
    	return false;
    	
    }
    private boolean isSelected(ITreeNode node) {
    	Thing o = (Thing) node.getObject();
    	
    	if (selected != null) {
    		return o.equals(selected);
    	} else {
    		return false;
    	}
    }
    
    /**
     * Release aquired resources to enable tag reusage.
     *
     * @see javax.servlet.jsp.tagext.Tag#release()
     */
    public void release() {
        super.release();
    }

}
