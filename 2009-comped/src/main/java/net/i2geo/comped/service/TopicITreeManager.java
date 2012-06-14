/**
 * 
 */
package net.i2geo.comped.service;

import java.util.Locale;

import com.jenkov.prizetags.tree.itf.ITree;

import net.i2geo.comped.model.AbstractTopic;
import net.i2geo.comped.model.ConcreteTopic;

/**
 * @author Martin Homik
 *
 */
public interface TopicITreeManager {
    	
	public void setLocale(Locale locale);
    public ITree createTree(boolean individuals);
    public ITree createTree(AbstractTopic at);

    public ITree createSubTree(AbstractTopic at);
    public ITree createSuperTree(AbstractTopic at);
    
    public ITree createTree(AbstractTopic tg, boolean individuals);
    public ITree createTree(ConcreteTopic ti, boolean individuals);

    public ITree createTree(ConcreteTopic ti);
    public ITree createTree(AbstractTopic at, boolean individuals, boolean siblings);
    public ITree createTree(ConcreteTopic ti, boolean individuals, boolean siblings);
    public ITree createSubTree(AbstractTopic at, boolean individuals);
    public ITree createSuperTree(AbstractTopic at, boolean individuals);


}
