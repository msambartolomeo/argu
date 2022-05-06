<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<body>
    <div class="card debate-form-container">
        <c:url value="/create_debate" var="postPath" />
        <%--@elvariable id="createDebateForm" type="ar.edu.itba.paw.webapp.form.CreateDebateForm"--%>
        <form:form method="post" action="${postPath}" modelAttribute="createDebateForm" acceptCharset="utf-8" enctype="multipart/form-data">
            <div class="card-content">
                <span class="card-title center"><spring:message code="components.create-debate"/></span>
                <div class="input-field">
                    <c:set var="titleErrors" ><form:errors path="title"/></c:set>
                    <form:label path="title"><spring:message code="components.create-debate-title"/></form:label>
                    <form:textarea path="title" maxlength="64" cssClass="materialize-textarea ${not empty titleErrors ? 'invalid' : ''}"/>
                    <form:errors path="title" element="span" cssClass="error" />
                </div>

                <div class="input-field">
                    <c:set var="descriptionErrors" ><form:errors path="description"/></c:set>
                    <form:label path="description"><spring:message code="components.create-debate-description"/></form:label>
                    <form:textarea path="description" maxlength="280" cssClass="materialize-textarea ${not empty descriptionErrors ? 'invalid' : ''}"/>
                    <form:errors path="description" element="span" cssClass="error" />
                </div>

                <div class="input-field">
                    <c:set var="usernameError"><form:errors path="opponentUsername"/></c:set>
                    <form:label path="opponentUsername">
                        <spring:message code="components.create-debate-opponentUsername"/>
                    </form:label>
                    <form:input path="opponentUsername" maxlength="64" cssClass="${not empty usernameError ? 'invalid' : ''}"/>
                    <form:errors path="opponentUsername" element="span" cssClass="helper-text error" />
                </div>

                <table class="no-borders">
                    <tr>
                        <td><spring:message code="components.create-debate-category"/></td>
                        <td>
                            <div class="input-field">
                                <form:select path="category">
                                    <option disabled selected value> <spring:message code="category.select-category" /> </option>
                                    <c:forEach var="category" items="${categories}">
                                        <form:option value="${category}"><spring:message code="category.${category.name}"/></form:option>
                                    </c:forEach>
                                </form:select>
                                <form:errors path="category" element="span" cssClass="helper-text error" />
                            </div>
                        </td>
                    </tr>
                </table>

                <table class="no-borders">
                    <tr>
                        <td><spring:message code="components.create-debate-image"/></td>
                        <td>
                            <div class="file-field input-field">
                                <div class="btn">
                                    <form:label path="image" for="image"><spring:message code="components.user-image-button"/></form:label>
                                    <form:input id="image" path="image" type="file"/>
                                </div>
                                <div class="file-path-wrapper">
                                    <c:set var="imageErrors"><form:errors path="image"/></c:set>
                                    <form:input path="imageName" class="file-path validate ${not empty imageErrors ? 'invalid' : ''}" type="text"/>
                                </div>
                                <form:errors path="image" cssClass="helper-text error"/>
                            </div>
                        </td>
                    </tr>
                </table>

                <button class="btn waves-effect center-block" type="submit"
                        name="action"><spring:message code="components.create-debate"/>
                    <i class="material-icons right">post_add</i>
                </button>
            </div>
        </form:form>
    </div>
</body>
</html>
