/**
 * 
 */
package net.i2geo.comped.service;

import java.util.Locale;

import com.jenkov.prizetags.tree.itf.ITree;
import net.i2geo.comped.model.CompetencyProcess;
import net.i2geo.comped.model.ConcreteCompetency;

/**
 * @author Martin Homik
 *
 */
public interface CompetencyITreeManager {
    	
	public void setLocale(Locale locale);
    public ITree createTree(boolean individuals);
    public ITree createTree(CompetencyProcess cp);

    public ITree createSubTree(CompetencyProcess cp);
    public ITree createSuperTree(CompetencyProcess cp);
    
    public ITree createTree(ConcreteCompetency cc);
    public ITree createTree(CompetencyProcess cp, boolean individuals, boolean siblings);
    public ITree createTree(ConcreteCompetency cc, boolean individuals, boolean siblings);
    public ITree createSubTree(CompetencyProcess cp, boolean individuals);
    public ITree createSuperTree(CompetencyProcess cp, boolean individuals);
}
