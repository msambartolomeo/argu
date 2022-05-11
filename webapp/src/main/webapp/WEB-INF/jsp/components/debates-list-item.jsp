<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<html>
<body>
<a class="card black-text hoverable" href="<c:url value="/debates/${debate.debateId}"/>">
    <div class="card-content debate-info-holder">
        <div class="debate-holder-separator">
            <div class="debate-text-holder">
                <h5 class="debate-title word-wrap"><c:out value="${debate.name}"/></h5>
                <c:if test="${debate.creatorUsername != null}">
                    <c:set var="creator"><c:out value="${debate.creatorUsername}"/></c:set>
                    <h6><b><spring:message code="components.debate-list-item.creator"/></b> ${creator}</h6>
                </c:if>
            </div>
            <div class="debate-footer">
                <div class="chip"><spring:message code="category.${debate.debateCategory.name}"/></div>
                <div class="chip"><spring:message code="components.debate-created-on"/> ${debate.createdDate}</div>
                <div class="chip"><spring:message code="status.${debate.debateStatus.name}"/></div>
                <div class="chip"><spring:message code="page.debate.subscribed" arguments="${debate.subscribedUsers}"/></div>
            </div>
        </div>
        <c:if test="${debate.imageId != 0}">
            <div class="image-width">
                <img src="<c:url value="/images/${debate.imageId}"/>" class="limit-img-sm responsive-img" alt="<spring:message
                                    code="pages.debate-picture"/>"/>
            </div>
        </c:if>
    </div>
</a>
</body>
</html>