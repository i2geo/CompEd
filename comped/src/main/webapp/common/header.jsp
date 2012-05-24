<%@ include file="/common/taglibs.jsp"%>

<div id="branding">
    <h1><a href="<c:url value='/'/>"><fmt:message key="webapp.name"/></a></h1>
    <p><fmt:message key="webapp.tagline"/></p>
</div>

<hr />

<%-- Put constants into request scope --%>
<appfuse:constants scope="request"/>