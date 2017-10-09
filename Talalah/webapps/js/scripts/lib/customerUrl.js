/**
 * 
 */
define(function(require,exports,module){
	
	var PrototypeUrl = require('prototypeUrl');
	
	/* customer module */
	
	var $Customer = function(){
		var that = new PrototypeUrl('/customer');
		return that;
	}
	
	var $CustomerExtend = function(){
		var that = new PrototypeUrl('/customerExtend');
		return that;
	}
	
	var $Comment = function(){
		var that = new PrototypeUrl('/comment');
		return that;
	}
	
	var $CartProduct = function(){
		var that = new PrototypeUrl('/cartProduct');
		that.CartItem = new PrototypeUrl('/cartProduct/item');
		that.CartTravel = new PrototypeUrl('/cartProduct/travel');
		return that;
	}
	
	var $Cart = function(){
		var that = new PrototypeUrl('/cart');
		return that;
	}
	
	return {
		Customer		:	new $Customer(),
		CustomerExtend	: 	new $CustomerExtend(),
		Comment			:	new $Comment(),
		CartProduct		:	new $CartProduct(),
		Car				:	new $Cart()
	}
	
});