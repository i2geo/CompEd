/*
 * Description.java
 *
 * Created on 2. July 2008, 16:29
 *
 * Creator: Martin Homik
 *
 */

package net.i2geo.comped.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.appfuse.model.BaseObjectIdentified;

/**
 *
 * @author Martin Homik
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "description")
public class Description extends BaseObjectIdentified {
    	
	private Long id;
	
    private String description	= null;
    private String locale     	= null;
    // private Thing thing         = null;
    
	/** Creates a new instance of Description */
    public Description() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * Get description.
     * @return
     */
    @Lob
    @Column(name = "description", nullable=false, insertable=true, unique=false, updatable=true)
	public String getDescription() {
		return description;
	}

	/**
	 * Set description.
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get locale.
	 * @return
	 */
    @Column(name = "locale", nullable=false, insertable=true, unique=false, updatable=true, length=6)
	public String getLocale() {
		return locale;
	}

	/**
	 * Set locale.
	 * @param locale
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	
	/**
	 * Get Thing for which the description is valid.
	 * @return
	 */
	/*
	@ManyToOne
	public Thing getThing() {
		// owning side
		return thing;
	}
	*/
	
	/**
	 * Set thing for which the description is valid.
	 * @param thing
	 */
	/*
	public void setThing(Thing thing) {
		this.thing = thing;
	}
	*/
	
	/**
	 * @see java.lang.Object#equals(Object)
	 */
	/*
	public boolean equals(Object object) {
		if (!(object instanceof Description)) {
			return false;
		}
		Description rhs = (Description) object;
		return new EqualsBuilder()
			.append(this.id, rhs.id)
			.append(this.description, rhs.description)
			.append(this.locale, rhs.locale)
			.isEquals();
	}
	*/
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	/*
	public int hashCode() {
		return new HashCodeBuilder(568762737, 708093593)
		.append(this.id)
		.append(this.description)
		.append(this.locale)
		.toHashCode();
	}
	*/
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id)
		.append("description", this.description)
		.append("locale", this.locale)
		.toString();
	}
}
