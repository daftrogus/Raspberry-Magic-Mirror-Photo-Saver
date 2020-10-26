import sys
import os
#perintah jalanin command di terminal
# com_cd1 = "cd ~/MagicMirror/modules/Photo_Saver-v1301164061/openCV_FR"
# os.system(com_cd1)

command = "python3 -u ~/MagicMirror/modules/Photo_Saver-v1301164061/openCV_FR/encode_faces.py --dataset ~/MagicMirror/modules/Photo_Saver-v1301164061/openCV_FR/dataset --encodings ~/MagicMirror/modules/Photo_Saver-v1301164061/openCV_FR/encodings.pickle --detection-method hog"
os.system(command)

sys.stdout.flush()
