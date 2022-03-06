<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head th:replace="fragment.basic :: head" ></head>
<body>
<div class="page-header">
    <h1 th:text="#{label.page.login.title}" class="ml-1" style="margin-left: 20px;"></h1>
</div>

<div  style="position: relative" >
    <div style="width: 500px; margin: auto; padding-top: 30px">
        <form action="#" th:action="@{/login}" method="post" th:object="${credentials}">
            <!--      <div class="user-icon-box">-->
            <!--        <i class="fa fa-user"></i>-->
            <!--      </div>-->
            <!--      <div class="key-icon-box">-->
            <!--        <i class="fa fa-key"></i>-->
            <!--      </div>-->
            <div class="form-group">
                <label for="userName">Login:</label>
                <input type="text" class="form-control" th:field="*{userName}" id="userName">
            </div>
            <div class="form-group">
                <label for="pwd">Password:</label>
                <input type="password" class="form-control" th:field="*{password}" id="pwd">
            </div>
            <button type="submit" class="btn btn-default">Submit</button>
        </form>
    </div>
</div>

<script th:inline="javascript">
    /*<![CDATA[*/

    var credentials = /*[[${credentials}]]*/

    /*]]>*/
</script>

<!--<div th:replace="fragment.basic :: 'footer'">-->
<!--  &copy; 2016 The Static Templates-->
<!--</div>-->
</body>
</html>