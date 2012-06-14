<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="item.tgroups"/></title>
    <meta name="heading" content="<fmt:message key='item.tgroups'/>"/>
  
    <script type="text/javascript" src="<c:url value='/scripts/jquery.treeview.pack.js'/>"></script>
  	<script>
  		jQuery(document).ready(function(){
    		jQuery("#itree").treeview();
  		});
  	</script>

</head>

<div>
	<appfuse:cptree name="" folded="true" treemap="${tree}"/>
</div>

	