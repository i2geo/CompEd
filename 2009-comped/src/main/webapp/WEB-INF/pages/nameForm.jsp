<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="nameDetail.title"/></title>
    <meta name="heading" content="<fmt:message key='nameDetail.heading'/>"/>
</head>

<s:form id="nameForm" action="saveName" method="post" validate="true">
<s:hidden name="name.id" value="%{name.id}"/>

    <li>
    	<s:textfield key="name.name" required="true" cssClass="text large" labelposition="bottom"/>
	</li>

	<li>	
    	<div class="left">
			<s:select key="name.type" name="name.type" list="nameTypes" listKey="key" listValue="value" 
				required="true" theme="xhtml" cssClass="text medium" labelposition="bottom" 
				/>
		</div>
		<div>
    		<s:set name="language" value="name.locale" scope="page"/>
	    	<appfuse:language name="name.locale" prompt="" default="${language}"/>
   			<p>
				<label for="name.locale">
      	 			<fmt:message key="name.locale"/> <span class="req">*</span>
   				</label>
			</p>
		</div>
	</li>

	<c:if test="${name.type=='COMMON'}"> 
		<li>
   			<p>
				<label for="name.default">
      	 			<fmt:message key="name.default"/> <span class="req">*</span>
   				</label>
			<s:checkbox name="name.defName" id="name.defName" fieldValue="true" 
				required="true" theme="xhtml" labelposition="left"/>
			</p>
		</li>
	</c:if>

    <li class="buttonBar bottom left">         
        <s:submit cssClass="button" method="save" key="button.save" theme="simple"/>
        <c:if test="${not empty name.id}"> 
            <s:submit cssClass="button" method="delete" key="button.delete" onclick="return confirmDelete('name')" theme="simple"/>
        </c:if>
        <s:submit cssClass="button" method="cancel" key="button.cancel" theme="simple"/>
    </li>
</s:form>

<script type="text/javascript">
    Form.focusFirstElement($("nameForm"));
</script>