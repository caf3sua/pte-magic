// code style: https://github.com/johnpapa/angular-styleguide
// Create by: Nam, Nguyen Hoai - ITSOL.vn

function getQuestionGroup(selQuestion) {
	var i = selQuestion.indexOf("_");
	var type = selQuestion.substring(0, i);
	console.log(type);
	return type;
}
var checkColor = false;
function hightlight(activeSpan) {
    if(checkColor == false){
    	$(activeSpan).css('color','#000');
        $(activeSpan).css('background','#f9db01');
        $(activeSpan).css('padding','2px 5px');
        $(activeSpan).css('border-radius','4px');
    	$(activeSpan).addClass("hightlight");
        checkColor = true;
    }else{
    	$(activeSpan).css('color','black');
        $(activeSpan).css('background','#fff');
        $(activeSpan).css('padding','0px 0px');
        $(activeSpan).css('border-radius','0px');
    	$(activeSpan).removeClass("hightlight");
        checkColor == false
    }
}
