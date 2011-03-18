package cn.com.icore.sys.model;

import java.util.Date;

import cn.com.icore.util.dao.IBeanPrimaryKey;



/**
 * AbstractCpUserLog generated by MyEclipse Persistence Tools
 */
@SuppressWarnings("unchecked")
public class SystemLog implements IBeanPrimaryKey, java.io.Serializable {

	/**
	 * @author 陈海峰
	 * @createDate 2010-12-16 下午04:01:53
	 * @description 
	 */
	private static final long serialVersionUID = 1L;
	public static final long ALL_LOG = -1;
	// static
	public static final long TYPE_LOGIN = 1;
	public static final long TYPE_LOGOUT = 2;
	public static final long TYPE_LOGIN_ERROR = 3;
	public static final long TYPE_COMPANY_LOGIN = 4;
	public static final long TYPE__COMPANY_LOGOUT = 5;
	public static final long TYPE_COMPANY_LOGIN_ERROR = 6;
	public static final long TYPE_SYSTEM = 0;

	// Fields

	private Long id;

	private Long logtype;

	private String logtitle;

	private String logcontent;

	private Date createat;

	private Long createby;

	private String ip;
	
	private String username;

	// Constructors

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/** default constructor */
	public SystemLog() {
	}

	/** full constructor */
	public SystemLog(Long logtype, String logtitle, String logcontent,
			Date createat, Long createby, String ip) {
		this.logtype = logtype;
		this.logtitle = logtitle;
		this.logcontent = logcontent;
		this.createat = createat;
		this.createby = createby;
		this.ip = ip;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLogtype() {
		return this.logtype;
	}

	public void setLogtype(Long logtype) {
		this.logtype = logtype;
	}

	public String getLogtitle() {
		return this.logtitle;
	}

	public void setLogtitle(String logtitle) {
		this.logtitle = logtitle;
	}

	public String getLogcontent() {
		return this.logcontent;
	}

	public void setLogcontent(String logcontent) {
		this.logcontent = logcontent;
	}

	public Date getCreateat() {
		return this.createat;
	}

	public void setCreateat(Date createat) {
		this.createat = createat;
	}

	public Long getCreateby() {
		return this.createby;
	}

	public void setCreateby(Long createby) {
		this.createby = createby;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}