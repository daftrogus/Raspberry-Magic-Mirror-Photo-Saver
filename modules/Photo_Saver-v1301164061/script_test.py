import sys
import os
#perintah jalanin command di terminal
# com_cd1 = "cd ~/MagicMirror/modules/Photo_Saver-v1301164061/openCV_FR"
# os.chdir("")
# os.system("cd ~/MagicMirror/modules/Photo_Saver-v1301164061/openCV_FR")

#command = "python3 pi_face_recognition.py --cascade haarcascade_frontalface_default.xml --encodings encodings.pickle"
os.system("python3 -u ~/MagicMirror/modules/Photo_Saver-v1301164061/openCV_FR/pi_face_recognition.py --cascade ~/MagicMirror/modules/Photo_Saver-v1301164061/openCV_FR/haarcascade_frontalface_default.xml --encodings ~/MagicMirror/modules/Photo_Saver-v1301164061/openCV_FR/encodings.pickle")

# sys.stdout.flush()
