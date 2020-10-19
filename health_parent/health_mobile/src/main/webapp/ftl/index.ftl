<html>
<head>
    <meta charset="utf-8">
    <title>Freemarker入门</title>
</head>
<body>
<table width="500px" align="center" border="1px">
    <tr>
        <th>商品编号</th>
        <th>商品名称</th>
        <th>商品价格</th>
    </tr>
<#list goodsList as goods>
    <tr>
        <td>${goods.id}</td>
        <td>${goods.name}</td>
        <td>${goods.price}</td>
    </tr>
</#list>
</table>
</body>
</html>