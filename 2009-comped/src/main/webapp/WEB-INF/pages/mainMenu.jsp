<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="mainMenu.title"/></title>
    <!--  <meta name="heading" content="<fmt:message key='mainMenu.heading'/>"/> -->
    <meta name="menu" content="MainMenu"/>
</head>


  		
<!-- div>
	<p><fmt:message key="mainMenu.message"/></p>
</div -->

<div class="separator"></div>

<!-- 
<ul class="glassList">
	<li>
    	<a href="<c:url value="/competencies.html"/>"><fmt:message key="item.competencies"/></a>
	</li>
	<li>
    	<a href="<c:url value="/processes.html"/>"><fmt:message key="item.processes"/></a>
	</li>
	<li>
    	<a href="<c:url value="/topics.html"/>"><fmt:message key="item.topics"/></a>
	</li>
	<li>
    	<a href="<c:url value="/abstractTopics.html"/>"><fmt:message key="item.tgroups"/></a>
	</li>
</ul>
-->
<c:set var="maxNameLength" value="50" />

<div>
<table><tr><td>
<div id="lastCreatedCompetencies" class="last-list">
		<s:if test="lastCreatedConcreteCompetencies.isEmpty">
			<span><fmt:message key="mainMenu.lastCreatedConcreteCompetencies.not"/></span>
		</s:if>
	    <s:else>
			<h3><span><fmt:message key="mainMenu.lastCreatedConcreteCompetencies"/></span></h3>
			<p>
			<ol>
				<s:iterator value="lastCreatedConcreteCompetencies">
					<li> 
						<s:url id="showCompetency" action="showCompetency" includeParams="none">
							<s:param name="uri">${fn:substringAfter(uri,"ontology.owl#")}</s:param>
						</s:url>
						<a href="<s:property value="#showCompetency"/>">
							<s:set id="iName" name="iName" value="name"/>
							<c:set var="nameLength" value="${fn:length(iName)}"/>
							${fn:substring(iName, 0, 50)} 
							<c:if test="${nameLength > maxNameLength}">...</c:if>
						</a>
					</li>
				</s:iterator>
			</ol>
			</p>
		</s:else>
</div>
</td>
<td>
<div id="lastModifiedCompetencies" lastCreatedCompetencies class="last-list">
		<s:if test="lastModifiedConcreteCompetencies.isEmpty">
			<h3><span><fmt:message key="mainMenu.lastModifiedConcreteCompetencies.not"/></span></h3>
		</s:if>
	    <s:else>
			<h3><span><fmt:message key="mainMenu.lastModifiedConcreteCompetencies"/></span></h3>
			<p>
			<ol>
				<s:iterator value="lastModifiedConcreteCompetencies">
					<li> 
						<s:url id="showCompetency" action="showCompetency" includeParams="none">
							<s:param name="uri">${fn:substringAfter(uri,"ontology.owl#")}</s:param>
						</s:url>
						<a href="<s:property value="#showCompetency"/>">
							<s:set id="iName" name="iName" value="name"/>
							<c:set var="nameLength" value="${fn:length(iName)}"/>
							${fn:substring(iName, 0, 50)} 
							<c:if test="${nameLength > maxNameLength}">...</c:if>
						</a>
					</li>
				</s:iterator>
			</ol>
			</p>
		</s:else>	
</div>
</td></tr></table>
</div>

<div>
<table><tr><td>
<div id="lastCreatedProcesses"class="last-list left">
	<p>
		<s:if test="lastCreatedCompetencyProcesses.isEmpty">
			<h3><span><fmt:message key="mainMenu.lastCreatedCompetencyProcesses.not"/></span></h3>
		</s:if>
	    <s:else>
			<h3><span><fmt:message key="mainMenu.lastCreatedCompetencyProcesses"/></span></h3>
			<ol>
				<s:iterator value="lastCreatedCompetencyProcesses">
					<li> 
						<s:url id="showProcess" action="showProcess" includeParams="none">
							<s:param name="uri">${fn:substringAfter(uri,"ontology.owl#")}</s:param>
						</s:url>
						<a href="<s:property value="#showProcess"/>">
							<s:set id="iName" name="iName" value="name"/>
							<c:set var="nameLength" value="${fn:length(iName)}"/>
							${fn:substring(iName, 0, 50)} 
							<c:if test="${nameLength > maxNameLength}">...</c:if>
						</a>
					</li>
				</s:iterator>
			</ol>
		</s:else>
	</p>
