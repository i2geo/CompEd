HOWTO use the encoder
---------------------

  - create a ChangeRequest object
  
     ChangeRequest myChangeReq = new ChangeRequest(String responseURL,
      String sessionID, String destinationURL);
     
     * String responseURL:
        where does your Program listen for asynchronous messages from the
        server (such as what has additionally changed / Errors)
     * String sessionID:
        an ID that will be referenced in async messages
     * String destinationURL:
        the URL, where the server listens (servlet's URL)
        e.g. http://myserver/ontoServer/doAction
     
  - add new change objects* from namespace "net.i2geo.changeCoder.changes"
         (*see below for a list of actions and what they are meant for)
         
      myChangeReq.addChangeElement(new AddElement(testCompetency));
         * where testCompetency is an XML string that encodes e.g. a Competency

      myChangeReq.addChangeElement(new DeleteElement(testCompetency));
         * where testCompetency is an XML string that encodes e.g. a Competency
         * this deletes the competency if no properties are added (in XML),
           or the competencies properties if some are in the XML
      
  - send the request to the server
      
      boolean result = changeRequest.sendMessage();
  
      * result specify whether the message could be properly parsed by the server
    
    
ChangeActions
-------------

  * NameChange:
    - Change the name (URL) of an entity
    
    EXAMPLE:
    
  * ClassChange:
    - Change the class of an entity
    
    EXAMPLE:
    
  * AddElement:
    - adds a new Entity (if no subelements are given) or
    - adds new Properties (if element has subelements)
    
    EXAMPLE:
    
  * DeleteElement:
    - deletes an entity (in no subelements are given) or
    - deletes properties (if element has subelements)
    
    EXAMPLE:
    
  * ChangeElement:
    - changes the value of an element (in no subelements are given) or
    - changes the value of a property (if element has subelements)
    
    EXAMPLE:
    
    