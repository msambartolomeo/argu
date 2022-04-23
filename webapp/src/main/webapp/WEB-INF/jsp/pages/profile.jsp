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
       <%-- <div class="row">
            <div class="col s12">
                <div class="z-depth-3 profile-container card">
                    <div class="col s3 profile-img center">
                        <img src="${pageContext.request.contextPath}/resources/images/user-profile-default.png" class=" responsive-img">
                    </div>
                    <div class="col s9 profile-data">
                        <h5>
                            <c:out value="${user.username}"/>
                        </h5>
                        <div class="email-format">
                            <i class="material-icons">email</i>
                            <h6>
                                <c:out value="${user.email}"/>
                            </h6>
                        </div>
                        <h6>Created in: </h6>
                    </div>
                </div>
            </div>
        </div>--%>
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
            </div>
        </div>

    </body>
</html>
