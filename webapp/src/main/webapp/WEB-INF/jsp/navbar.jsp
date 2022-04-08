<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <!--Import Google Icon Font-->
      <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    </head>
<body>
    <div class="navbar-fixed">
        <nav>
            <div class="nav-wrapper nav-bar">
                <a href="#" class="brand-logo logo-text logo">
                    <i class="material-icons">record_voice_over</i>
                    NoReddit
                </a>
                <form class="right hide-on-med-and-down">
                    <div class="input-field" style="max-width: 400pt;">
                      <input class="search-color" placeholder="Search" id-="search" type="search" required>
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

<style>
    .nav-bar {
        background: var(--primary-color);
    }
    .search-color {
        background: var(--white);
    }
    .logo-text {
        margin-left: 1%;
    }
    .logo {
        display: inline-block;
        height: 100%;
    }
    .logo>img {
        vertical-align: middle;
    }
</style>
