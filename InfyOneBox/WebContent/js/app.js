/**
 * 
 */

var button = document.querySelector('#speechButton'),
    input = document.querySelector('#searchText'),
    element = document.querySelector('#recognition-element'),
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
    icon.style.color = "black";
	icon.setAttribute("class", "fa fa-microphone");
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
var doSearch = function(event) {
	$('#searchDiv').removeClass('verticalCenter');
	var srchTxt = $('#searchText').val();
	console.log(srchTxt);
	
	$.ajax({
		  method: "POST",
		  url: "oneinfysearch",
		  data: { srchTxt: srchTxt}
		})
	.done(function( msg ) {
		console.log("Done Handler");
		processData(msg);
	}).fail(function(err){
		console.log("Error: "+error.message);
	});//.always(processData(1));//Delete this always
	
	return false;
};

var processData = function(jsonData){
	//Remove this hardcoding of jsonData
	/*jsonData = {
			type: 'show',
			url: 'http://google.com'
	};*/
	runFunction(jsonData.type, [jsonData]);
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

