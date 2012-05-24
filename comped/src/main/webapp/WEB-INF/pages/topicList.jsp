<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="topicList.title"/></title>
    <meta name="heading" content="<fmt:message key='topicList.heading'/>"/>
</head>

<c:set var="buttons">
	<security:authorize ifAnyGranted="ROLE_CURRICULUM_ENCODER">
	    <input type="button" style="margin-right: 5px"
    	    onclick="location.href='<c:url value="/editTopic.html"/>'"
        	value="<fmt:message key="button.add"/>"/>
    </security:authorize>
    
    <input type="button" onclick="location.href='<c:url value="/mainMenu.html"/>'"
        value="<fmt:message key="button.done"/>"/>
</c:set>

<!-- 
   <c:out value="${buttons}" escapeXml="false" />
-->

<!-- MH: Would like to add columns: show, edit, delete, translate -->
<!-- requestURI="http://draft.i2geo.net/comped/" -->
<s:set name="topics" value="topics" scope="request"/>
<fmt:message key="item.topic" var="itemt"/>
<fmt:message key="item.topics" var="items"/>
<display:table name="topics" class="table" requestURI="topics.html" id="topicList" export="false" pagesize="25">

    <!-- display:column property="id" sortable="true" href="showTopic.html" 
        paramId="id" paramProperty="id" titleKey="topic.id"/> -->
    <display:column property="name" sortable="true" titleKey="item.name"
    	url="/showTopic.html" paramId="id" paramProperty="id"/>

    <display:setProperty name="paging.banner.item_name" value="${item}"/>
    <display:setProperty name="paging.banner.items_name" value="${items}"/>

	 
	<display:setProperty name="export.excel.filename" value="Topic List.xls"/> 
   	<display:setProperty name="export.csv.filename" value="Topic List.csv"/> 
   	<display:setProperty name="export.pdf.filename" value="Topic List.pdf"/> 
	
</display:table>

<c:out value="${buttons}" escapeXml="false" />

<script type="text/javascript">
    highlightTableRows("topicList");
</script>

