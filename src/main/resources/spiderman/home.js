$(document).ready(function() {

	$.ajax({
		type : 'post',
		url : 'http://localhost:8080/api/blog/recent',
//		headers: {
//			"id":localstorage.getitem("ls-id"),
//			"token":localstorage.getitem("ls-token")
//		},
		contentType: "application/json;charset=utf-8",
		success: function(output, status, xhr) {
			console.log("success in the response : "+ output);
			var a = JSON.parse(xhr.responseText);
//			alert("After parse:"+a.id)
			localStorage.setItem("ls-currentBlogId", a.blog_id);

			//alert( "output: " +output);
			fill_blog(output);
		},
		error : function(xhr, ajaxoptions, thrownerror) {
			console.log("error in the respomse : "+ xhr.responsetext);
			alert(xhr.responsetext);
		}
	});

	function fill_blog(resp) {
		var rec_blog = null;
		try{
			var rec_blog = JSON.parse(resp);
//			alert(rec_blog);
			console.log("rec blog: " + rec_blog);
		}catch(e) {
			rec_blog = { blogtitle:'no recent blogs',
					blogContent: 'there is no latest blog in your favourite list'};		
		}
		//addd header
		$("#blog-title").text(rec_blog.blogTitle);
		$("#blog-author").text(rec_blog.blogAuthorUsername);
//		var timestamp = parseint(rec_blog.blogtimestamp);
//		var timestamp = parseInt(rec_blog.blogCreatedTimeStamp);
//		var timestamp = new Date(rec_blog.blogCreatedTimeStamp);
//		console.log(timestamp);
//		var d = new Date();
//		d.setTime(timestamp);
		
//		$("#blog-posted").text("posted on " +  timestamp.toString());
		//$("#blog-posted").prepend("<span class=\"glyphicon glyphicon-time\"></span>");
		var bodydata = rec_blog.blogContent.split("\n");
		if(bodydata.length > 0 ) {
			$("#section-blog-content").empty();
			console.log("length : "+ bodydata.length);
			bodydata.forEach(function(item, index){
			//add the para div
				console.log("[para] : " + item);
				$("#section-blog-content").append("<p " + ((index == 0)?" class=\"lead\" ":"") + ">" + item + "</p>");
			});
		}	
	}
	
	
	
		$.ajax({
			type : 'post',
			url : 'http://localhost:8080/api/blog/favorites/'+localStorage.getItem("ls-id"),
			headers: {
				"id":localStorage.getItem("ls-id"),
				"token":localStorage.getItem("ls-token")
			},
			contentType: "application/json;charset=utf-8",
			success: function(output, status, xhr) {
				alert( "Fetching Fav blogs output: " +output);
//				alert( "Fetching Fav blogs status:" +status);
//				alert( "Fetching Fav blogs xhr.responseText: " +xhr.responseText);
//				var a = JSON.parse(xhr.responseText);
				
//				alert("Fetching Fav blogs After parse: "+a)
				
//				$.each( xhr.responseText, function( key, value ) {
//					  alert( key + " %%%%% " + value );
//					  var b = JSON.parse(value);
//					  $.each( b, function( key2, value2) {
//						  alert( key + "2: " + value );
//					  });
//					});
				
			},
			error : function(xhr, ajaxOptions, thrownError) {
				alert(xhr.responseText);
			}
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