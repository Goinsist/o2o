$(function() {
    function  getContextPath() {
        return "/o2o/";
    }
	var loading = false;
	//允许分页返回的最大条数，超过此数则禁止访问后台
	var maxItems = 20;
	//默认一页返回的商品数
	var pageSize = 10;
//列出商品列表的url
	var listUrl = '/o2o/frontend/listproductsbyshop';
//默认的页码
	var pageNum = 1;
//从地址栏里获取shopId
	var shopId = getQueryString('shopId');
	var productCategoryId = '';
	var productName = '';
//获取本店铺信息以及商品类别信息列表的url
	var searchDivUrl = '/o2o/frontend/listshopdetailpageinfo?shopId='
			+ shopId;
	//渲染出店铺基本信息以及商品类别列表以供搜索
    getSearchDivData();
    //预先加载10条商品信息
	addItems(pageSize,pageNum);

	//给对话礼品的a标签赋值兑换礼品的url，2.0讲解
	$('#exchangelist').attr('href','/o2o/frontend/awardlist?shopId='+shopId);
//获取本店铺信息以及商品类别信息列表

	function getSearchDivData() {
		var url = searchDivUrl;
		$
				.getJSON(
						url,
						function(data) {
							if (data.success) {
								var shop = data.shop;
								$('#shop-cover-pic').attr('src',getContextPath()+shop.shopImg);
								$('#shop-update-time').html(
										new Date(shop.lastEditTime)
												.Format("yyyy-MM-dd"));
								$('#shop-name').html(shop.shopName);
								$('#shop-desc').html(shop.shopDesc);
								$('#shop-addr').html(shop.shopAddr);
								$('#shop-phone').html(shop.phone);
							//获取后台返回的该店铺的商品列表
								var productCategoryList = data.productCategoryList;
								var html = '';
								//遍历商品列表，生成可以点击搜索相应商品类别下的商品的a标签集
								productCategoryList
										.map(function(item, index) {
											html += '<a href="#" class="button" data-product-search-id='
													+ item.productCategoryId
													+ '>'
													+ item.productCategoryName
													+ '</a>';
										});
								//将商品类别a标签绑定到相应html里
								$('#shopdetail-button-div').html(html);
							}
						});
	}

    /**
	 * 获取分页展示的商品列表信息
     * @param pageSize
     * @param pageIndex
     */
	function addItems(pageSize, pageIndex) {
		// 生成新条目的HTML
		var url = listUrl + '?' + 'pageIndex=' + pageIndex + '&pageSize='
				+ pageSize + '&productCategoryId=' + productCategoryId
				+ '&productName=' + productName + '&shopId=' + shopId;
		//设定加载付，若还在后台取数据则不能再次访问后台，避免多次重复加载
		loading = true;
		//访问后台获取相应查询条件下的商品列表
		$.getJSON(url, function(data) {
			if (data.success) {
				//获取当前查询条件下商品的总数
				maxItems = data.count;
				var html = '';
				//遍历商品列表，拼接出卡片集合
				data.productList.map(function(item, index) {
					html += '' + '<div class="card" data-product-id='
							+ item.productId + '>'
							+ '<div class="card-header">' + item.productName
							+ '</div>' + '<div class="card-content">'
							+ '<div class="list-block media-list">' + '<ul>'
							+ '<li class="item-content">'
							+ '<div class="item-media">' + '<img src="'+getContextPath()
							+ item.imgAddr + '" width="44">' + '</div>'
							+ '<div class="item-inner">'
							+ '<div class="item-subtitle">' + item.productDesc
							+ '</div>' + '</div>' + '</li>' + '</ul>'
							+ '</div>' + '</div>' + '<div class="card-footer">'
							+ '<p class="color-gray">'
							+ new Date(item.lastEditTime).Format("yyyy-MM-dd")
							+ '更新</p>' + '<span>点击查看</span>' + '</div>'
							+ '</div>';
				});
				$('.list-div').append(html);
				//获取目前为止以显示的卡片总数，包含之前已经加载的
				var total = $('.list-div .card').length;
				//若总数达到跟按照此查询条件列出来的总数一致，则停止后台的加载
				if (total >= maxItems) {

					// 隐藏加载提示符
					$('.infinite-scroll-preloader').hide();
				}else {
                    $('.infinite-scroll-preloader').show();
				}
				//否则页码加1，继续load出新的店铺
				pageNum += 1;
				//加载结束，可以再次加载了
				loading = false;
				//刷新页码，显示新加载的店铺
				$.refreshScroller();
			}
		})
	}


//下滑屏幕自动进行分页搜索
	$(document).on('infinite', '.infinite-scroll-bottom', function() {
		if (loading)
			return;
		addItems(pageSize, pageNum);
	});
//选择新的商品类别之后，重置页码，清空原先的商品列表，按照新的类别去查询
	$('#shopdetail-button-div').on(
			'click',
			'.button',
			function(e) {
				//获取商品类别id
				productCategoryId = e.target.dataset.productSearchId;
				if (productCategoryId) {
					//若之前已选择了别的category，则移除其选定效果
					if ($(e.target).hasClass('button-fill')) {
						$(e.target).removeClass('button-fill');
						productCategoryId = '';
					} else {
						$(e.target).addClass('button-fill').siblings()
								.removeClass('button-fill');
					}
					$('.list-div').empty();
					pageNum = 1;
					addItems(pageSize, pageNum);
				}
			});
//点击商品的卡片进入该商品的详情页
	$('.list-div')
			.on(
					'click',
					'.card',
					function(e) {
						var productId = e.currentTarget.dataset.productId;
						window.location.href = '/o2o/frontend/productdetail?productId='
								+ productId;
					});
	//需要查询的商品名字发生变化后，重置页码，清空原先的商品列表，按照新的名字去查询

	$('#search').on('change', function(e) {
		productName = e.target.value;
		$('.list-div').empty();
		pageNum = 1;
		addItems(pageSize, pageNum);
	});

	$('#me').click(function() {
		$.openPanel('#panel-left-demo');
	});
	$.init();
});
