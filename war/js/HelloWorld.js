$("document").ready(function() {
    // List of label classes
    var classList = [
	"label-default",
	"label-success",
	"label-warning",
	"label-important",
	"label-info",
	"label-inverse"
    ];

    // Set an interval timer to go off every second
    var counter = 0;
    $(".label").addClass(classList[counter]);
    setInterval(function() {
	$(".label").removeClass(classList[counter]);
	counter = (counter + 1) % classList.length;
	$(".label").addClass(classList[counter]);
    }, 1000); // end setInterval
});
