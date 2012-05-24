<%@ include file="/common/taglibs.jsp"%>

<head>
    <c:if test="${empty competency.id}">
		<title>CompEd: <fmt:message key="competencyDetail.title"/></title>
	    <meta name="heading" content="<fmt:message key='competencyDetail.heading'/>"/>
	</c:if>
    <c:if test="${not empty competency.id}">
		<title>CompEd: <s:property value="competency.name"/></title>
    	<meta name="heading" content="<s:property value="competency.name"/>"/>
	</c:if>
	
    <script language='javascript' src='<s:property value="searchI2GManager.browserPath"/>'></script>
	
    <script type="text/javascript" src="<c:url value='/scripts/jquery.dimensions.pack.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/jquery.hoverIntent.minified.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/jquery.cluetip.js'/>"></script>
	
	<script  type="text/javascript"> 
		jQuery(document).ready(function(){
			jQuery('.skb').hide();
			jQuery('#editTopics').click(function () {
				jQuery("#skbTopics").slideToggle();
			})
			jQuery('#editProcesses').click(function () {
				jQuery("#skbProcesses").slideToggle();
			})

			jQuery('#helpCompetencyNameLink').cluetip({local:true, cursor: 'pointer', cluetipClass: 'default', showTitle: false});
			jQuery('#helpCompetencyUriLink').cluetip({local:true, cursor: 'pointer', cluetipClass: 'default', showTitle: false});
			jQuery('#helpCompetencyTopicsLink').cluetip({local:true, cursor: 'pointer', cluetipClass: 'default', showTitle: false});
			jQuery('#helpCompetency2ProcessLink').cluetip({local:true, cursor: 'pointer', cluetipClass: 'default', showTitle: false});
						
		})
    </script>

</head>

<s:url value="%{'images/16x16/support.png'}" includeParams="none" id="supportURL" />

<s:form id="competencyForm" action="saveCompetency" method="post" validate="true">

	<s:hidden name="competency.id" value="%{competency.id}"/>
		
	<table>
	<!-- Row 1: name -->
	<tr>
		<td><s:textfield key="competency.name" required="true" cssClass="text large"/></td>
	   	<td> <a id="helpCompetencyNameLink" href="#helpCompetencyName" rel="#helpCompetencyName">
  			<img src="<s:property value="#supportURL"/>"/>
 		 </a></td>

    </tr>
	<!-- Row 2: uri -->
    <tr>
    	<td><s:textfield key="competency.uriSuffix" required="true" cssClass="text large"/>
    		<s:property value="competency.uri"/>
    	</td>
	   	<td> <a id="helpCompetencyUriLink" href="#helpCompetencyUri" rel="#helpCompetencyUri">
  			<img src="<s:property value="#supportURL"/>"/>
 		 </a></td>
    </tr>
    </table>

   	<script type="text/javascript">
       	window.top.skbPleaseReplaceMeActive =  "SkillsTextBoxTopics|tStorage| topic, abstractTopic |false;SkillsTextBoxProcesses|pStorage| competencyProcess |false;";
       	window.top.skbConfigBasePath = "/SearchI2G/net.i2geo.skillstextbox.SkillsTextBox/";
   	</script>
    
    <table>
	<!-- Row 1: hint sentence: SKB manage topics and topic-groups -->
	<tr>
		<td> <li id="editTopics" class="text"> <fmt:message key="competencyDetail.editTopics" /> </li></td>
	   	<td> <a id="helpCompetencyTopicsLink" href="#helpCompetencyTopics" rel="#helpCompetencyTopics">
  			<img src="<s:property value="#supportURL"/>"/>
 		 </a></td>
	</tr>
	
	<!-- Row 2: SKB manage topics and topic-groups -->
	<tr>
	<td colspan="2">
	<div id="skbTopics" class="skb">
    	<div id="SkillsTextBoxTopics" class="SkillsTextBox"></div>
      	<s:hidden id="tStorage" name="tStorage" value="%{tStorage}"/>
	</div>
	</td>
	<td></td>
	</tr>
	
	<!-- Row 3: hint sentence: SKB manage processes -->
	<tr>
		<td><li id="editProcesses" class="text"><fmt:message key="competencyDetail.link.Competency2Process" /></li></td>
	   	<td> <a id="helpCompetency2ProcessLink" href="#helpCompetency2Process" rel="#helpCompetency2Process">
  			<img src="<s:property value="#supportURL"/>"/>
 		 </a></td>
	</tr>
	
	<!-- Row 4: SKB manage processes -->
	<tr>
	<td colspan="2">
		<div id="skbProcesses" class="skb">
			<div id="SkillsTextBoxProcesses" class="SkillsTextBox"></div>
			<s:hidden id="pStorage" name="pStorage" value="%{pStorage}" />
		</div>
	</td></tr>
	</table>
	
    <li class="buttonBar bottom">         
        <s:submit cssClass="button" method="save" key="button.save" theme="simple"/>
        <c:if test="${not empty competency.id}"> 
            <s:submit cssClass="button" method="delete" key="button.delete" onclick="return confirmDelete('competency')" theme="simple"/>
        </c:if>
        <s:submit cssClass="button" method="cancel" key="button.cancel" theme="simple"/>
    </li>

</s:form>

<div id="helpCompetencyName" class="cluetip-default">
	<p><fmt:message key="help.competency.name"/><p>
</div>

<div id="helpCompetencyUri" class="cluetip-default">
	<p><fmt:message key="help.competency.uri"/><p>
</div>

<div id="helpCompetencyTopics" class="cluetip-default">
	<p><fmt:message key="help.competency.topics"/><p>
</div>

<div id="helpCompetency2Process" class="cluetip-default">
	<p><fmt:message key="help.competency.process"/><p>
</div>

<script type="text/javascript">
    Form.focusFirstElement($("competencyForm"));
</script>