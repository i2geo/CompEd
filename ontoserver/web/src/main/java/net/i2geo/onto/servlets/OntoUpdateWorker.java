/**
 * 
 */
package net.i2geo.onto.servlets;

import java.util.concurrent.*;

import org.apache.log4j.*;

import net.i2geo.onto.updates.actions.OntologyActionBase;
import net.i2geo.onto.updates.actions.OntologyHelper;

/**
 * @author Arndt Faulhaber
 * @author Martin Homik
 */
public class OntoUpdateWorker extends Thread {
	
	private Logger log = LogManager.getLogger(this.getClass().toString());

    // Where to look for more jobs...
    private BlockingQueue<OntologyActionBase> actionQueue;

    /**
     * The constructor, where the worker is told in which queue to look for jobs...
     * The Queue *must* be threadsafe since no checking or synchronisation
     *  occurs in this thread.
     * 
     * @param workingQueue - A queue with jobs
     */
    public OntoUpdateWorker(BlockingQueue<OntologyActionBase> workingQueue)
    {
        this.actionQueue = workingQueue;
        //        setDaemon(true);
    }

    /**
     * The entry method of this thread...
     * 
     * @overwritten
     */
    public void run() {
        log.info("Worker thread ready.");

        // run forever ;)
        while (true) {

            try {
                OntologyActionBase action = actionQueue.take();
                log.info("Handling request...");

                try {
                    // sanity check
                    if (action == null) {
                        log.warn("NULL-Action!!!");
                    }
                    else {
                        action.perform();
                        log.info("Processed request: " + action.getClass());
                    }

                    // checkpoint writing

                    log.info("Writing checkpoint of current ontology to OWL-file...");
                    OntologyHelper.getInstance().writeOntology();
                }
                catch (Exception ex) {
                    log.error("Error processing request:", ex);
                }
            }
            catch (Exception ex) {
                if (this.isInterrupted()) {
                    log.info("Got interrupt msg, exiting...");
                    log.info("Writing changes to file...");
                    OntologyHelper.getInstance().writeOntology();
                }
                else {
                    log.error("An Error occurred: ", ex);
                }
            }
        }

    }

    @Override
    protected void finalize()
        throws Throwable
    {
        log.info("Got interrupt msg, exiting...");
        log.info("Writing changes to file...");
        OntologyHelper.getInstance().writeOntology();
        super.finalize();
    }
}
