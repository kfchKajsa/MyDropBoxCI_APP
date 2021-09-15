package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.http.Java11HttpClientExample4;

import helper.FileInformation;

/**
 * Servlet implementation class ProductServlet
 */
@WebServlet("/SearchForFilesServlet")
public class SearchForFilesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Java11HttpClientExample4 obj = new Java11HttpClientExample4();
	private List fileInfo;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SearchForFilesServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String body = "";
		Document doc = null;

		try {
			obj.setSearchString("<search>" + request.getParameter("searchString") + "</search>");
			obj.sendPOST();
			body = obj.getResponseBody();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		PrintWriter pw = response.getWriter();

		pw.append("Served at: ").append(request.getContextPath());

		try {
			fileInfo = parseFromJSON(body);
		} catch (Exception e) {
			e.printStackTrace();
		}

		request.setAttribute("searchString", request.getParameter("searchString"));

		if ((fileInfo != null) && (!fileInfo.isEmpty())) {
			request.setAttribute("fileInfo", fileInfo);
		} else {
			request.setAttribute("EmptySearch",
					"No files found with the search string " + "\"" + request.getParameter("searchString") + "\"");
		}

		RequestDispatcher RequetsDispatcherObj = request.getRequestDispatcher("/SearchResult.jsp");
		RequetsDispatcherObj.forward(request, response);
	}

	private List parseFromJSON(String inFileInfo) throws Exception {
		List<FileInformation> fileInformationList = new ArrayList<FileInformation>();
		String size;
		String sizeFl;
	    DecimalFormat df = new DecimalFormat("0.00");

		try {
			//new JsonParser();
			JsonElement jsonElement = JsonParser.parseString(inFileInfo);
			JsonArray jsonArray = jsonElement.getAsJsonArray();

			Iterator<JsonElement> it = jsonArray.iterator();

			while (it.hasNext()) {
				JsonObject obj = it.next().getAsJsonObject();
				FileInformation fileInformation = new FileInformation();
				fileInformation.setName(obj.get("name").getAsString());
				fileInformation.setPath(obj.get("path").getAsString());
				
				Integer sizeInt = Integer.parseInt(obj.get("size").getAsString());
				Float sizeFloat = Float.parseFloat(obj.get("size").getAsString());
				
				if(sizeFloat > 1000000) {
					sizeFloat = sizeFloat/1000000;
					sizeFl = df.format(sizeFloat) + " MB"; 
				} else if (sizeFloat > 1000) {
					sizeFloat = sizeFloat/1000;
					sizeFl = df.format(sizeFloat) + " KB"; 
				} else {
					sizeFl = sizeInt + " byte";
				}
				
				fileInformation.setSize(sizeFl);

				fileInformationList.add(fileInformation);
			}

		} catch (Exception e) {
			return null;
		}

		return fileInformationList;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
