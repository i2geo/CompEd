/*
 * Topic.java
 *
 * Created on 2. July 2008, 16:29
 *
 * Creator: Martin Homik
 *
 */

package net.i2geo.comped.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name="topic")
public class Topic extends Thing implements Nameable {

	private static final long serialVersionUID = -7977191851782061995L;

	// Topic types
	public static final String   TYPE_NONE = "NONE";
	public static final String   TYPE_ITEM = "CONCRETE";
	public static final String   TYPE_PURE = "PURE";
	public static final String   TYPE_REPRESENTATIVE = "REPRESENTATIVE";

	// List of topic types
	public static final List<String> TYPES = 
		new ArrayList<String>(Arrays.asList(
			TYPE_ITEM, TYPE_PURE, TYPE_REPRESENTATIVE ));
	
	
	private String name = "";
	
	/** 
	 * Set of names for this topic.
	 */
	private List<Name> 	names	  = new ArrayList<Name>();
	
	public Topic() {
	}

	/**
	 * @return the name
	 */
	@Transient
	public String getName() {
		return name;
	}
	
	/**
	 * Get type of the topic instance. 
	 * @return topic type
	 */
	@Transient
	public String getType() {
		return Topic.TYPE_NONE;
	}
	
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	
	@OneToMany(cascade = {CascadeType.ALL})
	@JoinTable(name = "topic_names")
	public List<Name> getNames() {
		return names;
	}

	public void setNames(List<Name> names) {
		this.names = names;
	}	


	/**
	 * Inclusion of related objects is omitted due to hash/equality problems with
	 * Hibernate. There has been lots of discussion in the AppFuse and in the Hibernate
	 * forum. See also:
	 * <ul>
	 * <li> <a href="http://forum.hibernate.org/viewtopic.php?t=928172"> Hibernate Forum </a> </li>
	 * <li> <a href="http://www.hibernate.org/109.html"> Hibernate Documentation </a></li> 
	 * <li> <a href="http://www.nabble.com/why-illegal-access-to-lazy-collection-with-collection-in-commonclipse-methods-tf3224699s2369.html#a8970660"> AppFuse Forum </a></li> 
	 * <li> <a href="http://www.nabble.com/LazyInitializationException%3A-illegal-access-to-loading-collection....-tf4152650s2369.html#a11813896"> AppFuse Forum</a></li>
	 * </ul>
	 * @see java.lang.Object#equals(Object)
	 */
	/*
	public boolean equals(Object object) {
		if (!(object instanceof Topic)) {
			return false;
		}
		Topic rhs = (Topic) object;
		return new EqualsBuilder()
			.appendSuper(super.equals(object))
			// .append(this.name, rhs.name)
			// .append(this.uri, rhs.uri)
			.isEquals();
	}
	*/
	
	/**
	 * Inclusion of related objects is omitted due to hash/equality problems with
	 * Hibernate. There has been lots of discussion in the AppFuse and in the Hibernate
	 * forum. See also:
	 * <ul>
	 * <li> <a href="http://forum.hibernate.org/viewtopic.php?t=928172"> Hibernate Forum </a> </li>
	 * <li> <a href="http://www.hibernate.org/109.html"> Hibernate Documentation </a></li> 
	 * <li> <a href="http://www.nabble.com/why-illegal-access-to-lazy-collection-with-collection-in-commonclipse-methods-tf3224699s2369.html#a8970660"> AppFuse Forum </a></li> 
	 * <li> <a href="http://www.nabble.com/LazyInitializationException%3A-illegal-access-to-loading-collection....-tf4152650s2369.html#a11813896"> AppFuse Forum</a></li>
	 * </ul>
	 * @see java.lang.Object#hashCode()
	 */
	/*
	public int hashCode() {
		return new HashCodeBuilder(903448621, 128887903)
			.appendSuper(super.hashCode())
			// .append(this.name)
			// .append(this.uri)
			.toHashCode();
	}
	*/
	
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
			.append("id", this.getId())
			.append("uri", getUri())
			.toString();
	}
	
	
}
