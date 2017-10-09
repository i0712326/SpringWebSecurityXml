/**
 * 
 */
define(function(require,exports,module){
	
	var $ = require('jquery');
	var Backgrid = require('backgrid');
	var Model = require('model');
	var View = require('cart.view');
	var PrdView = require('product.view');
	var CommView = require('comment.view');
	var init = require('init');
	
	var talalah = init.init();
	var content = talalah.com.client.app.page.content.merchant;
	var max		= talalah.com.client.app.page.max;
	
	var CartPage = function(){
		
		this.render = function(){
			var appStorage = window.sessionStorage;
			var cart = JSON.parse(appStorage.getItem('cart'));
			var cartProducts = JSON.parse(appStorage.getItem('cartProducts'));
			
			var total = cart.total;
			var sum = cart.sum;
			
			var products = new Model.Products();
			var cartModel = new Model.Cart(cart);
			var cartView = new View.CartView({model:cartModel});
			var $ele = cartView.render();
			
			if(total>0){
				var sum = 0;
				for(var i=0;i<total;i++){
					var data = cartProducts[i];
					products.add(new Model.Product(data));
					sum += data.price;
				}
				var cartProductViews = new View.CartProductViews({collection:products});
				$ele.find('#cartProductDataGrid').append(cartProductViews.render());
			}
			else{
				$ele.find('#empty-info').show();
			}
			
			$ele.find('.badge').text(total);
			$ele.find('#loader00').hide();
			
			return $ele;
		}
	}
	
	var CartProductPage = function(cpid){
		
		this.render = function(){
			var appStorage = window.sessionStorage;
			var cart = JSON.parse(appStorage.getItem('cart'));
			var cartProducts = JSON.parse(appStorage.getItem('cartProducts'));
			var cp = null;
			var total = cart.total;
			for(var i=0;i<total;i++){
				var data = cartProducts[i];
				if(data.id===cpid){
					cp=data;
					break;
				}
			}
			
			if(cp===null)
				throw "Data Error Occured while fetching data";
			var cartProduct = new Model.CartProduct(cp);
			var cartProductView = new View.CartProductView({model:cartProduct});
			var $ele = cartProductView.render();
			
			var merchant = cp.product.merchant;
			var mcc = merchant.merchantCode.mcc;
			var mcId = merchant.mcId;
			var id = cp.product.id;
			var img = cp.product.img;
			
			$ele.find('#prdImg').attr('src', content+"/"+mcc+"/"+mcId+"/"+id+"/"+img);
			
			var item = cp.product;
			var product = new Model.Product(item);
			if(mcc==='4722'){
				var productTravelDetailView = new View.ProductTravelDetailView({model:product});
				$ele.find('#product-detail').append(productTravelDetailView.render());
				var prdId = item.id;
				var destinationColumns = [{name: "name",label: "Name", cell: "string", editable:false}];
				var destinations = new Model.Destinations();
				destinations.url = talalah.com.client.app.entity.product.travel.destination.get+"/product?prdId="+prdId+"&first=0&max=100";
				var destinationGrid = new Backgrid.Grid({
					  columns: destinationColumns,
					  collection: destinations
					});
				$ele.find('#destinationList').append(destinationGrid.render().$el);
				destinations.fetch({reset:true});
				var activityColumns = [{name: "name",label: "Name", cell: "string", editable:false}];
				var activities = new Model.Activities();
				activities.url = talalah.com.client.app.entity.product.travel.activity.get+"/product?prdId="+prdId+"&first=0&max=100";
				var activitiesGrid = new Backgrid.Grid({
					columns : activityColumns,
					collection : activities
				});
				$ele.find('#activitiesList').append(activitiesGrid.render().$el);
				activities.fetch({reset:true});
			}
			else{
				var productItemDetailView = new View.ProductItemDetailView({model:product});
				$ele.find('#product-detail').append(productItemDetailView.render());
				var prdId = item.id;
				var cpId = cp.id;
				var pvdId = cp.shipping.itemShippingProvider.itemShippingProviderId.shippingProvider.id;
				var itemShippingProviders = new Model.ItemShippingProviders();
				itemShippingProviders.url = talalah.com.client.app.entity.product.item.itemShippingProvider.get+"/product?id="+prdId;
				itemShippingProviders.fetch({
					success: function(items, resp, opts){
						var itemShippingProviderViews = new View.ItemShippingProviderViews({collection:items});
						$ele.find('#shppvdlist').append(itemShippingProviderViews.render(pvdId));
					}
				});
			}
			var products = new Model.Products();
			products.url = talalah.com.client.app.entity.product.product.get+"/mcc?mcc="+mcc+"&review=1&first=0&max=6";
			products.fetch({
				success : function(items, resp, opts){
					var relatedProductViews = new PrdView.ProductRelatedViews({collection:items});
					$ele.find('#related-products').append(relatedProductViews.render());
				}
			});
			// render related images
			
			var productImgs = new Model.ProductImgs();
			productImgs.url = talalah.com.client.app.entity.product.productImg.get+"?prdId="+prdId+"&first=0&max="+max;
			productImgs.fetch({
				success:function(items, resp, opts){
					var productImgViews = new PrdView.ProductImgViews({collection:items});
					$ele.find('#related-img').append(productImgViews.render());
				}
			});
			
			// render comments
			var comments = new Model.Comments();
			comments.url = talalah.com.client.app.entity.customer.comment.get+"?pId="+prdId+"&first=0&max="+max;
			comments.fetch({
				success:function(items,resp,opts){
					var commentViews = new CommView.CommentViews({collection:items});
					$ele.find('#comments').append(commentViews.render());
				}
			});
			
			return $ele;
			
		}
	}
	
	var endPoint = {
			showCart : function(){
				var cartPage = new CartPage();
				return cartPage.render();
			},
			showCartProduct : function(id){
				var cartProductPage = new CartProductPage(id);
				return cartProductPage.render();
			}
	};
	
	return endPoint;
	
	
});