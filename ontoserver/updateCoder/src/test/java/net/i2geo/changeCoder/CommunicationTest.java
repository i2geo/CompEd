package net.i2geo.changeCoder;

import net.i2geo.changeCoder.changes.AddElement;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class CommunicationTest
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public CommunicationTest(String testName)
    {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite(CommunicationTest.class);
    }

    /**
     * Rigorous Test :-)
     * @throws Exception 
     */
    public void testEncoder() throws Exception
    {
        ChangeRequest changeRequest = new ChangeRequest("http://localhost:8080/testresponse",
                "testSession", "http://localhost:8080/i2geo-ontoUpdate/OntoUpdate.do");

        String testCompetency = "<owl:Competency rdf:ID=\"fancy_competency\">"
                + "<gs:commonName xml:lang=\"fr\">compétence fantaisiste</gs:commonName>"
                + "<gs:rareName xml:lang=\"fr\">compétence farfelue</gs:rareName>"
                + "</owl:Competency>";

        changeRequest.addChangeElement(new AddElement(testCompetency));

        // print generated Message
        System.out.append(changeRequest.getMessage());

        // send the request to the server
        boolean result;
        try {
            result = changeRequest.sendMessage();
        }
        catch (Exception e) {
            return;
        }

        // check that everything is ok
        assertTrue(result);
    }

}
