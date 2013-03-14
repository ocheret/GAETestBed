(function() {
    var msgCount = 0;
    var displayMessage = function (msg) {
	msgCount++;
	$("#messages").prepend("<li>" + msgCount + ": " + msg + "</li>");
    };
    var sendMessage = function () {
	// XXX
	displayMessage("Message");
    };
    var establishConnection = function (that) {
	var stateTable = { Disconnect: "Connect", Connect: "Disconnect" };
	$(that).toggleClass("btn-success").toggleClass("btn-danger");
	$(that).text(stateTable[$(that).text()]);
	var foo = { one: "won",
		    two: "too",
		    three: [1, 2, 3],
		    four: new Date()
		  };
	displayMessage("JSON:" + JSON.stringify(foo));
	displayMessage("$.param:" + $.param(foo));
// Demonstrates which browsers can't parse ISO dates
//	var da = (Date.parse("2013-03-11T02:43:38.688Z"));
//	var db = (Date.parse("2013-03-11T02:43:38.688+0000"));
//	displayMessage("da = " + da);
//	displayMessage("db = " + db);
    };
    $("document").ready(function() {
	$("#connect").click(function() {
	    establishConnection(this);
	});
	$("#sender").click(function() {
	    sendMessage();
	});
	$("#clear").click(function() {
	    $("#messages").html("");
	});
    });
} ());
