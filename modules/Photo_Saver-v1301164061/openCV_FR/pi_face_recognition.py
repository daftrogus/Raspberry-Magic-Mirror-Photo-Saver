#import the necessary packages
from datetime import date
from imutils.video import VideoStream
from imutils.video import FPS
import os
import sys
import face_recognition
import argparse
import imutils
import pickle
import time
import cv2
import threading
import ini_script_upload as isup
import requests
import json
import subprocess

#construct the argument parser and parse the arguments
ap = argparse.ArgumentParser()
ap.add_argument("-c", "--cascade", required=True, help = "path to where the face cascade resides")
ap.add_argument("-e", "--encodings", required=True, help = "path to serialized db of facial encodings")
args = vars(ap.parse_args())

path = "/home/daftrogus/MagicMirror/modules/Photo_Saver-v1301164061/openCV_FR/dataset"

#initialize the video stream and allow the camera sensor to warm up
print("[INFO] starting video stream...")
vs = VideoStream(src=2, usePiCamera=False).start()	# ganti src kalo pake webcam
# vs = VideoStream(usePiCamera=False).start()
time.sleep(2.0)

def new_exist():
	url = 'https://daftrogus.com/check_new.php'
	get_response = requests.get(url)
	json_data = json.loads(get_response.content)
	data_exist = False
	for i in range(0, len(json_data)):
		uri_per_name = url+'?nama_user='+json_data[i]
		resp = requests.get(uri_per_name)
		json_array = json.loads(resp.content)
		if (len(json_array) != 0):
			for j in range(0, len(json_array)):
				print("[INFO] Downloading File:", json_array[j])
				data_exist = True
				# /**
				#   * MANDATORY PARAMS
				#   * $_POST['dir'] = Directory of file
				#   * $_POST['nama_user'] = Expected Filename to download
				#   */
				# Download the image
				url = 'https://daftrogus.com/download_file.php'
				mydict = {'dir': './dataset/'+json_data[i]+'/'+json_array[j], 'nama_user': json_array[j]}
				r = requests.post(url, mydict)
				if r.status_code == 200:
					if not os.path.exists(path+"/"+str(json_data[i].split("_")[0])):
						os.makedirs(path+"/"+str(json_data[i].split("_")[0]))
					print("[INFO] Writing data to", path+"/"+str(json_data[i].split("_")[0]))
					with open(path+"/"+str(json_data[i].split("_")[0])+"/"+json_array[j], 'wb') as f:
						f.write(r.content)
				os.system('clear')
	return data_exist

def call_training():
	print("[CLEANUP] Cleaning Up Running Programs")
	cv2.destroyAllWindows()
	vs.stop()
	# os.system('python3 -u /home/daftrogus/MagicMirror/modules/Photo_Saver-v1301164061/script_training.py');

	# WARNING: NEW CODE VERSION START HERE
	print("[INFO] Running Training Script")

	p1 = subprocess.Popen(['python3', '-u', '/home/daftrogus/MagicMirror/modules/Photo_Saver-v1301164061/script_training.py'])
	# print(repr(p1.communicate()[0]))
	p1.wait()
	print("[INFO] Subprocess is dead")
	data = pickle.loads(open(args["encodings"], "rb").read())
	vs.start()

	# exec(open('/home/daftrogus/MagicMirror/modules/Photo_Saver-v1301164061/script_training.py').read())
#load the known faces and embeddings along with openCV's Haar Cascade for face detection
print("[INFO] loading encodings + face detector...")
if not os.path.isfile(args["encodings"]):
	if new_exist():
		print("[INFO] Calling Training Script")
		call_training()
	else:
		print("[WARN] Face Encodings Does Not Exist!")
		print("[WARN] Calling Training Script!")
		call_training()

data = pickle.loads(open(args["encodings"], "rb").read())
detector = cv2.CascadeClassifier(args["cascade"])

#start the FPS counter
fps = FPS().start()

