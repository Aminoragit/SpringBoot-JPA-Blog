let index = {
	init: function() {
		$("#btn-save").on("click", () => { //왜 function()을 안쓰고 ()=>{}를 쓰는가? this로 바인딩하기 위해서 일부러 쓰는거임
			this.save();
		});
		$("#btn-delete").on("click", () => { //왜 function()을 안쓰고 ()=>{}를 쓰는가? this로 바인딩하기 위해서 일부러 쓰는거임
			this.deleteById();
		});
		$("#btn-update").on("click", () => { //왜 function()을 안쓰고 ()=>{}를 쓰는가? this로 바인딩하기 위해서 일부러 쓰는거임
			//if문을 추가 할수는 없나? 현재 접속한 사람의 id와 작성자의 id를 비교해서 맞으면 this.update가 수행되고 아니면 경고창만 띄우고 싶음
			this.update();
		});
		$("#btn-reply-save").on("click", () => { //왜 function()을 안쓰고 ()=>{}를 쓰는가? this로 바인딩하기 위해서 일부러 쓰는거임
			//if문을 추가 할수는 없나? 현재 접속한 사람의 id와 작성자의 id를 비교해서 맞으면 this.update가 수행되고 아니면 경고창만 띄우고 싶음
			this.replySave();
		});

	},

	save: function() {
		let data = {
			title: $("#title").val(),
			content: $("#content").val()
		}
		$.ajax({
			type: "POST",
			url: "/api/board",
			data: JSON.stringify(data),
			contentType: "application/json; charset=utf-8",
			dataType: "json"
		}).done(function(resp) {
			alert("게시글 등록 완료");
			location.href = "/";
		})
			.fail(function(error) {
				alert(JSON.stringify(error));
			});
	},


	deleteById: function() {
		let id = $("#id").text();
		$.ajax({
			type: "DELETE",
			url: "/api/board/" + id,
			dataType: "json"
		}).done(function(resp) {
			alert("삭제 완료");
			location.href = "/";
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	},



	update: function() {
		let id = $("#id").val();

		let data = {
			title: $("#title").val(),
			content: $("#content").val()
		}
		$.ajax({
			type: "PUT",
			url: "/api/board/" + id,
			data: JSON.stringify(data),
			contentType: "application/json; charset=utf-8",
			dataType: "json"
		}).done(function(resp) {
			if (resp.status === 500) {
				alert("글 수정 실패 : 작성자 본인이 아닙니다");
				location.href = "/api/board/" + id;;
			} else {
				alert("게시글 수정완료");
				location.href ="./";
			}
		})
			.fail(function(error) {
				alert(JSON.stringify(error));
			});
	},

	replySave: function() {
		let data = {
			userId:$("#userId").val(),
			boardId:$("#boardId").val(),
			content: $("#reply-content").val()
		}
		//ajax 데이터 호출이후 정보가 다 지워지므로 이렇게 적어줘야 location.href할때 적용가능함
		//url의 boardId와
		//let boardId= $("#boardId").val();
		
		$.ajax({
			type: "POST",
			url: `/api/board/${data.boardId}/reply`,
			data: JSON.stringify(data),
			contentType: "application/json; charset=utf-8",
			dataType: "json"
		}).done(function(resp) {
			alert("댓글 작성 완료");
			location.href = `/board/${data.boardId}`;
		})
			.fail(function(error) {
				alert(JSON.stringify(error));
			});
	},
}

index.init();