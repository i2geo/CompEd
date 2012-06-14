package org.appfuse.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;


/**
 * BaseObject implementing equals and hashCode to be safe for
 * HashSets and HashMaps.<br>
 * Extended the class to contain some additional values, like
 * a version flag.<br><br>
 * This base implementation is not meant to be used by all sub-
 * classes. You are encouraged to implement your own equals and
 * hashCode methods, whenever you can find an appropriate businnes
 * key for your objects, which is immutable enough to last during
 * the lifetime of a hashed Collection. This implementation is
 * thought of as a makeshift for classes, where you cannot build
 * an immutable businness key. It also has one major drawback, see
 * comment of {@link #getObject()} for more information.
 * 
 * 
 * @see http://www.avaje.org/equals.html#gerry for details.
 * 
 * @author Gerry Power<br>
 * 		Extended with JPA annotations by Tobias Vogel
 *
 */
@MappedSuperclass
public abstract class BaseObjectIdentified extends BaseObject {

	private transient volatile Object object;
	private Long id;
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return this.id;
	}
	
	/**
	 * Sets the id of this object
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/* (non-Javadoc)
	 * @see org.appfuse.model.BaseObject#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		final boolean ret;
		if (obj instanceof BaseObjectIdentified) {
			return getObject().equals(((BaseObjectIdentified) obj).getObject());
		} else {
			ret = false;
		}
		return ret;
	}

	/* (non-Javadoc)
	 * @see org.appfuse.model.BaseObject#hashCode()
	 */
	@Override
	public int hashCode() {
		return getObject().hashCode();
	}
	
	/**
	 * Returns the identity object. If the object has not been persisted,
	 * a new object is constructed, which will provide the hashCode of
	 * this object during its lifetime. When an object is (re)loaded from
	 * the database at a later time, the hashCode will always be generated
	 * from the id.<br>
	 * It does mean that a pre-persisted object and <b>new instance<b>/
	 * retrieved object will not be equal, but two new instances of the same
	 * retrieved object will always be equal regardless of session or JVM.
	 * So you can freely mix and match disconnected objects (even across JVMs),
	 * and you can mix and match pre-persisted and persisted objects (with the
	 * same JVM), but you can't mix and match newly retrieved and disconnected
	 * objects with pre-persisted objects.
	 * 
	 * @return The object to be used for hashCode and equals
	 */
	@Transient
	private Object getObject() {
		if ((object != null) || ((object == null) && (id == null))) {
			if (object == null) {
				synchronized (this) {
					if (object == null) { object = new Object(); }
				}
			}
			return object;
		}
		return id;
	}

}
