package net.i2geo.onto.tasks;

import net.i2geo.onto.GeoSkillsAccess;

public class ConsistencyCheck {

    public static void main(String[] args) throws Exception {
        GeoSkillsAccess gs =
                new GeoSkillsAccess("file:///Users/paul/projects/intergeo/ontologies/GeoSkills.owl");
        gs.open();
    }

}
