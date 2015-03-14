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



