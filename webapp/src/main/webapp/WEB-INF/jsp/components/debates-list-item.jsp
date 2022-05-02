<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>

<html>
<body>
<a class="card black-text" href="<c:url value="/debates/${debate.debateId}"/>">
    <div class="card-content debate-info-holder">
        <div class="debate-holder-separator">
            <div class="debate-text-holder">
                <h3 class="debate-title word-wrap"><c:out value="${debate.name}"/></h3>
                <c:if test="${debate.creatorUsername != null}">
                    <c:set var="creator"><c:out value="${debate.creatorUsername}"/></c:set>
                    <h6><spring:message code="components.debate-list-item.creator" arguments="${creator}"/></h6>
                </c:if>
            </div>
            <div class="debate-footer">
                <span class="new badge blue-grey darken-2"
                      data-badge-caption="<spring:message code="status.${debate.debateStatus.name}"/>"></span>
                <span class="new badge blue-grey darken-2" data-badge-caption="${debate.createdDate}"></span>
                <span class="new badge blue-grey darken-2"
                      data-badge-caption="<spring:message code="category.${debate.debateCategory.name}"/>"></span>
                <span class="new badge blue-grey darken-2"
                      data-badge-caption="<spring:message code="page.debate.subscribed" arguments="${debate.subscribedUsers}"/>"></span>
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