$(document).ready(function() {

		$.ajax({
			type : 'post',
			url : 'http://localhost:8080/api/blog/'+'589b0561e58b9e15f0dc92c0',
			// data: '{"username": "' + $('#uname').val() + '","pwd":"' +$('#pwd').val() + '"}',
			headers: {
				"id":localStorage.getItem("ls-id"),
				"token":localStorage.getItem("ls-token")
			},
			contentType: "application/json;charset=utf-8",
			// success : function(response) {
			// 	var data = jQuery.parseJSON(response)
			// 	if(response.data.isJSON())	{
			// 		alert( "Data Loaded: user found ");
			// 	}esle	{
			// 		alert( "Data Loaded: no user found ");
			// 	}
			// 	alert( "Data Loaded: "+response);
			// }
			success: function(output, status, xhr) {
				alert( "output: " +output);
//				alert( "status:" +status);
//				alert( "xhr.responseText: " +xhr.responseText);
				var a = JSON.parse(xhr.responseText);
//				alert("After parse: blog_id: "+a.blog_id)
				alert("After parse: blogTitle: "+a.blogTitle)
				alert("After parse: blogContent: "+a.blogContent)
//				alert("After parse: blogAuthorUsername: "+a.blogAuthorUsername)
//				alert("After parse: blogAreaOfInterest: "+a.blogAreaOfInterest)
//				alert("After parse: blogTimestamp: "+a.blogTimestamp)
				// window.location="home.html";
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert(xhr.responseText);
			}
		});
	
	
// 	$('#save-blog').click(function() {
// 		$.ajax({
// 			type : 'post',
// 			url : 'http://localhost:8080/api/blog/',
// 			data: '{"blogTitle": "' + $('#blogtitle').val() +
// 				  '","blogContent":"' + $('#blogcontent').val() +
// 				  '","blogAuthorUsername":"' + localStorage.getItem("ls-username") +
// 				  '","blogAreaOfInterest":"' +$('#areaofinterest').val()+
// 				  '"}',
// 			headers: {
// 				"id":localStorage.getItem("ls-id"),
// 				"token":localStorage.getItem("ls-token")
// 				},
// 			contentType: "application/json;charset=utf-8",
//
// 			success: function(output, status, xhr) {
// 				alert( "output: " +output);
// 				alert( "status:" +status);
// 				alert( "xhr.responseText: " +xhr.responseText);
// //				var a = JSON.parse(xhr.responseText);
// //				alert("After parse:"+a.id)
// //				localStorage.setItem("ls-id", a.id);
// //				alert("From local storage: "+localStorage.getItem("ls-id"))
// 				window.location="home.html";
// 			},
// 			error : function(xhr, ajaxOptions, thrownError) {
// 				alert(xhr.responseText);
// 			}
// 		});
// 	});
	
		$('#logout').click(function() {

			$.ajax({
				type : 'post',
				url : 'http://localhost:8080/api/user/logout',
				data: '{"id": "' + localStorage.getItem("ls-id") + '","token":"' + localStorage.getItem("ls-token") + '"}',
				contentType: "application/json;charset=utf-8",
				success: function(output, status, xhr) {
					var a = JSON.parse(xhr.responseText);
					localStorage.setItem("ls-id", "");
					localStorage.setItem("ls-username", "");
					localStorage.setItem("ls-fullName", "");
					localStorage.setItem("ls-phno", "");
					localStorage.setItem("ls-areaofinterest", "");
					localStorage.setItem("ls-token", "");
					window.location="index.html";
				},
				error : function(xhr, ajaxOptions, thrownError) {
					localStorage.setItem("ls-id", "");
					localStorage.setItem("ls-username", "");
					localStorage.setItem("ls-fullName", "");
					localStorage.setItem("ls-phno", "");
					localStorage.setItem("ls-areaofinterest", "");
					localStorage.setItem("ls-token", "");
					window.location="index.html";
				}
			});
		});

		
	$("cancel").click(function () {
        location.href = "index.html";
    });
	
});