<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>dictionary-Helper</title>
<style type="text/css">
body {
	font-family: "宋体";
	font-size: 14px;
	color: #1B5072;
	margin: 0px;
	padding: 0px;
	background-image: url(/maxejo/images/Ejo2-bg.jpg);
	background-repeat: repeat-x;
	background-position: 0px 0px;
}
html, body, div, span, object, iframe, h1, h2, h3, h4, h5, h6, p, blockquote, pre, a, abbr, acronym, address, code, del, dfn, em, img, q, dl, dt, dd, ol, ul, li, fieldset, form, label, legend, table, caption, tbody, tfoot, thead, tr, th, td {
	margin: 0px;
	padding: 0px;
}
ul li {
	list-style-type: none;
}
.Ejo2 {
	width: 1205px;
	margin-top: 0px;
	margin-right: auto;
	margin-bottom: 0px;
	margin-left: auto;
}
.EjoBox {
	background-image: url(/maxejo/images/MAXEJO-2bg.jpg);
	background-repeat: no-repeat;
	background-position: 0px 0px;
	height: 590px;
	width: 911px;
	padding-top: 52px;
	padding-right: 147px;
	padding-left: 147px;
}
.Etitle {
	font-weight: bold;
	color: #ffffff;
	background-image: url(/maxejo/images/table-thead-bg.jpg);
	background-repeat: repeat-x;
	background-position: 0px 0px;
	height: 31px;
	width: 901px;
	line-height: 31px;
	padding-left: 10px;
}
.Ebox {
	position: relative;
}
.Eboxleft {
	height: 542px;
	width: 210px;
	border-right-width: 1px;
	border-right-style: solid;
	border-right-color: #C4CDD6;
	background-color: #D4EBF9;
	position: absolute;
	padding-top: 18px;
	padding-left: 18px;
}
.Eboxleft ul li {}
.Eboxleft ul li a {
	font-weight: bold;
	color: #1D4F74;
	line-height: 26px;
	text-decoration: none;
}
.Eboxleft ul li a:hover {
	color: #ffffff;
	background-color: #1D4F74;
}
.Eboxright {
	height: 558px;
	width: 678px;
	position: absolute;
	left: 229px;
	background-color: #ffffff;
	padding-top: 2px;
	padding-right: 2px;
	padding-left: 2px;
}
.EjoD .EjoDtitle {
	background-color: #D4EBF9;
	height: 27px;
	width: 668px;
	line-height: 27px;
	padding-left: 10px;
	font-weight: bold;
	position: relative;
}
.EjoD p {
	font-size: 12px;
	font-weight: normal;
	color: #000;
	padding: 20px;
	line-height: 20px;
}
.EjoD .EjoDtitle a {
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 10px;
	font-weight: bold;
	color: #04984A;
	text-decoration: none;
	background-image: url(/maxejo/images/arrow-top.gif);
	background-repeat: no-repeat;
	background-position: 0px 11px;
	padding-left: 12px;
	position: absolute;
	left: 630px;
	*top: -1px;
}
</style>
		<%@ include file="/include/taglibs.jsp"%>
		<%@ include file="/include/jquery.jsp"%>
		<script src="${cxp }/appjs/plugin/dictionary/jquery.dictionary.js"></script>

	</head>
<body>
<div class="Ejo2">
<div class="EjoBox">
  <div class="Etitle">基于数据字典写的级联菜单，支持N级</div>
  <div class="Ebox">
  
<br/>
	<select name="s1" id="s1">
	<option value="">请选择</option>
	</select>
	<select name="s2" id="s2">
	<option value="">请选择</option>
	</select>
	<select name="s3" id="s3">
	<option value="">请选择</option>
	</select>
	
	<script >
	$("#s1").dictionary({
	url:'${cxp }/manager/dictionary/dictionaryAction!getDictionarysByParentId.action',
	   data:{id:'1'},
	   
	   
		  callback:function(value){
		     $("#s2").dictionary({
				url:'${cxp }/manager/dictionary/dictionaryAction!getDictionarysByParentId.action',
				   data:{id:value},
				   callback:function(value){
				  	     $("#s3").dictionary({
							url:'${cxp }/manager/dictionary/dictionaryAction!getDictionarysByParentId.action',
							   data:{id:value},
							   callback:function(value){
							   		
							   },
							   defaultOp:[{name:'请选择信息',value:''}]
							    
							});
				   },
				   defaultOp:[{name:'请选择信息',value:''}]
				    
				});
		   },
		   defaultOp:[{name:'请选择信息',value:''}]
	    
	});
	
	
	
	</script>

</div>
</div>
<div class="yrepeat"></div>
<div class="Efooter"></div>
</div>
</body>
</html>

