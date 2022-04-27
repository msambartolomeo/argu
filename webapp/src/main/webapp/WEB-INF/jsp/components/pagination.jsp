<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<html>
<body>
<div class="center pagination-margin">
    <ul class="pagination">
        <c:if test="${total_pages > 0}">
            <c:if test="${param.page != null && param.page > 0}">
                <li class="active page-number">
                    <a href="<c:url value="?page=${param.page - 1}"/>"><</a>
                </li>
            </c:if>
            <c:forEach var="page" begin="1" end="${total_pages}">
                <li class="active page-number ${page-1 == param.page ? 'selected-button' : ''}">
                    <a href="<c:url value="?page=${page-1}"/>">${page}</a>
                </li>
            </c:forEach>
            <c:if test="${param.page + 1 < total_pages}">
                <li class="active page-number">
                    <a href="<c:url value="?page=${param.page + 1}"/>">></a>
                </li>
            </c:if>
        </c:if>
    </ul>
</div>
</body>
</html>
