<!DOCTYPE html>
<html lang="us">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <title>GAETestBed: Login</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
    <script src="js/jquery-1.9.1.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
  </head>
  <body class="container">
    <header>
      <div class="row">
	<div class="span12">
	  <h1 class="well">Login</h1>
	</div>
      </div>
    </header>
    <section>
      <#if errorMessage??>
	<div class="row">
	  <div class="span12">
	    <div class="alert alert-error">
	      <button type="button" class="close" data-dismiss="alert">
		&times;</button>
	      <strong>Login Error:</strong>
	      ${errorMessage}
	    </div>
	  </div>
	</div>
      </#if>
      <div class="row">
	<div class="span12">
	  <form class="form-horizontal well" action="login" method="post">
	    <fieldset>
	      <legend>
		Please enter your credentials to get to protected content
	      </legend>

	      <div class="control-group">
		<label class="control-label" for="username">User name:</label>
		<div class="controls">
		  <input type="text" id="username" name="username"
			 placeholder="username@company.com">
		</div>
	      </div>

	      <div class="control-group">
		<label class="control-label" for="password">Password:</label>
		<div class="controls">
		  <input type="password" id="password" name="password">
		</div>
	      </div>

	      <div class="control-group">
		<div class="controls">
		  <br><button type="submit" class="btn-success">Login</button>
		  <a href="." class="btn btn-primary">Home Page</a>
		</div>
	      </div>
	    </fieldset>
	  </form>
	</div>
      </div>
    </section>
  </body>
</html>
