<?php
// $servername = "localhost";
// // $username = "daftrogu_proyekTA";
// // $password = "8gv3BV5D3bY9C3i";
// // ^^ buat di server
// $username = "root";
// $password = "";
//
// // Create connection
// $conn = new mysqli($servername, $username, $password);
//
// // Check connection
// if ($conn->connect_error) {
//   die("Connection failed: " . $conn->connect_error);
// }


$files = $_FILES["thisisfile"];
echo var_dump($_POST['thisisfile']);
echo var_dump($_FILES);
$file_path = $files['tmp_name'];
$namefile = $files['name'];
$namauser = strtok($namefile, '_');

if (!is_dir($namauser)) {
  mkdir("./dataset/".$namauser);
}
move_uploaded_file($file_path, './dataset/'.$namauser.'/'.basename($files['name']));
// $file_path = $files['tmp_name'];
// move_uploaded_file($file_path, './uploads/'.basename($files['name']));



//
// echo "files uploaded successfully";

// $target_dir = "uploads/";
// $target_file = $target_dir . basename($_FILES["fileToUpload"]["name"]);
// $uploadOk = 1;
// $imageFileType = strtolower(pathinfo($target_file,PATHINFO_EXTENSION));
// // Check if image file is a actual image or fake image
// $check = getimagesize($_FILES["fileToUpload"]["tmp_name"]);
// try {
//
// }
// if($check !== false) {
//   echo "File is an image - " . $check["mime"] . ".";
//   $uploadOk = 1;
// } else {
//   echo "File is not an image.";
//   $uploadOk = 0;
// }
?>


<!-- <!DOCTYPE html>
<html>
<head>
    <title>Upload File</title>
</head>

<body>
    <form action="index.php" method="post" enctype="multipart/form-data">
        Pilih file: <input type="file" name="thisisfile" />
        <input type="submit" name="upload" value="upload" />
    </form>
</body>
</html> -->
