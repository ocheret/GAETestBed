<!DOCTYPE html>
<html lang="us">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>GAETestBed: Channel</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
    <script src="js/jquery-1.9.1.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script type="text/javascript" src="/_ah/channel/jsapi"></script>
    <script src="js/Channel.js"></script>
  </head>
  <body class="container">
    <header>
      <div class="row">
	<div class="span12">
	  <h1 class="well">Channel</h1>
	  <p>Welcome, ${username!"whoever you are"}</p>
	</div>
      </div>
    </header>
    <section>
      <div class="row">
	<div class="span12">
	  <div class="well">
	    <button id="connect" class="btn btn-success">Connect</button>
	    <button id="sender" class="btn btn-success">Send Message</button>
	    <button id="clear" class="btn btn-warning">Clear Messages</button>
	    <p><strong>Messages:</strong></p>
	    <ul id="messages"></ul>
	  </div>
	</div>
      </div>
    </section>
    <footer>
      <div class="row">
	<div class="span12">
	  <div class="well">
	    <a href="." class="btn btn-primary">Home Page</a>
	    <a href="logout" class="btn btn-danger pull-right">Logout</a>
	  </div>
	</div>
      </div>
    </footer>
  </body>
</html>
