<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-success">
			<div class="panel-heading">
				<strong>Frequent Patterns</strong>
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="table-responsive">
					<table id="freqPattern" class="table table-striped table-bordered" style="cellspacing: 0; width: 100%;"></table>
				</div>
				<!-- /.table-responsive -->
			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
</div>

<div class="row">
	<div class="col-lg-9">
		<div id="d3Svg" style="boder: 1px solid;"></div>
		<div class="col-lg-9"></div>
		<div id="displayTrail"></div>
		<div class="col-lg-9"></div>
		<div id="chart">
			<div id="explanation" style="visibility: hidden;"
			
			>
				<span id="percentage"></span><br/> 
				The Result of FP-Growth Algorithm
			</div>
		</div>
	</div>
	<div class="col-lg-3">
		<table class="table table-striped" id="itemsList">
			<thead>
				<tr>
					<th>Product Code</th>
					<th>Product Name</th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</div>
</div>

<script type="text/javascript">
	var dataFreqPatterns = ${dataFreqPatterns};
	$(document).ready(function() {
		$('#freqPattern').DataTable({
			data : dataFreqPatterns,
			columns : [ {
				title : "Pattern"
			}, {
				title : "Frequence"
			} ],
			responsive : true
		});
	});
</script>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Sequences sunburst</title>
<script src="http://d3js.org/d3.v3.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="https://fonts.googleapis.com/css?family=Open+Sans:400,600">
<link rel="stylesheet" type="text/css"
	href="<spring:url value="/resources/css/sequences.css?ver=7"/>"/>
</head>

