<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ include file="../layout/header.jsp"%>


<div class="container">
	<form action="/auth/loginProc" method="POST">
		<div class="form-group">
			<label for="username">UserName:</label> <input type="text" name="username" class="form-control" placeholder="Enter UserName" id="username">
		</div>

		<div class="form-group">
			<label for="password">Password:</label> <input type="password" name="password" class="form-control" placeholder="Enter password" id="password">
		</div>


		<button id="btn-login" class="btn btn-primary">로그인</button>
		<a href="https://kauth.kakao.com/oauth/authorize?client_id=51a8e33ebd36eacb05fe1cecf1974f61&redirect_uri=http://localhost:8000/auth/kakao/callback&response_type=code">
		<img height="38px"
			src="/image/kakao_login_button.png"
		/></a>

	</form>


</div>

<!--  <script src="/js/user.js"></script> -->
<%@ include file="../layout/footer.jsp"%>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>


