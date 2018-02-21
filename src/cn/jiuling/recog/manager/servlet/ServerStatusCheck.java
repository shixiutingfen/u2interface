package cn.jiuling.recog.manager.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.loocme.sys.util.StringUtil;

/**
 * Servlet implementation class RecogTest
 */
@WebServlet("/check/tomcatstatus")
public class ServerStatusCheck extends BaseServlet
{
    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = Logger.getLogger(ServerStatusCheck.class);
    
    private static int CURRENT_STATUS = 1;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServerStatusCheck()
    {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        this.doPost(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        super.LoggerRequest(request);
        
        String status = request.getParameter("status");
        if (StringUtil.isNotNull(status) && StringUtil.isInteger(status))
        {
            CURRENT_STATUS = StringUtil.getInteger(status);
        }
        
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println("" + CURRENT_STATUS);
        
        String[] uriArray = request.getRequestURI().split("/");
        String method = uriArray[uriArray.length - 1];
        logger.info(" [" + method + "] response :" + CURRENT_STATUS);
        
        out.flush();
        out.close();
    }

}
