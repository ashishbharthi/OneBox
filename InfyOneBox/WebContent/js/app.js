/**
 * 
 */

var button = document.querySelector('#speechButton'),
    input = document.querySelector('#searchText'),
    element = document.querySelector('#recognition-element'),
    talker = document.querySelector('#oneInfyTalker'),
	icon = document.getElementById('speechButtonIcon');
	
button.addEventListener('click', function(e) {
	input.value = '';
	icon.style.color = "red";
	icon.setAttribute("class", "fa fa-microphone animated infinite tada");
    e.preventDefault();
    element.text = '';
    element.start();
});

element.addEventListener('result', function(e) {
	
    input.value = e.detail.result;
    element.stop();
    var elem = document.getElementById("voiceMode");
    elem.value = "true";
    icon.style.color = "black";
	icon.setAttribute("class", "fa fa-microphone");
	
	$("#searchButton").click();
});

$(function(){
	$('#searchText').focus();
	
	$('#searchText').keyup(function(e){
		showHideCrossButton();
	});
	
	$('#searchText').change(function(e){
		showHideCrossButton();
	});
	
	
	
	function showHideCrossButton(){
		if($('#searchText').val().length > 0){
			$('#crossButton').show();
		}else{
			$('#crossButton').hide();
		}
	}
});

//$('#crossButton').click(function(event){
//	event.preventDefault();
//	event.stopPropagation();
//	$('#searchText')[0].val = '';
//	$('#resultList')[0].empty();
//});

var infyOneResp;
var doSearch = function(event) {
	//event.preventDefault();
	//event.stopPropagation();
	$('#searchDiv').removeClass('verticalCenter');
	$('#resultList').empty();
	var srchTxt = $('#searchText').val();
	var voiceMode = $('#voiceMode').val();
	
	$.ajax({
		  method: "POST",
		  url: "oneinfysearch",
		  data: { 
			  srchTxt: srchTxt,
			  voice: voiceMode		
			  }
		})
	.done(function( msg ) {
		console.log("Done Handler");
		infyOneResp = msg;
		processResponse();
	}).fail(function(err){
		console.log("Error: ",err);
	});//.always(processResponse(1));//Delete this always
	
	return false;
};


var processResponse = function(){
	
	if(infyOneResp.say === undefined) {
		console.log(infyOneResp);
		infyOneResp.say = "Nothing to say yet.";
	}

	talker.setAttribute('text', infyOneResp.say);
	setTimeout(function(){talker.speak();}, 500);
};

talker.addEventListener('end', function(e) {
	console.log("Taking Action...", infyOneResp);
	if(!infyOneResp){
		
	} else if(infyOneResp.results.length === 1) {
		runFunction(infyOneResp.results[0].type, [infyOneResp.results[0]]);
	} else {
		var html ="";
		var results = infyOneResp.results;
		for(var i=0; i<results.length; i++){
			html = html + "<li class=\"list-group-item\"><a href='" + results[i].url + "'>" + results[i].name + "</a></li>";
		}
		$('#resultList').append(html);
	}
});

function runFunction(name, arguments)
{
    var fn = window[name];
    if(typeof fn !== 'function')
        return;

    fn.apply(window, arguments);
}

/* One Infy Handlers */

/* Opens msg.url in new tab */
function show(msg)
{
	window.open(msg.url,'_target');
}

/* Asks user for a date for apply leave workflow */
function apply(msg){
	if(msg.status === "incomplete"){
		$("#oneInfyTalkerText").html(msg.nextUserInputName);
		if(msg.nextUserInputType === "date") {
			var operator = " ";
			var options = {
					autoClose: true, 
					toggleActive: true
				};
			if(msg.nextUserInputName === "To Date"){
				operator = " to "
				options.startDate = new Date();
			} else if(msg.nextUserInputName === "From Date"){
				//options.startDate = new Date();
			}
			$('#datepicker').datepicker(options);
			$('#datepicker').show();
			$("#datepicker").on("changeDate", function(event) {
			    $("#my_hidden_input").val(
			        $("#datepicker").datepicker('getFormattedDate')
			     );
			    
			    $("#oneInfyTalkerText").empty();
			    input.value = input.value + operator + $("#datepicker").datepicker('getFormattedDate');
			    
			    $("#datepicker").hide();
			    $("#searchButton").click();
			});
		}
	} else if (msg.status === "confirm"){
		$("#confirmBoxContainer").addClass("show");
		$("#oneInfyTalkerText").html(msg.statusMessage);
		
		cancelCircle = new ProgressBar.Circle('#confirmBoxContainer', {
		    color: '#FCB03C',
		    strokeWidth: 2.1,
		    trailWidth: 1,
		    duration: 3000,
		    fill: "rgba(0, 114, 206, 0.5)",
		    text: {
		        value: 'Cancel'
		    },
		    step: function(state, bar) {
		        //bar.setText((bar.value() * 100).toFixed(0));
		    }
		});

		cancelCircle.animate(1, function(){
			cancelCircle.destroy();
			$("#oneInfyTalkerText").empty();
			$("#confirmBoxContainer").removeClass("show");
			input.value = msg.statusMessage;
			$("#searchButton").click();
		});
	} else if (msg.status === "complete") {
		input.value = "";
	}
}
$("#confirmBoxContainer").click(function(){
	cancelCircle.stop();
	cancelCircle.destroy();
	$("#oneInfyTalkerText").empty();
	$("#confirmBoxContainer").removeClass("show");
	input.value = "";
});
