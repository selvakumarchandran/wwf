<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="/include/taglibs.jsp"%>
<%@ include file="/include/jquery.jsp"%>
<script src="${cxp }/appjs/ajaxform.common.js"></script>

	<head>
		<title>网站后台</title>

		<style type="text/css">
			table.form {
				width: 45em; 
				margin: 20px auto 0px auto;
			}
			table.form tr {
				height: 2em;
			}
		</style>
	</head>
	<body style="background-color: transparent;">
		<form id="myForm" name="myForm" action="sysUser!save.action" method="post"
			>
				
				<div class="buttons">
					<input type="submit" name="subOk" value="添加" class="button"/>
					<input type="button" name="backOk" value="关闭" class="button"
						onclick="window.parent.closeWindow()"/>
						
				</div>
				<p id="errorMsg" style="color:red">&nbsp;</p>
				<table class="table" >
					<tr>
						<td width="100px">
							用户名：
						</td>
						<td>
							<input type="text" name="sysUser.username"  dataType="Require" msg="用户名不能为空"
								class="EditBox" style="width:90%" /></td>
							
					</tr>
					
					<tr>
					<td>
							姓名：
						</td>
						<td>
							<input type="text" name="sysUser.truename" style="width:90%" dataType="Require" msg="姓名不能为空"
								 class="EditBox" />
													</td>
							
						
		
					</tr>
					
					<tr>
						<td>
							密码：
						</td>
						<td>
							<input type="password" name="sysUser.password" id="password" style="width:90%"  dataType="Require" msg="密码不能为空"
								class="EditBox" />
													</td>
							
					</tr>
					<tr>
						<td>
							重输：
						</td>
						<td>
							<input type="password" name="password2" style="width:90%" dataType="Repeat" to="sysUser.password" msg="两次输入的密码不一致"
								class="EditBox" />
												</td>
							
					</tr>
					<tr>
						<td>
							性别：
						</td>
						<td>
							<select name="sysUser.sex" class="selectButtonCss">
								<option value="男">男</option>
								<option value="女">女</option>
							</select>
									</td>
						
						
					</tr>
					<tr>
						<td>
							邮箱：
						</td>
						<td>
							<input type="text" name="sysUser.email"  style="width:90%" 
								class="EditBox" />
													</td>
						
						
					</tr>
					<tr>
						<td>
							电话：
						</td>
						<td>
							<input type="text" name="sysUser.tel" style="width:90%"  
								class="EditBox" />
						</td>
						 
						
					</tr>
					
					
					<tr>
					<td style="vertical-align: top;">
							其它：
						</td>
						<td>
							<textarea name="sysUser.other" class="MultiEditBox" style="width:90%;height:100px"></textarea>
						
										</td>
						
					</tr>					
				</table>
	
		</form>
		
		<script type="text/javascript">
			Validator.createCheckForm(document.forms[0]);
			function nameCheck(temp){//暂不用
									
					$.ajax({
										            type : "post",//使用get方法访问后台
										            url : "nameCheck.do",//要访问的后台地址
										            data : "method=nameCheck&userName="+temp,//要发送的数据
										            success : function(data){//data为返回的数据，在这里做数据绑定
										            	data = eval(data);
										           
										            	backValue=data+"";
										            	if(backValue=='exist'){
										            		alert("对不起，姓名缩写重复请重新输入!");
										            		$("#extendf5").val("");
										            		$("#extendf5").focus();
										            	}
										            	
										            }
							      				 });
			}
			
		</script>
</html>
