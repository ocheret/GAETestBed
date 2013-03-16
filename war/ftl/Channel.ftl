<!DOCTYPE html>
<html lang="us">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>GAETestBed: Channel</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
    <script src="js/jquery-1.9.1.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/json2-min.js"></script>
    <script type="text/javascript" src="/_ah/channel/jsapi"></script>
    <script src="js/Channel.js"></script>
  </head>
  <body class="container">
    <header>
      <div class="row">
	<div class="span12">
	  <h1 class="well">Channel</h1>
	  <p>Welcome, ${username!"whoever you are"}</p>
	  <div class="well">
	    <p>To play with the GAE Channel Service, you must first
	      get a connection token from the service and initiate the
	      connection, by clicking on the <span class="label
	      label-success">Connect</span> button.  Once this
	      succeeds you will be able to disconnect using
	      the <span class="label
	      label-important">Disconnect</span> button.  Connection
	      progress details will appear in the
	      <strong>Messages:</strong> area below.</p>
	    <p>To trigger a message broadcast to all people visiting
	      this page, click the <span class="label
	      label-success">Send Message</span> button.  Messages
	      received due to any broadcasts will appear in
	      the <strong>Messages:</strong> area at the bottom.</p>
	    <p>Clear the <strong>Messages:</strong> area by clicking
	      on the <span class="label label-warning">Clear
	      Messages</span> button.</p>
	  </div>
	</div>
      </div>
    </header>
    <section>
      <div class="row">
	<div class="span12">
	  <div class="well">
	    <button id="connect" class="btn btn-success">Connect</button>
	    <button id="clear" class="btn btn-warning">Clear Messages</button>
	  </div>
	</div>
      </div>
      <div class="row">
	<div class="span12">
	  <div class="well">
	    <div class="form-inline">
	      <button id="subscribe" class="btn btn-success">Subscribe</button>
	      <input type="text" id="topic" placeholder="Topic"></input>
	      <input type="text" id="message" placeholder="Message"></input>
	      <button id="send" class="btn btn-success">Send Message</button>
	    </div>
	    <p><strong>Messages:</strong> (most recent on top)</p>
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
