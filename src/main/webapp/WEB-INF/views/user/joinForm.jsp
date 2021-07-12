<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../layout/header.jsp"%>


<div class="container">

	<!-- 옛날 방식 -->
	<!-- 	<form action="/user/join" method="POST"> -->
	<form>
		<div class="form-group">
			<label for="username">UserName:</label> <input type="text" class="form-control" placeholder="Enter UserName" id="username">
		</div>
		<div class="check_font" id="id_check"></div>

		<div class="form-group">
			<label for="email">Email:</label> <input type="email" class="form-control" placeholder="Enter email" id="email">
		</div>

		<div class="form-group">
			<label for="password">Password:</label> <input type="password" class="form-control" placeholder="Enter password" id="password">
		</div>
	</form>

	<!-- 버튼을 form안에 넣으면 submit이 되버리므로 form밖으로 뺀다. -->
	<button id="btn-save-user" class="btn btn-primary">회원가입완료</button>
</div>

<script src="/js/user.js"></script>
<%@ include file="../layout/footer.jsp"%>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>


