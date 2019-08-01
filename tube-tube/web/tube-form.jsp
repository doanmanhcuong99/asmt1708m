<%--
  Created by IntelliJ IDEA.
  User: doanm
  Date: 18/07/2019
  Time: 9:43 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<ul id="content"></ul>
<script>
    document.addEventListener("DOMContentLoaded", function (event) {
        loadList();
    });
    function loadList() {
        var xhr = new XMLHttpRequest();
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4 && xhr.status == 200) {
                var responseData = JSON.parse(xhr.responseText);
                if (responseData.data != null && responseData.data.length > 0) {
                    var contentToAdd = "";
                    for (let i = 0; i < responseData.data.length; i++) {
                        contentToAdd += `<li>${responseData.data[i].name} &nbsp;&nbsp;`
                            + `<a href="./tube-form.html?id=${responseData.data[i].id}">edit</a>`
                            + `&nbsp; <a href="#${responseData.data[i].id}" onclick="doDelete(${responseData.data[i].id})" class="btn-delete">delete</a></li>`;
                    }
                    document.getElementById("content").innerHTML = contentToAdd;
                }
            }
        };
        xhr.open("GET", "https://1-dot-backup-server-002.appspot.com/api/v1/atubes", true);
        xhr.send();
    }
    function doDelete(id) {
        if(confirm("Are you sure wanna delete atube with id: " + id)){
            var xhr = new XMLHttpRequest();
            xhr.onreadystatechange = function () {
                if (xhr.readyState == 4 && xhr.status == 200) {
                    var responseData = JSON.parse(xhr.responseText);
                    alert("Delete success");
                    window.location.reload();
                }
            };
            xhr.open("DELETE", "https://1-dot-backup-server-002.appspot.com/api/v1/atubes?id=" + id, true);
            xhr.send();
        }
    }
</script>

</body>
</html>
