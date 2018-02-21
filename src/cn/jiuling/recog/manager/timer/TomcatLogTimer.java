package cn.jiuling.recog.manager.timer;

import java.io.IOException;

import com.loocme.plugin.schedule.annotation.LJob;
import com.loocme.plugin.schedule.annotation.LSchedule;

import cn.jiuling.recog.manager.util.LinuxUtils;

@LSchedule
public class TomcatLogTimer {
	// 定时任务，切分日志并压缩，每天23:50执行一次，运行$Tomcat/bin/catlog.sh文件
	@LJob(cron = "0 50 23 * * ?")
	public void catLog() {
		String command = "sh ../../u2s/manager/apache-tomcat-8.5.23/bin/catlog.sh";
		try {
			LinuxUtils.execueteCommand(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 定时任务，删除历史日志，默认保留三个月，在sh中配置，每天00:02执行一次，运行$Tomcat/bin/clearlog.sh文件
	@LJob(cron = "0 2 0 * * ?")
	public void clearLog() {
		String command = "sh ../../u2s/manager/apache-tomcat-8.5.23/bin/clearlog.sh";
		try {
			LinuxUtils.execueteCommand(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
