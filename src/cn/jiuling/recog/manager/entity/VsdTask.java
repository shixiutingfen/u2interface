package cn.jiuling.recog.manager.entity;

import java.util.Date;

import com.loocme.sys.annotation.database.Column;
import com.loocme.sys.annotation.database.Id;
import com.loocme.sys.annotation.database.JoinColumn;
import com.loocme.sys.annotation.database.Table;

@Table(TableName = "vsd_task")
public class VsdTask implements java.io.Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @Column(ColumnName = "id")
    private Long id;

    @Column(ColumnName = "serialnumber")
    private String serialnumber;

    @Column(ColumnName = "type")
    private String type;

    @Column(ColumnName = "status")
    private Integer status;

    @Column(ColumnName = "reserve")
    private String reserve;

    @Column(ColumnName = "slaveip")
    private String slaveip;

    @Column(ColumnName = "createtime")
    private Date createtime;

    @Column(ColumnName = "endtime")
    private Date endtime;// 结束时间

    @Column(ColumnName = "param")
    private String param;

    @Column(ColumnName = "progress")
    private Integer progress;

    @Column(ColumnName = "isvalid")
    private Integer isValid;
    
    @JoinColumn(Left = "id", Right="taskId")
    private VsdTaskRelation taskRelation;
    
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getSerialnumber()
    {
        return serialnumber;
    }

    public void setSerialnumber(String serialnumber)
    {
        this.serialnumber = serialnumber;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public String getReserve()
    {
        return reserve;
    }

    public void setReserve(String reserve)
    {
        this.reserve = reserve;
    }

    public String getSlaveip()
    {
        return slaveip;
    }

    public void setSlaveip(String slaveip)
    {
        this.slaveip = slaveip;
    }

    public Date getCreatetime()
    {
        return createtime;
    }

    public void setCreatetime(Date createtime)
    {
        this.createtime = createtime;
    }

    public Date getEndtime()
    {
        return endtime;
    }

    public void setEndtime(Date endtime)
    {
        this.endtime = endtime;
    }

    public String getParam()
    {
        return param;
    }

    public void setParam(String param)
    {
        this.param = param;
    }

    public Integer getProgress()
    {
        return progress;
    }

    public void setProgress(Integer progress)
    {
        this.progress = progress;
    }

    public Integer getIsValid()
    {
        return isValid;
    }

    public void setIsValid(Integer isValid)
    {
        this.isValid = isValid;
    }

    public VsdTaskRelation getTaskRelation()
    {
        return taskRelation;
    }

    public void setTaskRelation(VsdTaskRelation taskRelation)
    {
        this.taskRelation = taskRelation;
    }
}