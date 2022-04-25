<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<body>
    <div class="card debate-form-container">
        <c:url value="/create-debate" var="postPath" />
        <%--@elvariable id="createDebateForm" type="ar.edu.itba.paw.webapp.form.CreateDebateForm"--%>
        <form:form method="post" action="${postPath}" modelAttribute="createDebateForm" acceptCharset="utf-8">
            <div class="card-content">
                <c:set var="globalErrors"><form:errors/></c:set>
                <span class="card-title center"><spring:message code="components.create-debate"/></span>
                <div class="input-field">
                    <form:label path="title" for="title">* <spring:message code="components.create-debate-title"/></form:label>
                    <form:textarea path="title" id="title" maxlength="64" cssClass="materialize-textarea"/>
                    <form:errors path="title" element="span" cssClass="error" />
                </div>

                <div class="input-field">
                    <form:label path="description" for="description">* <spring:message code="components.create-debate-description"/>
                    </form:label>
                    <form:textarea path="description" id="description" maxlength="280" cssClass="materialize-textarea"/>
                    <form:errors path="description" element="span" cssClass="error" />
                </div>

                <div class="input-field">
                    <c:set var="usernameError"><form:errors path="opponentUsername"/></c:set>
                    <form:label path="opponentUsername" for="opponentUsername">* <spring:message
                            code="components.create-debate-opponentUsername"/></form:label>
                    <form:input path="opponentUsername" id="opponentUsername" maxlength="64"
                                cssClass="${not empty usernameError ? 'invalid' : ''}"/>
                    <form:errors path="opponentUsername" element="span" cssClass="helper-text error" />
                </div>

                <table class="no-borders">
                    <tr>
                        <td>* <spring:message code="components.create-debate-category"/></td>
                        <td>
                            <div class="input-field">
                                <form:select id="categoryId" path="categoryId">
                                    <form:option value="0"><spring:message code="components.debate-category-00"/></form:option>
                                    <form:option value="1"><spring:message code="components.debate-category-01"/></form:option>
                                    <form:option value="2"><spring:message code="components.debate-category-02"/></form:option>
                                    <form:option value="3"><spring:message code="components.debate-category-03"/></form:option>
                                    <form:option value="4"><spring:message code="components.debate-category-04"/></form:option>
                                    <form:option value="5"><spring:message code="components.debate-category-05"/></form:option>
                                    <form:option value="6"><spring:message code="components.debate-category-06"/></form:option>
                                    <form:option value="7"><spring:message code="components.debate-category-07"/></form:option>
                                    <form:option value="8"><spring:message code="components.debate-category-08"/></form:option>
                                    <form:option value="9"><spring:message code="components.debate-category-09"/></form:option>
                                    <form:option value="10"><spring:message code="components.debate-category-10"/></form:option>
                                    <form:option value="11"><spring:message code="components.debate-category-11"/></form:option>
                                </form:select>
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
                                    <input class="file-path validate" type="text"/>
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

        <h6 class="center">(*) <spring:message code="components.mandatory-field"/></h6>
    </div>
</body>
</html>
