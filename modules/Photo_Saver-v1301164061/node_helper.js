const NodeHelper = require("node_helper");
const multer = require("multer");
const url = require("url");
const fs = require("fs");
const path = require("path");
const {spawn} = require('child_process');
const ls = spawn('python3', ['modules/Photo_Saver-v1301164061/public']);
let { PythonShell } = require("python-shell");

const handleError = (err, res) => {
	res
		.status(500)
		.contentType("text/plain")
		.end("Oops! Terjadi Kesalahan!");
}

const upload = multer({
	dest: "modules/Photo_Saver-v1301164061/public/upload"
})

module.exports = NodeHelper.create({

	start: function(){
		//Latihan Run file python
		this.expressApp.get('/sys-img', (req, res) => {
			let options = {
			  mode: 'text',
			  pythonPath: '/usr/bin/python3',
			  pythonOptions: ['-u'], // get print results in real-time
			  scriptPath: 'modules/Photo_Saver-v1301164061/public'
			  //args: ['value1', 'value2', 'value3']
			};

			PythonShell.run('script_test.py', null, function (err) {
				if (err) throw err;
				console.log('finished');
			});

			// PythonShell.run('script_test.py', options, function (err, results) {
			//   if (err) throw err;
			//   // results is an array consisting of messages collected during execution
			//   console.log('results: %j', results);
			// });
			//console.log("Run this beatch");
			//console.log(__dirname+'/public/script_test.py');
			//ls.stdout.on('data', function(data) {
				//console.log("STDOUT ON");
				//console.log(`stdout: ${data}`);
				//res.send(data);
			//});

			//ls.stderr.on('data', function(data) {
				//console.log("STDERR ON");
				//console.log('stderr: ${data}');
				//res.send(data);
			//});

			//ls.on('data', function(data) {
				//console.log("LS ON");
				//console.log('child process exited with code ${data}');
				//res.send(data);
			//});

			//console.log("FUNCT STOPPED");
		});

		//Latihan Upload file
		this.expressApp.post
			("/upload", upload.single("form_upload"),(req, res) => {
				const tempPath = req.file.path;
				const targetPath = "modules/Photo_Saver-v1301164061/public/upload/"+req.file.originalname;

					if (path.extname(req.file.originalname).toLowerCase() === ".jpeg"){
						fs.rename(tempPath, targetPath, err => {
							if(err){
								return handleError(err, res);
							}
							res
								.status(200)
								.contentType("text/plain")
								.end("File telah di Upload!.");
						});
					}else{
						fs.unlink(tempPath, err => {
							if(err){
								return handleError(err, res);
							}
							res
								.status(200)
								.contentType("text/plain")
								.end("Hanya file .jpeg yang dapat di upload!");
						})
					}

				}
			);

		// Latihan Sending File
		this.expressApp.get('/request_image/:user_id', (req, res) => {
			if (req.params.user_id == 1) {
				console.log("modules/Photo_Saver-v1301164061/public/download.jpeg");
				res.sendFile("modules/Photo_Saver-v1301164061/public/download.jpeg");
			} else {
				res.send(0);
			}
		});

		// Latihan Receive Notifikasi
		this.expressApp.get('/sysbag', (req, res) => {
			// Yang bisa dikirim / diterima
			// image, data diri (string)
			var query = url.parse(req.url, true).query;
			var message = query.message;		// base64 image atau string (json) data diri -- nama
			var type = query.type;				// string atau image
			var silent = query.silent || false;

			if (message == null && type == null){
				res.send({"status": "gagal", "error": "tidak dapat memberikan pesan dan tipe."});
			}
			else if(message == null){
				res.send({"status": "gagal", "error": "tidak dapat memberikan pesan."});
			}
			else if(type == null){
				res.send({"status": "gagal", "error": "tidak dapat memberikan tipe."});
			}
			else{
				var log = {"type": type, "message": message, "silent": silent, "timestamp": new Date()};
				res.send({"status": "success","payload": log});
				this.sendSocketNotification("LOAD_HISTORY", log);
				this.storeLog(log);
			}
		});
	},

	socketNotificationReceived: function(notification, payload){
		if(notification === "CONNECT"){
			let options = {
			  mode: 'text',
			  pythonPath: '/usr/bin/python3',
			  pythonOptions: ['-u'], // get print results in real-time
			  scriptPath: 'modules/Photo_Saver-v1301164061'
			  //args: ['value1', 'value2', 'value3']
			};

			var pyShell = PythonShell.run('script_test.py', options, function (err) {
				if (err) throw err;
				console.log('finished');
			});

			pyShell.on('message', function (message) {
			  // received a message sent from the Python script (a simple "print" statement)
			  console.log(message);
			});
			// this.logFile = payload.logFile;
			// this.loadLogs();
			// this.max = payload.max;
		}
	},

	storeLog: function(log){
		this.logs.push(log);
		while(this.logs.length > this.max){
			this.logs.shift();
		}
		fs.writeFileSync(this.logFile, JSON.stringify({"messages":this.logs}),'utf8');
	},

	loadLogs: function(){
		if(this.fileExists(this.logFile)){
			this.logs = JSON.parse(fs.readFileSync(this.logFile, 'utf8')).messages;
			for(var i = 0; i< this.logs.length;i++){
				this.sendSocketNotification("LOAD_HISTORY", this.logs[i]);
			}
		} else{
			this.logs = [];
		}
	},

	fileExists: function(path){
		try{
			return fs.statSync(path).isFile();
		}catch(e){
			console.log("tidak ditemukan file Log.");
			return false;
		}
	}

});
