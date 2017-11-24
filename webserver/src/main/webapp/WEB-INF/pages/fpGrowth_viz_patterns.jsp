<style>

circle {
  stroke-width: 18.5px;
}

line {
  stroke: #999;
}

</style>

<script src="//d3js.org/d3.v3.min.js"></script>
<script>

var processedData = ${processedData};
var json = JSON.parse(processedData);

var matchingTable = ${matchingTable};

var width = 1000, height = 800, radius = 6, active = d3.select(null);

var fill = d3.scale.category20();

//define scale
var xScale = d3.scale.linear().domain([ 0, width ]).range([ 0, width ]);
var yScale = d3.scale.linear().domain([ 0, height ]).range([ 0, height ]);

var force = d3.layout.force().gravity(0.9).charge(-400).linkDistance(600)
		.size([ width, height ]);

//use viewBox for reponsive chart 
var svg = d3.select("#d3Svg").append("svg").attr("viewBox", "0 0 " + width + " " + height).attr("preserveAspectRatio", "xMindYMid meet");

var link = svg.selectAll("line").data(json.links).enter().append("line");

var node = svg.selectAll("circle").data(json.nodes).enter()
		.append("circle").attr("r", radius - .55).style("fill",
				function(d) {
					return fill(d.group);
				}).style("stroke", function(d) {
			return d3.rgb(fill(d.group)).darker();
		}).call(force.drag).on("click", nodeClick).on("mouseout", mouseOut);

//display the name in a circle
var label = svg.selectAll("text").data(json.nodes).enter().append("text")
		.text(function(d) {
			return d.name;
		}).style("text-anchor", "middle").style("fill", "#1").style(
				"font-family", "Arial").style("font-size", 12);

//display itemsList on the right side
var value;
var html = '';
$('#itemsList tr').not(':first').not(':last').remove();
Object.keys(matchingTable).forEach(function(key) {
	value = matchingTable[key];
	 html += '<tr><td>' + key + '</td><td>' + value + '</td></tr>';
});
$('#itemsList tr').first().after(html);

force.nodes(json.nodes).links(json.links).on("tick", tick).start();

function nodeClick(d) {

	var groupValue = d.group;
	var nameValue = d.name;
	var focusRadiusValue = 1.6;
	var fillValue = "orange";
	var radiusValue = radius * focusRadiusValue;
	var groupArray = new Array();

	//Fade all the segments
	d3.selectAll("circle").style("opacity", 0.3);

	//Select click circle
	d3.selectAll("circle").filter(function(d) {
		return d.group == groupValue
	}).attr({
		fill : "orange",
		r : radius * 3
	}).style("opacity", 1);

	d3.selectAll("circle").filter(function(i) {
		return i.name == nameValue
	}).attr({
		fill : "orange",
		r : radius * 3
	}).style("opacity", 1);

	d3.selectAll("circle").filter(function(p) {
		if (p.name == nameValue) {
			groupArray.push(p.group);
		}
	});

	d3.selectAll("circle").filter(function(o) {
		for (var i = 0; i < groupArray.length; i++) {
			if (o.group == groupArray[i]) {
				return groupArray[i];
			}
		}
	}).attr({
		fill : fillValue,
		r : radiusValue
	}).style("opacity", 1);
}

function mouseOut(d, i) {
	d3.selectAll("circle").transition(1000).transition(1000).attr({
		r : radius * 0.9
	}).style("opacity", 1);
}

function tick() {
	node.attr("cx", function(d) {
		return d.x = Math.max(radius, Math.min(width - radius, d.x));
	}).attr("cy", function(d) {
		return d.y = Math.max(radius, Math.min(height - radius, d.y));
	});

	link.attr("x1", function(d) {
		return d.source.x;
	}).attr("y1", function(d) {
		return d.source.y;
	}).attr("x2", function(d) {
		return d.target.x;
	}).attr("y2", function(d) {
		return d.target.y;
	});

	label.attr("x", function(d) {
		return d.x;
	}).attr("y", function(d) {
		return d.y + 5;
	});
}
</script>