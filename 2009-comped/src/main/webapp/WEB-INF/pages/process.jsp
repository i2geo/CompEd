<%@ include file="/common/taglibs.jsp"%>

<head>
    <c:if test="${empty competency.id}">
		<title><fmt:message key="competencyDetail.title"/></title>
	</c:if>
    <c:if test="${not empty competency.id}">
		<title><s:property value="competency.name"/></title>
	</c:if>

    <script type="text/javascript" src="<c:url value='/scripts/jquery.treeview.pack.js'/>"></script>

	<script  type="text/javascript"> 
		jQuery(document).ready(function(){
			jQuery('.ohmy').hide();
			jQuery("moreCommonNames").click(function () {
				jQuery("#moreCommonNames").slideToggle();
			})
			jQuery("moreUncommonNames").click(function () {
				jQuery("#moreUncommonNmes").slideToggle();
			})
			jQuery("moreRareNames").click(function () {
				jQuery("#moreRareNames").slideToggle();
			})
			jQuery("moreFalseFriendNames").click(function () {
				jQuery("#moreFalseFriendNames").slideToggle();
			})
    		jQuery("#itree").treeview();
		})
    </script>
</head>

<!-- set action/id to return to -->
<s:set id="getBackId"  name="getBackId" scope="session" value="competency.id"/>
<c:set var="getBackAction" scope="session" value="showProcess"/>

<!-- shorten item access variables -->
<fmt:message key="item.process" var="item"/>
<fmt:message key="item.processes" var="items"/>
<fmt:message key="item.competencies" var="competencies"/>

<!-- define the chooseMe link -->
<c:set var="chooseMe">
	<a href='javascript:window.opener.chooseNode("${competency.uriSuffix}");'>
		[<fmt:message key='button.chooseMe'/>]
	</a>
</c:set>

<c:set var="editLink">
	<security:authorize ifAnyGranted="ROLE_CURRICULUM_ENCODER">
		<a href="<s:url action="editProcess"><s:param name="id" value="competency.id"/></s:url>">
			[<fmt:message key='button.edit'/>]
		</a>
	</security:authorize>
</c:set>

<c:set var="editName">
	<security:authorize ifAnyGranted="ROLE_TRANSLATOR,ROLE_CURRICULUM_ENCODER">
		<a href="<s:url action="editName"><s:param name="id" value="id"/></s:url>">
			[<fmt:message key='button.edit'/>]
		</a>
		<a href="<s:url action="deleteNameAndGetBack"><s:param name="id" value="id"/></s:url>"
			onclick="return confirmDelete('name')">
			[<fmt:message key='button.delete'/>]
		</a>
	</security:authorize>
</c:set>


<h1 id="itemTitle"><s:property value="competency.name"/></h1>
		<security:authorize ifNotGranted="ROLE_USER">
      <div class="message"><fmt:message key="newbie.welcome"/><br/></div>
    </security:authorize>


