<?php
$success = $_GET['success'];
?>

<html>
	<head>
		<title>Be Ballsy!</title>

		<style>
		
		body {
			background-image: url('images/bg.jpg');
			background-position: top center;
			background-color: #94F1FF;
		}

		#header {
			background-image: url('images/logo.png');
			width:550px;
			height:300px;
			background-repeat: no-repeat;
			display:block;
			margin:0 auto;
			overflow:hidden;
			position:relative;
			top:25%;

		}

		#teaser {

			padding:162px 0 0 140px;
			color: #315acb;
		}
		
		#teaser h1 {
			font: bold 17pt Verdana;
			margin: 0;
			padding: 0;
			
		}
		
		#teaser h2 {
			font: normal 11pt Verdana;
			margin: 0;
			padding-top 4px;
		}
		
		#teaser a {
			color: #315acb;
		}
		
		#email {
			font: bold 11pt Verdana;
			margin:30px 0 0 80px;
		}
		
		#email input {
			border-color: #3159cb;
			color:#3159cb;
			padding:5px;
			width:200px;

		}
		
		#email input.submit {
			margin-left:10px;
			width: 70px;
			background-color:#f5b800;
			border-color:#fded8e;
		}


		</style>


	</head>

	<body>
		<div id="header">
			<div id="teaser">
				<h1>Coming soon.</h1>
				<h2>Want updates? Leave your email.</h2>
			
				<div id="email">
				<?php if(!$success) { ?>
					<form name="updates" method="post" action="emails.php">
							<input type="text" name="email">
							<input type="submit" value="I'm in." class="submit">
					</form>
				<?php
				}else if ($success == "true") {?>
				
				Thanks for your interest!
				
				<?php
				}else if ($success == "false") {?>
				
				Bad email. <a href="index.php">Please try again.</a>
				
				<?php } ?>
				
				</div>
			</div>
		</div>
	</body>

</html>