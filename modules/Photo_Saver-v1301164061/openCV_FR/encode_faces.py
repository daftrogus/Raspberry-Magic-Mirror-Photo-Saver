from imutils import paths
import face_recognition
import argparse
import pickle
import cv2
import os
import time

#construct the argument parser and parse the arguments
ap = argparse.ArgumentParser()
ap.add_argument("-i", "--dataset", required=True, help="path to input directory of faces + images")
ap.add_argument("-e", "--encodings", required=True, help="path to serialized db or facial encodings")
ap.add_argument("-d", "--detection-method", type=str, default="cnn", help="face detection model to use: either HOG or CNN")
args = vars(ap.parse_args())
start_time = time.time()

if os.path.isfile(args["encodings"]):
	os.remove(args["encodings"])
	print("[INFO] Successfully Delete Old Faces!")

#grab the paths to the input images in ourdataset
print("[INFO] quantifying faces...")
imagePaths = list(paths.list_images(args["dataset"]))

#initialize the list of known encodings and known names
knownEncodings = []
knownNames = []

#loop over the image paths
for(i, imagePath) in enumerate(imagePaths):
	#extract the person name from the image path
	print("[INFO] processing image {}/{}".format(i + 1, len(imagePaths)))
	name = imagePath.split(os.path.sep)[-2]

	#load the input image and convert it from BGR (openCV ordering)
	#to dlib ordering (RGB)
	image = cv2.imread(imagePath)
	rgb = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)

	#detect the (x, y)-coordinates of the bounding boxes
	#coressponding to each face in the input image
	boxes = face_recognition.face_locations(rgb, model=args["detection_method"])

	#compute the facial embedding for the face
	encodings = face_recognition.face_encodings(rgb, boxes)

	#loop over the encodings
	for encoding in encodings:
		#add each encoding + name to our set of known names and encodings
		knownEncodings.append(encoding)
		knownNames.append(name)

#dump the facial encodings + names to disk
print("[INFO] serializing encodings...")
data = {"encodings": knownEncodings, "names": knownNames}
f = open(args["encodings"], "wb")
f.write(pickle.dumps(data))
print("--- %s seconds ---" % (time.time() - start_time))
f.close()
print("[TERM] Killing this subprocess")

exit(0)

# exec(open('/home/daftrogus/MagicMirror/modules/Photo_Saver-v1301164061/script_test.py').read())
