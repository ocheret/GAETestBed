(function() {
    var channel = null;
    var socket = null;
    var msgCount = 0;
    var displayMessage = function (msg) {
	msgCount++;
	$("#messages").prepend("<li>" + msgCount + ": " + msg + "</li>");
    };
    var sendMessage = function () {
	// XXX
	displayMessage("Message");
    };
    var openSocket = function(that) {
	socket = channel.open({
	    onopen: function() {
		displayMessage("Socket opened");
	    },
	    onmessage: function(msg) {
		// XXX
		displayMessage(msg.data);
	    },
	    onerror: function(error) {
		displayMessage("Error:" + error.code + "/" +
			       error.description);
		if (error.code == 401 || error.code == 0) {
		    // The channel has timed out or is invalid
		    shutdownConnection(that);
		}
	    },
	    onclose: function() {
		if (channel != null) {
		    // If channel is null then we meant to close this
		    openSocket(that);
		} else {
		    establishConnection(that);
		}
	    }
	});
    };
    var establishConnection = function (that) {
	// Issue an AJAX call to get a connection token
	$.ajax({
	    type: "get",
	    url: "channel?c=connect",
	    dataType: "json",
	    beforeSend: function() {
		$(that).removeClass("btn-success").addClass("btn-warning")
		    .text("Connecting...");
	    }, // end beforeSend
	    error: function(xhr, status, error) {
		displayMessage("Connection error:"
			       + status + "/" + xhr.status);
		$(that).removeClass("btn-warning").addClass("btn-success")
		    .text("Connect");
	    }, // end error
	    success: function(data, status, xhr) {
		displayMessage("token = " + data.token);
		channel = new goog.appengine.Channel(data.token);
		openSocket();
		$(that).removeClass("btn-warning").addClass("btn-danger")
		    .text("Disconnect");
	    } // end success
	});
    };
    var shutdownConnection = function(that) {
	channel = null;
	delete socket.onerror;
	delete socket.onclose;
	socket.close();
	socket = null;
	$(that).removeClass("btn-danger").addClass("btn-success")
	    .text("Connect");
    };
    $("document").ready(function() {
	$("#connect").click(function() {
	    if (socket == null) {
		establishConnection(this);
	    } else {
		shutdownConnection(this);
	    }
	});
	$("#sender").click(function() {
	    sendMessage();
	});
	$("#clear").click(function() {
	    $("#messages").html("");
	});
    });
} ());
