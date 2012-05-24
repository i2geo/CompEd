<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="competencyList.title"/></title>
    <meta name="heading" content="<fmt:message key='competencyList.heading'/>"/>
</head>

<c:set var="buttons">
	<security:authorize ifAnyGranted="ROLE_CURRICULUM_ENCODER">
	    <input type="button" style="margin-right: 5px"
    	    onclick="location.href='<c:url value="/editCompetency.html"/>'"
        	value="<fmt:message key="button.add"/>"/>
    </security:authorize>
    
    <input type="button" onclick="location.href='<c:url value="/mainMenu.html"/>'"
        value="<fmt:message key="button.done"/>"/>
</c:set>

<!-- 
   <c:out value="${buttons}" escapeXml="false" />
-->


<!-- MH: Would like to add columns: show, edit, delete, translate -->
<s:set name="competencies" value="competencies" scope="request"/>
<fmt:message key="item.competency" var="itemt"/>
<fmt:message key="item.competencies" var="items"/>

<display:table name="competencies" class="table" requestURI="competencies.html" id="competencyList" export="false" pagesize="25">
    <!-- display:column property="id" sortable="true" href="showCompetency.html" 
        paramId="id" paramProperty="id" titleKey="competency.id" /> -->
    <display:column property="name" sortable="true" titleKey="item.name"
    	url="/showCompetency.html" paramId="id" paramProperty="id"/>

    <display:setProperty name="paging.banner.item_name" value="${item}"/>
    <display:setProperty name="paging.banner.items_name" value="${items}"/>
	 
	<display:setProperty name="export.excel.filename" value="Competency List.xls"/> 
   	<display:setProperty name="export.csv.filename" value="Competency List.csv"/> 
   	<display:setProperty name="export.pdf.filename" value="Competency List.pdf"/> 
	
</display:table>

<c:out value="${buttons}" escapeXml="false" />

<script type="text/javascript">
    highlightTableRows("competencyList");
</script>

