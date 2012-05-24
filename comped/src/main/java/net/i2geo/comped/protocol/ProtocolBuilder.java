/**
 * 
 */
package net.i2geo.comped.protocol;

import net.i2geo.comped.model.AbstractTopic;
import net.i2geo.comped.model.CompetencyProcess;
import net.i2geo.comped.model.ConcreteCompetency;
import net.i2geo.comped.model.ConcreteTopic;
import net.i2geo.comped.model.Thing;
import net.i2geo.comped.model.Topic;

/**
 * @author mhomik
 *
 */
public interface ProtocolBuilder {

	public void add(ConcreteCompetency c);
	public void add(CompetencyProcess c);
	public void add(ConcreteTopic t); 
	public void add(AbstractTopic t); 

	public void update(ConcreteCompetency c);
	public void update(CompetencyProcess c);
	public void update(ConcreteTopic t); 
	public void update(AbstractTopic t);

	public void delete(ConcreteCompetency c);
	public void delete(CompetencyProcess c);
	public void delete(ConcreteTopic t);
	public void delete(AbstractTopic t);
	
	public void delete(Thing t);
	public String getXML();
}
