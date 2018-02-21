package cn.jiuling.recog.elasticsearch.model;

import java.util.Date;

import com.loocme.sys.annotation.database.Column;
import com.loocme.sys.annotation.database.Id;
import com.loocme.sys.annotation.database.Table;

@Table(TableName = "result_ymd")
public class ResultYmd implements java.io.Serializable
{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(ColumnName = "ymd")
    private String ymd;
    @Column(ColumnName = "createTime")
    private Date createTime = new Date();
    @Column(ColumnName = "status")
    private Integer status = 1;
    @Column(ColumnName = "dropTime")
    private Date dropTime;
    @Column(ColumnName = "objextEsCount")
    private Long objextEsCount;
    @Column(ColumnName = "vlprEsCount")
    private Long vlprEsCount;
    public ResultYmd()
    {
        super();
    }
    
    public ResultYmd(String ymd)
    {
        super();
        this.ymd = ymd;
    }

    public String getYmd()
    {
        return ymd;
    }

    public void setYmd(String ymd)
    {
        this.ymd = ymd;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Date getDropTime()
    {
        return dropTime;
    }

    public void setDropTime(Date dropTime)
    {
        this.dropTime = dropTime;
    }

	public Long getObjextEsCount() {
		return objextEsCount;
	}

	public void setObjextEsCount(Long objextEsCount) {
		this.objextEsCount = objextEsCount;
	}

	public Long getVlprEsCount() {
		return vlprEsCount;
	}

	public void setVlprEsCount(Long vlprEsCount) {
		this.vlprEsCount = vlprEsCount;
	}
    
}