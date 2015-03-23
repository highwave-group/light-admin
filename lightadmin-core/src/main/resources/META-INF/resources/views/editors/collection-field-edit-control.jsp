<%@ page import="org.lightadmin.core.persistence.metamodel.PersistentPropertyType" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="light" uri="http://www.lightadmin.org/tags" %>

<jsp:useBean id="attributeMetadata" type="org.springframework.data.mapping.PersistentProperty" scope="request"/>

<input name="${attributeMetadata.name}[]" type="text" />
<br />
<a href="javascript:void(0);"
	title="Add Element" class="${attributeMetadata.name}-collection-add btn14 mr5"><img
	src="<light:url value='/images/icons/dark/create.png'/>"
	alt="Add Element"></a>
	
<script type="text/javascript">
	$(function () {
		$( ".${attributeMetadata.name}-collection-add " ).click(function() {
			nextEditor = $("<input name='${attributeMetadata.name}[]' type='text' />");
			$(this).parent().children('input').last().after(nextEditor);
		});
	});
</script>