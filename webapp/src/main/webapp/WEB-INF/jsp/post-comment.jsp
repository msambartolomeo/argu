<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<html>
<body>
<div class="card">
    <form action="<c:url value="/debate"/>" method="post">
        <div class="card-content">
            <span class="card-title">${user.username}</span>
            <div>
                <label for="comment">Add your comment:</label>
                <input type="text" name="comment" id="comment"/>
            </div>
            <div>
                <label for="email">Email:</label>
                <input type="email" name="email" id="email"/>
            </div>
        </div>
        <div class="card-action">
            <a type="submit">Post comment!</a>
        </div>
    </form>
</div>
</body>
</html>
