<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="search.title"/></title>
    <meta name="heading" content="<fmt:message key='search.heading'/>"/>
</head>


<div class="separator"></div>

<s:if test="%{!cpMatch.isEmpty}">
	<div class="competency-relations">
		<p>
			<em><fmt:message key="item.processes"/></em>
			<ul>
				<s:iterator value="cpMatch">
					<li> 
						<s:url id="showProcess" action="showProcess" includeParams="none">
								<s:param name="uri">${fn:substringAfter(uri,"GeoSkills#")}</s:param>
							</s:url>

						<a href="<s:property value="#showProcess"/>">
							<s:property value="name"/>
						</a>
					</li>
				</s:iterator>
			</ul>
		</p>
	</div>
</s:if>

<s:if test="%{!ccMatch.isEmpty}">
	<div class="competency-relations">
		<p>
			<em><fmt:message key="item.competencies"/></em>
			<ul>
				<s:iterator value="ccMatch">
					<li> 
						<s:url id="showCompetency" action="showCompetency" includeParams="none">
								<s:param name="uri">${fn:substringAfter(uri,"GeoSkills#")}</s:param>
							</s:url>

						<a href="<s:property value="#showCompetency"/>">
							<s:property value="name"/>
						</a>
					</li>
				</s:iterator>
			</ul>
		</p>
	</div>
</s:if>

<s:if test="%{!tMatch.isEmpty}">
	<div class="competency-relations">
		<p>
			<em><fmt:message key="item.topics"/></em>
			<ul>
				<s:iterator value="tMatch">
					<li> 
						<s:url id="showTopic" action="show" includeParams="none">
								<s:param name="uri">${fn:substringAfter(uri,"GeoSkills#")}</s:param>
							</s:url>

						<a href="<s:property value="#showTopic"/>">
							<s:property value="name"/>
						</a>
					</li>
				</s:iterator>
			</ul>
		</p>
	</div>
</s:if>

<script type="text/javascript">
    Form.focusFirstElement($("searchForm"));
</script>