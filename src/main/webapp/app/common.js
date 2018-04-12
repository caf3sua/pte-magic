// code style: https://github.com/johnpapa/angular-styleguide 
// Create by: Nam, Nguyen Hoai - ITSOL.vn

function getQuestionGroup(selQuestion) {
	var i = selQuestion.indexOf("_");
	var type = selQuestion.substring(0, i);
	console.log(type);
	return type;
}

function hightlight(activeSpan) {
    if($(activeSpan).css('color') == "rgb(0, 0, 0)"){
    	$(activeSpan).css('color','red');
    	$(activeSpan).addClass("hightlight");
    }else{
    	$(activeSpan).css('color','black');
    	$(activeSpan).removeClass("hightlight");
    }
}