<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<html>
    <body>
        <a class="card black-text" href="<c:url value="/debates/${debate.debateId}"/>">
            <div class="card-content">
                <c:choose>
                    <c:when test="${debate.imageId != 0}">
                        <div class="row">
                            <div class="col s3">
                                <img src="<c:url value="/images/${debate.imageId}"/>" class="circle responsive-img" alt="<spring:message
                                    code="pages.debate-picture"/>"/>
                            </div>
                            <div class="col s9">
                                <span class="card-title word-wrap">
                                    <c:out value="${debate.name}"/>
                                    <span class="new badge blue-grey darken-2" data-badge-caption="<spring:message code="status.${debate.debateStatus.name}"/>"></span>
                                    <span class="new badge blue-grey darken-2" data-badge-caption="${debate.createdDate}"></span>
                                    <span class="new badge blue-grey darken-2" data-badge-caption="<spring:message code="category.${debate.debateCategory.name}"/>"></span>
                                </span>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <span class="card-title word-wrap">
                            <c:out value="${debate.name}"/>
                            <span class="new badge blue-grey darken-2" data-badge-caption="<spring:message code="status.${debate.debateStatus.name}"/>"></span>
                            <span class="new badge blue-grey darken-2" data-badge-caption="${debate.createdDate}"></span>
                            <span class="new badge blue-grey darken-2" data-badge-caption="<spring:message code="category.${debate.debateCategory.name}"/>"></span>
                        </span>
                    </c:otherwise>
                </c:choose>
            </div>
        </a>
    </body>
</html>