<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<script type="text/javascript">
	function show_cart(){
		$.ajax({
			url : "miniCart.do",
			type : "POST",
			success : function(data) {
				$("#cart_list").html(data);
			}
		});
		// 让购物车列表显示
		$("#cart_list").show();
	}
	
	function hide_cart(){
		// 让购物车列表隐藏
		$("#cart_list").hide();
	}
</script>
</head>
<body>
	<div class="card">
		<a href="goto_cart_list.do" onmouseover="show_cart()" onmouseout="hide_cart()">购物车<div class="num">0</div></a>
		<!--购物车商品-->
		<div id="cart_list" class="cart_pro">
			<jsp:include page="miniCartList.jsp"></jsp:include>
		</div>
	</div>
</body>
</html>