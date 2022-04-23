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
                <!-- Modal Trigger -->
                <a class="waves-effect waves-light btn modal-trigger" href="#edit-profile-image">Edit Profile Image</a>
                <!-- Modal Structure -->
                <div id="edit-profile-image" class="modal">
                    <div class="modal-content">
                        <h4>Edit Profile Image</h4>
                        <form action="#">
                            <div class="file-field input-field">
                                <div class="btn">
                                    <span>Upload</span>
                                    <input type="file">
                                </div>
                                <div class="file-path-wrapper">
                                    <input class="file-path validate" type="text">
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <a href="#!" class="modal-close waves-effect waves-green btn-flat">Close</a>
                        <a href="#!" class="modal-close waves-effect waves-green btn-flat">Edit</a>
                    </div>
                </div>
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
        <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
        <script>
            M.AutoInit();
            $(document).ready(function(){
                $('.modal').modal();
            });
        </script>
    </body>
</html>
