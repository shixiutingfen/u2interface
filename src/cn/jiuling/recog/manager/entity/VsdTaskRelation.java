package cn.jiuling.recog.manager.entity;

import java.sql.Date;

import com.loocme.plugin.spring.enums.SqlCompare;
import com.loocme.sys.annotation.database.Column;
import com.loocme.sys.annotation.database.JoinColumn;
import com.loocme.sys.annotation.database.Table;
import com.loocme.sys.annotation.database.Where;

@Table(TableName = "vsd_task_relation")
public class VsdTaskRelation implements java.io.Serializable
{

    private static final long serialVersionUID = 1L;

    @Column(ColumnName = "id")
    private Long id;
    @Column(ColumnName = "task_id")
    private Long taskId;
    @Column(ColumnName = "serialnumber")
    private String serialnumber;
    @Column(ColumnName = "camera_file_id")
    private Long cameraFileId;
    @Column(ColumnName = "from_type")
    private Long fromType;
    @Column(ColumnName = "createtime")
    private Date createTime;
    @Column(ColumnName = "createuser")
    private Long createUser;
    @Column(ColumnName = "c1")
    private String c1;
    @Column(ColumnName = "c2")
    private String c2;

    @JoinColumn(Left = "cameraFileId", Right = "id")
    @Where(FieldName = "fromType", compare = SqlCompare.EQ, Value = {"2", "3"})
    private CtrlUnitFile offUnitFile;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getTaskId()
    {
        return taskId;
    }

    public void setTaskId(Long taskId)
    {
        this.taskId = taskId;
    }

    public String getSerialnumber()
    {
        return serialnumber;
    }

    public void setSerialnumber(String serialnumber)
    {
        this.serialnumber = serialnumber;
    }

    public Long getCameraFileId()
    {
        return cameraFileId;
    }

    public void setCameraFileId(Long cameraFileId)
    {
        this.cameraFileId = cameraFileId;
    }

    public Long getFromType()
    {
        return fromType;
    }

    public void setFromType(Long fromType)
    {
        this.fromType = fromType;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Long getCreateUser()
    {
        return createUser;
    }

    public void setCreateUser(Long createUser)
    {
        this.createUser = createUser;
    }

    public String getC1()
    {
        return c1;
    }

    public void setC1(String c1)
    {
        this.c1 = c1;
    }

    public String getC2()
    {
        return c2;
    }

    public void setC2(String c2)
    {
        this.c2 = c2;
    }

    public CtrlUnitFile getOffUnitFile()
    {
        return offUnitFile;
    }

    public void setOffUnitFile(CtrlUnitFile offUnitFile)
    {
        this.offUnitFile = offUnitFile;
    }
}
