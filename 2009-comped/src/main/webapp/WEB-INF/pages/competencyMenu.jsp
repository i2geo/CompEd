<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="item.competencies"/></title>
    <meta name="heading" content="<fmt:message key='item.competencies'/>"/>
</head>

<div class="separator"></div>

<table width="600">
<tr><td width="50%">
	<div id="cMenuConcrete" class="last-list">
	<h3><span><fmt:message key="item.competencies"/> </span></h3>
	<ul>
		<security:authorize ifAnyGranted="ROLE_CURRICULUM_ENCODER">
			<li>
				<a href="<c:url value="/editCompetency.html"/>"><fmt:message key="button.add"/></a>
			</li>
		</security:authorize>
		<li>
			<a href="<c:url value="/ccTree.html"/>"><fmt:message key="menu.viewTree"/></a>
		</li>
		<li>
			<a href="<c:url value="/competencies.html"/>"><fmt:message key="menu.viewList"/></a>
		</li>
	</ul>
	</div>
</td>
<td width="50%">
	<div id="cMenuProcess" class="last-list">
	<h3><span><fmt:message key="item.processes"/></span></h3>
	<ul>
		<security:authorize ifAnyGranted="ROLE_CURRICULUM_ENCODER">
			<li>
				<a href="<c:url value="/editProcess.html"/>"><fmt:message key="button.add"/></a>
			</li>
		</security:authorize>
		<li>
			<a href="<c:url value="/cpTree.html"/>"><fmt:message key="menu.viewTree"/></a>
		</li>
		<li>
			<a href="<c:url value="/processes.html"/>"><fmt:message key="menu.viewList"/></a>
		</li>
	</ul>
	</div>
</td></tr>
</table>			