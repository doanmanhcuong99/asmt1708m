<%--
  Created by IntelliJ IDEA.
  User: doanm
  Date: 18/07/2019
  Time: 9:42 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<form action="#">
    Name: <input type="text" name="name">
    <br>
    Description: <input type="text" name="description">
    <br>
    <input type="button" value="Submit" onclick="doSubmit()">
    <input type="reset" value="Reset">
</form>
<script>
    var action = 1; // create
    var id = 0;
    document.addEventListener("DOMContentLoaded", function (event) {
        var urlString = window.location.href;
        var url = new URL(urlString);
        id = url.searchParams.get("id");
        if (id != null && id.length > 0 && id != 0) {
            action = 2 // update
        }
        console.log(action);
        if (action == 2) {
            console.log("load detail");
            loadDetail(id);
        }
    });
    function loadDetail(id) {
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4 && xhr.status == 200) {
                var responseData = JSON.parse(xhr.responseText);
                if (responseData.data != null) {
                    document.querySelector("[name='name']").value = responseData.data.name;
                    document.querySelector("[name='description']").value = responseData.data.description;
                }
            }
        };
        xhr.open("GET", "https://1-dot-backup-server-002.appspot.com/api/v1/atubes?id=" + id, true);
        xhr.send();
    }
    function doSubmit() {
        var methodAction = "POST";
        var methodUrl = "https://1-dot-backup-server-002.appspot.com/api/v1/atubes";
        if (action == 2) {
            methodAction = "PUT";
            methodUrl = "https://1-dot-backup-server-002.appspot.com/api/v1/atubes?id=" + id;
        }
        var name = document.querySelector("[name='name']").value;
        var description = document.querySelector("[name='description']").value;
        var data = {
            "name": name,
            "description": description
        };
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function () {
            var acceptStatus = 201;
            if (action == 2) {
                acceptStatus = 200;
            }
            if (xhr.readyState == 4 && xhr.status == acceptStatus) {
                var responseData = JSON.parse(xhr.responseText);
                alert("Save success");
                window.location.href = "/aptech-tube/web/list-tube.html";
            }
        };
        xhr.open(methodAction, methodUrl, true);
        xhr.send(JSON.stringify(data));
    }
</script>

</body>
</html>
