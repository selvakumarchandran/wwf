package com.mpaike.util.app;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.mpaike.bot.service.IWebUrlService;
import com.mpaike.bot.spider.BotSpider;
import com.mpaike.core.util.page.Pagination;
import com.mpaike.util.ParamHelper;
import com.mpaike.util.pager.Pager;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("unchecked")
public class BaseAction extends ActionSupport {
	protected final Log logger = LogFactory.getLog(getClass());



	/**
	 * @author 陈海峰
	 * @createDate 2010-12-17 下午02:39:34
	 * @description
	 */
	private static final long serialVersionUID = -2713334070045301162L;
	ActionContext context = ActionContext.getContext();
	protected HttpServletRequest request = (HttpServletRequest) context
			.get(ServletActionContext.HTTP_REQUEST);
	protected HttpServletResponse response = (HttpServletResponse) context
			.get(ServletActionContext.HTTP_RESPONSE);

	public Pager getPager(HttpServletRequest request) {
		Pager pager = new Pager();
		try {

			int Pg = ParamHelper.getIntParamter(request, "Pg", 1);

			int pageSize = ParamHelper.getIntParamter(request, "pageSize",
					Integer.parseInt(getAppProps().get("pageSize").toString()));
			pager.setPage(Pg);
			pager.setPageSize(pageSize);
		} catch (Exception e) {

			logger.error(e);
		}
		return pager;
	}

	public AppProps getAppProps() {

		return (AppProps) ApplictionContext.getInstance()
				.getBean("appProps");
	}

	public void printSuccessJson(String message) {
		try {
			response.setCharacterEncoding("utf-8");
			response.getWriter().print(
					"{status:1,message:'" + ParamHelper.toJsString(message)
							+ "'}");
		} catch (IOException e) {
			logger.error(e);
		}
	}

	public void printBeansJson(Object beans) {
		try {
			JSONArray jsonArray = JSONArray.fromObject(beans);
			response.setCharacterEncoding("utf-8");
			
			response.getWriter().println(jsonArray);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void printJson(String message){
		response.setCharacterEncoding("utf-8");
		try {
			response.getWriter().print(
					 ParamHelper.toJsString(message)
							);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	public void printPageList(List beans,Pagination p){
		JsonPage jp = new JsonPage();
		jp.setRows(p.getPageNo());
		jp.setList(beans);
		printBeansJson(jp);
	}
	
	//service
	public BotSpider getBotSpider() {
		return (BotSpider) ApplictionContext.getInstance().getBean("botSpider");
	}
	
	
	public IWebUrlService getWebUrlService() {
		return (IWebUrlService) ApplictionContext.getInstance().getBean(IWebUrlService.ID_NAME);
	}
	
	public class JsonPage{
		
		private long rows;
		private List list;
		
		public long getRows() {
			return rows;
		}
		public void setRows(long rows) {
			this.rows = rows;
		}
		public List getList() {
			return list;
		}
		public void setList(List list) {
			this.list = list;
		}
		
	}
	
	
}
