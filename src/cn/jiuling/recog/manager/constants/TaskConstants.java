package cn.jiuling.recog.manager.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.loocme.plugin.spring.comp.Select;
import com.loocme.plugin.spring.enums.SqlCompare;
import com.loocme.sys.util.ListUtil;

import cn.jiuling.recog.manager.entity.VsdTask;

public class TaskConstants
{
    /**
     * 查询结构化任务列表pageNo
     */
    public static final Integer TASK_PAGE_PAGENO = 1;

    /**
     * 查询结构化任务列表pageSize
     */
    public static final Integer TASK_PAGE_PAGESIZE = 10;

    public static final Integer VEDIO_TYPE_REAL = 1;
    public static final Integer VEDIO_TYPE_OFFLINE = 0;

    private static Map<String, VsdTask> currentTaskMap = new HashMap<String, VsdTask>();
    
    public static void addTaskInfo(VsdTask task)
    {
    	currentTaskMap.put(task.getSerialnumber(), task);
    }

    public static void finishTaskInfo(VsdTask task)
    {
        removeTaskInfo(task.getSerialnumber());
    }

    public static void failedTaskInfo(VsdTask task)
    {
        removeTaskInfo(task.getSerialnumber());
    }

    public static void removeTaskInfo(String serialnumber)
    {
        if (currentTaskMap.containsKey(serialnumber))
        {
            currentTaskMap.remove(serialnumber);
        }
    }

    public static VsdTask getTaskInfo(String serialnumber)
    {
        VsdTask vsdTask = currentTaskMap.get(serialnumber);
        if (null == vsdTask)
        {
        	vsdTask = Select.from(VsdTask.class)
                    .addWhereCause("serialnumber", SqlCompare.EQ, serialnumber)
                    .map();
        }
        return vsdTask;
    }
    
    public static void reloadTaskStatus()
    {
        List<VsdTask> taskList = Select.from(VsdTask.class)
                .addWhereCause("slaveip", SqlCompare.EQ, MyIpaddress.Local_Ip)
                .list();
        Map<String, Boolean> currTaskSeriMap = new HashMap<String, Boolean>();
        if (ListUtil.isNotNull(taskList))
        {
            VsdTask task = null;
            
            for (int i = 0; i < taskList.size(); i++)
            {
                task = taskList.get(i);
                currTaskSeriMap.put(task.getSerialnumber(), true);
                if (currentTaskMap.containsKey(task.getSerialnumber()))
                {
                    if (task.getStatus() > 2)
                    {
                        failedTaskInfo(task);
                    }
                    else if (task.getStatus() == 2)
                    {
                        finishTaskInfo(task);
                    }
                    else if (task.getIsValid() == 0)
                    {
                        removeTaskInfo(task.getSerialnumber());
                    }
                    else
                    {
                    	addTaskInfo(task);
                    }
                }
                else
                {
                    addTaskInfo(task);
                }
            }
        }

        Iterator<String> taskSeriIt = currentTaskMap.keySet().iterator();
        List<String> delTaskSeriList = new ArrayList<String>();
        while (taskSeriIt.hasNext())
        {
            String serialnumber = taskSeriIt.next();
            if (!currTaskSeriMap.containsKey(serialnumber))
            {
                delTaskSeriList.add(serialnumber);
            }
        }
        for (int i = 0; i < delTaskSeriList.size(); i++)
        {
            removeTaskInfo(delTaskSeriList.get(i));
        }
    }
}
