<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
    <body>
        <div class="navbar-fixed">
            <nav>
                <div class="nav-wrapper nav-bar">
<%--                    <div class="left ">--%>
                        <a href="/" class="brand-logo">
                            <img src="/resources/images/image.png" class="logo-image" alt="NoReddit Logo">
                            NoReddit
                        </a>
<%--                    </div>--%>
                    <form class="right">
                        <div class="input-field search-size">
                            <input class="search-color" placeholder="Search" id="search" type="search" required>
                            <label class="label-icon" for="search">
                                <i class="material-icons">search</i>
                            </label>
                            <i class="material-icons">close</i>
                        </div>
                    </form>
                </div>
            </nav>
        </div>
    </body>
</html>