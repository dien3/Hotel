<?php
include 'dbconfig.php';
$conn = mysqli_connect($host, $user, $pw, $db);

if($_SERVER['REQUEST_METHOD']=='POST'){

 //Getting values
 $username = $_POST['NamaUser'];
 $password = $_POST['Pass'];

 //Creating sql query
 $sql = "SELECT * FROM user WHERE username='$username' AND pass ='$password'";

 echo $sql;
 //executing query
 $result = mysqli_query($conn,$sql);

 //fetching result
 $check = mysqli_fetch_array($result);

 //if we got some result
 if(isset($check)){
 //displaying success
 echo "success";
 }else{
 //displaying failure
 echo "failure";
 }
 mysqli_close($conn);
}
?>
