<?php
  if (isset($_GET['nama_user'])) {
    $scanned_dir = scandir('./dataset/'.$_GET['nama_user']);
  } else {
    $scanned_dir = scandir('./dataset');
  }
  unset($scanned_dir[0]);
  unset($scanned_dir[1]);
  $scanned_dir = array_values($scanned_dir);
  echo json_encode($scanned_dir);
?>
