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

talker.addEventListener('end', function(e) {
	console.log("Taking Action...", infyOneResp);
	if(!infyOneResp){
		
	} else if(infyOneResp.results.length === 1) {
		runFunction(infyOneResp.results[0].type, [infyOneResp.results[0]]);
	} else {
		var html ="";
		var results = infyOneResp.results;
		for(var i=0; i<results.length; i++){
			html = html + "<li class=\"list-group-item\"><a href='" + results[i].url + "'>" + results[i].url + "</a></li>";
		}
		$('#resultList').append(html);
	}
});

$(function(){
	$('#searchText').focus();
	
	$('#searchText').keyup(function(e){
		showHideCrossButton();
	});
	
	$('#searchText').change(function(e){
		showHideCrossButton();
	});
	
	$('#crossButton').click(function(e){
		$('#searchText').val = '';
	});
	
	function showHideCrossButton(){
		if($('#searchText').val().length > 0){
			$('#crossButton').show();
		}else{
			$('#crossButton').hide();
		}
	}
});

var infyOneResp;
var doSearch = function(event) {
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

