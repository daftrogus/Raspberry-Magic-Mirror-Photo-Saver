<?php
    $dir = 'captured';
    $file_display = array('jpg', 'jpeg', 'png', 'gif');

    if (file_exists($dir) == false) 
    {
        echo 'Directory "', $dir, '" not found!';
    } 
    else 
    {
        $dir_contents = scandir($dir);
        unset($dir_contents[0]);
        unset($dir_contents[1]);
        $dir_contents = array_values($dir_contents);
        $c_extension = explode(".", $dir_contents[0])[1];
        
        $names = [];
        foreach ($dir_contents as $file) {
            if (!in_array(explode("_", $file)[0])) {
                $names[explode("_", $file)[0]] = [];
            }
        }
        
        foreach ($dir_contents as $file) {
            array_push($names[explode("_", $file)[0]], $file);
        }
        
        foreach ($names as $name_key => $name) {
            $temp_photos = [];                   
            foreach($name as $image) {           
                array_push($temp_photos, strtok(explode("_", $image)[1], '.'));
            }
            rsort($temp_photos);                
            array_values($temp_photos);         
            foreach($temp_photos as $key => $image) {
                $temp_photos[$key] = $name_key."_".$image.".".$c_extension;
            }
            $names[$name_key] = $temp_photos;
        }
        
        rsort($names);
    }
?>

<!DOCTYPE html>
<html lang="" dir="ltr">
  <head>
    <meta charset="utf-8">
    <title></title>
  </head>
  <body>
    <?php 
        foreach($names as $key => $images) { ?>
            <div>
            </div>
            <div class="thumbnail">
                <?php foreach ($images as $image) { ?>
                    <h4 class="filename">
                        <span><?php echo basename(explode(".", $image)[0]);?></span>
                    </h4>
                    <img src='/captured/<?php echo $image; ?>'>   
                <?php } ?>
            </div>
        <?php }
    ?>
  </body>
</html>

