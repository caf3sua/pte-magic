/*License (MIT)

Copyright © 2013 Matt Diamond

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
documentation files (the "Software"), to deal in the Software without restriction, including without limitation 
the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and 
to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of 
the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO 
THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
DEALINGS IN THE SOFTWARE.
*/

(function(window){

  var WORKER_PATH = 'content/audio_recorder/recorderWorker.js';

  var Recorder = function(source, cfg){
    var config = cfg || {};
    var bufferLen = config.bufferLen || 4096;
    this.context = source.context;
    if(!this.context.createScriptProcessor){
       this.node = this.context.createJavaScriptNode(bufferLen, 2, 2);
    } else {
       this.node = this.context.createScriptProcessor(bufferLen, 2, 2);
    }
   
    var worker = new Worker(config.workerPath || WORKER_PATH);
    worker.postMessage({
      command: 'init',
      config: {
        sampleRate: this.context.sampleRate
      }
    });
    var recording = false,
      currCallback;

    this.node.onaudioprocess = function(e){
      if (!recording) return;
      worker.postMessage({
        command: 'record',
        buffer: [
          e.inputBuffer.getChannelData(0),
          e.inputBuffer.getChannelData(1)
        ]
      });
    }

    this.configure = function(cfg){
      for (var prop in cfg){
        if (cfg.hasOwnProperty(prop)){
          config[prop] = cfg[prop];
        }
      }
    }

    this.record = function(){
      recording = true;
    }

    this.stop = function(){
      recording = false;
    }

    this.clear = function(){
      worker.postMessage({ command: 'clear' });
    }

    this.getBuffers = function(cb) {
      currCallback = cb || config.callback;
      worker.postMessage({ command: 'getBuffers' })
    }

    this.exportWAV = function(cb, type){
      currCallback = cb || config.callback;
      type = type || config.type || 'audio/wav';
      if (!currCallback) throw new Error('Callback not set');
      worker.postMessage({
        command: 'exportWAV',
        type: type
      });
    }

    this.exportMonoWAV = function(cb, type){
      currCallback = cb || config.callback;
      type = type || config.type || 'audio/wav';
      if (!currCallback) throw new Error('Callback not set');
      worker.postMessage({
        command: 'exportMonoWAV',
        type: type
      });
    }

    worker.onmessage = function(e){
      var blob = e.data;
      
//      var arrayBuffer;
//	  var fileReader = new FileReader();
//	  
//	  fileReader.onload = function(){
//		arrayBuffer = this.result;
//		var buffer = new Uint8Array(arrayBuffer),
//        data = parseWav(buffer);
//        
//        console.log(data);
//		console.log("Converting to Mp3");
//		log.innerHTML += "\n" + "Converting to Mp3";
//
//        encoderWorker.postMessage({ cmd: 'init', config:{
//            mode : 3,
//			channels:1,
//			samplerate: data.sampleRate,
//			bitrate: data.bitsPerSample
//        }});
//
//        encoderWorker.postMessage({ cmd: 'encode', buf: Uint8ArrayToFloat32Array(data.samples) });
//        encoderWorker.postMessage({ cmd: 'finish'});
//        encoderWorker.onmessage = function(e) {
//            if (e.data.cmd == 'data') {
//			
//				console.log("Done converting to Mp3");
//				log.innerHTML += "\n" + "Done converting to Mp3";
//				
//				/*var audio = new Audio();
//				audio.src = 'data:audio/mp3;base64,'+encode64(e.data.buf);
//				audio.play();*/
//                
//				//console.log ("The Mp3 data " + e.data.buf);
//				
//				var mp3Blob = new Blob([new Uint8Array(e.data.buf)], {type: 'audio/mp3'});
//				uploadAudio(mp3Blob);
//				
//				var url = 'data:audio/mp3;base64,'+encode64(e.data.buf);
//				var li = document.createElement('li');
//				var au = document.createElement('audio');
//				var hf = document.createElement('a');
//				  
//				au.controls = true;
//				au.src = url;
//				hf.href = url;
//				hf.download = 'audio_recording_' + new Date().getTime() + '.mp3';
//				hf.innerHTML = hf.download;
//				li.appendChild(au);
//				li.appendChild(hf);
//				recordingslist.appendChild(li);
//				
//            }
//        };
//	  };
//	  
//	  fileReader.readAsArrayBuffer(blob);
      
      currCallback(blob);
    }
    
    function encode64(buffer) {
		var binary = '',
			bytes = new Uint8Array( buffer ),
			len = bytes.byteLength;

		for (var i = 0; i < len; i++) {
			binary += String.fromCharCode( bytes[ i ] );
		}
		return window.btoa( binary );
	}

	function parseWav(wav) {
		function readInt(i, bytes) {
			var ret = 0,
				shft = 0;

			while (bytes) {
				ret += wav[i] << shft;
				shft += 8;
				i++;
				bytes--;
			}
			return ret;
		}
		if (readInt(20, 2) != 1) throw 'Invalid compression code, not PCM';
		if (readInt(22, 2) != 1) throw 'Invalid number of channels, not 1';
		return {
			sampleRate: readInt(24, 4),
			bitsPerSample: readInt(34, 2),
			samples: wav.subarray(44)
		};
	}

	function Uint8ArrayToFloat32Array(u8a){
		var f32Buffer = new Float32Array(u8a.length);
		for (var i = 0; i < u8a.length; i++) {
			var value = u8a[i<<1] + (u8a[(i<<1)+1]<<8);
			if (value >= 0x8000) value |= ~0x7FFF;
			f32Buffer[i] = value / 0x8000;
		}
		return f32Buffer;
	}
	
	function uploadAudio(mp3Data){
		var reader = new FileReader();
		reader.onload = function(event){
			var fd = new FormData();
			var mp3Name = encodeURIComponent('audio_recording_' + new Date().getTime() + '.mp3');
			console.log("mp3name = " + mp3Name);
			fd.append('fname', mp3Name);
			fd.append('data', event.target.result);
			$.ajax({
				type: 'POST',
				url: 'upload.php',
				data: fd,
				processData: false,
				contentType: false
			}).done(function(data) {
				//console.log(data);
				log.innerHTML += "\n" + data;
			});
		};      
		reader.readAsDataURL(mp3Data);
	}

    source.connect(this.node);
    this.node.connect(this.context.destination);   // if the script node is not connected to an output the "onaudioprocess" event is not triggered in chrome.
  };

  Recorder.setupDownload = function(blob, filename){
    var url = (window.URL || window.webkitURL).createObjectURL(blob);
    var link = document.getElementById("save");
    link.href = url;
    link.download = filename || 'output.wav';
  }

  window.Recorder = Recorder;

})(window);
