package cn.jiuling.recog.manager.entity;

import java.util.Date;

import com.loocme.sys.annotation.database.Column;
import com.loocme.sys.annotation.database.Id;
import com.loocme.sys.annotation.database.Table;

@Table(TableName = "ctrl_unit_file")
public class CtrlUnitFile implements java.io.Serializable{
   private static final long serialVersionUID = 1L;
   
   @Id
   @Column(ColumnName = "id")
   private Long id;
   
   @Column(ColumnName = "file_type")
   private String fileType;
   
   @Column(ColumnName = "file_suffix")
   private String fileSuffix;

   @Column(ColumnName = "file_name")
   private String fileName;

   @Column(ColumnName = "file_nameafterupload")
   private String fileNameafterupload;

   @Column(ColumnName = "file_pathafterupload")
   private String filePathafterupload;

   @Column(ColumnName = "file_local_path")
   private String fileLocalPath;
   
   @Column(ColumnName = "ctrl_unit_id")
   private String ctrlUnitId;

   @Column(ColumnName = "camera_id")
   private Long cameraId;
   
   @Column(ColumnName = "file_description")
   private String fileDescription;

   @Column(ColumnName = "thumb_nail")
   private String thumbNail;

   @Column(ColumnName = "file_size")
   private String fileSize;

   @Column(ColumnName = "create_uerid")
   private Long createUerid;

   @Column(ColumnName = "entry_time")
   private Date entryTime;
   
   @Column(ColumnName = "create_time")
   private Date createTime;   
  
   @Column(ColumnName = "serialnumber")
   private String serialnumber;
   
   @Column(ColumnName = "transcoding_id")
   private String transcodingId;
   // 转码进度
   @Column(ColumnName = "transcoding_progress")
   private Integer transcodingProgress;
   
   @Column(ColumnName = "frame_count")
   private Long frameCount;
   // 每秒多少帧
   @Column(ColumnName = "framerate")
   private Integer framerate;
   
   @Column(ColumnName = "resolution")
   private String resolution;
   
   @Column(ColumnName = "transcode_status")
   private Integer transcodeStatus;
   
   @Column(ColumnName = "tasticsentityid")
   private String tasticsentityId;
   
   @Column(ColumnName = "file_ftp_path")
   private String fileFtpPath;

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getFileType() {
		return fileType;
	}
	
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	
	public String getFileSuffix() {
		return fileSuffix;
	}
	
	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFileNameafterupload() {
		return fileNameafterupload;
	}
	
	public void setFileNameafterupload(String fileNameafterupload) {
		this.fileNameafterupload = fileNameafterupload;
	}
	
	public String getFilePathafterupload() {
		return filePathafterupload;
	}
	
	public void setFilePathafterupload(String filePathafterupload) {
		this.filePathafterupload = filePathafterupload;
	}
	
	public String getFileLocalPath() {
		return fileLocalPath;
	}
	
	public void setFileLocalPath(String fileLocalPath) {
		this.fileLocalPath = fileLocalPath;
	}
	
	public String getCtrlUnitId() {
		return ctrlUnitId;
	}
	
	public void setCtrlUnitId(String ctrlUnitId) {
		this.ctrlUnitId = ctrlUnitId;
	}
	
	public Long getCameraId() {
		return cameraId;
	}
	
	public void setCameraId(Long cameraId) {
		this.cameraId = cameraId;
	}
	
	public String getFileDescription() {
		return fileDescription;
	}
	
	public void setFileDescription(String fileDescription) {
		this.fileDescription = fileDescription;
	}
	
	public String getThumbNail() {
		return thumbNail;
	}
	
	public void setThumbNail(String thumbNail) {
		this.thumbNail = thumbNail;
	}
	
	public String getFileSize() {
		return fileSize;
	}
	
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	
	public Long getCreateUerid() {
		return createUerid;
	}
	
	public void setCreateUerid(Long createUerid) {
		this.createUerid = createUerid;
	}
	
	public Date getEntryTime() {
		return entryTime;
	}
	
	public void setEntryTime(Date entryTime) {
		this.entryTime = entryTime;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public String getSerialnumber() {
		return serialnumber;
	}
	
	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
	}
	
	public String getTranscodingId() {
		return transcodingId;
	}
	
	public void setTranscodingId(String transcodingId) {
		this.transcodingId = transcodingId;
	}
	
	public Integer getTranscodingProgress() {
		return transcodingProgress;
	}
	
	public void setTranscodingProgress(Integer transcodingProgress) {
		this.transcodingProgress = transcodingProgress;
	}
	
	public Long getFrameCount() {
		return frameCount;
	}
	
	public void setFrameCount(Long frameCount) {
		this.frameCount = frameCount;
	}
	
	public Integer getFramerate() {
		return framerate;
	}
	
	public void setFramerate(Integer framerate) {
		this.framerate = framerate;
	}
	
	public String getResolution() {
		return resolution;
	}
	
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	
	public Integer getTranscodeStatus() {
		return transcodeStatus;
	}
	
	public void setTranscodeStatus(Integer transcodeStatus) {
		this.transcodeStatus = transcodeStatus;
	}
	
	public String getTasticsentityId() {
		return tasticsentityId;
	}
	
	public void setTasticsentityId(String tasticsentityId) {
		this.tasticsentityId = tasticsentityId;
	}
	
	public String getFileFtpPath() {
		return fileFtpPath;
	}
	
	public void setFileFtpPath(String fileFtpPath) {
		this.fileFtpPath = fileFtpPath;
	}
	   
   
   
}
