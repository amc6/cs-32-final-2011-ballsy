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

#content {
	width: 800px;
	margin: 0 auto;
	display: block;
	font: normal 12pt Verdana;
	line-height:1.5em;
}

#header {
	background-image: url('images/logo.png');
	width:550px;
	height:300px;
	background-repeat: no-repeat;
	display:block;
	margin:0 auto;

}

h1 {
	font-size: 15pt;
	border-bottom:2px solid black;
}

#downloads {
	padding-bottom: 0px;
	clear:left;
}

#mac {
	width: 50%;
	background-image: url('images/mac.png');
	text-align: center;
	padding-top: 140px;
	background-position: top center;
	background-repeat: no-repeat;
	float: left;
}

#win {
	width: 50%;
	background-image: url('images/win.png');
	text-align: center;
	padding-top: 140px;
	background-position: top center;
	background-repeat: no-repeat;
	float: left;
}

a {
	color: blue;
}

#copyright {
	text-align:center;
	margin-top:20px;
	font-size:9pt;
	color: #A0A0A0;
}

</style>


</head>

<body>
	<div id="content">
		<div id="header">			
		</div>
		
		<h1>Introducing... Ballsy!</h1>
		
		Ballsy is a two-dimensional physics puzzle game in which the user must navigate Ballsy, a colorful beach ball, along his quest in search of his best friend, the sand pail.  Sporting a grappling hook that allows him to swing from surface to surface, Ballsy's adventures through treacherous levels of deadly rectangles and irregular polygons make for quite the brain teaser. Enjoy the exciting, built-in levels or create your own with the fully featured level editor!
		
		<h1>Downloads</h1>
		
		<div id="downloads">
			<div id="mac">
			<a href="download.php?version=mac">DOWNLOAD [.ZIP]</a>
			</div>
			<div id="win">
			<a href="download.php?version=win32">DOWNLOAD [.ZIP]</a><br/>
			<small>If you run into problems, download <a href="download.php?version=win64">this alternate version</a>.</small>
			</div>
			

			After download, extract the contents of the .ZIP file to a folder of your choice. On Mac, double-click on the <b>Ballsy</b> application, and on Windows, double-click on <b>Ballsy.jar</b> to launch the game.
			
		</div>
		
		<h1>Compatibility</h1>
		Ballsy utilizes Java <b>1.6</b>, which is most likely already installed on your computer. However, if the application does not run or if your version of Java is out-of-date, the latest version of Java can be downloaded <a href="http://www.java.com" target="_blank">here</a>. Please send any bug reports to <a href="mailto:team@beballsy.com">team@beballsy.com</a>, making sure to indicate your platform, screen resolution, etc. Have fun!
		
		<h1>Release Notes</h1>
		<b>5/16/11: 1.0.0</b> (Current Version)<br/>
		In this first release, the background within the level has been disabled due to compatibility issues.  If you would like to see what the background looks like (and it would help us for testing), edit your <tt>res/config.txt</tt> file, changing the first line to <tt>draw_background: true</tt>.
		
		<h1>About Ballsy</h1>
		Ballsy was developed as a final project for a course in software engineering at Brown University by Adam Cook, Isaac Goldberg, Jessica Liu and Matt Nichols. We plan to continue development into the future.
		
		<div id="copyright">&copy; 2011 Adam Cook, Isaac Goldberg, Jessica Liu, Matt Nichols. All rights reserved. 
	</div>
</body>

</html>