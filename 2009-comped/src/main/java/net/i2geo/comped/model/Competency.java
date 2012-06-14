/*
 * Competency.java
 *
 * Created on 2. July 2008, 16:29
 *
 * Creator: Martin Homik
 *
 */

package net.i2geo.comped.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Index;

@Entity
@Table(name="competency")
public class Competency extends Thing implements Nameable {

	private static final long serialVersionUID = -2415409320487376516L;
	
	private String name = "";
	private String description;
	
	private List<Name>       names      		= new ArrayList<Name>();
	private Set<Topic>       topics      		= new HashSet<Topic>();
	
	private List<Competency> subsumes   		= new ArrayList<Competency>();
	private List<Competency> subsumedBy 		= new ArrayList<Competency>();
	private List<Competency> composes   		= new ArrayList<Competency>();
	private List<Competency> composedBy 		= new ArrayList<Competency>();
	private Set<Competency>  similarTo  		= new HashSet<Competency>();
	private Set<Competency>  inverseSimilarTo 	= new HashSet<Competency>();
	private Set<Competency>  allSimilar         = new HashSet<Competency>();
	private Set<Description> descriptions       = new HashSet<Description>();
	
	public Competency() {
	}

	/**
	 * @return the name
	 */
	@Transient
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * @return the description
	 * TODO: set length value to something that does not bound to a MySQL value
	 * (length=2147483647)
	 */
	@Index(name = "INDEX_COMPETENCY_DESCRIPTION")
	@Column(name="description", updatable=true, insertable=true, unique=false, nullable=true)
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return subsumed competencies
	 */
	@ManyToMany
	@JoinTable(name = "c_subsumption")
	public List<Competency> getSubsumes() {
		return subsumes;
	}

	/**
	 * @param subsumes the subsumed competencies
	 */
	public void setSubsumes(List<Competency> subsumes) {
		this.subsumes = subsumes;
	}

	/**
	 * Add a new subsumed competency.
	 * @param competency
	 * TODO: is that needed actually? usually, you would write
	 * in the manager: c1.getSubsumes().add(c2);
	 * competencyDao.save(c1)
	 * 
	 */
	public void addSubsumes(Competency competency) {
		getSubsumes().add(competency);
	}
	
	
	/**
	 * Remove competency from the subsumed list.
	 * @param competency
	 * TODO: is that needed actually? usually, you would write
	 * in the manager: c1.getSubsumes().remove(c2);
	 * competencyDao.save(c1)
	 */
	public void removeSubsumes(Competency competency) {
		getSubsumes().remove(competency);
	}
	
	/**
	 * @return the subsuming competencies
	 */
	@ManyToMany(
			mappedBy="subsumes" 
			)
	public List<Competency> getSubsumedBy() {
		return subsumedBy;
	}

	/**
	 * @param subsumedBy the subsuming competencies
	 */
	public void setSubsumedBy(List<Competency> subsumedBy) {
		this.subsumedBy = subsumedBy;
	}

	/*
	 * @return the competencies that compose the current competency; 
	 */
	@ManyToMany// (cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinTable(name = "c_composition")
	public List<Competency> getComposes() {
		return composes;
	}
	
	public void setComposes(List<Competency> composes) {
		this.composes = composes;
	}
	
	
	/**
	 * Add competency to composition list.
	 * @param competency
	 * TODO: is that needed actually? usually, you would write
	 * in the manager: c1.getComposes().add(c2);
	 * competencyDao.save(c1)
	 */
	public void addComposes(Competency competency) {
		getComposes().add(competency);
	}
	
	
	/**
	 * Remove competency from composition list.
	 * @param competency
 	 * TODO: is that needed actually? usually, you would write
	 * in the manager: c1.getComposes().remove(c2);
	 * competencyDao.save(c1)
	 */
	public void removeComposes(Competency competency) {
		getComposes().remove(competency);
	}

	/*
	 * @return the competencies the current competency is part of their composition
	 */
	@ManyToMany(
			mappedBy = "composes" 
			) //cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	public List<Competency> getComposedBy() {
		return composedBy;
	}
	
	public void setComposedBy(List<Competency> composedBy) {
		this.composedBy = composedBy;
	}

	/**
	 * @return similar competencies
	 */
	@ManyToMany// (cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinTable(name = "c_similarity")
	public Set<Competency> getSimilarTo() {
		return similarTo;
	}

	/**
	 * @param set current competency similar to competencies
	 */
	public void setSimilarTo(Set<Competency> similarTo) {
		this.similarTo = similarTo;
	}

	/**
	 * Add a new similar competency.
	 * @param competency
	 * TODO: is that needed actually? usually, you would write
	 * in the manager: c1.getSubsumes().add(c2);
	 * competencyDao.save(c1)
	 * 
	 */
	public void addSimilarTo(Competency competency) {
		getSimilarTo().add(competency);
	}
	
	
	/**
	 * Remove competency from the similarity list.
	 * @param competency
	 * TODO: is that needed actually? usually, you would write
	 * in the manager: c1.getSubsumes().remove(c2);
	 * competencyDao.save(c1)
	 */
	public void removeSimilarTo(Competency competency) {
		getSimilarTo().remove(competency);
	}

	/**
	 * @return the competencies that claim to be similar to the current competency
	 */
	@ManyToMany(
			mappedBy="similarTo")
			// cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	public Set<Competency> getInverseSimilarTo() {
		return inverseSimilarTo;
	}

	/**
	 * @param subsumedBy the subsuming competencies
	 */
	public void setInverseSimilarTo(Set<Competency> inverseSimilarTo) {
		this.inverseSimilarTo  = inverseSimilarTo;
	}

	@Transient
	public Set<Competency> getAllSimilar() {
		setAllSimilar();
		return allSimilar;
	}
	
	public void setAllSimilar() {
		allSimilar.clear();
		allSimilar.addAll(similarTo);
		allSimilar.addAll(inverseSimilarTo);
	}
	
	@OneToMany(cascade = {CascadeType.ALL})
	@JoinTable(name = "c_names")
	public List<Name> getNames() {
		return names;
	}

	public void setNames(List<Name> names) {
		this.names = names;
	}

	@ManyToMany
	@JoinTable(name = "c_topics")
	public Set<Topic> getTopics() {
		return topics;
	}

	public void setTopics(Set<Topic> topics) {
		this.topics = topics;
	}

	@OneToMany(cascade = {CascadeType.ALL})
	@JoinTable(name="c_description")
	public Set<Description> getDescriptions() {
		return descriptions;
	}
	
	public void setDescriptions(Set<Description> descriptions) {
		this.descriptions = descriptions;
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
		if (!(object instanceof Competency)) {
			return false;
		}
		Competency rhs = (Competency) object;
		return new EqualsBuilder()
			.append(getId(), rhs.getId())
			.appendSuper(super.equals(object))
			.append(this.name, rhs.name)
			.append(this.description, rhs.description)
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
			.append(this.name)
			.append(this.description)
			.toHashCode();
	}
	*/
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
			.append("class", getClass().getName())
			.append("id", getId())
			.append("uri", getUri())
			.toString();		
	}
}
