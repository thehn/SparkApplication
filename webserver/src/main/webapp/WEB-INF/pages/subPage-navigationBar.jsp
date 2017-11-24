<!-- Navigation bar -->
<nav class="navbar navbar-inverse navbar-fixed-top">
	<div class="container-fluid">
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#myNavbar">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<!--<a class="navbar-brand " href="/ml/"><span
				class="glyphicon glyphicon-home"></span> Home</a> -->
		</div>
		<div class="collapse navbar-collapse" id="myNavbar">
			<ul class="nav navbar-nav">
				
				<li id="mainDashboard"><a href="/ml"
					class="dropdown-toggle"><span
						class="fa fa-line-chart"></span> Dashboard</a>
				</li>
				
				<li id="sysMonitor"><a href="/ml/system-resource-monitoring"
					class="dropdown-toggle"><span
						class="fa fa-television"></span> Resource Monitoring</a>
				</li>	
				
				<li id="uploadNav"><a href="/ml/uploadPage"><span
						class="fa fa-database"></span> Pre-Processing</a></li>

				<!-- <li id="morphNav" class="dropdown"><a href="#"
					class="dropdown-toggle" data-toggle="dropdown"><span
						class="glyphicon glyphicon-tasks"></span> Morpheme <span
						class="glyphicon glyphicon-menu-down"></span></a>
					<ul class="dropdown-menu">
						<li id="stt"><a href="/ml/morph/stt">STT</a></li>
						<li id="kMorph"><a href="/ml/morph">analysis</a></li>
					</ul>
				</li> -->

				<!-- <li id="patternMiningNav" class="dropdown"><a href="#"
					class="dropdown-toggle" data-toggle="dropdown"><span
						class="glyphicon glyphicon-list-alt"></span> Pattern Mining <span
						class="glyphicon glyphicon-menu-down"></span></a>
					<ul class="dropdown-menu">
						<li><a href="/ml/fpGrowth-homepage">FP-Growth</a></li>
					</ul>
				</li> -->
				
				<!-- <li id="featuresSelNav" class="dropdown"><a href="#"
					class="dropdown-toggle" data-toggle="dropdown"><span
						class="glyphicon glyphicon-check"></span> Features Selection <span
						class="glyphicon glyphicon-menu-down"></span></a>
					<ul class="dropdown-menu">
						<li><a href="/ml/featuresSel-FSChiSqSelector">ChiSqSelector</a></li>
					</ul>
				</li> --> <!-- #PC0016 -->

				<li id="classificationNav" class="dropdown"><a href="#"
					class="dropdown-toggle" data-toggle="dropdown"><span
						class="glyphicon glyphicon-road"></span> Failure Detection Model <span
						class="glyphicon glyphicon-menu-down"></span></a>
					<ul class="dropdown-menu">
						<li><a href="/ml/classification-svm-homepage">Linear Support Vector Machine</a></li>
						<li><a href="/ml/classification-svc-homepage">Linear Support Vector Classifier</a></li>
						<li><a href="/ml/classification-rfc-homepage">Random Forest</a></li>
						<li><a href="/ml/classification-mlp-homepage">Multiple Layers Perceptron</a></li>
						<li><a href="/ml/classification-nb-homepage">Naive Bayes</a></li>
						<li><a href="/ml/classification-lr-homepage">Logistic Regression</a></li>
					</ul>
				</li>

				<!-- <li id="clusteringNav" class="dropdown"><a href="#"
					class="dropdown-toggle" data-toggle="dropdown"><span
						class="glyphicon glyphicon-th"></span> Clustering <span
						class="glyphicon glyphicon-menu-down"></span></a>
					<ul class="dropdown-menu">
						<li><a href="/ml/clustering-KmeansClustering">K-Means</a></li>
						<li><a href="/ml/clustering-KMediodsClustering">K-Mediods</a></li>
						<li><a href="/ml/clustering-DBSCAN">DBSCAN</a></li>
					</ul>
				</li> -->
				
				<!-- <li id="schedulerNav" class="dropdown"><a href="#"
					class="dropdown-toggle" data-toggle="dropdown"><span
						class="glyphicon glyphicon-time"></span> Scheduled <span
						class="glyphicon glyphicon-menu-down"></span></a>
					<ul class="dropdown-menu">
						<li><a href="/ml/scheduledJobs">Scheduler</a></li>
						<li><a href="#">Alert</a></li>
					</ul>

				</li>	 -->			
				
				<!-- <ul class="nav navbar-nav">
					<li><a href="/ml/dashboard_demo">Dashboard Demo</a></li>
				</ul>
				
				<ul class="nav navbar-nav">
					<li><a href="/ml/dashboard_test">DataBinding</a></li>
				</ul>	 -->			
			</ul>
			
			<ul class="nav navbar-nav navbar-right">
				<li><a data-toggle="modal" data-target="#squarespaceModal"><span
						class="glyphicon glyphicon-cog"></span> Setting</a></li>
				<li id="userMenu" class="dropdown"><a href="#"
					class="dropdown-toggle" data-toggle="dropdown"><span
						class="fa fa-user-circle"></span>  <span
						class="glyphicon glyphicon-menu-down"></span></a>
					<ul class="dropdown-menu">
						<li><a href="/ml/logout">Sign out</a></li>
					</ul>
				</li>
			</ul>
		</div>
	</div>
</nav>