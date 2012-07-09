package net.i2geo.onto;

import net.i2geo.onto.tasks.GeoSkillsParser;
import net.i2geo.onto.parse.GeoSkillsParseListener;

import java.util.Set;
import java.util.Iterator;
import java.util.HashSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;

/** Simple stub that goes to system out.
 */
public class TestParseListening extends junit.framework.TestCase  {

  public TestParseListening(String name) { super(name); }



    private class ParseListeningSysout implements GeoSkillsParseListener {
        public void thereIsItem(String uri, NodeType type) {
            System.out.println("There is item: " + shortenName(uri) + " of type " + type + ".");
        }

        public void topicType(String uri, Set<String> parentTypeURIs, NamesMap names) {
            System.out.println("Topic type: " + shortenName(uri) + " of types " + shortenName(parentTypeURIs) + ": ");
            outputNames(names);
        }

        public void topicDescription(String uri, Set<String> parentTypeURIs, NamesMap names) {
            System.out.println("Topic : " + shortenName(uri) + " of types " + shortenName(parentTypeURIs) + ": ");
            outputNames(names);
        }

        public void competencyType(String uri, Set<String> parentTypeURIs, NamesMap names) {
            System.out.println("Competency type: " + shortenName(uri) + " of types " + shortenName(parentTypeURIs)+ ": ");
            outputNames(names);
        }


        public void competencyDescription(String uri, Set<String> parentTypeURIs, Set<String> hasTopicsURIs, NamesMap names) {
            System.out.println("Competency: " + shortenName(uri) + " of types " + shortenName(parentTypeURIs) + ": ");
            outputNames(names);
            System.out.println("Topics: " + shortenName(hasTopicsURIs));
        }

    }

    private static void outputNames(GeoSkillsParseListener.NamesMap map) {
        for(Iterator<String> it = map.getLanguages(); it.hasNext(); ) {
            String lang = it.next();
            for(Iterator<GeoSkillsParseListener.NameWithFrequency> i=map.getNames(lang); i.hasNext(); ) {
                System.out.print(lang + " : ");
                GeoSkillsParseListener.NameWithFrequency nf = i.next();
                System.out.print(nf.getFrequency());
                System.out.print(" : ");
                System.out.println(nf.getName());
            }
        }
    }

    public void testOutputGeoSkills() throws Throwable {
        try {
            Level level = Logger.getLogger("org.mindswap.pellet.ABox").getLevel();
            Logger.getLogger("org.mindswap.pellet.ABox").setLevel(Level.ERROR);
            new GeoSkillsParser().runParser(new ParseListeningSysout());
            Logger.getLogger("org.mindswap.pellet.ABox").setLevel(level);
        } catch(Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    private String ontBaseU = GeoSkillsAccess.getInstance().getBaseURI();
    private int ontBaseUL = ontBaseU.length();

    public String shortenName(String maybeFullURI) {
        if(maybeFullURI==null) return null;
        if(maybeFullURI.startsWith(ontBaseU))
            return maybeFullURI.substring(ontBaseUL);
        return maybeFullURI;
    }
    public Set<String> shortenName(Set<String> maybeFullURIs) {
        if(maybeFullURIs==null) return null;
        Set<String> r = new HashSet<String>(maybeFullURIs.size());
        for(String s: maybeFullURIs) {
            r.add(shortenName(s));
        }
        return r;
    }
}
