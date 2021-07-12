/** UserJoin 실행시 할것들
 */
//jQuery문으로 $(id).on("실행내용","실행내용시 실행함수")
let index = {
	//여기의 this값과
	//function내부의 this값은 다르다.
	//굳이 function을 쓰고 싶다면
	//let _this = this;로 정의 해줘야한다.
	init: function() {
		$("#btn-save-user").on("click", () => { //왜 function()을 안쓰고 ()=>{}를 쓰는가? this로 바인딩하기 위해서 일부러 쓰는거임
			this.saveUser();
		});
		$("#btn-update").on("click", () => { //왜 function()을 안쓰고 ()=>{}를 쓰는가? this로 바인딩하기 위해서 일부러 쓰는거임
			this.update();
		});
		$("#email-check").blur("click", () => { //왜 function()을 안쓰고 ()=>{}를 쓰는가? this로 바인딩하기 위해서 일부러 쓰는거임
			this.emailCheck();
		});


		//$("#btn-login").on("click", () => { //왜 function()을 안쓰고 ()=>{}를 쓰는가? this로 바인딩하기 위해서 일부러 쓰는거임
		//	this.login();
		//});
	},
	
	emailCheck: function() {
		//alert('user의 save함수 호출됨');
		let data = {
			email: $("#email-check").val(),
		};
		console.log(data.email);

		//console.log(data);

		// ajax호출시 default가 비동기 호출
		// ajax 통신을 이용해서 3개의 데이터를 json으로 변경하여 insert 요청!!
		// ajax가 통신을 성공하고 서버가 json을 리턴해주면 자동으로 자바 오브젝트로 변환해주네요.
		

	},
	
	
	
	
	
	
	
	
	

	saveUser: function() {
		//alert('user의 save함수 호출됨');
		let data = {
			username: $("#username").val(),
			password: $("#password").val(),
			email: $("#email").val()
		};

		//console.log(data);

		// ajax호출시 default가 비동기 호출
		// ajax 통신을 이용해서 3개의 데이터를 json으로 변경하여 insert 요청!!
		// ajax가 통신을 성공하고 서버가 json을 리턴해주면 자동으로 자바 오브젝트로 변환해주네요.
		$.ajax({
			type: "POST",
			url: "/auth/joinProc",
			data: JSON.stringify(data), // http body데이터
			contentType: "application/json; charset=utf-8",// body데이터가 어떤 타입인지(MIME)
			dataType: "json" // 요청을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열 (생긴게 json이라면) => javascript오브젝트로 변경
		}).done(function(resp) {
			if(resp.status === 500) {
				alert("회원가입에 실패하였습니다.");
			}  else {
				alert("회원가입이 완료되었습니다.");
				location.href = "/";
			}

		}).fail(function(error) {
			alert(JSON.stringify(error));
		});

	},


	update: function() {
		//alert('user의 save함수 호출됨');
		let data = {
			id: $("#id").val(),
			email: $("#email").val(),
			password: $("#password").val(),
			username: $("#username").val(),
		}

		$.ajax({
			type: "PUT",
			url: "/user",
			data: JSON.stringify(data),
			contentType: "application/json; charset=utf-8",
			dataType: "json"
		}).done(function(resp) {
			if (resp.status === 500) {
				alert("에러 발생");
				location.href = "/";
			} else {
				alert("회원수정이 완료되었습니다.");
				location.href = "/";
			}
			//location.href = "/";
		})
			.fail(function(error) {
				alert(JSON.stringify(error));
			}); //ajax통신을 이용해서 3개의 parameter를 json으로 변경해서
		//insert 요청을 할거임
	}
	/*
		login: function() {
		//alert('user의 save함수 호출됨');
			et data = {
			username: $("#username").val(),
			password: $("#password").val()
		}
		//		console.log(data);
				//ajax호출시 default가 비동기 호출이다==여러 코드가 있어도 알아서 비동기 처리해줌
			.ajax({
			//회원가입시 수행요청	
			type: "POST",
			url: "/api/user/login", //com.cos.blog.controller.api.UserApiController에 PostMapping 주소로 날아감
			data: JSON.stringify(data), //그냥 data를 써주면 JS형식이라 이해 못함
			contentType: "application/json; charset=utf-8", //body 데이터가 어떤 타입인지
			dataType: "json" //json으로 적으면 요청을 서버로 하면 서버에서 응답하는 결과를(기본적으로 모두 String but 생긴게 JSON이라면) JS오브젝트로 변경해줌)
			).done(function(resp) {
			alert("로그인이 완료되었습니다.");
								console.log(resp);
						location.href="/";
			)
				fail(function(error) {
				alert(JSON.stringify(error));
			}); //ajax통신을 이용해서 3개의 parameter를 json으로 변경해서
		//insert 요청을 할거임
	}*/
}

index.init();