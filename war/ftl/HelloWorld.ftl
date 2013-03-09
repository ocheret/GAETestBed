<!DOCTYPE html>
<html lang="us">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>GAETestBed: Hello ${clown}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
    <script src="js/jquery-1.9.1.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script src="js/HelloWorld.js"></script>
  </head>
  <body class="container">
    <header>
      <div class="row">
	<div class="span12">
	  <h1 class="well">Hello ${clown}</h1>
	  <p>Welcome, ${username!"whoever you are"}</p>
	</div>
      </div>
    </header>
    <section>
      <div class="row">
	<div class="span12">
	  <div class="well">
	    <h2>Blah, blah, blah...</h2>
	    <ul>
	      <li><span class="label">One</span> is the first.</li>
	      <li><span class="label">Two</span> is the second.</li>
	      <li><span class="label">Three</span> is the third.</li>
	    </ul>
	  </div>
	</div>
      </div>
    </section>
    <footer>
      <div class="row">
	<div class="span12">
	  <div class="well">
	    <a href="." class="btn btn-primary">Home Page</a>
	    <#if username??>
	      <a href="logout" class="btn btn-danger pull-right">Logout</a>
	    </#if>
	  </div>
	</div>
      </div>
    </footer>
  </body>
</html>
