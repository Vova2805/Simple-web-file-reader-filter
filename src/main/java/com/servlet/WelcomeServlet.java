package com.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet("/")
public class WelcomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.write("<h1> Welcome </h1>");
        out.write("<h2>To start reading click <a href='/api/files/test.txt?includeMetaData=true'>here</a></h1>");
        out.write("<h2>To start querying click <a href='/api/files/test.txt?includeMetaData=true&limit=100&q=Java&length=10'>here</a></h1>");
        out.write("<h2>Get file from other dir click <a href='/api/samples/doc.txt?includeMetaData=true'>here</a></h1>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

}