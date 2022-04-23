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
                <ul class="right hide-on-med-and-down">
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
            </nav>
<%--            </nav>--%>
        </div>

    </body>
</html>