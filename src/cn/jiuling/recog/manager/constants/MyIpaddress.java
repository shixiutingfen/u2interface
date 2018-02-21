package cn.jiuling.recog.manager.constants;

import java.io.File;

import com.loocme.sys.datastruct.Var;
import com.loocme.sys.util.FileUtil;
import com.loocme.sys.util.PatternUtil;
import com.loocme.sys.util.StringUtil;

public class MyIpaddress
{

    public static String Local_Ip = "127.0.0.1";

    public static void reloadLocalIp()
    {
        String fileStr = FileUtil.read(
                new File("/u2s/slave/objext/objext/VideoObjExtService.json"));
        if (StringUtil.isNotNull(fileStr))
        {
            Var paramVar = Var.fromJson(fileStr);
            String ip = paramVar.getString("ip");
            if (PatternUtil.isMatch(ip, "^\\d+\\.\\d+\\.\\d+\\.\\d+$"))
            {
                Local_Ip = ip;
            }
        }
    }
}
