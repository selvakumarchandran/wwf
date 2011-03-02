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
	fields : [makeFieldsArray('id', 'code', 'describe')]
};

var colsConfig1 = [
		{ id : 'id'  , header : "id" , width : 50 , 	editable:false ,  
		//editor: { type :'text' ,validRule : 'R,integer' },
		// checkBox列
		isCheckColumn : true
		},
	    displayColumnByRenderer('code', "权限代码", 145),
		displayColumnByRenderer('describe', "权限代码描述", 200) 
];
var dsConfig2 = {
	uniqueField : 'id',
	fields : [makeFieldsArray('id', 'code', 'describe')]
};

var colsConfig2 = [
		{ id : 'id'  , header : "id" , width : 50 , 	editable:false ,  

		isCheckColumn : true
		},
	    displayColumnByRenderer('code', "权限代码", 145),
		displayColumnByRenderer('describe', "权限代码描述", 200) 
];
var exportURL="";
var loadURL1 = "sysRoleAction.do?method=getNotCheckPopedoms";
var exportURL1="sysRoleAction.do?method=getNotCheckPopedoms";
var mygrid1 = createGrid( loadURL1, dsConfig1,
		colsConfig1, "grid1", "grid1_container", 10, exportURL1, "导出");

var exportURL="";
var loadURL2 = "sysRoleAction.do?method=getCheckPopedoms";
var exportURL2="sysRoleAction.do?method=getCheckPopedoms";
var mygrid2 = createGrid( loadURL2, dsConfig2,
colsConfig2, "grid2", "grid2_container", 10, exportURL2, "导出");
		
//查询
function query() {

//	var isDelete = getCheckedValues("query_isDelete");
//	var typeEnum = getCheckedValues("query_type");

	var param = {
		id :roleId,
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
		method:"addRolePopedoms",
		cs:ids,
		id:roleId
	}
	doPost("sysRoleAction.do", param, function(data) {
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
		method:"delRolePopedoms",
		cs:ids,
		id:roleId
	}
	doPost("sysRoleAction.do", param, function(data) {
				if (data.status) {
					query();
					window.parent.parent.jAlert(data.message, "系统提示");
				}else{
					window.parent.parent.jAlert(data.message, "系统提示");
				}
		});

}

