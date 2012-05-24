<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="item.competencies"/></title>
    <meta name="heading" content="<fmt:message key='item.competencies'/>"/>
  
    <script type="text/javascript" src="<c:url value='/scripts/jquery.treeview.pack.js'/>"></script>
  	<script>
  		jQuery(document).ready(function(){
    		jQuery("#itree").treeview();
  		});
  	</script>

</head>

<div>
	<appfuse:cptree name="" folded="true" individuals="true" treemap="${tree}"/>
</div>

	