package com.servlet;

import com.model.TextFile;
import com.util.JSONConverter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = {"/api/*"})
public class FileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String q = "";
        int limit = -1;
        int length = -1;
        boolean includeMetaData = false;
        boolean useNIO = true;
        PrintWriter out = response.getWriter();

        try {
            String pathInfo = request.getPathInfo();
            String[] pathParts = pathInfo.split("/");
            String directoryName = "";
            String fileName = "";
            if (pathParts.length > 1) {
                directoryName = pathParts[1];
                fileName = pathParts[2];
            }
            response.setContentType("text/json;charset=UTF-8");

            //retrieving parameters
            String parameter = request.getParameter("q");
            q = null != parameter ? parameter : "";
            parameter = request.getParameter("limit");
            limit = null != parameter ? Integer.parseInt(parameter) : -1;
            parameter = request.getParameter("length");
            length = null != parameter ? Integer.parseInt(parameter) : -1;
            parameter = request.getParameter("includeMetaData");
            includeMetaData = null != parameter ? Boolean.parseBoolean(parameter) : false;
            parameter = request.getParameter("useNIO");
            useNIO = null != parameter ? Boolean.parseBoolean(parameter) : false;

            //getting and checking file
            TextFile textFile = new TextFile(directoryName, fileName);
            if (textFile.getFile().isDirectory()) {
                throw new Exception();
            }

            //performing action
            textFile.fill(q, length, limit, useNIO);

            //return JSON
            JSONConverter jsonConverter = new JSONConverter(textFile);
            out.print(jsonConverter.getJSON(includeMetaData));

        } catch (Exception e) {
            response.setContentType("text/html");
            out.write("<h1 style='color:red;'> File is not existed or request is wrong </h1>");
            out.write("<h2>Try other file name or check request parameters</h1>");
        } finally {
            out.close();
        }

    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

}