<div class="competency">
	<span class="competency-date"><fmt:message key="item.created"/>: <s:property value="competency.created"/></span>,
	<span class="competency-date"><fmt:message key="item.modified"/>:<s:property value="competency.modified"/></span>
		
	<!-- <h4 class="competency-title"><s:property value="competency.name"/></h4> -->
	<div class="competency-description">
	 	<p><fmt:message key="item.label.uri"/>: <s:property value="competency.uri"/> </p>
	 	<p><s:property escape="false" value="competency.description"/></p>
	 	<p>
			<c:out value="${chooseMe}" escapeXml="false"/>
			<c:out value="${editLink}" escapeXml="false"/>
	 	</p>
	</div>

	<h4 class="competency-sub-title">
		<fmt:message key="item.names"/>
		<security:authorize ifAnyGranted="ROLE_TRANSLATOR,ROLE_CURRICULUM_ENCODER">
		<a href="<s:url action="editName" includeParams="none"/>">
			[<fmt:message key='button.add'/>]
		</a>
		</security:authorize>
	</h4>

	<div class="competency-relations">
		<p>
			<s:if test="defaultLangCommonNames.isEmpty & otherLangCommonNames.isEmpty">
				<fmt:message key="name.type.common.label.not"/>
				<s:if test="!otherLangCommonNames.isEmpty">
					<moreCommonNames><u>[<fmt:message key="names.more"/>]</u></moreCommonNames>
				</s:if>
			</s:if>
		    <s:else>
				<fmt:message key="name.type.common.label"/>
				<s:if test="!otherLangCommonNames.isEmpty">
					<moreCommonNames><u>[<fmt:message key="names.more"/>]</u></moreCommonNames>
				</s:if>
				<ul>
					<s:iterator value="defaultLangCommonNames">
						<s:url value="%{'images/flags/'+locale+'.png'}" includeParams="none" id="localeURL" />
						<li> 
 							<img src="<s:property value="#localeURL"/>"/>
							<s:property value="name"/>
							<security:authorize ifAnyGranted="ROLE_TRANSLATOR,ROLE_CURRICULUM_ENCODER">
								<a href="<s:url action="editName"><s:param name="id" value="id"/></s:url>">
									[<fmt:message key='button.edit'/>]
								</a>
								<a href="<s:url action="deleteNameAndGetBack"><s:param name="id" value="id"/></s:url>"
									onclick="return confirmDelete('name')">
									[<fmt:message key='button.delete'/>]
								</a>
							</security:authorize>
						</li>
					</s:iterator>
				</ul>
			</s:else>
			</p>
			<div id="moreCommonNames" class="ohmy">
				<ul>
					<s:iterator value="otherLangCommonNames">
						<s:url value="%{'images/flags/'+locale+'.png'}" includeParams="none" id="localeURL" />
						<li> 
 							<img src="<s:property value="#localeURL"/>"/>
							<s:property value="name"/>
							<security:authorize ifAnyGranted="ROLE_TRANSLATOR,ROLE_CURRICULUM_ENCODER">
								<a href="<s:url action="editName"><s:param name="id" value="id"/></s:url>">
									[<fmt:message key='button.edit'/>]
								</a>
								<a href="<s:url action="deleteNameAndGetBack"><s:param name="id" value="id"/></s:url>"
									onclick="return confirmDelete('name')">
									[<fmt:message key='button.delete'/>]
								</a>
							</security:authorize>
						</li>
					</s:iterator>
				</ul>
			</div>
	</div>

	<div class="competency-relations">
		<p>
			<s:if test="defaultLangUncommonNames.isEmpty & otherLangUncommonNames.isEmpty">
				<fmt:message key="name.type.uncommon.label.not"/>
				<s:if test="!otherLangUncommonNames.isEmpty">
					<moreUncommonNames><u>[<fmt:message key="names.more"/>]</u></moreUncommonNames>
				</s:if>
			</s:if>
		    <s:else>
				<fmt:message key="name.type.uncommon.label"/>
				<s:if test="!otherLangUnommonNames.isEmpty">
					<moreUncommonNames><u>[<fmt:message key="names.more"/>]</u></moreUncommonNames>
				</s:if>
				<ul>
					<s:iterator value="defaultLangUncommonNames">
						<s:url value="%{'images/flags/'+locale+'.png'}" includeParams="none" id="localeURL" />
						<li> 
 							<img src="<s:property value="#localeURL"/>"/>
							<s:property value="name"/>
							<security:authorize ifAnyGranted="ROLE_TRANSLATOR,ROLE_CURRICULUM_ENCODER">
								<a href="<s:url action="editName"><s:param name="id" value="id"/></s:url>">
									[<fmt:message key='button.edit'/>]
								</a>
								<a href="<s:url action="deleteNameAndGetBack"><s:param name="id" value="id"/></s:url>"
									onclick="return confirmDelete('name')">
									[<fmt:message key='button.delete'/>]
								</a>
							</security:authorize>
						</li>
					</s:iterator>
				</ul>
			</s:else>
		</p>
		<div id="moreUncommonNames" class="ohmy">
				<ul>
					<s:iterator value="otherUnommonNames">
						<s:url value="%{'images/flags/'+locale+'.png'}" includeParams="none" id="localeURL" />
						<li> 
 							<img src="<s:property value="#localeURL"/>"/>
							<s:property value="name"/>
							<security:authorize ifAnyGranted="ROLE_TRANSLATOR,ROLE_CURRICULUM_ENCODER">
								<a href="<s:url action="editName"><s:param name="id" value="id"/></s:url>">
									[<fmt:message key='button.edit'/>]
								</a>
								<a href="<s:url action="deleteNameAndGetBack"><s:param name="id" value="id"/></s:url>"
									onclick="return confirmDelete('name')">
									[<fmt:message key='button.delete'/>]
								</a>
							</security:authorize>
						</li>
					</s:iterator>
				</ul>
		</div>
	</div>

	<div class="competency-relations">
		<p>
			<s:if test="defaultLangRareNames.isEmpty & otherLangRareNames.isEmpty">
				<fmt:message key="name.type.rare.label.not"/>
				<s:if test="!otherLangRareNames.isEmpty">
					<moreRareNames><u>[<fmt:message key="names.more"/>]</u></moreRareNames>
				</s:if>
			</s:if>
		    <s:else>
				<fmt:message key="name.type.rare.label"/>
				<s:if test="!otherLangRareNames.isEmpty">
					<moreRareNames><u>[<fmt:message key="names.more"/>]</u></moreRareNames>
				</s:if>
				<ul>
					<s:iterator value="defaultLangRareNames">
						<s:url value="%{'images/flags/'+locale+'.png'}" includeParams="none" id="localeURL" />
						<li> 
 							<img src="<s:property value="#localeURL"/>"/>
							<s:property value="name"/>
							<security:authorize ifAnyGranted="ROLE_TRANSLATOR,ROLE_CURRICULUM_ENCODER">
								<a href="<s:url action="editName"><s:param name="id" value="id"/></s:url>">
									[<fmt:message key='button.edit'/>]
								</a>
								<a href="<s:url action="deleteNameAndGetBack"><s:param name="id" value="id"/></s:url>"
									onclick="return confirmDelete('name')">
									[<fmt:message key='button.delete'/>]
								</a>
							</security:authorize>
						</li>
					</s:iterator>
				</ul>
			</s:else>
		</p>
		<div id="moreRareNames" class="ohmy">
				<ul>
					<s:iterator value="otherLangRareNames">
						<s:url value="%{'images/flags/'+locale+'.png'}" includeParams="none" id="localeURL" />
						<li> 
 							<img src="<s:property value="#localeURL"/>"/>
							<s:property value="name"/>
							<security:authorize ifAnyGranted="ROLE_TRANSLATOR,ROLE_CURRICULUM_ENCODER">
								<a href="<s:url action="editName"><s:param name="id" value="id"/></s:url>">
									[<fmt:message key='button.edit'/>]
								</a>
								<a href="<s:url action="deleteNameAndGetBack"><s:param name="id" value="id"/></s:url>"
									onclick="return confirmDelete('name')">
									[<fmt:message key='button.delete'/>]
								</a>
							</security:authorize>
						</li>
					</s:iterator>
				</ul>
		</div>
	</div>

	<div class="competency-relations">
		<p>
			<s:if test="defaultLangFalseFriendNames.isEmpty & otherLangFalseFriendNames.isEmpty">
				<fmt:message key="name.type.falsefriend.label.not"/>
				<s:if test="!otherLangFalseFriendNames.isEmpty">
					<moreFalseFriendNames><u>[<fmt:message key="names.more"/>]</u></moreFalseFriendNames>
				</s:if>
			</s:if>
		    <s:else>
				<fmt:message key="name.type.falsefriend.label"/>
				<s:if test="!otherLangFalseFriendNames.isEmpty">
					<moreFalseFriendNames><u>[<fmt:message key="names.more"/>]</u></moreFalseFriendNames>
				</s:if>
				<ul>
					<s:iterator value="defaultLangFalseFriendNames">
						<s:url value="%{'images/flags/'+locale+'.png'}" includeParams="none" id="localeURL" />
						<li> 
 							<img src="<s:property value="#localeURL"/>"/>
							<s:property value="name"/>
							<security:authorize ifAnyGranted="ROLE_TRANSLATOR,ROLE_CURRICULUM_ENCODER">
								<a href="<s:url action="editName"><s:param name="id" value="id"/></s:url>">
									[<fmt:message key='button.edit'/>]
								</a>
								<a href="<s:url action="deleteNameAndGetBack"><s:param name="id" value="id"/></s:url>"
									onclick="return confirmDelete('name')">
									[<fmt:message key='button.delete'/>]
								</a>
							</security:authorize>
						</li>
					</s:iterator>
				</ul>
			</s:else>
		</p>
		<div id="moreFalseFriendNames" class="ohmy"/>
				<ul>
					<s:iterator value="otherLangFalseFriendNames">
						<s:url value="%{'images/flags/'+locale+'.png'}" includeParams="none" id="localeURL" />
						<li> 
 							<img src="<s:property value="#localeURL"/>"/>
							<s:property value="name"/>
							<security:authorize ifAnyGranted="ROLE_TRANSLATOR,ROLE_CURRICULUM_ENCODER">
								<a href="<s:url action="editName"><s:param name="id" value="id"/></s:url>">
									[<fmt:message key='button.edit'/>]
								</a>
								<a href="<s:url action="deleteNameAndGetBack"><s:param name="id" value="id"/></s:url>"
									onclick="return confirmDelete('name')">
									[<fmt:message key='button.delete'/>]
								</a>
							</security:authorize>
						</li>
					</s:iterator>
				</ul>
		</div>
	</div>


	<h4 class="competency-sub-title">
		<fmt:message key="structure"/>
	</h4>

	<div>
		<appfuse:cptree name="" individuals="true" selected="${competency}" treemap="${tree}"/>
	</div>

	<div class="competency-relations">
		<p>
			<s:if test="allSimilar.isEmpty">
				<fmt:message key="structure.allSimilar.not">
					<fmt:param value="${item}"/>
				</fmt:message>
			</s:if>
			<s:else>
				<fmt:message key="structure.allSimilar">
					<fmt:param value="${item}"/>
				</fmt:message>
				<ul>
					<s:iterator value="allSimilar">
						<li>
							<s:url id="showProcess" action="showProcess" includeParams="none">
								<s:param name="uri">${fn:substringAfter(uri,"ontology.owl#")}</s:param>
							</s:url>
							<a href="<s:property value="#showProcess"/>">
								<s:property value="name"/>
							</a>
						</li>
					</s:iterator>
				</ul>
			</s:else>
		</p>
	</div>

	<p class="competency-info">
		<em class="user">
			<fmt:message key='item.creator'/>: <s:property value="competency.creator.username"/>
			<c:out value="${editLink}" escapeXml="false"/>		
		</em>
	</p>
</div>

