$.ajaxSetup({
    contentType : "application/x-www-form-urlencoded;charset=UTF-8",
    complete : function(XMLHttpRequest, textStatus) {
    	f_noPopeDomHandle(XMLHttpRequest, textStatus);
    }
});
var f_noPopeDomHandle = function (XMLHttpRequest, textStatus){
    var sessionstatus = XMLHttpRequest.getResponseHeader("sessionstatus");
    var uri = XMLHttpRequest.getResponseHeader("nourl");
    if (sessionstatus == "noPopedom") {
    	console.log("未经授权: "+uri);
    	alert("未授权,请联系管理员!");
    } else if (sessionstatus == "timeout") {        	
    	// 如果超时就处理 ，指定要跳转的页面
    	window.location.replace("/");
    } else {
    	
    }
};

var f_createModelObj = function (row, modelName) {
	var newRow = {};
	for ( var p in row) {
		newRow[modelName+"." + p] = row[p];
	}
	return newRow;
}

var f_cloneObj = function (obj) {
	var newObj = {};
	if(obj instanceof Array) {
		newObj =[];
	}
	for(var key in obj) {
		var val = obj[key];
		//newObj[Key] = typeof val === 'object' ? arguments.callee(val) :val ;//arguments.callee在哪个函数中允许,他就代表那个函数,一般用在匿名函数中
		newObj[key] = typeof val === 'object' ? cloneObj(val):val;
	}
	return newObj;
}


/**
 * 默认加载，控制权限按钮的显示
 */
var popedomDataCache = null;
$.post('/popedom/permissionButtonList',function(data){
	//console.log(data)
	popedomDataCache = data;
	refreshPermssion();
},'json');

function refreshPermssion () {
	$.each(popedomDataCache, function(i, p){
		$(".my-permission").each(function(){
			//console.log($(this))
			var per = $(this).data("permisson");
		console.log(p.FORWARD_ACTION)
			if(per == p.FORWARD_ACTION){
				$(this).removeClass("my-permission");
			}
		});
	});
}


//var f_formatForm = function (formTarget) {
//	var serializeObj = {};
//	$.each(formTarget.serializeArray(), function (i, s){
//		if(s.value != null && s.value != 0) {
//			serializeObj[s.name] = s.value;
//		}
//	});
//	return serializeObj;
//}
//	