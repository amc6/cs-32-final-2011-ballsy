<?php

$email = $_POST['email'];

if(eregi("^[_a-z0-9-]+(\.[_a-z0-9-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)*(\.[a-z]{2,3})$", $email)){

	$toWrite = $email . "\n";
	$file = fopen("secret_email_collection.txt", "a");
	fwrite($file, $toWrite);
	fclose($file);
	
	$host  = $_SERVER['HTTP_HOST'];
	$uri  = rtrim(dirname($_SERVER['PHP_SELF']), '/\\');
	$extra = 'index.php?success=true';
	header("Location: http://$host$uri/$extra");
	exit;
	
}else{
	$host  = $_SERVER['HTTP_HOST'];
	$uri  = rtrim(dirname($_SERVER['PHP_SELF']), '/\\');
	$extra = 'index.php?success=false';
	header("Location: http://$host$uri/$extra");
	exit;
}
?>