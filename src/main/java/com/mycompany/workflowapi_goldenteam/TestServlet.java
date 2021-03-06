/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.workflowapi_goldenteam;

import com.google.appengine.api.utils.SystemProperty;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;

/**
 *
 * @author Aless
 */
public class TestServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        //The below lines to open new instance of config.properties located under WEB-INF
        Properties prop = new Properties();
        prop.load(getServletContext().getResourceAsStream("/WEB-INF/config.properties"));

        PrintWriter out = resp.getWriter();
        
        try {

            if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {

                Class.forName(prop.getProperty("googleDriverPath")).newInstance();

                String url = prop.getProperty("prodURL");
                Connection connection = DriverManager.getConnection(url);

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("select user_name from wf_accounts;");
                while (resultSet.next()) {
                    out.println(resultSet.getString(1));
                }

            } else {
                Class.forName(prop.getProperty("localDriverPath"));
                String url = prop.getProperty("qcURL");
                Connection connection = DriverManager.getConnection(url);

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("select user_name from wf_accounts;");
                while (resultSet.next()) {
                    out.println(resultSet.getString(1));
                }

            }
        } catch (Exception ex) {
            resp.sendError(400, ex.toString());
        }
    }
}
