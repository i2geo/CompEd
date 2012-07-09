package net.i2geo.changeCoder;

import java.io.*;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class CommunicationTestFullXML
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public CommunicationTestFullXML(String testName)
    {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite(CommunicationTestFullXML.class);
    }

    /**
     * Rigorous Test :-)
     * @throws Exception 
     */
    public void testEncoder() throws Exception
    {
        StringBuffer xml = new StringBuffer();
        File file = new File("src/test/resources/net/i2geo/onto/updates/sample-update-test.xml");
        FileReader fr = new FileReader(file);
        
        
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        while (line != null ) {
            xml.append(line);
            line = br.readLine();
        }
        
        ChangeRequest changeRequest = new ChangeRequest(xml.toString(),
                "http://localhost:8080/i2geo-ontoUpdate/OntoUpdate.do");

//        changeRequest.addChangeElement(new AddElement(testCompetency));

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
