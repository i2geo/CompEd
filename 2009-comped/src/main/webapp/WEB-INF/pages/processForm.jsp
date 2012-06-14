<%@ include file="/common/taglibs.jsp"%>

<head>
<c:if test="${empty competency.id}">
	<title><fmt:message key="processDetail.title" /></title>
	<meta name="heading"
		content="<fmt:message key='processDetail.heading'/>" />
</c:if>
<c:if test="${not empty competency.id}">
	<title><s:property value="competency.name" /></title>
	<meta name="heading" content="<s:property value="competency.name"/>" />
</c:if>

    <script language='javascript' src='<s:property value="searchI2GManager.browserPath"/>'></script>

    <script type="text/javascript" src="<c:url value='/scripts/jquery.dimensions.pack.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/scripts/jquery.hoverIntent.minified.js'/>"></script>
 	<script type="text/javascript" src="<c:url value='/scripts/jquery.cluetip.js'/>"></script>

	<script type="text/javascript">
		jQuery(document).ready( function() {
			jQuery('.skb').hide();
			jQuery('#editProcesses').click( function() {
				jQuery("#skbProcesses").slideToggle();
			})

			jQuery('#helpProcessNameLink').cluetip({local:true, cursor: 'pointer', cluetipClass: 'default', showTitle: false});
			jQuery('#helpProcessUriLink').cluetip({local:true, cursor: 'pointer', cluetipClass: 'default', showTitle: false});
			jQuery('#helpProcess2ProcessLink').cluetip({local:true, cursor: 'pointer', cluetipClass: 'default', showTitle: false});
		
		})
</script>

</head>

<s:url value="%{'images/16x16/support.png'}" includeParams="none" id="supportURL" />


<s:form id="processForm" action="saveProcess" method="post"
	validate="true">
	<s:hidden name="competency.id" value="%{competency.id}" />

	<table>
	<!-- Row 1: name -->
	<tr>
		<td><s:textfield key="competency.name" required="true" cssClass="text large"/></td>
	   	<td> <a id="helpProcessNameLink" href="#helpProcessName" rel="#helpProcessName">
  			<img src="<s:property value="#supportURL"/>"/>
 		 </a></td>
    </tr>	
	<!-- Row 2: uri -->
    <tr>
    	<td><s:textfield key="competency.uriSuffix" required="true" cssClass="text large" />
		<s:property value="competency.uri" /></td>
	   	<td> <a id="helpProcessUriLink" href="#helpProcessUri" rel="#helpProcessUri">
  			<img src="<s:property value="#supportURL"/>"/>
 		 </a></td>
    </tr>
    </table>

	<script type="text/javascript">
		window.top.skbPleaseReplaceMeActive = "SkillsTextBoxProcesses|pStorage| competencyProcess |false";
		window.top.skbConfigBasePath = "/SearchI2G/net.i2geo.skillstextbox.SkillsTextBox/";
	</script>

    <table>
    
	<!-- Row 1: hint sentence: SKB manage processes -->
	<tr>
		<td><li id="editProcesses" class="text"><fmt:message key="competencyDetail.link.Process2Process" /></li></td>
	   	<td> <a id="helpProcess2ProcessLink" href="#helpProcess2Process" rel="#helpProcess2Process">
  			<img src="<s:property value="#supportURL"/>"/>
 		 </a></td>
	</tr>
	
	<!-- Row 2: SKB manage processes -->
	<tr>
	<td colspan="2">
		<div id="skbProcesses" class="skb">
			<div id="SkillsTextBoxProcesses" class="SkillsTextBox"></div>
			<s:hidden id="pStorage" name="pStorage" value="%{pStorage}" />
		</div>
	</td></tr>
	</table>
	
	<li class="buttonBar bottom"><s:submit cssClass="button"
		method="save" key="button.save" theme="simple" /> <c:if
		test="${not empty competency.id}">
		<s:submit cssClass="button" method="delete" key="button.delete"
			onclick="return confirmDelete('competency')" theme="simple" />
	</c:if> <s:submit cssClass="button" method="cancel" key="button.cancel"
		theme="simple" /></li>
</s:form>

<div id="helpProcessName" class="cluetip-default">
	<p><fmt:message key="help.process.name"/><p>
</div>

<div id="helpProcessUri" class="cluetip-default">
	<p><fmt:message key="help.process.uri"/><p>
</div>

<div id="helpProcess2Process" class="cluetip-default">
	<p><fmt:message key="help.process.processes"/><p>
</div>

<script type="text/javascript">
	Form.focusFirstElement($("processForm"));
</script>