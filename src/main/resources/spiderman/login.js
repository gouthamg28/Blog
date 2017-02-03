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
			url : 'http://localhost:8080/api/login/',
			data: '{"username": "' + $('#uname').val() + '","pwd":"' +$('#pwd').val() + '"}',
			contentType: "application/json;charset=utf-8",
			success : function(response) {
//				var data = jQuery.parseJSON(response)
//				if(response.data.isJSON())	{
//					alert( "Data Loaded: user found ");
//				}esle	{
//					alert( "Data Loaded: no user found ");
//				}
				alert( "Data Loaded: "+response);
//				window.location="home.html";
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
			success : function(response) {
				//alert( "Data Loaded: " + response );
				window.location="home.html";
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert(xhr.responseText);
//				alert(xhr.status+);
//				alert(thrownError);
//				window.location="home.html";
			}
		});
	});
	
	$("cancel").click(function () {
        location.href = "index.html";
    });
});