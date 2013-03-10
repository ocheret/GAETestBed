$("document").ready(function() {
    $("#connect").click(function() {
	var stateTable = { Disconnect: "Connect", Connect: "Disconnect" };
	$(this).toggleClass("btn-success").toggleClass("btn-danger");
	$(this).text(stateTable[$(this).text()]);
    });
    $("#sender").click(function() {
	$("#messages").append("<li>something</li>");
    });
    $("#clear").click(function() {
	$("#messages").html("");
    });
});
