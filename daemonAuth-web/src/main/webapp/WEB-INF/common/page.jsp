<%@ page language="java" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<div id="pageStr">
    <c:out value="${pagestr}"/>
</div>
<script>
    $(document).ready(function () {
        $('#pageStr').html($('#pageStr').text());
    });

</script>