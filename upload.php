<?php 

ini_set('display_errors', 'On');

$db_user = "********";
$db_password = "********";
$db_database = "********";
$db_server = "localhost";
 
$con = new mysqli($db_server, $db_user, $db_password, $db_database);

if (!$con) {
  die('Could not connect: ' . mysql_error());
  echo "Code: D001";
} else {
    $modus = $_POST['modus'];
    $a1 = $_POST['answerOne'];
    $a2 = $_POST['answerTwo'];
    $a3 = $_POST['answerThree'];
    $a4 = $_POST['answerFour'];
    $a5 = $_POST['answerFive'];
    $comment = $_POST['comment'];


    $result = mysqli_query($con,"
        INSERT INTO survey (monetization, answer_one, answer_two, answer_three, answer_four, answer_five, comment) 
        VALUES ('$modus', '$a1', '$a2', '$a3', '$a4', '$a5', '$comment')");

    if($result){
        echo "successfully";
    }
}

mysqli_close($con);

function jsonResponse($success, $message){
    $response["success"] = $success;
    $response["message"] = $message;
    echo json_encode($response);
}
