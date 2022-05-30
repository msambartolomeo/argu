<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
  <body>
    <c:url value="/profile" var="imagePostPath" />
    <%--@elvariable id="profileImageForm" type="ar.edu.itba.paw.webapp.form.ProfileImageForm"--%>
    <form:form modelAttribute="profileImageForm" method="post" action="${imagePostPath}"
               acceptCharset="utf-8" id="photoForm" enctype="multipart/form-data">
      <c:set var="imageError" scope="request" ><form:errors path="file"/></c:set>
      <div class="modal-content">
        <h4>
          <spring:message code="pages.profile.edit-profile-image"/>
        </h4>
          <div class="file-field input-field">
            <div class="btn">
              <form:label path="file"><spring:message code="pages.profile.upload"/></form:label>
              <form:input path="file" type="file"/>
            </div>
            <div class="file-path-wrapper">
              <form:input path="fileName" class="file-path validate ${not empty imageError? 'invalid' : ''}" type="text"/>
            </div>
            <form:errors path="file" cssClass="helper-text error"/>
          </div>
      </div>
      <div class="modal-footer">
        <a href="" class="modal-close waves-effect btn-flat">
          <spring:message code="pages.profile.close"/>
        </a>
        <button class="modal-close waves-effect btn-flat" type="submit" form="photoForm"
                id="photoForm" onclick="this.form.submit();" name="editImage">
          <spring:message code="pages.profile.confirm"/>
        </button>
      </div>
    </form:form>
  </body>
</html>