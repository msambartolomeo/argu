<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<body>
<div class="chat-box card-content">

    <h5><spring:message code="components.chat.title"/></h5>

    <div class="message-container" id="chat">
        <c:choose>
            <c:when test="${chats.size() > 0}">
                <c:forEach items="${chats}" var="chat">
                    <div class="message">
                        <div class="datetime"><c:out value="${chat.formattedDate}"/></div>
                        <p><c:out value="${chat.user.username}"/></p>
                        <p><c:out value="${chat.message}"/></p>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="message">
                    <h6><spring:message code="components.chat.no-messages"/></h6>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <c:choose>
        <c:when test="${pageContext.request.userPrincipal != null }">
            <c:if test="${(debate.status.name != 'closed' && debate.status.name != 'deleted')}">
                <c:url value="/debates/${debate.debateId}/chat" var="postPath"/>
                <%--@elvariable id="chatForm" type="ar.edu.itba.paw.webapp.form.ChatForm"--%>
                <form:form modelAttribute="chatForm" action="${postPath}" method="post" acceptCharset="utf-8"
                           id="postForm">
                    <div class="send-chat">
                        <div class="input-field flex-grow">
                            <c:set var="messageError"><form:errors path="message"/></c:set>
                            <form:label path="message"><spring:message code="components.chat.message"/></form:label>
                            <form:input class="materialize-textarea ${not empty messageError ? 'invalid' : ''}"
                                        path="message"/>
                            <form:errors path="message" element="span" cssClass="error"/>
                        </div>
                        <button class="btn waves-effect center-block submitBtn" type="submit" name="chat"
                                form="postForm">
                            <spring:message code="components.chat.send"/>
                        </button>
                    </div>
                </form:form>
            </c:if>


            <div class="center pagination-margin">
                <ul class="pagination">
                    <c:if test="${total_chat_pages > 0}">
                        <p class="center"><spring:message code="components.chat.pages"/></p>
                        <c:if test="${param.chatPage != null && param.chatPage > 0}">
                            <li class="active page-number">
                                <spring:url value="" var="prevPage">
                                    <c:forEach items="${param}" var="curParam">
                                        <c:if test="${curParam.key != 'chatPage'}">
                                            <spring:param name="${curParam.key}" value="${curParam.value}"/>
                                        </c:if>
                                    </c:forEach>
                                    <spring:param name="chatPage" value="${param.chatPage - 1}"/>
                                </spring:url>
                                <a href="${prevPage}"><</a>
                            </li>
                        </c:if>
                        <c:forEach var="page" begin="1" end="${total_chat_pages}">
                            <li class="active page-number ${(page == 1 && empty param.chatPage) || page-1 == param.chatPage ? 'selected-button' : ''}">
                                <spring:url value="" var="link">
                                    <c:forEach items="${param}" var="curParam">
                                        <c:if test="${curParam.key != 'chatPage'}">
                                            <spring:param name="${curParam.key}" value="${curParam.value}"/>
                                        </c:if>
                                    </c:forEach>
                                    <spring:param name="chatPage" value="${page - 1}"/>
                                </spring:url>
                                <a href="${link}">${page}</a>
                            </li>
                        </c:forEach>
                        <c:if test="${param.chatPage + 1 < total_chat_pages}">
                            <li class="active page-number">
                                <spring:url value="" var="nextPage">
                                    <c:forEach items="${param}" var="curParam">
                                        <c:if test="${curParam.key != 'chatPage'}">
                                            <spring:param name="${curParam.key}" value="${curParam.value}"/>
                                        </c:if>
                                    </c:forEach>
                                    <spring:param name="chatPage" value="${param.chatPage + 1}"/>
                                </spring:url>
                                <a href="${nextPage}">></a>
                            </li>
                        </c:if>
                    </c:if>
                </ul>
            </div>
        </c:when>
        <c:otherwise>
            <a href="<c:url value="/login"/>" class="center"><spring:message code="components.chat.login-to-chat"/></a>
        </c:otherwise>
    </c:choose>
</div>
<script>
    const chat = document.getElementById("chat");
    chat.scrollTop = chat.scrollHeight;
</script>
</body>
</html>
