$(document).ready(function() {
	$('#login').click(function() {

		var username = $.trim($('#uname').val());
		var password = $.trim($('#pwd').val());
		if((username === '') || (password === ''))	{
			alert('Enter proper user name & password')
			return false;
		}
		
		$.ajax({
			type : 'post',
			url : 'http://localhost:8080/api/user/login',
			data: '{"username": "' + $('#uname').val() + '","pwd":"' +$('#pwd').val() + '"}',
			contentType: "application/json;charset=utf-8",
//			success : function(response) {
//				var data = jQuery.parseJSON(response)
//				if(response.data.isJSON())	{
//					alert( "Data Loaded: user found ");
//				}esle	{
//					alert( "Data Loaded: no user found ");
//				}
//				alert( "Data Loaded: "+response);
//			}
			success: function(output, status, xhr) {
				alert( "output: " +output);
//				alert( "status:" +status);
//				alert( "xhr.responseText: " +xhr.responseText);
				var a = JSON.parse(xhr.responseText);
//				alert("After parse:"+a.id)
				localStorage.setItem("ls-id", a.id);
				localStorage.setItem("ls-username", a.username);
				localStorage.setItem("ls-fullName", a.fullName);
				localStorage.setItem("ls-phno", a.phno);
				localStorage.setItem("ls-areaofinterest", a.areaofinterest);
				localStorage.setItem("ls-token", a.token);
//				alert("From local storage: "+localStorage.getItem("ls-id"))
				alert("From local storage: "+localStorage.getItem("ls-token"))
				window.location="home.html";
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert(xhr.responseText);
			}
		});
	});
	
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
				localStorage.setItem("ls-currentBlogId", "");
				window.location="index.html";
			},
			error : function(xhr, ajaxOptions, thrownError) {
				localStorage.setItem("ls-id", "");
				localStorage.setItem("ls-username", "");
				localStorage.setItem("ls-fullName", "");
				localStorage.setItem("ls-phno", "");
				localStorage.setItem("ls-areaofinterest", "");
				localStorage.setItem("ls-token", "");
				localStorage.setItem("ls-currentBlogId", "");
				window.location="index.html";
			}
		});
	});
	
	$('#register').click(function() {
		$.ajax({
			type : 'post',
			url : 'http://localhost:8080/api/user/registeration/',
			data: '{"fullName": "' + $('#fullname').val() +
				  '","pwd":"' +$('#pwd').val() + 
				  '","username":"' +$('#email').val() +
				  '","phno":"' +$('#phno').val() +
				  '","areaofinterest":"' +$('#areaofinterest').val()+
				  '"}',
			contentType: "application/json;charset=utf-8",

			success: function(output, status, xhr) {
//				alert( "output: " +output);
//				alert( "status:" +status);
//				alert( "xhr.responseText: " +xhr.responseText);
				var a = JSON.parse(xhr.responseText);
				alert("After parse:"+a.id)
				localStorage.setItem("ls-id", a.id);
				alert("From local storage: "+localStorage.getItem("ls-id"))
				window.location="home.html";
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert(xhr.responseText);
			}
		});
	});
	
	$('#update-profile').click(function() {
		$.ajax({
			type : 'post',
			url : 'http://localhost:8080/api/user/profileupdate/',
			data: '{"fullName": "' + $('#fullname').val() +
				  '","pwd":"' +$('#pwd').val() + 
				  '","username":"' +$('#email').val() +
				  '","phno":"' +$('#phno').val() +
				  '","areaofinterest":"' +$('#areaofinterest').val()+
				  '","id":"' +localStorage.getItem("ls-id")+
				  '","token":"' +localStorage.getItem("ls-token")+
				  '"}',
			contentType: "application/json;charset=utf-8",
			success : function(response) {
				//alert( "Data Loaded: " + response );
				window.location="home.html";
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert(xhr.responseText);
			}
		});
	});

	$('#save-blog').click(function() {
		$.ajax({
			type : 'post',
			url : 'http://localhost:8080/api/blog/',
			data: '{"blogTitle": "' + $('#blogtitle').val() +
				  '","blogContent":"' + $('#blogcontent').val() + 
				  '","blogAuthorUsername":"' + localStorage.getItem("ls-username") +
				  '","blogAreaOfInterest":"' +$('#areaofinterest').val()+
				  '"}',
			headers: {
				"id":localStorage.getItem("ls-id"),
				"token":localStorage.getItem("ls-token")
				},
			contentType: "application/json;charset=utf-8",

			success: function(output, status, xhr) {
//				alert( "output: " +output);
//				alert( "status:" +status);
//				alert( "xhr.responseText: " +xhr.responseText);
//				var a = JSON.parse(xhr.responseText);
//				alert("After parse:"+a.id)
//				localStorage.setItem("ls-id", a.id);
//				alert("From local storage: "+localStorage.getItem("ls-id"))
				window.location="home.html";
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert(xhr.responseText);
			}
		});
	});
	
	$("cancel").click(function () {
        location.href = "index.html";
    });
});