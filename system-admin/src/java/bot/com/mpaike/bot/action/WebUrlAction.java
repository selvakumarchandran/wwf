package com.mpaike.bot.action;

import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.mpaike.bot.model.WebUrl;
import com.mpaike.util.app.BaseAction;

public class WebUrlAction extends BaseAction{


	/**
	 * 
	 */
	private static final long serialVersionUID = 2884893102617198833L;
	private WebUrl webUrl;
	private Long id;
	private boolean restart;
	/**
	 * 查询列表
	 */
	public void list(){
		List<WebUrl> datas = this.getWebUrlService().find(this.pageInfo,this.getOrderby());
		this.printPageList(datas);
	}
	/**
	 * @author Chen.H
	 * @serialData
	 * 
	 */
	public void remove(){
		
	}
	/**
	 * @author Chen.H
	 * @serialData
	 * 
	 */
	public String edit(){
		webUrl = this.getWebUrlService().find(id);
		return "edit";
	}

	public void save(){
		String result = "保存成功！";
		if(webUrl.getId()!=null){
			result = "修改成功！";
		}
		this.getWebUrlService().save(webUrl);
		super.printSuccessJson(result);
	}

	public void start(){
		if(this.getWebUrlService().startWebSpider(id,restart)){
			printSuccessJson("启动成功！");
		}else{
			printSuccessJson("启动失败！");
		}
	}

	public void stop(){
		if(this.getWebUrlService().stopWebSpider(id)){
			printSuccessJson("停止成功！");
		}else{
			printSuccessJson("停止失败！");
		}
	}
	
	public String viewSpider(){
		webUrl = this.getWebUrlService().find(id);
		return "spider_log";
	}
	
	public void spiderLog(){
		printBeansJson(getWebUrlService().spiderLog(webUrl.getUrl()));
	}
	
	public WebUrl getWebUrl() {
		return webUrl;
	}
	public void setWebUrl(WebUrl webUrl) {
		this.webUrl = webUrl;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
}
