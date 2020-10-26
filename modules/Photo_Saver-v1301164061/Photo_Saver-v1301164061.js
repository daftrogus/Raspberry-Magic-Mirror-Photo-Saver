Module.register('Photo_Saver-v1301164061',{

	messages:[],

	defaults: {
		max: 5,
		format: false,
		types: {
			INFO: "dimmed",
			WARNING: "normal",
			ERROR: "bright"
		},
		icons: {
			INFO: "info",
			WARNING: "exclamation",
			ERROR: "exclamation-triangle"
		},
		shortenMessage: false,
	alert: true
	},

	getStyles: function() {
		return ["font-awesome.css"];
	},

	getScripts: function() {
		return ["moment.js"];
	},


	start: function() {
		this.sendSocketNotification("CONNECT",{max: this.config.max, logFile: this.file("logs.json")})
		// Log.info("Starting module: "+ this.name);
		// moment.locale(config.language);
		//
		// let options = {
		// 	mode: 'text',
		// 	pythonPath: '/usr/bin/python3',
		// 	pythonOptions: ['-u'], // get print results in real-time
		// 	scriptPath: 'modules/Photo_Saver-v1301164061/public'
		// 	//args: ['value1', 'value2', 'value3']
		// };
		//
		// PythonShell.run('script_test.py', options, function (err, results) {
		// 	if (err) throw err;
		// 	// results is an array consisting of messages collected during execution
		// 	console.log('results: %j', results);
		// });

		// setInterval(() =>{
		// 	this.updateDom();
		// }, 60000);
	},

	socketNotificationReceived: function(notification, payload){
		if(notification == "LOAD_HISTORY"){
			if(this.config.alert && !payload.silent){
				this.sendNotification("SHOW_ALERT", {type:"notification", title: payload.type, message: payload.message});
			}
			this.messages.push(payload);
			while(this.messages.length > this.config.max){
				this.messages.shift();
			}
			this.updateDom(3000);
		}
	},

	getDom: function(){

		var wrapper = document.createElement("div");
		if(this.config.title !== false){
			var title = document.createElement("header");
			title.innerHTML = this.config.title || this.name;
			wrapper.appendChild(title);
		}
		var logs = document.createElement("table");

		for(var i = this.messages.length - 1; i >= 0; i--){

			//membuat callWrapper
			var callWrapper = document.createElement("tr");
			callWrapper.classList.add("normal");

			var iconCell = document.createElement("td");
			var icon =  document.createElement("i");
			if(this.config.icons.hasOwnProperty(this.messages[i].type)){
				icon.classList.add("fa", "fa-fw", "fa-" + this.config.icons[this.messages[i].type]);
			}
			else {
				icon.classList.add("fa", "fa-fw", "fa-question");
			}
			if(this.config.types.hasOwnProperty(this.messages[i].type)){
				icon.classList.add(this.config.types[this.messages[i].type]);
			}

			iconCell.classList.add("small");

			iconCell.appendChild(icon);
			callWrapper.appendChild(iconCell);

			var message = this.messages[i].message;
			if(this.config.shortenMessage && message.length > this.config.shortenMessage){
				message = message.slice(0, this.config.shortenMessage) + "&#8230;";
			}

			//set pemanggil
			var caller =  document.createElement("td");
			caller.innerHTML = " " + message;
			caller.classList.add("title", "small", "align-left");
			if(this.config.types.hasOwnProperty(this.messages[i].type)){
				caller.classList.add(this.config.types[this.messages[i].type]);
			}
			callWrapper.appendChild(caller);

			//set waktu pada row
			var time =  document.createElement("td");
			time.innerHTML = this.config.format ? moment(this.messages[i].timestamp).format(this.config.format) : moment(this.messages[i].timestamp).fromNow();
			time.classList.add("time", "light", "xsmall");
			callWrapper.appendChild(time);

			//add to logs
			logs.appendChild(callWrapper);
		}
		wrapper.appendChild(logs);
		return wrapper;
	}
});
