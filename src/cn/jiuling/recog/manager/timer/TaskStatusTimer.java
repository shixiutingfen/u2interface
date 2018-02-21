package cn.jiuling.recog.manager.timer;

import com.loocme.plugin.schedule.annotation.LJob;
import com.loocme.plugin.schedule.annotation.LSchedule;

import cn.jiuling.recog.manager.constants.MyIpaddress;
import cn.jiuling.recog.manager.constants.TaskConstants;

@LSchedule
public class TaskStatusTimer
{

    @LJob(cron = "1/5 * * * * ?")
    public void reloadTasks()
    {
        TaskConstants.reloadTaskStatus();
    }
    
    @LJob(cron = "2/5 * * * * ?")
    public void reloadMyIp()
    {
        MyIpaddress.reloadLocalIp();
    }
}
