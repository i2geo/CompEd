<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="abstractTopicDetail.title"/></title>
    <meta name="heading" content="<fmt:message key='abstractTopicDetail.heading'/>"/>

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

			jQuery('#helpAbstractTopicNameLink').cluetip({local:true, cursor: 'pointer', cluetipClass: 'default', showTitle: false});
			jQuery('#helpAbstractTopicUriLink').cluetip({local:true, cursor: 'pointer', cluetipClass: 'default', showTitle: false});
			jQuery('#helpAbstractTopic2AbstractTopicLink').cluetip({local:true, cursor: 'pointer', cluetipClass: 'default', showTitle: false});
			
		})
    </script>
</head>

<s:url value="%{'images/16x16/support.png'}" includeParams="none" id="supportURL" />

<s:form id="abstractTopicForm" action="saveAbstractTopic" method="post" validate="true">
<s:hidden name="topic.id" value="%{topic.id}"/>

	<table>
	<!-- Row 1: name -->
	<tr>
    	<td><s:textfield key="topic.name" required="true" cssClass="text large"/></td>
	   	<td> <a id="helpAbstractTopicNameLink" href="#helpAbstractTopicName" rel="#helpAbstractTopicName">
  			<img src="<s:property value="#supportURL"/>"/>
 		 </a></td>

    </tr>
	<!-- Row 2: uri -->
    <tr>
	    <td><s:textfield key="topic.uriSuffix" required="true" cssClass="text large"/>
		<s:property value="topic.uri"/>
		</td>
	   	<td> <a id="helpAbstractTopicUriLink" href="#helpAbstractTopicUri" rel="#helpAbstractTopicUri">
  			<img src="<s:property value="#supportURL"/>"/>
 		 </a></td>
    </tr>
    </table>

   	<script type="text/javascript">
     	window.top.skbPleaseReplaceMeActive =  "SkillsTextBoxAbstractTopics|atStorage| abstractTopic |false";
       	window.top.skbConfigBasePath = "/SearchI2G/net.i2geo.skillstextbox.SkillsTextBox/";
   	</script>

	<table>

	<!-- Row 1: hint sentence: SKB manage abstract topic taxonomy -->
	<tr>
		<td><li id="editAbstractTopics" class="text"> <fmt:message key="topicDetail.link.AbstractTopic2AbstractTopic"/> </li></td>
	   	<td> <a id="helpAbstractTopic2AbstractTopicLink" href="#helpAbstractTopic2AbstractTopic" rel="#helpAbstractTopic2AbstractTopic">
  			<img src="<s:property value="#supportURL"/>"/>
 		 </a></td>
	</tr>

	<!-- Row 2: SKB manage competencies -->
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

<div id="helpAbstractTopicName" class="cluetip-default">
	<p><fmt:message key="help.abstractTopic.name"/><p>
</div>

<div id="helpAbstractTopicUri" class="cluetip-default">
	<p><fmt:message key="help.abstractTopic.uri"/><p>
</div>

<div id="helpAbstractTopic2AbstractTopic" class="cluetip-default">
	<p><fmt:message key="help.abstractTopic.abstractTopics"/><p>
</div>

<script type="text/javascript">
    Form.focusFirstElement($("abstractTopicForm"));
</script>