<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="abstractTopicList.title"/></title>
    <meta name="heading" content="<fmt:message key='abstractTopicList.heading'/>"/>
</head>

<c:set var="buttons">
    <input type="button" style="margin-right: 5px"
        onclick="location.href='<c:url value="/editAbstractTopic.html"/>'"
        value="<fmt:message key="button.add"/>"/>
    
    <input type="button" onclick="location.href='<c:url value="/mainMenu.html"/>'"
        value="<fmt:message key="button.done"/>"/>
</c:set>

<!-- 
   <c:out value="${buttons}" escapeXml="false" />
-->

<!-- MH: Would like to add columns: show, edit, delete, translate -->
<!-- requestURI="http://draft.i2geo.net/comped/" -->
<s:set name="topics" value="topics" scope="request"/>
<fmt:message key="item.tgroup" var="itemt"/>
<fmt:message key="item.tgroups" var="items"/>

<display:table name="topics" class="table" requestURI="abstractTopics.html" id="topicList" export="false" pagesize="25">

    <!-- display:column property="id" sortable="true" href="showTopic.html" 
        paramId="id" paramProperty="id" titleKey="topic.id"/> -->
    <display:column property="name" sortable="true" titleKey="item.name"
    	url="/showAbstractTopic.html" paramId="id" paramProperty="id"/>

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

