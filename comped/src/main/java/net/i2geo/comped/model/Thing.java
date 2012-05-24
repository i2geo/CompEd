/*
 * Thing.java
 *
 * Created on 2. July 2008, 16:29
 *
 * Creator: Martin Homik
 *
 */

package net.i2geo.comped.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.appfuse.model.BaseObjectIdentified;
import org.appfuse.model.User;
import org.hibernate.annotations.Index;

/**
 *
 * @author Martin Homik
 */

@SuppressWarnings("serial")
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@Table(name = "thing")
public class Thing extends BaseObjectIdentified implements Identifiable, URIAddressable {
    	
    public static final String STATUS_DRAFT         = "DRAFT";
    public static final String STATUS_PUBLISHED     = "PUBLISHED";
    public static final String STATUS_PENDING       = "PENDING";
    public static final String STATUS_SCHEDULED     = "SCHEDULED";

    public static final String COMMENTS_OPENED      = "OPENED";
    public static final String COMMENTS_CLOSED      = "CLOSED";
    public static final String COMMENTS_REGISTEREDD = "REGISTERED";
    
    private static final String defaultNameSpace    =
    	"http://www.inter2geo.eu/2008/ontology/GeoSkills";
    
	private Long id;
	
	// Dates
    private Date 	created           = null;
    private Date 	modified          = null;

    // Status
    private String 	status            = null;
    private String 	commentStatus     = null; 

    // Other
    private Integer commentDays       = new Integer(7);
    private User    creator			  = null;

    private String uri;
    
	/** Creates a new instance of Thing */
    public Thing() {
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
     * Get the user who created this resource.
     * @return
     */
    @ManyToOne
	@JoinColumn(name="creator_id", nullable = false, updatable = false)
    public User getCreator() {
		return creator;
	}

    /**
     * Set the user who created this resource.
     * @param creator
     */
	public void setCreator(User creator) {
		this.creator = creator;
	}

	@Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "date_created", nullable = false)
    public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}    

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "date_modified", nullable = false)
	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}
	

	/**
	 * 
	 * Get comment status. Possible comment states are: "DRAFT", "PUBLISHED",
	 * "PENDING", and "SCHEDULED".
	 * Note that all classes derived from THING can be commented. 
	 *
	 * @return
	 */
	@Column(name="status", updatable=true, insertable=true, unique=false, nullable=false)
	public String getStatus() {
		return status;
	}

	/**
	 * 
	 * Set comment status. Possible comment states are: "DRAFT", "PUBLISHED", 
	 * "PENDING", and "SCHEDULED".
	 * Note that all classes derived from THING can be commented. 
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Set comments for this thing. Anything can be commented.
	 * 
	 * @return
	 
	@ManyToMany
	@JoinTable(name = "seepo_thing_seepo_comment")
    public List<BlogEntry> getComments() {
		return comments;
	}
	*/
	
	/**
	 * Get comments for this thing. Anything can be commented.
	 
	public void setComments(List<BlogEntry> comments) {
		this.comments = comments;
	}
	*/
	
	/**
	 * 
	 * Get comment status. Possible comment states are: "OPENED", "CLOSED", and "REGISTERED".
	 * Note that all classes derived from THING can be commented. 
	 * 
	 * @return commentStatus
	 */
	@Column(name="comment_status", updatable=true, insertable=true, unique=false, nullable=false)
	public String getCommentStatus() {
		return commentStatus;
	}

	/**
	 * Set comment status. Possible comment states are: "OPENED", "CLOSED", and "REGISTERED".
	 * Note that all classes derived from THING can be commented. 
	 * @param commentStatus
	 */
	public void setCommentStatus(String commentStatus) {
		this.commentStatus = commentStatus;
	}

	/**
	 * 
     * Number of days after pubTime that comments should be allowed, or 0 for no limit.
	 * @return
	 */
	@Column(name="comment_days", updatable=true, insertable=true, unique=false, nullable=false)
	public Integer getCommentDays() {
		return commentDays;
	}

	/**
     * Number of days after pubTime that comments should be allowed, or 0 for no limit.
	 * @param commentDays
	 */
	public void setCommentDays(Integer commentDays) {
		this.commentDays = commentDays;
	}

	/**
	 * The uri points to a location where the model of the competency is defined.
	 * @return the name
	 */
	@Index(name = "INDEX_COMPETENCY_URI")
	@Column(name="uri", updatable=true, insertable=true, unique=true, nullable=true, length=255)
	public String getUri() {
		return uri;
	}

	/**
	 * @param name the name to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	@Transient
	public String getUriSuffix() {
		String defaultMatch = "GeoSkill#";

		return getUriSuffix(defaultMatch);
	}

	@Transient
	public String getUriSuffix(String match) {		
		if (match == null || match.isEmpty()) {
			match = "GeoSkills#";
		}
        String uri = getUri();
		
		if (uri == null || uri.isEmpty()) {
			return "";
		}

		int uriIndex = uri.lastIndexOf(match);
        if(uriIndex==-1) {
            uriIndex = uri.indexOf(match);
            if(uriIndex==-1) uriIndex = defaultNameSpace.length() + 1;
            if(uriIndex > uri.length()) uriIndex = -1;
        } else uriIndex = uriIndex + match.length();
		String postUri= null;
        if(uriIndex>-1)
            postUri = getUri().substring(uriIndex);
        else postUri = uri;
	
		return postUri;
	}

	public void setUriSuffix(String suffix) {
		suffix = suffix.replace(" ", "");
		suffix = suffix.replace("#", "");
		suffix = suffix.replace("/", "");
		suffix = suffix.replace(" ", "");

		if (getUri() == null || getUri().isEmpty()) {
			setUri(defaultNameSpace + "#" + suffix);
		} else {
			int index = getUri().lastIndexOf("#");
			String prefix = getUri().substring(0, index+1);
			setUri(prefix+suffix);
		}
	}
	
	/**
	 * @see java.lang.Object#equals(Object)
	 */
	/*
	public boolean equals(Object object) {
		if (!(object instanceof Thing)) {
			return false;
		}
		Thing rhs = (Thing) object;
		return new EqualsBuilder()
			.append(this.id, rhs.id)
			.append(this.created, rhs.created)
			.append(this.modified, rhs.modified)
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
		.append(this.created)
		.append(this.modified)
		.toHashCode();
	}
	*/
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this)
		.append("id", this.id)
		.append("created", this.created)
		.append("modified", this.modified)
		.append("status", this.status)
		.append("commentStatus", this.commentStatus)
		.toString();
	}
}
