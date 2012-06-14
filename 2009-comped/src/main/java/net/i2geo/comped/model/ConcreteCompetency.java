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

/**
 * @author Martin Homik
 *
 */
@Entity
@Table(name="c_concrete")
public class ConcreteCompetency extends Competency {

	private static final long serialVersionUID = 4622359971279818195L;

	private List<CompetencyProcess> processes = new ArrayList<CompetencyProcess>();

	@ManyToMany
	@JoinTable(name="ccs_cp",
			joinColumns=@JoinColumn(name="cc_id", referencedColumnName="id"),
			inverseJoinColumns=@JoinColumn(name="cp_id", referencedColumnName="id"))
	public List<CompetencyProcess> getProcesses() {
		return processes;
	}

	public void setProcesses(List<CompetencyProcess> processes) {
		this.processes = processes;
	}
	
	
}
