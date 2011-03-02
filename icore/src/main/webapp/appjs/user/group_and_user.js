/**
 * 取得对象,通过id方式
 * 
 * @param {}
 *            id
 * @return {}
 */
function getObject(id) {
	var objDiv = document.getElementById(id);
	return objDiv;
}

var dsConfig1 = {
	uniqueField : 'id',
	fields : [makeFieldsArray('id', 'username', 'truename', 'sex', 'email', 'tel', 'other', 'regtime')]
};

var colsConfig1 = [
		{ id : 'id'  , header : "id" , width : 50 , 	editable:false ,  
		//editor: { type :'text' ,validRule : 'R,integer' },
		// checkBox列
		isCheckColumn : true
		},
	    displayColumnByRenderer('username', "用户名", 80),
		displayColumnByRenderer('truename', "姓名", 80),
		displayColumnByRenderer('sex', "性别", 40, GT.Grid.mappingRenderer( {'0': '未知' ,'1':'男', '2':'女'})),
		displayColumnByRenderer('email', "邮箱", 140)
];
var dsConfig2 = {
	uniqueField : 'id',
	fields : [makeFieldsArray('id', 'code', 'describe')]
};

var colsConfig2 = [
		{ id : 'id'  , header : "id" , width : 50 , 	editable:false ,  

		isCheckColumn : true
		},
	    displayColumnByRenderer('username', "用户名", 80),
		displayColumnByRenderer('truename', "姓名", 80),
		displayColumnByRenderer('sex', "性别", 40, GT.Grid.mappingRenderer( {'0': '未知' ,'1':'男', '2':'女'})),
		displayColumnByRenderer('email', "邮箱", 140)
];
var exportURL="";
var loadURL1 = "sysGroupAction.do?method=getNotCheckUsers";
var exportURL1="sysGroupAction.do?method=getNotCheckUsers";
var mygrid1 = createGrid( loadURL1, dsConfig1,
		colsConfig1, "grid1", "grid1_container", 10, exportURL1, "导出");

var exportURL="";
var loadURL2 = "sysGroupAction.do?method=getCheckUsers";
var exportURL2="sysGroupAction.do?method=getCheckUsers";
var mygrid2 = createGrid( loadURL2, dsConfig2,
colsConfig2, "grid2", "grid2_container", 10, exportURL2, "导出");
		
//查询
function query() {

//	var isDelete = getCheckedValues("query_isDelete");
//	var typeEnum = getCheckedValues("query_type");

	var param = {
		groupId :groupId,
//		describe : GT.U.getValue(GT.$('describe')),
//		isDelete : "0",
//		beginDate:GT.U.getValue(GT.$('beginDate')),
//		endDate:GT.U.getValue(GT.$('endDate')),
	//	content : GT.U.getValue(GT.$('query_content')),
	//	sp : sp,
		randomid : Math.random()
	}
 	//alert(mygrid);
	// alert( "在查询数据的request中, 将会有如下参数:\n\n"+GT.toQueryString(param) );
	gridQuery(mygrid1, param);
	gridQuery(mygrid2, param);
}

// 界面中只读的时候被设置为 disable的 元素 id
var disableElementIds = new Array();
function init() {
	mygrid1.showIndexColumn=false;
	mygrid2.showIndexColumn=false;
	mygrid1.render();
	mygrid2.render();
	query();


}
/**
 * 新建
 * 
 * @return {}
 */
function showNewDialog() {
	var templateId = -1;
	return showEditDialog(templateId);

}
GT.U.onLoad( init);

function addSelect(){
	var ids = "";
	var obs  = mygrid1.getSelectedRecords();
	for(var i=0;i<obs.length;i++){
		if(ids!="")
			ids+=",";
		ids+=obs[i]["id"]
	}
	if(ids == "")
	{
		window.parent.parent.jAlert("请选择记录", "系统提示");
		return ;
	}
	var param = {
		method:"addSysUsers",
		cs:ids,
		id:groupId
	}
	// wait("body","提交中...请稍候！");
	doPost("sysGroupAction.do", param, function(data) {
	//	$("body").unblock();
				if (data.status) {
					query();
					window.parent.parent.jAlert(data.message, "系统提示");
				}else{
					window.parent.parent.jAlert(data.message, "系统提示");
				}
		});

}
function removeSelect(){
	var ids = "";
	var obs  = mygrid2.getSelectedRecords();
	for(var i=0;i<obs.length;i++){
		if(ids!="")
			ids+=",";
		ids+=obs[i]["id"]
	}
	if(ids == "")
	{
		window.parent.parent.jAlert("请选择记录", "系统提示");
		return ;
	}
	var param = {
		method:"delSysUsers",
		cs:ids,
		id:groupId
	}
	//wait("body","提交中...请稍候！");
	doPost("sysGroupAction.do", param, function(data) {
	//$("body").unblock();
				if (data.status) {
					query();
					window.parent.parent.jAlert(data.message, "系统提示");
				}else{
					window.parent.parent.jAlert(data.message, "系统提示");
				}
				
		});

}
