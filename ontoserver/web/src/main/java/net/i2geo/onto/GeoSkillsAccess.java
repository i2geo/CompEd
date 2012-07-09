package net.i2geo.onto;

import org.semanticweb.owl.model.*;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.io.WriterOutputTarget;
import org.semanticweb.owl.io.OWLOntologyOutputTarget;

import java.net.URI;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.*;
import java.lang.reflect.Constructor;
import java.io.Writer;

/** Commidity class to access the GeoSkills ontology
 */
public class GeoSkillsAccess {

    public static final String ontBaseU = "http://www.inter2geo.eu/2008/ontology/GeoSkills";
    public static final String geoSkillsDevUrl= "http://i2geo.net/ontologies/dev/GeoSkills.owl";
    //"file:///Users/paul/projects/intergeo/ontologies/GeoSkills.owl";
            //"http://i2geo.net/ontologies/dev/GeoSkills.owl";

    private static GeoSkillsAccess theInstance = null;
    public static GeoSkillsAccess getInstance() {
        if(theInstance == null)
            theInstance = new GeoSkillsAccess(geoSkillsDevUrl);
        return theInstance;
    }

    private OWLOntologyManager manager;
    private OWLOntology ont;
    private OWLReasoner reasoner;

    private URL ontologyURL;
    private OWLDataProperty commonNameProp;
    private OWLDataProperty unCommonNameProp;
    private OWLDataProperty rareNameProp;
    private OWLDataProperty falseFriendNameProp;

    private OWLClass competencyClass;
    private OWLClass nameableBitClass;
    private OWLClass topicClass;
    private OWLClass levelClass;

    public OWLClass getTopicClass() {
        return topicClass;
    }
    public OWLClass getNameableBitClass() {
        return nameableBitClass;
    }
    public OWLClass getLevelClass() {
        return levelClass;
    }


