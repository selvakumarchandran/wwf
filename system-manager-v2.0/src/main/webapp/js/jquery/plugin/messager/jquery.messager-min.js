(function(A){this.version="@1.1";this.layer={"width":180,"height":100};this.title="\u4fe1\u606f\u63d0\u793a";this.time=30*1000;this.anims={"type":"slide","speed":600};this.inits=function(C,B){if($("#message").is("div")){return }$(document.body).prepend("<div id=\"message\" style=\"border:#b9c9ef 1px solid;z-index:100;width:"+this.layer.width+"px;height:"+this.layer.height+"px;position:fixed; display:none;background-image:url(/images/bg-1.gif); bottom:0; right:0;\"><div style=\"border:1px solid #fff;border-bottom:none;width:100%;height:25px;font-size:12px;overflow:hidden;color:#1f336b;\"><span id=\"message_close\" style=\"float:right;padding:2px 0 10px 0;width:18px;line-height:auto;color:#1D96BE;font-size:16px;font-weight:bold;text-align:center;cursor:pointer;overflow:hidden;\">\u00d7</span><div style=\"padding:3px 0 9px 5px;width:100px;line-height:18px;text-align:left;overflow:hidden;\">"+C+"</div><div style=\"clear:both;\"></div></div> <div style=\"padding-bottom:5px;border:1px solid #fff;border-top:none;width:100%;height:auto;font-size:12px;\"><div id=\"message_content\" style=\"margin:0 0px 0 0px;border:#b9c9ef 0px solid;padding:0px 2 2px 2px;font-size:12px;width:"+this.layer.width+"px;height:"+(this.layer.height-28)+"px;color:#1f336b;text-align:left;overflow:hidden;\">"+B+"</div></div></div>")};this.show=function(D,C,B){if($("#message").is("div")){return }if(D==0||!D){D=this.title}this.inits(D,C);if(B){this.time=B}switch(this.anims.type){case"slide":$("#message").slideDown(this.anims.speed);break;case"fade":$("#message").fadeIn(this.anims.speed);break;case"show":$("#message").show(this.anims.speed);break;default:$("#message").slideDown(this.anims.speed);break}$("#message_close").click(function(){setTimeout("this.close()",1)});this.rmmessage(this.time)};this.lays=function(C,B){if($("#message").is("div")){return }if(C!=0&&C){this.layer.width=C}if(B!=0&&B){this.layer.height=B}};this.anim=function(B,C){if($("#message").is("div")){return }if(B!=0&&B){this.anims.type=B}if(C!=0&&C){switch(C){case"slow":break;case"fast":this.anims.speed=200;break;case"normal":this.anims.speed=400;break;default:this.anims.speed=C}}};this.rmmessage=function(B){setTimeout("this.close()",B)};this.close=function(){switch(this.anims.type){case"slide":$("#message").slideUp(this.anims.speed);break;case"fade":$("#message").fadeOut(this.anims.speed);break;case"show":$("#message").hide(this.anims.speed);break;default:$("#message").slideUp(this.anims.speed);break}setTimeout("$(\"#message\").remove();",this.anims.speed);this.original()};this.original=function(){this.layer={"width":200,"height":100};this.title="\u4fe1\u606f\u63d0\u793a";this.time=4000;this.anims={"type":"slide","speed":600}};A.messager=this;return A})(jQuery)