<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
    <head>
        <title>Profile</title>
        <%@include file="../components/imports.jsp"%>
    </head>
    <body>
        <%@include file="../components/navbar.jsp" %>
        <div class="profile-container">
            <div class="card profile-data">
                <img src="${pageContext.request.contextPath}/resources/images/user-profile-default.png" class="responsive-img">
                <h5>
                    <c:out value="${user.username}"/>
                </h5>
                <div class="email-format">
                    <i class="material-icons">email</i>
                    <p>
                        <c:out value="${user.email}"/>
                    </p>
                </div>
                <p>
                    Created in: <c:out value="${user.getcreatedDate()}"/>
                </p>
            </div>
            <div class="card user-debates">
                <h5>Debates subscribed:</h5>
                <c:if test="${suscribed_debates.size() > 0}">
                    <c:forEach var="debate" items="${suscribed_debates}">
                        <div class="list-item">
                            <c:set var="debate" value="${suscribed_debates}" scope="request"/>
                            <%@include file="../components/debates-list-item.jsp" %>
                        </div>
                    </c:forEach>
                </c:if>
            </div>
        </div>

    </body>
</html>
