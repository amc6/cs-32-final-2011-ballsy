<?php

$versionNum = "1.0.0";

$version = $_REQUEST['version'];

if ($version == "mac" || $version == "win32" || $version == "win64"){
	$file = fopen($version."_count.txt", "r");
	$count = fread($file, 1024);
	fclose($file);
	$count = $count + 1;
	$file = fopen($version."_count.txt", "w");
	fwrite($file, $count);
	fclose($file);
	header('Location: http://www.beballsy.com/downloads/ballsy_'.$version.'_'.$versionNum.'.zip' ) ;
}

echo "Invalid request.";

?>