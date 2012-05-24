<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="item.topics"/></title>
    <meta name="heading" content="<fmt:message key='item.topics'/>"/>
</head>

<div class="separator"></div>

	<div id="tMenuConcrete" class="last-list">
	<h3><span><fmt:message key="item.topics"/> </span></h3>
	<ul>
		<security:authorize ifAnyGranted="ROLE_CURRICULUM_ENCODER">
			<li>
				<a href="<c:url value="/editTopic.html"/>"><fmt:message key="button.add"/></a>
			</li>
		</security:authorize>
		<li>
			<a href="<c:url value="/tiTree.html"/>"><fmt:message key="menu.viewTree"/></a>
		</li>
		<li>
			<a href="<c:url value="/topics.html"/>"><fmt:message key="menu.viewList"/></a>
		</li>
        <li>
            <a href="/static/topicsFrameset.html" target="_top">TopicsFrameset</a>
        </li>
	</ul>
	</div>
