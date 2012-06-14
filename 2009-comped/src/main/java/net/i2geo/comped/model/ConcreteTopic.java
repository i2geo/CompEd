/**
 * 
 */
package net.i2geo.comped.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Martin Homik
 *
 */
@Entity
@Table(name="t_item")
public class ConcreteTopic extends Topic {

	private static final long serialVersionUID = -7033255082509649532L;

	private List<AbstractTopic> ats = new ArrayList<AbstractTopic>();

	@ManyToMany
	@JoinTable(name="tis_tg",
			joinColumns=@JoinColumn(name="ti_id", referencedColumnName="id"),
			inverseJoinColumns=@JoinColumn(name="tg_id", referencedColumnName="id"))
	public List<AbstractTopic> getAts() {
		return ats;
	}

	public void setAts(List<AbstractTopic> ats) {
		this.ats = ats;
	}

	@Transient
	public String getType() {
		return Topic.TYPE_ITEM;
	}
	
	public static ConcreteTopic createConcreteTopic(Topic topic) {
		ConcreteTopic t = new ConcreteTopic();
		
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
