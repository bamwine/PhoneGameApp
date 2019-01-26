<?php
//error_reporting(0);
include('connect.php');
	$page=$_GET["action"];
switch($page){

	
	 case'login':
	 login();
	 break;
	  
	 
	 case'register':
	 register();
	  break;
	  
	  case'gallery':
	 gallery();
	  break; 
	  
	  
	  case'voting':
	 voting();
	  break;
	  
	  case'votingcount':
	 votingcount();
	  break;
	  
	 
	  
	 
default:

	
	}
?>




<?php

	

$target_dir = "uploads/";


// Check if image file is a actual image or fake image
if (isset($_FILES["passport"])) 
{

$response = array();
$originalImgName= $_FILES['passport']['name'];

$uname=$_POST['uname'];
$title=$_POST['title'];
$category=$_POST['category'];
$descption=$_POST['descption'];
$location=$_POST['location'];
$datetaken=$_POST['datetaken'];

$upvotes=0;
$downvotes=0;
$datesup= date("Y-m-d H:i:s");
	
	$target_file_name = $target_dir .basename($originalImgName);




 if (move_uploaded_file($_FILES["passport"]["tmp_name"], $target_file_name)) 
 {	
 
 

$query = mysql_query("INSERT INTO filesup (uname, title, category, descption, location, datetaken, upvotes, downvotes, datesup,filename)
VALUES ( '$uname', '$title', '$category', '$descption', '$location', '$datetaken', '$upvotes', '$downvote', '$datesup','$target_file_name')");

 $result = mysql_query($query);
		$response['message'] = 'File uploaded successfully';
        $response['error'] = false;
 }  else{
        $response['error'] = true;
        $response['message'] = 'Could not move the file too big';
    }
 
 
 
} else {

    $response['error'] = true;
    $response['message'] = 'Not received any file';
}
echo json_encode($response);



?>

		



<?php
function register(){	
?>


<?php

$response = array();

if (isset($_POST['email']) && isset($_POST['Username'])) 
{  
    
    $Username = $_POST['Username'];
    $email = $_POST['email'];
	$date=   date("Y-m-d H:i:s");
	
	include('connect.php');
	if(!userExists($Username)){
    $result = mysql_query("Insert into users(uname,email,dates) values('$Username','$email',now())");

    if ($result)
	{
        $response["success"] = 1;
        $response["message"] = "Registration successfully.";
        echo json_encode($response);
    }
	else
	{
      $response["success"] = 0;
      $response["message"] = "Oops! An error occurred.";
      echo json_encode($response);  
    }
	
	} else{
		$response["success"] = 0;
		$response["message"] = "User exists";
	}
 }
 
 else 
 {
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
    echo json_encode($response);
 } 
?>

<?php
}	
?>





		

<?php
function gallery(){	
?>


<?php


$response = array();


$result = mysql_query("SELECT * FROM filesup ORDER BY `id` ASC") or die(mysql_error());

if (mysql_num_rows($result) > 0) {
  
    $response["items"] = array();

    while ($row = mysql_fetch_array($result)) {
            // temp user array
            $item = array();
           
				$item["id"]		    = $row["id"];
				$item["uname"] 	= $row["uname"];
				$item["title"]	= $row["title"];
				
				$item["category"]	= $row["category"];
				$item["descption"] 	= $row["descption"];
				$item["location"]	= $row["location"];
				
				$item["filename"]	= $row["filename"];
				$item["datetaken"] 	= $row["datetaken"];
				$item["upvotes"]	= $row["upvotes"];
				$item["downvotes"]	= $row["downvotes"];
						
				
        
            // push ordered items into response array 
            array_push($response["items"], $item);
           }
      // success
     $response["success"] = 1;
}
else {
    // order is empty 
      $response["success"] = 0;
      $response["message"] = "No Content Yet";
}
// echoing JSON response
echo json_encode($response);

}	
?>


<?php
function userExists($username){
	$result = mysql_query("Select * from users where uname='$username'");
	
    if (mysql_num_rows($result)> 0)
	{
	return true;
	}
 
	return false;
}

?>




<?php
function login(){	
?>


<?php


$response = array();
$username =isset($_GET['Username']) ? $_GET['Username'] : '';
// $username = $_POST['Username'];
$result = mysql_query("SELECT * FROM users where uname='$username'") or die(mysql_error());

if (mysql_num_rows($result) > 0) {
  
    $response["items"] = array();

    while ($row = mysql_fetch_array($result)) {
            // temp user array
            $item = array();
           
				$item["id"]		    = $row["id"];
				$item["uname"] 	= $row["uname"];
				$item["email"]	= $row["email"];
            // push ordered items into response array 
            array_push($response["items"], $item);
           }
      // success
     $response["success"] = 1;
}
else {
    // order is empty 
      $response["success"] = 0;
      $response["message"] = "No Content Yet";
}
// echoing JSON response
echo json_encode($response);

}	
?>





<?php
function voting(){	
?>


<?php


$response = array();
$username =isset($_GET['username']) ? $_GET['username'] : '';
$article =isset($_GET['article']) ? $_GET['article'] : '';
$vote =isset($_GET['vote']) ? $_GET['vote'] : '';
// $username = $_POST['Username'];
$result = mysql_query("SELECT * FROM voting where usern='$username' and article ='$article'") or die(mysql_error());

if (mysql_num_rows($result) > 0) {
  
    while ($row = mysql_fetch_array($result)) {
				$id	= $row["id"];
           }
     mysql_query("Update voting set vote ='$vote' where id ='$id'") or die(mysql_error());
     
	 
}
else {
    $result = mysql_query("Insert into voting(usern, article, vote) values('$username','$article','$vote')");
     
}
// echoing JSON response
echo json_encode($response);

}	
?>





<?php
function votingcount(){	
?>


<?php


$response = array();
$articleid =isset($_GET['articleid']) ? $_GET['articleid'] : '';
$value =isset($_GET['value']) ? $_GET['value'] : '';
// $username = $_POST['Username'];
$result = mysql_query("SELECT COUNT(*)as count FROM voting WHERE vote='$value' and article='$articleid' GROUP BY vote") or die(mysql_error());
 $response["items"] = array();
if (mysql_num_rows($result) > 0) {
  
    $response["items"] = array();

    while ($row = mysql_fetch_array($result)) {
            // temp user array
            $item = array();				
				$item["count"] 	= $row["count"];
				
            // push ordered items into response array 
            array_push($response["items"], $item);
           }
} else{

$item = 0;
 array_push($response["items"], $item);
}
// echoing JSON response
echo json_encode($response);

}	
?>