    public GeoSkillsAccess(String ontologyURL) {
        try {
            this.ontologyURL = new URL(ontologyURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public GeoSkillsAccess(URL ontologyURL) {
        this.ontologyURL = ontologyURL;
    }


    public void open() throws Exception {
        manager = OWLManager.createOWLOntologyManager();
        ont = manager.loadOntologyFromPhysicalURI(URI.create(ontologyURL.toExternalForm()));
        System.out.println("Ontology loaded.");
        reasoner = createReasoner(manager);
        Set<OWLOntology> importsClosure = manager.getImportsClosure(ont);
        reasoner.loadOntologies(importsClosure);


        competencyClass = manager.getOWLDataFactory().getOWLClass(URI.create("http://www.inter2geo.eu/2008/ontology/GeoSkills#Competency"));
        topicClass = manager.getOWLDataFactory().getOWLClass(URI.create("http://www.inter2geo.eu/2008/ontology/GeoSkills#Topic"));
        nameableBitClass = manager.getOWLDataFactory().getOWLClass(URI.create("http://www.inter2geo.eu/2008/ontology/GeoSkills#NamableBit"));
        levelClass = manager.getOWLDataFactory().getOWLClass(URI.create("http://www.inter2geo.eu/2008/ontology/GeoSkills#EducationalLevel"));
        commonNameProp = manager.getOWLDataFactory().getOWLDataProperty(
            URI.create(ontBaseU + "#commonName"));
        unCommonNameProp = manager.getOWLDataFactory().getOWLDataProperty(
            URI.create(ontBaseU + "#unCommonName"));
        rareNameProp = manager.getOWLDataFactory().getOWLDataProperty(
            URI.create(ontBaseU + "#rareName"));
        falseFriendNameProp = manager.getOWLDataFactory().getOWLDataProperty(
            URI.create(ontBaseU + "#falseFriendName"));


        Set<OWLClass> inconsistentClasses = reasoner.getInconsistentClasses();
        if(inconsistentClasses!=null && !inconsistentClasses.isEmpty()) {
            System.out.println("Inconsistent classes " + inconsistentClasses);
        } else {
            System.out.println("Ontology is consistent.");
        }
        reasoner.classify();
    }



    private static OWLReasoner createReasoner(OWLOntologyManager man) {
           try {
               // Where the full class name for Reasoner is org.mindswap.pellet.owlapi.Reasoner
               // Pellet requires the Pellet libraries  (pellet.jar, aterm-java-x.x.jar) and the
               // XSD libraries that are bundled with pellet: xsdlib.jar and relaxngDatatype.jar
               String reasonerClassName = "org.mindswap.pellet.owlapi.Reasoner";
               Class reasonerClass = Class.forName(reasonerClassName);
               Constructor<OWLReasoner> con = reasonerClass.getConstructor(OWLOntologyManager.class);
               return con.newInstance(man);
           }
           catch (Exception e) {
               throw new RuntimeException(e);
        }
    }


    public OWLOntology getOnt() {
        return ont;
    }

    public String getBaseURI() {
        return ontBaseU;
    }

    public OWLReasoner getReasoner() {
        return reasoner;
    }

    public OWLDataProperty getCommonNameProp() {
        return commonNameProp;
    }

    public OWLDataProperty getUnCommonNameProp() {
        return unCommonNameProp;
    }

    public OWLDataProperty getRareNameProp() {
        return rareNameProp;
    }

    public OWLDataProperty getFalseFriendNameProp() {
        return falseFriendNameProp;
    }

    public OWLClass getCompetencyClass() {
        return competencyClass;
    }

    public OWLClass getOntologyClassOfName(String shortName) {
        return manager.getOWLDataFactory().getOWLClass(URI.create("http://www.inter2geo.eu/2008/ontology/GeoSkills#" + shortName));
    }

    public OWLIndividual getOntologyIndividualOfName(String shortName) {
        return manager.getOWLDataFactory().getOWLIndividual(URI.create("http://www.inter2geo.eu/2008/ontology/GeoSkills#" + shortName));
    }

    public OWLObjectProperty getOntologyPropertyOfName(String shortName) {
        return manager.getOWLDataFactory().getOWLObjectProperty(URI.create("http://www.inter2geo.eu/2008/ontology/GeoSkills#" + shortName));
    }

    public Map<OWLObjectPropertyExpression,Set<OWLIndividual>>
            getPropertyValues(OWLIndividual i) {
        return i.getObjectPropertyValues(ont);
    }

    public String getFirstCommonName(OWLIndividual i) {
        return i.getDataPropertyValues(ont).get(commonNameProp)
                .iterator().next().getLiteral().toString();
    }

    public Map<String,Set<String>> getCommonNames(OWLIndividual i) {
        return getNamesOfProp(i,commonNameProp);
    }
    public Map<String,Set<String>> getUnCommonNames(OWLIndividual i) {
        return getNamesOfProp(i,unCommonNameProp);
    }
    public Map<String,Set<String>> getRareNames(OWLIndividual i) {
        return getNamesOfProp(i,rareNameProp);
    }
    public Map<String,Set<String>> getFalseFriendNames(OWLIndividual i) {
        return getNamesOfProp(i,falseFriendNameProp);
    }

    public Map<String,Set<String>> getNamesOfProp(OWLIndividual i, OWLProperty nameProp) {
        if(i==null) {
            return Collections.emptyMap();
        } else if(i.getDataPropertyValues(ont) == null) {
            return Collections.emptyMap();
        } else {
            Map<String,Set<String>> r = new HashMap<String,Set<String>>();
            Set<OWLConstant> set = i.getDataPropertyValues(ont).get(nameProp);
            if(set==null) return Collections.emptyMap();
            for(OWLConstant x : set) {
                String l = null;
                if(x.isTyped())
                    l=x.asOWLTypedConstant().getLiteral();
                else
                    l= x.asOWLUntypedConstant().getLang();
                Set<String> forLang = r.get(l);
                if(forLang==null) {
                    forLang = new HashSet<String>();
                    r.put(l,forLang);
                }
                forLang.add(x.getLiteral());
            }
            return r;
        }
    }


    public String getCommonName(OWLIndividual i, String[] langs, boolean firstOrIdIfFail) {
        // first name in given language
        if(i==null || i.getDataPropertyValues(ont)==null || i.getDataPropertyValues(ont).get(commonNameProp)==null)
            return firstOrIdIfFail && i!=null ? i.getURI().getFragment() : null;
        for(String lang:langs) {
            for(OWLConstant x : i.getDataPropertyValues(ont).get(commonNameProp)) {
                String l = null;
                if(x.isTyped())
                    l=x.asOWLTypedConstant().getLiteral();
                else
                    l= x.asOWLUntypedConstant().getLang();
                if(lang.equals(l))
                    return x.getLiteral();
            }
        }
        // otherwise first name without language
        for(OWLConstant x : i.getDataPropertyValues(ont).get(commonNameProp)) {
            if(!x.isTyped() && !x.asOWLUntypedConstant().hasLang())
                return x.getLiteral();
        }
        if(!firstOrIdIfFail) return null;

        // otherwise the first name
        for(OWLConstant x : i.getDataPropertyValues(ont).get(commonNameProp)) {
            return x.getLiteral(); // fancy way to take the first of an iterator if there's one!
        }

        // otherwise the identifier's fragment
        return i.getURI().getFragment();

    }

    public Set<OWLIndividual> getIndividualsOfClass(OWLClass cls) {
        try {
            return reasoner.getIndividuals(cls,false);
        } catch (OWLReasonerException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public Set<OWLClass> getDescendantClasses(OWLClass cls) {
        try {
            Set<Set<OWLClass>> subs = reasoner.getDescendantClasses(cls);
            Set<OWLClass> r = new HashSet<OWLClass>();
            for(Set<OWLClass> s: subs) {
                r.addAll(s);
            }
            return r;
        } catch (OWLReasonerException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public Set<String> toURIsListIndiv(Set<OWLIndividual> desc) {
        if(desc==null || desc.size()==0) return Collections.EMPTY_SET;
        Set<String> s= new HashSet<String>(desc.size());
        for(OWLIndividual i:desc) {
            s.add(i.getURI().toASCIIString());
        }
        return s;
    }
    public Set<String> toURIsList(Set<OWLDescription> desc) {
        Set<String> s= new HashSet<String>(desc.size());
        for(OWLDescription obj:desc) {
            if(obj instanceof OWLNamedObject )
                s.add(((OWLNamedObject)obj).getURI().toASCIIString());
        }
        return s;
    }

    public boolean isOfLevelType(OWLIndividual node) {
        for(OWLDescription desc: node.getTypes(ont)) {
            if(isSubclassOf(desc.asOWLClass(),levelClass)) return true;
        }
        return false;
    }

    public boolean isOfTopicType(OWLIndividual node) {
        for(OWLDescription desc: node.getTypes(ont)) {
            if(isSubclassOf(desc.asOWLClass(),topicClass)) return true;
        }
        return false;
    }
    public boolean isOfCompetencyType(OWLIndividual node) {
        for(OWLDescription desc: node.getTypes(ont)) {
            if(isSubclassOf(desc.asOWLClass(),competencyClass)) return true;
        }
        return false;
    }
    public boolean isSubclassOf(OWLClass cls, OWLClass expectedSup) {
        if(cls==null) return false;
        if(cls.equals(expectedSup)) return true;
        for(OWLDescription sup: cls.getSuperClasses(ont)) {
            if(sup.isAnonymous()) continue;
            if(isSubclassOf(sup.asOWLClass(),expectedSup)) return true;
        }
        return false;
    }

    public void saveOntology(Writer out) throws OWLOntologyStorageException {
        manager.saveOntology(getOnt(),new WriterOutputTarget(out));
    }

}