<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="nameList.title"/></title>
    <meta name="heading" content="<fmt:message key='nameList.heading'/>"/>
</head>

<c:set var="buttons">
    <input type="button" style="margin-right: 5px"
        onclick="location.href='<c:url value="/editName.html"/>'"
        value="<fmt:message key="button.add"/>"/>
    
    <input type="button" onclick="location.href='<c:url value="/mainMenu.html"/>'"
        value="<fmt:message key="button.done"/>"/>
</c:set>

<!-- 
   <c:out value="${buttons}" escapeXml="false" />
-->

<!-- MH: Would like to add columns: show, edit, delete, translate -->
<s:set name="names" value="names" scope="request"/>
<display:table name="names" class="table" requestURI="names.html" id="nameList" export="false" pagesize="25">
    <!-- display:column property="id" sortable="true" href="editName.html" 
        paramId="id" paramProperty="id" titleKey="name.id" /> -->

    <display:column property="name" sortable="true" titleKey="item.label.name"
    	url="/editName.html" paramId="id" paramProperty="id"/>

    <display:setProperty name="paging.banner.item_name" value="name"/>
    <display:setProperty name="paging.banner.items_name" value="names"/>

	 
	<display:setProperty name="export.excel.filename" value="Name List.xls"/> 
   	<display:setProperty name="export.csv.filename" value="Name List.csv"/> 
   	<display:setProperty name="export.pdf.filename" value="Name List.pdf"/> 
	
</display:table>

<c:out value="${buttons}" escapeXml="false" />

<script type="text/javascript">
    highlightTableRows("nameList");
</script>

