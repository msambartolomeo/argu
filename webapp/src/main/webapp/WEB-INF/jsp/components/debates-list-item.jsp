<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
    <body>
        <a class="card radius black-text" href="/debate/${debate.debateId}">
            <div class="card-content">
                <span class="card-title">
                    <c:out value="${debate.name}"/>
<%--                    <span class="new badge blue-grey darken-2" data-badge-caption="16:00 15/04/22"></span>--%>
<%--                    <span class="new badge blue-grey darken-2" data-badge-caption="Category1"></span>--%>
                </span>
                <p><c:out value="${debate.description}"/></p>
            </div>
        </a>
    </body>

</html>

<style>
    :root {
        --badge-color: #364156;
    }

    .normalized-margins {
        margin: 1% auto;
        justify-content: center;
    }

    .radius {
        border-radius: 10px;
        width: 98%;
    }
</style>