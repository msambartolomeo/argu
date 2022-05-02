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
                    <spring:url value="" var="prevPage">
                        <c:forEach items="${param}" var="curParam">
                            <c:if test="${curParam.key != 'page'}">
                                <spring:param name="${curParam.key}" value="${curParam.value}"/>
                            </c:if>
                        </c:forEach>
                        <spring:param name="page" value="${param.page - 1}"/>
                    </spring:url>
                    <a href="${prevPage}"><</a>
                </li>
            </c:if>
            <c:forEach var="page" begin="1" end="${total_pages}">
                <li class="active page-number ${(page == 1 && empty param.page) || page-1 == param.page ? 'selected-button' : ''}">
                    <spring:url value="" var="link">
                        <c:forEach items="${param}" var="curParam">
                            <c:if test="${curParam.key != 'page'}">
                                <spring:param name="${curParam.key}" value="${curParam.value}"/>
                            </c:if>
                        </c:forEach>
                        <spring:param name="page" value="${page - 1}"/>
                    </spring:url>
                    <a href="${link}">${page}</a>
                </li>
            </c:forEach>
            <c:if test="${param.page + 1 < total_pages}">
                <li class="active page-number">
                    <spring:url value="" var="nextPage">
                        <c:forEach items="${param}" var="curParam">
                            <c:if test="${curParam.key != 'page'}">
                                <spring:param name="${curParam.key}" value="${curParam.value}"/>
                            </c:if>
                        </c:forEach>
                        <spring:param name="page" value="${param.page + 1}"/>
                    </spring:url>
                    <a href="${nextPage}">></a>
                </li>
            </c:if>
        </c:if>
    </ul>
</div>
</body>
</html>