</div>
</td><td>
<div id="lastModifiedProcesses" class="last-list right">
	<p>
		<s:if test="lastModifiedCompetencyProcesses.isEmpty">
			<h3><span><fmt:message key="mainMenu.lastModifiedCompetencyProcesses.not"/></span></h3>
		</s:if>
	    <s:else>
			<h3><span><fmt:message key="mainMenu.lastModifiedCompetencyProcesses"/></span></h3>
			<ol>
				<s:iterator value="lastModifiedCompetencyProcesses">
					<li> 
						<s:url id="showProcess" action="showProcess" includeParams="none">
							<s:param name="uri">${fn:substringAfter(uri,"ontology.owl#")}</s:param>
						</s:url>
						<a href="<s:property value="#showProcess"/>">
							<s:set id="iName" name="iName" value="name"/>
							<c:set var="nameLength" value="${fn:length(iName)}"/>
							${fn:substring(iName, 0, 50)} 
							<c:if test="${nameLength > maxNameLength}">...</c:if>
						</a>
					</li>
				</s:iterator>
			</ol>
		</s:else>
	</p>
</div>
</td></tr></table>
</div>






<div>
<table><tr><td>
<div id="lastCreatedTopics" class="last-list">
		<s:if test="lastCreatedTopics.isEmpty">
			<h3><span><fmt:message key="mainMenu.lastCreatedTopics.not"/></span></h3>
		</s:if>
	    <s:else>
			<h3><span><fmt:message key="mainMenu.lastCreatedTopics"/></span></h3>
			<p>
			<ol>
				<s:iterator value="lastCreatedTopics">
					<li>
						<s:url id="showTopic" action="show" includeParams="none">
							<s:param name="uri">${fn:substringAfter(uri,"ontology.owl#")}</s:param>
						</s:url>
						<a href="<s:property value="#showTopic"/>">
							<s:set id="iName" name="iName" value="name"/>
							<c:set var="nameLength" value="${fn:length(iName)}"/>
							${fn:substring(iName, 0, 50)} 
							<c:if test="${nameLength > maxNameLength}">...</c:if>
						</a>
					</li>
				</s:iterator>
			</ol>
			</p>
		</s:else>
</div>
</td>
<td>
<div id="lastModifiedTopics" class="last-list">
	<s:if test="lastModifiedTopics.isEmpty">
		<h3><span><fmt:message key="mainMenu.lastModifiedTopics.not"/></span></h3>
	</s:if>
    <s:else>
		<h3><span><fmt:message key="mainMenu.lastModifiedTopics"/></span></h3>
		<p>
			<ol>
				<s:iterator value="lastModifiedTopics">
					<li>
						<s:url id="showTopic" action="show" includeParams="none">
							<s:param name="uri">${fn:substringAfter(uri,"ontology.owl#")}</s:param>
						</s:url>
						<a href="<s:property value="#showTopic"/>">
							<s:set id="iName" name="iName" value="name"/>
							<c:set var="nameLength" value="${fn:length(iName)}"/>
							${fn:substring(iName, 0, 50)} 
							<c:if test="${nameLength > maxNameLength}">...</c:if>
						</a>					
					</li>
				</s:iterator>
			</ol>
		</p>
	</s:else>
</div>
</td></tr></table>
</div>










<!-- 

