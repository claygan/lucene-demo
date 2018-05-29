<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>

<head>
    <title>File Search Index</title>
    <script src="/js/jquery-3.1.1.min.js"></script>
</head>
<body>
    <div><input type="text" id="filePath" placeholder="${path}"><button onclick="javascript:indexcreate();">索引创建</button></div>
    <div style="align-content: center">
        <h1>我的文件搜索</h1>
        <input name="keywords" type="text"><button onclick="javascript:search();">搜索</button>
    </div>
    <div style="align-content: center" id="resultList"></div>
<script>
    $(function(){

    })
    function search() {
        var keywords = $("input[name='keywords']").val();
        $.ajax({
            cache: false,
            type: "POST",
            url: "${ctx}/file/search",
            dataType: "json",
            data: {"keywords":keywords},
            async: false,
            success: function (result) {
                var data = result.data;
                $("#resultList").append("<p>共检索到"+data.total+"条记录！</p>");

                for(var i=0;i<data.length;i++){
                    $("#resultList").append("<div>");
                    $("#resultList").append("<h1>"+data.name+"</h1>");
                    $("#resultList").append("<p>"+data.intro+"</p>");
                    $("#resultList").append("<h2>"+data.createTime+"</h2>");
                    $("#resultList").append("<a href='"+data.downUrl+"'>下载文档</a>");

                    $("#resultList").append("</div>");
                }
            }
        });
    }
    function indexcreate(){
        var path = $("#filePath").val();
        $.ajax({
            cache: false,
            type: "POST",
            url: "${ctx}/file/store",
            dataType: "json",
            data: {"path":path},
            async: false,
            success: function (data) {
                if(data.code == 200) {
                    alert("索引创建成功！");
                }
            }
        });
    }
</script>
</body>
</html>
