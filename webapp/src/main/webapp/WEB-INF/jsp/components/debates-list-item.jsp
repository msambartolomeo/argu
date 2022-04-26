<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<html>
    <body>
        <a class="card black-text" href="<c:url value="/debates/${debate.debateId}"/>">
            <div class="card-content">
                <span class="card-title word-wrap">
                    <c:out value="${debate.name}"/>
                    <span class="new badge blue-grey darken-2" data-badge-caption="${debate.createdDate}"></span>
                    <span class="new badge blue-grey darken-2" data-badge-caption="<spring:message code="category.${debate.debateCategory.name}"/>"></span>
                </span>
                <p class="word-wrap"><c:out value="${debate.description}"/></p>
            </div>
        </a>
    </body>
</html>