// code style: https://github.com/johnpapa/angular-styleguide 
// Create by: Nam, Nguyen Hoai - ITSOL.vn

function getQuestionGroup(selQuestion) {
	var i = selQuestion.indexOf("_");
	var type = selQuestion.substring(0, i);
	console.log(type);
	return type;
}