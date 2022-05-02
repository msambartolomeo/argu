<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
    <body>
        <div class="navbar-fixed">
            <nav class="nav-wrapper nav-bar">
                <a href="<c:url value="/"/>" class="brand-logo">
                    <img src="<c:url value="/resources/images/argu-logo-2.jpeg"/>" class="logo-image" alt="Argu Logo">
                    &#8205
                </a>
                <a href="" data-target="mobile-links" class="sidenav-trigger left"><i class="material-icons">menu</i></a>
                <ul class="right hide-on-med-and-down">
                    <li>
                        <a href="<c:url value="/debates"/>">
                            <i class="material-icons left">explore</i>
                            <spring:message code="components.navbar.explore"/>
                        </a>
                    </li>
                    <c:choose>
                        <c:when test="${pageContext.request.userPrincipal.name != null}">
                            <sec:authorize access="hasAuthority('MODERATOR')">
                                <li>
                                    <a href="<c:url value="/create_debate"/>">
                                        <i class="material-icons left">add</i>
                                        <spring:message code="components.navbar.create-debate"/>
                                    </a>
                                </li>
                            </sec:authorize>
                            <sec:authorize access="!hasAuthority('MODERATOR')">
                                <li>
                                    <a href="<c:url value="/moderator"/>">
                                        <i class="material-icons left">supervisor_account</i>
                                        <spring:message code="components.navbar.become-mod"/>
                                    </a>
                                </li>
                            </sec:authorize>
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
                <form method="get" action="<c:url value="/debates"/>">
                    <div class="input-field search-size hide-on-med-and-down">
                        <input placeholder="<spring:message code="components.navbar.search"/>" id="search"
                               type="search" required name="search">
                        <label class="label-icon" for="search">
                            <i class="material-icons">search</i>
                        </label>
                        <i class="material-icons"><input type="reset" value="" class="search-close">close</i>
                    </div>
                </form>
            </nav>
        </div>

        <ul class="sidenav" id="mobile-links">
            <li>
                <form method="get" action="<c:url value="/debates"/>">
                    <div class="input-field search-size">
                        <input placeholder="<spring:message code="components.navbar.search"/>" id="search-mobile"
                               type="search" required name="search">
                        <label class="label-icon" for="search-mobile">
                            <i class="material-icons">search</i>
                        </label>
                        <i class="material-icons">close</i>
                    </div>
                </form>
            </li>
            <c:choose>
                <c:when test="${pageContext.request.userPrincipal.name != null}">
                    <sec:authorize access="hasAuthority('MODERATOR')">
                        <li>
                            <a href="<c:url value="/create_debate"/>">
                                <i class="material-icons left">add</i>
                                <spring:message code="components.navbar.create-debate"/>
                            </a>
                        </li>
                    </sec:authorize>
                    <sec:authorize access="!hasAuthority('MODERATOR')">
                        <li>
                            <a href="<c:url value="/moderator"/>">
                                <i class="material-icons left">supervisor_account</i>
                                <spring:message code="components.navbar.become-mod"/>
                            </a>
                        </li>
                    </sec:authorize>
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

    </body>
</html>