# Set Timer
# cv2.imwrite('test_image.jpg', frame)
# BUG: Ini masih ngebug, setelah sekali kedetek dia ga cek lagi langsung capture ini.
def take_photo():
	# What to do
	# 1) Cron job di raspi (upload new data every X minute) ke server
	# 2) Aplikasi android (pake background service) request per X menit ke hosting
	# 3) Aplikasi android receive data dari server
	# 4) Profit
	global timer
	# camera = cv2.VideoStream(0)
	# for i in range(timer):
	if (name == detected_name):
		# Capture kalo nama yang di detek sama kayak nama pas pertama kali jalan (reference 1)
		dir = "/home/daftrogus/MagicMirror/modules/Photo_Saver-v1301164061/openCV_FR/captured/"+str(today).replace('-', '')
		if not os.path.exists(dir):
			os.makedirs(dir)
			# with open(f'{dir}/{name}_{today}.jpg', 'rb') as f:
		print(f"[INFO] {name} Image Captured")
		cv2.imwrite(os.path.join(dir, name+"_"+str(today).replace('-', '')+".jpg"), frame)
		isup.upload(f"{dir}/{name}_{str(today).replace('-', '')}.jpg")
		# 	r = requests.post('http://daftrogus.com/captured.php', files={f'{dir}/{name}_{today}.jpg'
		# print("Captured:", name)
		# if not timer_upload.is_alive():
		# 	timer_upload = threading.Timer(60, isup.upload(f'{dir}/{name}_{today}.jpg'))
		# 	timer_upload.start()
		if not timer.is_alive():
			timer = threading.Timer(10, take_photo)
			timer.start()
timer = threading.Timer(10, take_photo)
# timer_upload = threading.Timer(60, isup.upload(f'{dir}/{name}_{today}.jpg'))
detected_name = ""
good_var = 0
bad_var = 0
face_detection_count = 0
currently_detected = 0

#loop over frames from the video file stream
while True:
	if new_exist():
		call_training()

	#grab the frame from the threaded video stream and resize it to 500px (to speed up processing)
	frame = vs.read()
	frame = imutils.resize(frame, width = 1000)

	#convert the input frame from (1) BGR to Grayscale (for face detection) and (2) from BGR to RGB (for face recog)
	gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
	rgb = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)

	#detect faces in the grayscale frame
	rects = detector.detectMultiScale(gray, scaleFactor=1.1, minNeighbors=5, minSize=(30, 30))

	#openCV returns bounding box coordinates in (x, y, w, h) order
	#but we need them in (top, right, bottom left) order, so we need to do
	#a bit of reordering
	boxes = [(y, x + w, y + h, x) for (x, y, w, h) in rects]

	#compute the facial embeddings for each face bounding box
	encodings = face_recognition.face_encodings(rgb, boxes)
	names = []

	#loop over the facial embeddings
	for encoding in encodings:
		#attempt to match each face in the input image to our known encodings
		matches = face_recognition.compare_faces(data["encodings"], encoding)
		name = "Unknown"

		currently_detected = len(encodings)

		#check to see if we have found a match
		if  True in matches:
			#find the indexes of all matched faces then initialize a dictionary
			#to count the total number of times each face was matched
			matchedIdxs = [i for (i, b) in enumerate(matches) if b]
			counts = {}

			#loop over the matched indexes and maintain a count for each recognized face face
			for i in matchedIdxs:
				name = data["names"][i]
				counts[name] = counts.get(name, 0) + 1
			# print("names in idx", name)

			#determine the recognized face with the largest number of votes
			#(note: in the event of an unlikely tie Python will select first entry in the dictionary)
			name = max(counts, key=counts.get)
			# print("I am detecting:", name)

			if name != "Unknown":
				# print("Initiating Capture for:", name)
				detected_name = name; #(Reference 1)
				# Do shit over here
				#if face detected in 5 seconds, image captured and stored

				today = date.today()

				if not timer.is_alive():
					timer = threading.Timer(20, take_photo)
					timer.start()
				# if not timer_upload.is_alive():
				# 	timer_upload = threading.Timer(60, isup.upload(f'{dir}/{name}_{today}.jpg'))
				# 	timer_upload.start()
			else:
				timer = timer.Change(Timeout.Infinite, Timeout.Infinite)

			# print("currently detected:",currently_detected)
		#update the list of names
		names.append(name)

	#loop over the recognized faces
	for ((top, right, bottom, left), name) in zip(boxes, names):
		#draw the predicted face name on the image
		cv2.rectangle(frame, (left, top), (right, bottom), (0, 255, 0), 2)
		y = top - 15 if top - 15 > 15 else top + 15
		cv2.putText(frame, name, (left, y), cv2.FONT_HERSHEY_SIMPLEX, 0.75, (0, 255, 0), 2)

	#display the image to our screen
	cv2.imshow("Frame", frame)
	key = cv2.waitKey(1) & 0xFF

	#if the 'q' key was pressed, break from the loop
	if key == ord ("q"):
		break

	#update the FPS counter
	fps.update()
# End while

#stop the timer and dipslay the FPS information
fps.stop()
print("[INFO] elapsed time: {:.2f}".format(fps.elapsed()))
print("[INFO] approx. fps: {:.2f}".format(fps.fps()))

#do a bit of cleanup
cv2.destroyAllWindows()
vs.stop()
