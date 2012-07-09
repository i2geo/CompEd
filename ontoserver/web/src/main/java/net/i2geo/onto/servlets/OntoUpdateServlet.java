/**
 * 
 */
package net.i2geo.onto.servlets;

import java.io.*;
import java.util.concurrent.*;
import java.nio.charset.Charset;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.log4j.*;

import net.i2geo.onto.parse.OntoUpdateParser;
import net.i2geo.onto.updates.actions.*;

/**
 * @author afaulhab
 *
 */
public class OntoUpdateServlet extends HttpServlet {

	private static final long serialVersionUID = 7959486524274389854L;

	private Logger log = LogManager.getLogger(this.getClass().toString());

	// run in test mode?
    final boolean test = false;

    // The queue of Actions to be performed
    // maybe should become a priority queue...
    private static BlockingQueue<OntologyActionBase> actionWorkQueue =
        new LinkedBlockingQueue<OntologyActionBase>();  

    // The thread which actually handles all the work (updates to
    // the ontology)
    private static Thread worker = null;

    // A nice little constructor, which starts the worker-thread
    public OntoUpdateServlet()
    {
    }

    // overwritten Method which is invoked by the application server
    public void init(ServletConfig config) throws ServletException {
    	log.info("Initializing ontoUpdate servlet ...");
    	super.init(config);
        //        worker.setDaemon(true);
        if (worker == null) {
            worker = new OntoUpdateWorker(actionWorkQueue);
            worker.start();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

    	log.info("handling get request ...");

        resp.setContentType("text/html");
        PrintWriter pw = new PrintWriter(resp.getOutputStream());

        pw.println("<html>");
        pw.println(" <head><title>Ok</title></head>");
        pw.println(" <body>");

        pw.println("Please use the POST-Method to send data to this servlet...");

        pw.println("</body>");
        pw.println("</html>");
        pw.flush();
        pw.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

    	log.info("Hndling post request ...");

    	// first get the XML (encoded in variable "action")
        Reader xmlReader;
        if("application/x-www-form-urlencoded".equals(req.getContentType())) {
            // basically: it was submitted as a form
            String str = req.getParameter("action");
            byte[] b = new byte[str.length()];
            for(int i=0; i<b.length; i++) b[i] = (byte) str.charAt(i);
            // for the encoding utf-8
            xmlReader = new InputStreamReader(new ByteArrayInputStream(b),Charset.forName("utf-8"));
        } else {
            xmlReader = req.getReader();
        }
        StringBuffer buff = new StringBuffer();
        char[] b = new char[16]; int r=0;
        while((r=xmlReader.read(b,0,16))!=-1) {
            buff.append(b,0,r);
        }
        String xmlString = buff.toString();
        



        if (test) {
            this.addToQueue(new OntologyActionBase() {
                public String getActionId() {
                    return "DummyTestAction";
                };

                public String getInitiatorID() {
                    return "mySelf";
                };

                public java.net.URL getResultNotificationURL() {
                    return null;
                };

                public void perform()
                    throws org.semanticweb.owl.model.OWLOntologyChangeException {
                    System.out.println("Dummy Action!!!");
                };
            });
            printAction(xmlString, resp);
        }
        else {
            try {
                OntoUpdateSession actions = new OntoUpdateParser().parseActions(xmlString);

                // set the manager
                this.addToQueue(actions);
                // send ok-Response
                sendOk(resp);
            }
            catch (ActionCreationException ex) {
                log.error(ex.getStackTrace().toString(), ex);
                sendParseError(resp, xmlString);
            }
            catch (Exception ex) {
                // check what error and send back ErrorMessage
                log.error(ex.getStackTrace().toString(), ex);
                sendParseError(resp, xmlString);

            }
        }
    }

    private void sendParseError(HttpServletResponse response, String actionMsg)
        throws IOException
    {
        response.setContentType("text/html");
        PrintWriter pw = new PrintWriter(response.getOutputStream());

        pw.println("");
        pw.println("<html>");
        pw.println("<head><title>Error</title></head>");

        pw.println("<body>");
        pw.println("<h1>Parse Error</h1>"
                + " <p>Your request was malformed, the XML could not be interpreted"
                + " correctly...</p>");
        pw.println("<p>Your Request was:</p>");
        pw.println("<textarea name=\"userInput\" cols=\"80\" rows=\"20\">" + actionMsg
                + "</textarea>");
        pw.println("</body></html>");
        pw.flush();
        pw.close();
    }

    private void printAction(String actionMessage, HttpServletResponse response)
    {
        try {
            response.setContentType("text/html");
            PrintWriter pw = new PrintWriter(response.getOutputStream());

            pw.println(" <html><head><title>Input</title></head>");
            pw.println("<body>");

            pw.println("Your Message was:<br />");
            pw.println(actionMessage);

            pw.println("</body>");
            pw.println("</html>");
            pw.flush();
            pw.close();
        }
        catch (Exception ex) {

        }
    }

    /**
     * Just send an ok response, meaning, that the action was well-formed and
     * could be parsed correctly
     * @param response - the post response
     * @throws IOException
     */
    private void sendOk(HttpServletResponse response)
        throws IOException
    {
        response.setContentType("text/html");
        PrintWriter pw = new PrintWriter(response.getOutputStream());

        pw.println("");
        pw.println("<html>");
        pw.println("<head><title>Ok</title></head>");

        pw.println("<body><h1>Ok</h1>" + " <p>Your request could be interpreted"
                + " and will be processed in a background process.</p></body>");

        pw.println("</html>");
        pw.flush();
        pw.close();
    }

    /**
     * Add an action to the working queue and wake (if needed) the worker thread
     * @param actionBase - An action (modification of the Ontology)
     */
    private void addToQueue(OntologyActionBase actionBase)
    {
        while (!actionWorkQueue.offer(actionBase));
    }

    @Override
    public void destroy()
    {
        worker.interrupt();
        super.destroy();
    }
}
