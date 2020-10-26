<?php
$files = $_FILES["uploaded_file"];
echo var_dump($_POST['uploaded_file']);
echo var_dump($_FILES);
$file_path = $files['tmp_name'];
$namefile = $files['name'];

if (!is_dir($namafile)) {
  mkdir("./captured/".$namafile);
}
move_uploaded_file($file_path, './captured/'.$namauser.'/'.basename($files['name']));
?>