<div>
<table><tr><td>
<div id="lastCreatedTopics" class="last-list">
		<s:if test="lastCreatedConcreteTopics.isEmpty">
			<h3><span><fmt:message key="mainMenu.lastCreatedTopics.not"/></span></h3>
		</s:if>
	    <s:else>
			<h3><span><fmt:message key="mainMenu.lastCreatedTopics"/></span></h3>
			<p>
			<ol>
				<s:iterator value="lastCreatedConcreteTopics">
					<li>
						<s:url id="showTopic" action="showTopic" includeParams="none">
							<s:param name="uri">${fn:substringAfter(uri,"ontology.owl#")}</s:param>
						</s:url>
						<a href="<s:property value="#showTopic"/>">
							<s:set id="iName" name="iName" value="name"/>
							<c:set var="nameLength" value="${fn:length(iName)}"/>
							${fn:substring(iName, 0, 50)} 
							<c:if test="${nameLength > maxNameLength}">...</c:if>
						</a>
					</li>
				</s:iterator>
			</ol>
			</p>
		</s:else>
</div>
</td>
<td>
<div id="lastModifiedTopics" class="last-list">
	<s:if test="lastModifiedConcreteTopics.isEmpty">
		<h3><span><fmt:message key="mainMenu.lastModifiedTopics.not"/></span></h3>
	</s:if>
    <s:else>
		<h3><span><fmt:message key="mainMenu.lastModifiedTopics"/></span></h3>
		<p>
			<ol>
				<s:iterator value="lastModifiedConcreteTopics">
					<li>
						<s:url id="showTopic" action="showTopic" includeParams="none">
							<s:param name="uri">${fn:substringAfter(uri,"ontology.owl#")}</s:param>
						</s:url>
						<a href="<s:property value="#showTopic"/>">
							<s:set id="iName" name="iName" value="name"/>
							<c:set var="nameLength" value="${fn:length(iName)}"/>
							${fn:substring(iName, 0, 50)} 
							<c:if test="${nameLength > maxNameLength}">...</c:if>
						</a>					
					</li>
				</s:iterator>
			</ol>
		</p>
	</s:else>
</div>
</td></tr></table>
</div>

<div>
<table><tr><td>
<div id="lastCreatedTopicGroups" class="last-list">
	<s:if test="lastCreatedAbstractTopics.isEmpty">
		<h3><span><fmt:message key="mainMenu.lastCreatedAbstractTopics.not"/></span></h3>
	</s:if>
    <s:else>
		<h3><span><fmt:message key="mainMenu.lastCreatedAbstractTopics"/></span></h3>
		<p>
			<ol>
				<s:iterator value="lastCreatedAbstractTopics">
					<li>
						<s:url id="showAbstractTopic" action="showAbstractTopic" includeParams="none">
							<s:param name="uri">${fn:substringAfter(uri,"ontology.owl#")}</s:param>
						</s:url>
						<a href="<s:property value="#showAbstractTopic"/>">
							<s:set id="iName" name="iName" value="name"/>
							<c:set var="nameLength" value="${fn:length(iName)}"/>
							${fn:substring(iName, 0, 50)} 
							<c:if test="${nameLength > maxNameLength}">...</c:if>
						</a>
					</li>
				</s:iterator>
			</ol>
		</p>
	</s:else>
</div>
</td>
<td>
<div id="lastModifiedTopicGroups" class="last-list">
	<s:if test="lastModifiedAbstractTopics.isEmpty">
		<h3><span><fmt:message key="mainMenu.lastModifiedAbstractTopics.not"/></span></h3>
	</s:if>
    <s:else>
		<h3><span><fmt:message key="mainMenu.lastModifiedAbstractTopics"/></span></h3>
		<p>
			<ol>
				<s:iterator value="lastModifiedAbstractTopics">
					<li>
						<s:url id="showAbstractTopic" action="showAbstractTopic" includeParams="none">
							<s:param name="uri">${fn:substringAfter(uri,"ontology.owl#")}</s:param>
						</s:url>
						<a href="<s:property value="#showAbstractTopic"/>">
							<s:set id="iName" name="iName" value="name"/>
							<c:set var="nameLength" value="${fn:length(iName)}"/>
							${fn:substring(iName, 0, 50)} 
							<c:if test="${nameLength > maxNameLength}">...</c:if>
						</a>					
					</li>
				</s:iterator>
			</ol>
		</p>
	</s:else>
</div>
</td></tr></table>
</div>
-->