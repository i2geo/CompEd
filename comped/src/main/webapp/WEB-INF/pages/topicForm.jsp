<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="topicDetail.title"/></title>
    <meta name="heading" content="<fmt:message key='topicDetail.heading'/>"/>
    <script language='javascript' src='<s:property value="searchI2GManager.browserPath"/>'></script>

    <script type="text/javascript" src="<c:url value='/scripts/jquery.dimensions.pack.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/jquery.hoverIntent.minified.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/jquery.cluetip.js'/>"></script>

	<script  type="text/javascript"> 
		jQuery(document).ready(function(){
			jQuery('.skb').hide();
			jQuery('#editAbstractTopics').click(function () {
				jQuery("#skbAbstractTopics").slideToggle();
			})
			
			jQuery('#helpConcreteTopicNameLink').cluetip({local:true, cursor: 'pointer', cluetipClass: 'default', showTitle: false});
			jQuery('#helpConcreteTopicUriLink').cluetip({local:true, cursor: 'pointer', cluetipClass: 'default', showTitle: false});
			jQuery('#helpConcreteTopic2AbstractTopicLink').cluetip({local:true, cursor: 'pointer', cluetipClass: 'default', showTitle: false});

			})
    </script>
</head>

<s:url value="%{'images/16x16/support.png'}" includeParams="none" id="supportURL" />

<s:form id="topicForm" action="saveTopic" method="post" validate="true">
<s:hidden name="topic.id" value="%{topic.id}"/>

	<table>
	<!-- Row 1: name -->
	<tr>

    	<td><s:textfield key="topic.name" required="true" cssClass="text large"/></td>
	   	<td> <a id="helpConcreteTopicNameLink" href="#helpConcreteTopicName" rel="#helpConcreteTopicName">
  			<img src="<s:property value="#supportURL"/>"/>
 		 </a></td>

    </tr>
	<!-- Row 2: uri -->
    <tr>
	    <td><s:textfield key="topic.uriSuffix" required="true" cssClass="text large"/>
		<s:property value="topic.uri"/></td>
	   	<td> <a id="helpConcreteTopicUriLink" href="#helpConcreteTopicUri" rel="#helpConcreteTopicUri">
  			<img src="<s:property value="#supportURL"/>"/>
 		 </a></td>
    </tr>
    </table>

   	<script type="text/javascript">
     	window.top.skbPleaseReplaceMeActive =  "SkillsTextBoxAbstractTopics|atStorage| abstractTopic |false";
       	window.top.skbConfigBasePath = "/SearchI2G/net.i2geo.skillstextbox.SkillsTextBox/";
   	</script>

	<table>
	
	<!-- Row 1: hint sentence: SKB manage abstract topics -->
	<tr>
		<td><li id="editAbstractTopics" class="text"> <fmt:message key="topicDetail.link.Topic2Topic"/> </li></td>
	   	<td> <a id="helpConcreteTopic2AbstractTopicLink" href="#helpConcreteTopic2AbstractTopic" rel="#helpConcreteTopic2AbstractTopic">
  			<img src="<s:property value="#supportURL"/>"/>
 		 </a></td>
	</tr>

	<!-- Row 2: SKB manage abstract topics -->
	<tr>
	<td colspan="2">
	
	<div id="skbAbstractTopics" class="skb">
    	<div id="SkillsTextBoxAbstractTopics" class="SkillsTextBox"></div>
      	<s:hidden id="atStorage" name="atStorage" value="%{atStorage}"/>
	</div>
	</td></tr></table>

	
    <li class="buttonBar bottom">         
        <s:submit cssClass="button" method="save" key="button.save" theme="simple"/>
        <c:if test="${not empty topic.id}"> 
            <s:submit cssClass="button" method="delete" key="button.delete" onclick="return confirmDelete('topic')" theme="simple"/>
        </c:if>
        <s:submit cssClass="button" method="cancel" key="button.cancel" theme="simple"/>
    </li>
</s:form>

<div id="helpConcreteTopicName" class="cluetip-default">
	<p><fmt:message key="help.topic.name"/><p>
</div>

<div id="helpConcreteTopicUri" class="cluetip-default">
	<p><fmt:message key="help.topic.uri"/><p>
</div>

<div id="helpConcreteTopic2AbstractTopic" class="cluetip-default">
	<p><fmt:message key="help.topic.topics"/><p>
</div>


<script type="text/javascript">
    Form.focusFirstElement($("topicForm"));
</script>