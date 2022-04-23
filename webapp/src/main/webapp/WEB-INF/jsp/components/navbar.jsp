<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<html>
    <body>
        <div class="navbar-fixed">
<%--            <nav>--%>
            <nav class="nav-wrapper nav-bar">
                <a href="${pageContext.request.contextPath}/" class="brand-logo">
                    <img src="${pageContext.request.contextPath}/resources/images/argu-logo-2.jpeg" class="logo-image" alt="Argu Logo">
                    &#8205
                </a>
                <a href="" data-target="mobile-links" class="sidenav-trigger right"><i class="material-icons">menu</i></a>
                <ul class="right hide-on-med-and-down">
                    <c:choose>
                        <c:when test="${pageContext.request.userPrincipal.name != null}">
                            <li>
                                <a href="<c:url value="/profile"/>">
                                    <i class="material-icons left">account_circle</i>
                                    <spring:message code="components.navbar.profile"/>
                                </a>
                            </li>
                            <li>
                                <a href="<c:url value="/logout"/>">
                                    <i class="material-icons left">logout</i>
                                    <spring:message code="components.navbar.logout"/>
                                </a>
                            </li>
                        </c:when>

                        <c:otherwise>
                            <li>
                                <a href="<c:url value="/login"/>">
                                    <i class="material-icons left">check</i>
                                    <spring:message code="components.login"/>
                                </a>
                            </li>
                            <li>
                                <a href="<c:url value="/register"/>">
                                    <i class="material-icons left">person_add_alt</i>
                                    <spring:message code="components.register"/>
                                </a>
                            </li>
                        </c:otherwise>
                    </c:choose>

                    <li>
                        <div class="input-field search-size">
                            <input placeholder="<spring:message code="components.navbar.search"/>" id="search"
                                   type="search" required>
                            <label class="label-icon" for="search">
                                <i class="material-icons">search</i>
                            </label>
                            <i class="material-icons">close</i>
                        </div>
                    </li>
                </ul>
            </nav>
<%--            </nav>--%>
        </div>

        <ul class="sidenav" id="mobile-links">
            <li>
                <div class="row">
                    <div class="input-field search-size">
                        <input placeholder="<spring:message code="components.navbar.search"/>" id="search-mobile"
                               type="search" required>
                        <label class="label-icon" for="search-mobile">
                            <i class="material-icons">search</i>
                        </label>
                        <i class="material-icons">close</i>
                    </div>
                </div>
            </li>
            <c:choose>
                <c:when test="${pageContext.request.userPrincipal.name != null}">
                    <li>
                        <a href="<c:url value="/profile"/>">
                            <i class="material-icons left">account_circle</i>
                            <spring:message code="components.navbar.profile"/>
                        </a>
                    </li>
                    <li>
                        <a href="<c:url value="/logout"/>">
                            <i class="material-icons left">logout</i>
                            <spring:message code="components.navbar.logout"/>
                        </a>
                    </li>
                </c:when>

                <c:otherwise>
                    <li>
                        <a href="<c:url value="/login"/>">
                            <i class="material-icons left">check</i>
                            <spring:message code="components.login"/>
                        </a>
                    </li>
                    <li>
                        <a href="<c:url value="/register"/>">
                            <i class="material-icons left">person_add_alt</i>
                            <spring:message code="components.register"/>
                        </a>
                    </li>
                </c:otherwise>
            </c:choose>
        </ul>

        <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
        <script type="text/javascript" src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
        <script type="text/javascript">
            document.addEventListener('DOMContentLoaded', function() {
                var elems = document.querySelectorAll('.sidenav');
                var instances = M.Sidenav.init(elems, options);
            });
        </script>

    </body>
</html>