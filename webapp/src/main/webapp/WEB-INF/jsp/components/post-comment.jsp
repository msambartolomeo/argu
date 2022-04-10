<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>

    <body>
        <div class="card">
            <c:url value="/debate/${debate.debateId}" var="postPath" />
            <%--@elvariable id="postForm" type="ar.edu.itba.paw.webapp.form.PostForm" --%>
                <form:form modelAttribute="postForm" action="${postPath}" method="post">
                    <div class="card-content">
                        <span class="card-title">Insert your comment</span>
                        <div class="input-field">
                            <form:label path="content">Add your comment</form:label>
                            <form:textarea class="materialize-textarea" rows="5" cols="30" path="content" />
                            <form:errors path="content" element="span" cssClass="error" />
                        </div>
                        <div class="input-field">
                            <form:label path="email">Email</form:label>
                            <form:input type="text" path="email" />
                            <form:errors path="email" element="p" cssClass="error" />
                        </div>
                        <button class="btn waves-effect center-block" type="submit"
                            name="action">Post comment!
                            <i class="material-icons right">send</i>
                        </button>
                    </div>
                </form:form>
        </div>
    </body>
</html>