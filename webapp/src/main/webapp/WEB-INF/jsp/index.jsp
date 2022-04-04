<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<html>
    <body>
        <h2>Hello <c:out value="${user.username}"/>!</h2>
        <h2>The id is <c:out value="${user.id}"/>!</h2>
    </body>
</html>