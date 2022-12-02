<?php
include 'dbconfig.php';

$Username = $_POST['username'];
$Password  = $_POST['password'];
$id = $_POST['id'];

// Create connection
$conn = mysqli_connect($host, $user, $pw, $db);
// Check connection
if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

/* grab the posts from the db */
$query = "INSERT INTO user (username, pass) values('".$Username."','".$Password ."')";

$response = mysqli_query($conn, $query) or die('Error query:  '.$query);
$lid = mysqli_insert_id($conn);
$result["errormsg"]="User Berhasil";
$result["lid"]="$lid";
if ($response == 1){
	echo json_encode($result);
}
else{
	$result["errormsg"]="Fail";
	echo json_encode($result);
}
?>
