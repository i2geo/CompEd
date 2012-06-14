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
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * @author Martin Homik
 *
 */
@Entity
@Table(name="c_process")
public class CompetencyProcess extends Competency {

	private static final long serialVersionUID = 8381425523693909706L;

	/**
	 * Set of concrete competencies that are part included in a competency process.
	 */
	private List<ConcreteCompetency> ccompetencies = new ArrayList<ConcreteCompetency>();

	/**
	 * Structure for storing competency process taxonomy.
	 */
	private List<CompetencyProcess> children = new ArrayList<CompetencyProcess>();
	private List<CompetencyProcess> parent = new ArrayList<CompetencyProcess>();
	
	/**
	 * Get set of concrete competencies in the current competency process.
	 * @return
	 */
	@ManyToMany(mappedBy="processes")
	public List<ConcreteCompetency> getCcompetencies() {
		return ccompetencies;
	}

	/**
 	 * Set the set of concrete competencies in the current competency process.
	 * @param ccompetencies
	 */
	public void setCcompetencies(List<ConcreteCompetency> ccompetencies) {
		this.ccompetencies = ccompetencies;
	}
	
	/**
	 * @return children processes
	 */
	@ManyToMany(mappedBy="parent") 
	@OrderBy
	public List<CompetencyProcess> getChildren() {
		return children;
	}

	/**
	 * @param set of sub processes
	 */
	public void setChildren(List<CompetencyProcess> children) {
		this.children = children;
	}

	/**
	 * Add a new sub process.
	 * @param competency process
	 * TODO: is that needed actually? usually, you would write
	 * in the manager: c1.getSubProcess().add(c2);
	 * competencyProcessDao.save(c1)
	 * 
	 */
	public void addChildren(CompetencyProcess cp) {
		getChildren().add(cp);
	}
	
	
	/**
	 * Remove competency process from the children set.
	 * @param competency process
	 * TODO: is that needed actually? usually, you would write
	 * in the manager: c1.getSubProcesses().remove(c2);
	 * competencyProcessDao.save(c1)
	 */
	public void removeChildren(CompetencyProcess cp) {
		getChildren().remove(cp);
	}
	
	/**
	 * @return the parent competency processes
	 */
	@ManyToMany
	@OrderBy
	@JoinTable(name = "cp_subprocesses",
			joinColumns=@JoinColumn(name="child_id", referencedColumnName="id"),
			inverseJoinColumns=@JoinColumn(name="parent_id", referencedColumnName="id"))
	public List<CompetencyProcess> getParent() {
		return parent;
	}

	/**
	 * @param subsumedBy parent competency processes
	 */
	public void setParent(List<CompetencyProcess> parent) {
		this.parent = parent;
	}
	/**
	 * Add one more parent to the list of parents of thsi item.
	 * 
	 * @param parent
	 */
	public void addParent(CompetencyProcess parent) {
		getParent().add(parent);
	}

}
