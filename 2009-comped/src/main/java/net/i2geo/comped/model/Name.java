/*
 * Name.java
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
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.appfuse.model.BaseObjectIdentified;
import org.hibernate.annotations.Index;

/**
 *
 * @author Martin Homik
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "name")
public class Name extends BaseObjectIdentified implements Identifiable {
    	
    public static final String TYPE_COMMON		= "COMMON";
    public static final String TYPE_UNCOMMON    = "UNCOMMON";
    public static final String TYPE_RARE        = "RARE";
    public static final String TYPE_FALSEFRIEND = "FALSEFRIEND";

    public static final String[] TYPE_ARRAY = {TYPE_COMMON, TYPE_UNCOMMON, TYPE_RARE, TYPE_FALSEFRIEND};
    		
    private Long id;

    private String  name       = null;
    private String  locale     = null;
    private String  type       = null;
    private boolean defName    = false;
    
	/** Creates a new instance of Thing */
    public Name() {
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
     * Get name.
     * @return
     */
	@Index(name = "INDEX_NAME_NAME")
    @Column(name = "name", nullable=false, insertable=true, unique=false, updatable=true, length=256)
	public String getName() {
		return name;
	}

	/**
	 * Set name.
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
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
	 * Get type for this name. Allowed types are "COMMON", "UNCOMMON", "RARE", and "FALSEFRIEND". 
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * Set type for this name. Allowed types are "COMMON", "UNCOMMON", "RARE", and "FALSEFRIEND". 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Return true when name is the default value.
	 * @return
	 */
    @Column(name = "isDefName", nullable=false, insertable=true, unique=false, updatable=true)
	public boolean isDefName() {
		return defName;
	}

	/**
	 * Set name as default name.
	 * @param defName
	 */
	public void setDefName(boolean defName) {
		this.defName = defName;
	}
	
	// return boolean values that indicate a type match
	@Transient
	public boolean isCommon() {
		return type.equals(TYPE_COMMON);
	}

	@Transient
	public boolean isUncommon() {
		return type.equals(TYPE_UNCOMMON);
	}

	@Transient
	public boolean isRare() {
		return type.equals(TYPE_RARE);
	}

	@Transient
	public boolean isFalseFriend() {
		return type.equals(TYPE_FALSEFRIEND);
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof Name)) {
			return false;
		}
		Name rhs = (Name) object;
		return new EqualsBuilder()
			.append(this.id, rhs.id)
			.append(this.name, rhs.name)
			.append(this.type, rhs.type)
			.append(this.locale, rhs.locale)
			.isEquals();
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(568762737, 708093593)
		.append(this.id)
		.append(this.name)
		.append(this.locale)
		.append(this.type)
		.toHashCode();
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id)
		.append("name", this.name)
		.append("locale", this.locale)
		.append("type", this.type)
		.append("default", this.defName)
		.toString();
	}
}
