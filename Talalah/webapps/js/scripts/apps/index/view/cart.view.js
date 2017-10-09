/**
 * 
 */
define(function(require,exports,module){
	
	var $ = require('jquery');
	var _ = require('underscore');
	var Backbone = require('backbone');
	var Handlebars = require('handlebars');
	
	var init = require('init');
	var event = require('product.event');
	var talalah = init.init();
	
	var CartTempl	=	require('text!cartTempl');
	var CartProductTempl	= require('text!cartProductTempl');
	
	var $cartTempl 			= $(CartTempl).find('#cart-panel').html();
	var $cartTravelTempl	= $(CartTempl).find('#cartTravel').html();
	var $cartItemTempl		= $(CartTempl).find('#cartItem').html();
	
	var $cartProductTemp = $(CartProductTempl).find('#cartProduct').html();
	
	var $productTravelTemp = $(CartProductTempl).find("#travel-detail").html();
	var $productItemTemp = $(CartProductTempl).find('#item-detail').html();
	
	var $itemShippingProviderTemp =$(CartProductTempl).find('#itemshippingprovider').html();
	//var $cartRelatedPrdTemp = $(CartProductTempl).find('#relatedPrd').html();
	//var $cartCommentTemp = $(CartProductTempl).find('#comment').html();
	
	var content = talalah.com.client.app.page.content.merchant;
	
	var CartView= Backbone.View.extend({
		className : 'container',
		template : Handlebars.compile($cartTempl),
		render : function(){
			var data = this.model.toJSON();
			return this.$el.html(this.template(data));
		}
	});
	
	var CartTravelView = Backbone.View.extend({
		className : 'col-xs-12 col-sm-12 col-lg-12 space cartproduct',
		template : Handlebars.compile($cartTravelTempl),
		render : function(){
			var data = this.model.toJSON();
			this.$el.html(this.template(data));
			var mcc = data.product.merchant.merchantCode.mcc;
			var mcId = data.product.merchant.mcId;
			var id   = data.product.id;
			var img	= data.product.img;
			this.$el.find('img').attr('src',content+'/'+mcc+"/"+mcId+"/"+id+"/"+img); 
			return this.$el;
		},
		events : {
			'click #delete' 	: 'doDelete',
			'click #checkout'   : 'doCheckOut'
		},
		doDelete : function(e){
			e.preventDefault();
			event.travelProduct.deleteTravel(this);
		},
		doCheckOut : function(e){
			e.preventDefault();
			alert('Check Out travel');
		}
	});
	
	var CartItemView = Backbone.View.extend({
		className:'col-xs-12 col-sm-12 col-lg-12 space cartproduct',
		template : Handlebars.compile($cartItemTempl),
		render : function(){
			var data = this.model.toJSON();
			this.$el.html(this.template(data));
			var mcc = data.product.merchant.merchantCode.mcc;
			var mcId = data.product.merchant.mcId;
			var id   = data.product.id;
			var img	= data.product.img;
			this.$el.find('img').attr('src',content+'/'+mcc+"/"+mcId+"/"+id+"/"+img);
			return this.$el;
		},
		events : {
			'click #delete'   : 'doDelete',
			'click #checkout' : 'doCheckOut'
		},
		doDelete : function(e){
			e.preventDefault();
			event.itemProduct.deleteItem(this);
		},
		doCheckOut : function(e){
			e.preventDefault();
			// TODO redirect to paypal
		}
	});
	
	var CartProductViews = Backbone.View.extend({
		render : function(){
			_.each(this.collection.models,function(item){
				var data = item.toJSON();
				var mcc = data.product.merchant.merchantCode.mcc;
				if(mcc==='4722'){
					var cartTravelView = new CartTravelView({model:item});
					this.$el.append(cartTravelView.render());
				}
				else{
					var cartItemView = new CartItemView({model:item});
					this.$el.append(cartItemView.render());
				}
				
			},this);
			
			return this.$el;
		}
	});
	
	
	var CartProductView = Backbone.View.extend({
		initialize : function(){
			this.page = 1;
			/*_.bindAll(this, "render");
		    this.model.bind('change', this.render);*/
		},
		className:'container',
		template : Handlebars.compile($cartProductTemp),
		render : function(){
			var data = this.model.toJSON();
			this.$el.html(this.template(data));
			return this.$el;
		},
		events:{
			'click #loadmore'	: 'doLoadMore',
			'click #checkout'	: 'doCheckOut'
		},
		doLoadMore : function(e){
			e.preventDefault();
			alert('Hello work')
			/*customerCartController.doLoadMoreComments(this);
			this.page++;*/
		},
		doCheckOut : function(e){
			e.preventDefault();
			//.checkOutProduct(this);
			alert('check out');
		}
	});
	
	var ItemShippingProviderView = Backbone.View.extend({
		className : 'col-xs-12 col-sm-12 col-lg-12',
		template : Handlebars.compile($itemShippingProviderTemp),
		render:function(pvdId){
			var data = this.model.toJSON();
			var id = data.shippingProvider.id;
			this.$el.html(this.template(this.model.toJSON()));
			if(id===pvdId){
				this.$el.find('input[type=radio]').attr('checked', 'checked');
			}
			return this.$el;
		}
	}) ;
	
	var ItemShippingProviderViews = Backbone.View.extend({
		tagName:'form',
		render : function(pvdId){
			_.each(this.collection.models, function(item){
				var itemShippingProviderView = new ItemShippingProviderView({model:item});
				this.$el.append(itemShippingProviderView.render(pvdId));
			},this);
			return this.$el;
		}
	});
	
	var ProductItemDetailView = Backbone.View.extend({
		className : 'col-lg-12 col-xs-12 col-sm-12',
		template : Handlebars.compile($productItemTemp),
		render : function(){
			var data = this.model.toJSON();
			this.$el.html(this.template(data));
			return this.$el;
		}
	});
	
	var ProductTravelDetailView = Backbone.View.extend({
		className : 'col-lg-12 col-xs-12 col-sm-12',
		template : Handlebars.compile($productTravelTemp),
		render : function(){
			var data = this.model.toJSON();
			this.$el.html(this.template(data));
			return this.$el;
		}
	});
	
	return {
		CartView 		 : CartView,
		CartTravelView 	 : CartTravelView,
		CartItemView	 : CartItemView,
		CartProductViews : CartProductViews,
		CartProductView  : CartProductView,
		ItemShippingProviderView  : ItemShippingProviderView,
		ItemShippingProviderViews : ItemShippingProviderViews,
		ProductItemDetailView	  : ProductItemDetailView,
		ProductTravelDetailView	  : ProductTravelDetailView
	};
	
});
