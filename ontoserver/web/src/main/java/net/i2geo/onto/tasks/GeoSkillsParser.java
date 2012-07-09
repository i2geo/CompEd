package net.i2geo.onto.tasks;

import net.i2geo.onto.GeoSkillsAccess;
import net.i2geo.onto.parse.GeoSkillsParseListener;
import org.semanticweb.owl.model.*;
import org.semanticweb.owl.vocab.OWLRDFVocabulary;

import java.util.*;

/**
 */
public class GeoSkillsParser {



    public GeoSkillsParser() {
        this(null);
    }

    public GeoSkillsParser(String urlToOntology) {
        if(urlToOntology==null)
            urlToOntology = GeoSkillsAccess.geoSkillsDevUrl;
        this.urlToOntology = urlToOntology;
    }

    public void setUrlToOntology(String url) {
        this.urlToOntology = url;
    }

    private String urlToOntology = null;
    GeoSkillsAccess access;

    public void runParser(GeoSkillsParseListener listener) throws Exception {

        this.access = new GeoSkillsAccess(urlToOntology);
        this.access.open();

        // first notify of IDs

        for(OWLClass cls: access.getDescendantClasses(access.getTopicClass())) {
            listener.thereIsItem(cls.getURI().toASCIIString(),
                    GeoSkillsParseListener.TYPE_TOPIC_CLASS);
        }
        for(OWLClass cls: access.getDescendantClasses(access.getCompetencyClass())) {
            listener.thereIsItem(cls.getURI().toASCIIString(),
                    GeoSkillsParseListener.TYPE_COMPETENCY_CLASS);
        }
        for(OWLIndividual i: access.getIndividualsOfClass(access.getTopicClass())) {
            listener.thereIsItem(i.getURI().toASCIIString(),
                    GeoSkillsParseListener.TYPE_TOPIC);
        }
        for(OWLIndividual i: access.getIndividualsOfClass(access.getCompetencyClass())) {
            listener.thereIsItem(i.getURI().toASCIIString(),
                    GeoSkillsParseListener.TYPE_COMPETENCY);
        }

        // then describe types

        listener.topicType(access.getTopicClass().getURI().toASCIIString(), Collections.EMPTY_SET,
                readRDFLabels(access.getTopicClass()));
        for(OWLClass i: access.getDescendantClasses(access.getTopicClass())) {
            Set<String> parentTypes =
                access.toURIsList(i.getSuperClasses(access.getOnt()));
            listener.topicType(i.getURI().toASCIIString(), parentTypes,
                    readRDFLabels(i));
        }
        listener.competencyType(access.getCompetencyClass().getURI().toASCIIString(), Collections.EMPTY_SET,
                readRDFLabels(access.getCompetencyClass()));
        for(OWLClass i: access.getDescendantClasses(access.getCompetencyClass())) {
            Set<String> parentTypes = access.toURIsList(i.getSuperClasses(access.getOnt()));
            listener.competencyType(i.getURI().toASCIIString(), parentTypes,
                    readRDFLabels(i));
        }

        // then describe individuals
        for(OWLIndividual i: access.getIndividualsOfClass(access.getTopicClass())) {
            String uri = i.getURI().toASCIIString();
            Set<String> parentUris = access.toURIsList(i.getTypes(access.getOnt()));
            listener.topicDescription(uri,parentUris,makeNamesMap(i));
        }
        for(OWLIndividual i: access.getIndividualsOfClass(access.getCompetencyClass())) {
            String uri = i.getURI().toASCIIString();
            Set<String> parentUris = access.toURIsList(i.getTypes(access.getOnt()));
            Set<OWLIndividual> s = i.getObjectPropertyValues(access.getOnt()).get(access.getOntologyPropertyOfName("hasTopic"));
            listener.competencyDescription(uri,parentUris,access.toURIsListIndiv(s),makeNamesMap(i));
        }
    }

    private NamesMap makeNamesMap(OWLIndividual i) {
        NamesMap namesMap = new NamesMap();
        feedNamesInMap(access.getCommonNames(i),namesMap,1.0f);
        feedNamesInMap(access.getUnCommonNames(i),namesMap,0.7f);
        feedNamesInMap(access.getRareNames(i),namesMap,0.3f);
        feedNamesInMap(access.getFalseFriendNames(i),namesMap,-1.0f);
        return namesMap;
    }

    private void feedNamesInMap(Map<String,Set<String>> omap, NamesMap namesMap, float score) {
        for(Map.Entry<String,Set<String>> entry: omap.entrySet()) {
            if(entry==null) continue;
            String lang = entry.getKey();
            Set<GeoSkillsParseListener.NameWithFrequency> namesForLanguage = namesMap.map.get(lang);
            if(namesForLanguage==null) {
                namesForLanguage = new HashSet<GeoSkillsParseListener.NameWithFrequency>();
                namesMap.map.put(lang,namesForLanguage);
            }
            for(String s:entry.getValue()) {
                NameWithFrequencyImpl n = new NameWithFrequencyImpl();
                n.frequency = score;
                n.string = s;
                namesForLanguage.add(n);
            }
        }
    }


    public static class NamesMap implements GeoSkillsParseListener.NamesMap {
        public NamesMap(){}
        private Map<String,Set<GeoSkillsParseListener.NameWithFrequency>> map = new HashMap<String,Set<GeoSkillsParseListener.NameWithFrequency>>();
        public Iterator<String> getLanguages(){return map.keySet().iterator();}
        public Iterator<GeoSkillsParseListener.NameWithFrequency> getNames(String language)
            {return map.get(language).iterator(); }

    }
    public static class NameWithFrequencyImpl implements GeoSkillsParseListener.NameWithFrequency {

        float frequency;
        String string;

        public float getFrequency() {return frequency;}
        public String getName() { return string; }
    }

    public NamesMap readRDFLabels(OWLClass desc) {
        NamesMap map = new NamesMap();
        for(OWLAnnotation annotation: desc.getAnnotations(access.getOnt())) {
            if(!OWLRDFVocabulary.RDFS_LABEL.getURI().equals(annotation.getAnnotationURI()))
                continue;
            OWLConstant constant = annotation.getAnnotationValueAsConstant();
            if(!constant.isTyped()) {
                OWLUntypedConstant label = constant.asOWLUntypedConstant();
                Set<GeoSkillsParseListener.NameWithFrequency> names = map.map.get(label.getLang());
                if(names==null) {
                    names = new HashSet<GeoSkillsParseListener.NameWithFrequency>();
                    map.map.put(label.getLang(),names);
                }
                NameWithFrequencyImpl name = new NameWithFrequencyImpl();
                name.frequency = 1.0f;
                name.string = label.getLiteral();
                names.add(name);
            }
        }
        return map;
    }
}
