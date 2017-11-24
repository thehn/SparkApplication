<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page session="false" %>

<%@ include file="subPage-libraries.jsp"%>
<%-- <%@ include file="subPage-common-css.jsp"%> --%>
<link rel="stylesheet" type="text/css" href="<spring:url value="/resources/css/commonStyle.css"/>">
<script src="<spring:url value="/resources/js/morph.js?1.0"/>" type="text/javascript"></script>

<html>
<head>
	<title>Korean morpheme analysis</title>
</head>
<body>

	<%@ include file="subPage-navigationBar.jsp"%>
	
	<!-- Container -->
	<div class="container" id="container">

		<div class="tab-content">
			<div id="morphTap" class="tab-pane fade in active">
				<h2 class="algorithm-header">Korean Morpheme Analysis</h2>
				<div class="panel panel-main">
					<div class="panel-heading">
						<strong>Morpheme</strong>
					</div>
					<div class="panel-body">
						<div class="panel panel-default">
							<div class="panel-heading">
								<strong>Input text </strong>
							</div>
							<div class="panel-body">
								<div style="width: 100%">
									<form id="inputForm" style="margin-bottom: 20px" modelAttribute="vo">
										<div><center><textarea class="form-control" rows="5" name="contents">택배를 본인에게 전달하기는 커녕, 문앞에 놔두고 가고, 연락한번 안하네요? 회사에 사람도 많이있고, 불도 켜져있고, 문도 열려있는데, 그렇게 들어와서 주고 가는게 힘이 들고, 바쁘신가요?</textarea></center></div>
										<div class="form-group" style="padding-top: 1%">
											<div class="col-md-12">
												<input class="btn btn-md btn-success pull-right" type="button" id="analysis" value="Analysis"/>
											</div>
										</div>
									</form>
								</div>
							</div>
							<div class="panel-footer"></div>
						</div>
					</div>
				</div>
				<div id="results" class="panel panel-main"
					style="display: none;">
					<div class="panel-heading">
						<strong>Korean analysis</strong>
					</div>
					<div class="row">
						<div class="col-lg-12">
							<div class="panel panel-success">
								<div class="panel-heading">
									<strong>Morpheme results</strong>
								</div>
								<div class="panel-body">
									<div class="table-responsive">
										<table id="result" class="table table-striped table-bordered" style="cellspacing: 0; width: 100%;"></table>
									</div>
								</div>
							</div>
						</div>
					</div>
					<!-- <div id="responseViewer"></div> -->
				</div>
			</div>
		</div>

<!-- 		<div id="responseView" style="margin-bottom: 6%" /> -->
		
		<div id="loaderContainer">
			<div id="loader"></div>
		</div>
	</div>
	
</body>
</html>
