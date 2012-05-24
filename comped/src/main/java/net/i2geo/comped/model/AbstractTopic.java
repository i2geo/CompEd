/**
 * 
 */
package net.i2geo.comped.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * @author Martin Homik
 *
 */
@Entity
@Table(name="t_group")
public class AbstractTopic extends Topic {

	private static final long serialVersionUID = -6595342267828447695L;

	/**
	 * Set of topic items that are member of a topic group.
	 */
	private List<ConcreteTopic> items = new ArrayList<ConcreteTopic>();


	 /**
	 * This variable is part of a bidirectional mapping from AbstractTopic to AbstractTopic. The
	 * 'children' relations is used by the owning side. This is the only relation
	 * on which persistence to the database works. Use this to build up a topics
	 * hierarchy.
	 * 
	 */
	private List<AbstractTopic> children = new ArrayList<AbstractTopic>();
	/**
	 * This variable is part of a bidirectional mapping from AbstractTopic to AbstractTopic. The
	 * 'parents' relation is the counter relation to 'children'. Changes on parents
	 * are not persisted!!!
	 */
	private List<AbstractTopic> parent = new ArrayList<AbstractTopic>();

	/**
	 * The type of the topic can be either a pure topic collection or a 
	 * a topic representative.
	 * 
	 */
	private String type = TYPE_NONE;
	
	/**
	 * Get set of topic items in the current topic group.
	 * @return
	 */
	@ManyToMany(mappedBy="ats")
	public List<ConcreteTopic> getItems() {
		return items;
	}

	/**
 	 * Set the set of topic items in the current topic group.
	 * @param tItems
	 */
	public void setItems(List<ConcreteTopic> items) {
		this.items = items;
	}
	
	/**
	 * @return children topic groups
	 */
	@ManyToMany(mappedBy="parent") 
	@OrderBy
	public List<AbstractTopic> getChildren() {
		return children;
	}

	/**
	 * @param set of sub topic groups
	 */
	public void setChildren(List<AbstractTopic> children) {
		this.children = children;
	}

	/**
	 * Add a new sub topic group.
	 * @param topic group
	 * TODO: is that needed actually? usually, you would write
	 * in the manager: c1.getSubAbstractTopics().add(c2);
	 * abstractTopicDao.save(c1)
	 * 
	 */
	public void addChildren(AbstractTopic tp) {
		getChildren().add(tp);
	}
	
	
	/**
	 * Remove topic group from the children set.
	 * @param topic group
	 * TODO: is that needed actually? usually, you would write
	 * in the manager: c1.getSubAbstractTopics().remove(c2);
	 * abstractTopicDao.save(c1)
	 */
	public void removeChildren(AbstractTopic tp) {
		getChildren().remove(tp);
	}
	
	/**
	 * @return the parent abstract topic
	 */
	@ManyToMany
	@OrderBy
	@JoinTable(name = "tg_subgroups",
			joinColumns=@JoinColumn(name="child_id", referencedColumnName="id"),
			inverseJoinColumns=@JoinColumn(name="parent_id", referencedColumnName="id"))
	public List<AbstractTopic> getParent() {
		return parent;
	}

	/**
	 * @param parent topic group
	 */
	public void setParent(List<AbstractTopic> parent) {
		this.parent = parent;
	}

	@Column(name="type", nullable=false, insertable=true, unique=false, updatable=false)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static AbstractTopic createAbstractTopic(Topic topic) {
		AbstractTopic t = new AbstractTopic();
		
		t.setName(topic.getName());
		t.setUri(topic.getUri());
		t.setCommentDays(topic.getCommentDays());
		t.setId(topic.getId());
		t.setCommentStatus(topic.getCommentStatus());
		t.setCreated(topic.getCreated());
		t.setCreator(topic.getCreator());
		t.setModified(topic.getModified());
		t.setNames(topic.getNames());
		t.setStatus(topic.getStatus());
		t.setUriSuffix(topic.getUriSuffix());
		
		return t;
	}

}