<script type="text/javascript">
  
  var processedData = ${processedData};
  var matchingTable = ${matchingTable};
  
  //display itemsList on the right side
	var value;
	var html = '';
	$('#itemsList tr').not(':first').not(':last').remove();
	Object.keys(matchingTable).forEach(function(key) {
		value = matchingTable[key];
		html += '<tr><td>' + key + '</td><td>' + value + '</td></tr>';
	});
	$('#itemsList tr').first().after(html);
  
  // Dimensions of sunburst.
  var width = 750;
  var height = 600;
  var radius = Math.min(width, height) / 2;

  // Breadcrumb dimensions: width, height, spacing, width of tip/tail.
  var b = {
    w: 75, h: 30, s: 3, t: 10
  };

  // Mapping of step names to colors.
  var colors = {
    "1": "#154360",
    "2": "#1A5276",
    "3": "#1F618D",
    "4": "#2471A3",
    "5": "#2980B9",
    "6": "#5499C7",
    "7": "#7FB3D5",
    "8": "#A9CCE3",
    "9": "#1B4F72",
    "10": "#21618C",
    "11": "#2874A6",
    "12": "#2E86C1",
    "13": "#3498DB",
    "14": "#5DADE2",
    "15": "#85C1E9",
    "16": "#AED6F1",
    "17": "#D4E6F1",
    "18": "#EAF2F8"
  };

  // Total size of all segments; we set this later, after loading the data.
  var totalSize = 0;

  var vis = d3.select("#chart").append("svg:svg")
 	  //use viewBox for responsive 
      .attr("viewBox", "0 0 " + width + " " + height)
      .attr("preserveAspectRatio", "xMidYMid meet")
      .append("svg:g")
      .attr("id", "container")
      .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");
  
  var partition = d3.layout.partition()
      .size([2 * Math.PI, radius * radius])
      .value(function(d) { return d.size; });

  var arc = d3.svg.arc()
      .startAngle(function(d) { return d.x; })
      .endAngle(function(d) { return d.x + d.dx; })
      .innerRadius(function(d) { return Math.sqrt(d.y); })
      .outerRadius(function(d) { return Math.sqrt(d.y + d.dy); });
  
  // Use d3.text and d3.csv.parseRows so that we do not need to have a header
  // row, and can receive the csv as an array of arrays.
  d3.text("processedData", function(text) {
    var json = buildHierarchy(processedData);
    createVisualization(json);
  });

  // Main function to draw and set up the visualization, once we have the data.
  function createVisualization(json) {

    // Basic setup of page elements.
    initializeBreadcrumbTrail();
	
    // Bounding circle underneath the sunburst, to make it easier to detect
    // when the mouse leaves the parent g.
    vis.append("svg:circle")
        .attr("r", radius)
        .style("opacity", 0);

    // For efficiency, filter nodes to keep only those large enough to see.
    var nodes = partition.nodes(json)
        .filter(function(d) {
        return (d.dx > 0.005); // 0.005 radians = 0.29 degrees
        });

    var path = vis.data([json]).selectAll("path")
        .data(nodes)
        .enter().append("svg:path")
        .attr("display", function(d) { return d.depth ? null : "none"; })
        .attr("d", arc)
        .attr("fill-rule", "evenodd")
        .style("fill", function(d) { return colors[d.name]; })
        .style("opacity", 1)
        .on("mouseover", mouseover);

    // Add the mouseleave handler to the bounding circle.
    d3.select("#container").on("mouseleave", mouseleave);

    // Get total size of the tree = value of root node from partition.
    totalSize = path.node().__data__.value;
   };

  // Fade all but the current sequence, and show it in the breadcrumb trail.
  function mouseover(d) {

    var percentage = (100 * d.value / totalSize).toPrecision(3);
    var percentageString = percentage + "%";
    if (percentage < 0.1) {
      percentageString = "< 0.1%";
    }

    d3.select("#percentage")
        .text(percentageString);

    d3.select("#explanation")
        .style("visibility", "");

    var sequenceArray = getAncestors(d);
    updateBreadcrumbs(sequenceArray, percentageString);

    // Fade all the segments.
    d3.selectAll("path")
        .style("opacity", 0.3);

    // Then highlight only those that are an ancestor of the current segment.
    vis.selectAll("path")
        .filter(function(node) {
                  return (sequenceArray.indexOf(node) >= 0);
                })
        .style("opacity", 1);
  }

  // Restore everything to full opacity when moving off the visualization.
  function mouseleave(d) {

    // Hide the breadcrumb trail
    d3.select("#trail")
        .style("visibility", "hidden");

    // Deactivate all segments during transition.
    d3.selectAll("path").on("mouseover", null);

    // Transition each segment to full opacity and then reactivate it.
    d3.selectAll("path")
        .transition()
        .duration(1000)
        .style("opacity", 1)
        .each("end", function() {
                d3.select(this).on("mouseover", mouseover);
              });

    d3.select("#explanation")
        .style("visibility", "hidden");
  }

  // Given a node in a partition layout, return an array of all of its ancestor
  // nodes, highest first, but excluding the root.
  function getAncestors(node) {
    var path = [];
    var current = node;
    while (current.parent) {
      path.unshift(current);
      current = current.parent;
    }
    return path;
  }

  function initializeBreadcrumbTrail() {
    // Add the svg area.
    var trail = d3.select("#displayTrail").append("svg:svg")
        .attr("viewBox", "0 0 " + width + " " + "50")
      .attr("preserveAspectRatio", "xMidYMid meet")
        .attr("id", "trail")
    // Add the label at the end, for the percentage.
    trail.append("svg:text")
      .attr("id", "endlabel")
      .style("fill", "#000");
  }

  // Generate a string that describes the points of a breadcrumb polygon.
  function breadcrumbPoints(d, i) {
    var points = [];
    points.push("0,0");
    points.push(b.w + ",0");
    points.push(b.w + b.t + "," + (b.h / 2));
    points.push(b.w + "," + b.h);
    points.push("0," + b.h);
    if (i > 0) { // Leftmost breadcrumb; don't include 6th vertex.
      points.push(b.t + "," + (b.h / 2));
    }
    return points.join(" ");
  }

  // Update the breadcrumb trail to show the current sequence and percentage.
  function updateBreadcrumbs(nodeArray, percentageString) {

    // Data join; key function combines name and depth (= position in sequence).
    var g = d3.select("#trail")
        .selectAll("g")
        .data(nodeArray, function(d) { return d.name + d.depth; });

    // Add breadcrumb and label for entering nodes.
    var entering = g.enter().append("svg:g");

    entering.append("svg:polygon")
        .attr("points", breadcrumbPoints)
        .style("fill", function(d) { return colors[d.name]; });

    entering.append("svg:text")
        .attr("x", (b.w + b.t) / 2)
        .attr("y", b.h / 2)
        .attr("dy", "0.35em")
        .attr("text-anchor", "middle")
        .text(function(d) { return d.name; });

    // Set position for entering and updating nodes.
    g.attr("transform", function(d, i) {
      return "translate(" + i * (b.w + b.s) + ", 0)";
    });

    // Remove exiting nodes.
    g.exit().remove();

    // Now move and update the percentage at the end.
    d3.select("#trail").select("#endlabel")
        .attr("x", (nodeArray.length + 0.5) * (b.w + b.s))
        .attr("y", b.h / 2)
        .attr("dy", "0.35em")
        .attr("text-anchor", "middle")
        .text(percentageString);

    // Make the breadcrumb trail visible, if it's hidden.
    d3.select("#trail")
        .style("visibility", "");
  }

  // Take a 2-column CSV and transform it into a hierarchical structure suitable
  // for a partition layout. The first column is a sequence of step names, from
  // root to leaf, separated by hyphens. The second column is a count of how
  // often that sequence occurred.

  function buildHierarchy(csv) {
    var root = {"name": "root", "children": []};
    for (var i = 0; i < csv.length; i++) {
      var sequence = csv[i][0];
      var size = +csv[i][1];
      if (isNaN(size)) { // e.g. if this is a header row
        continue;
      }
      var parts = sequence.split("-");
      var currentNode = root;
      for (var j = 0; j < parts.length; j++) {
        var children = currentNode["children"];
        var nodeName = parts[j];
        var childNode;
        if (j + 1 < parts.length) {
     // Not yet at the end of the sequence; move down the tree.
    var foundChild = false;
    for (var k = 0; k < children.length; k++) {
      if (children[k]["name"] == nodeName) {
        childNode = children[k];
        foundChild = true;
        break;
      }
    }
    // If we don't already have a child node for this branch, create it.
    if (!foundChild) {
      childNode = {"name": nodeName, "children": []};
      children.push(childNode);
    }
    currentNode = childNode;
        } else {
    // Reached the end of the sequence; create a leaf node.
    childNode = {"name": nodeName, "size": size, "children" : []};
    children.push(childNode);
        }
      }
    }
    return root;
  };
  </script>
</html>