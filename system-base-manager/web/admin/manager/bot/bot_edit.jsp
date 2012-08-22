<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title></title>
		<%@ include file="/include/taglibs.jsp"%>
		<%@ include file="/include/jquery.jsp"%>
		<script src="${cxp }/appjs/ajaxform.common.js"></script>
	</head>
	<body>

			<form  id="myForm" name="myForm" action="weburlAction!save.action" method="post">
				<div class="buttons">
					<input type="submit" name="subOk" value="保存" class="button" />
					<input type="button" name="backOk" value="关闭" class="button"
						onclick="window.parent.parent.closeWindow()" />
				
				</div>
				<p id="errorMsg" style="color: red">
				&nbsp;
			</p>
				<table class="table" >

					<tr>
						<td width="161" bgcolor="#EEF2F2" class="leftAndTop">
							网站名称：
						</td>
						<td>
						
							<input type="text" name="webUrl.siteName"
								dataType="Require" msg="网站名称不能为空"  value="<s:property value="webUrl.siteName"/>" style="width:90%"/>
						</td>
					</tr>
					<tr>
						<td bgcolor="#EEF2F2" class="leftAndTop">
							网站链接：
						</td>
						<td>
							<input type="text" name="webUrl.url" 
								dataType="Require" msg="网站链接不能为空"  value="<s:property value="webUrl.url"/>" style="width:90%" />
						</td>
					</tr>
					<tr>
						<td bgcolor="#EEF2F2" class="leftAndTop">
							网站英文名称：
						</td>
						<td>
							<input type="text" name="webUrl.enName" 
								 value="<s:property value="webUrl.enName"/>" style="width:90%" />
						</td>
					</tr>
					<tr>
						<td bgcolor="#EEF2F2" class="leftAndTop">
							线程数：
						</td>
						<td>
							<input type="text" name="webUrl.threadNum" 
								dataType="Number" msg="线程数只能为数字"  value="<s:property value="webUrl.threadNum"/>" style="width:90%" />
						</td>
					</tr>
					<tr>
						<td bgcolor="#EEF2F2" class="leftAndTop">
							启用状态：
						</td>
						<td><select id="status" name="webUrl.status" dataType="Require" msg="启用状态不能为空"  >
							<option value="0" <s:if test="webUrl.status==0">selected</s:if> >停止</option>
							<option value="1" <s:if test="webUrl.status==1">selected</s:if>>启动</option>
							</select>
							
						</td>
					</tr>
						<tr>
						<td bgcolor="#EEF2F2" class="leftAndTop">
							抓取图片宽度：
						</td>
						<td>
							<input type="text" name="webUrl.width" 
								dataType="Number" msg="抓取图片只能为数字"  value="<s:property value="webUrl.width"/>" style="width:90%" />
						</td>
					</tr>
					<tr>
						<td bgcolor="#EEF2F2" class="leftAndTop">
							抓取图片高度：
						</td>
						<td>
							<input type="text" name="webUrl.height" 
								dataType="Number" msg="抓取图片高度只能为数字"  value="<s:property value="webUrl.height"/>" style="width:90%" />
						</td>
					</tr>
					<tr>
						<td bgcolor="#EEF2F2" class="leftAndTop">
							抓取模式：
						</td>
							<td><input type="radio" name="webUrl.pattern"   <s:if test="webUrl.pattern eq \"normal\"">checked</s:if><s:elseif test="webUrl.pattern eq \"ajax\""></s:elseif><s:else>checked</s:else>  value="normal"   
								  />普通 <input type="radio" name="webUrl.pattern"   <s:if test="webUrl.pattern eq \"ajax\"">checked</s:if><s:elseif test="webUrl.pattern eq \"normal\""></s:elseif><s:else></s:else> value="ajax"   
								  />Ajax
						</td>
					</tr>
						<tr>
						<td bgcolor="#EEF2F2" class="leftAndTop">
							抓取规则：
						</td>
						<td>
							<textarea 
										name="webUrl.rule" class="MultiEditBox"
										style="width: 90%; height: 40px"><s:property value="webUrl.rule" /></textarea>
						</td>
					</tr>
					<tr>
								<td width="20%" class="lefttd">分类：</td>
								<td><select name="webUrl.type" id="webtype"
									dataType="Require" msg="分类不能为空" style="width: 150px"></select>
								</td>
								
							</tr>

				</table>
				<input type="hidden" value="<s:property value="webUrl.id"/>" name="webUrl.id"/ >
			</form>
		
		<script>


			Validator.createCheckForm(document.forms[0]);
			/**
			回调
			**/
			function processJson(data) {
			if(data.status == SUCCESS){
				//图层解锁
			window.parent.parent.jAlert(data.message, "系统提示");
				$("#myForm").unblock();
				window.parent.frames["0"].location.reload();
				window.parent.parent.closeWindow();
			}else{
				window.parent.frames["0"].location.reload();
				//图层解锁
				$("#myForm").unblock();
			}
			}
			$("#webtype")
			.dictionary(
					{
						url : '${cxp }/manager/dictionary/dictionaryAction!getDictionarysByParentId.action',
						data : {
							id : '301'
						},
						defaultOp : [ {
							name : '请选择信息',
							value : ''
						} ],
						defaultValue : '${webUrl.type}',
						c1 : "name"//列名1
					});

		</script>
	</body>
</html>

