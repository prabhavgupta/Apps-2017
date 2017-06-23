package com.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class FileDownloadHelper {
	public static void downloadFile(String filePath,
			HttpServletResponse response) {
		OutputStream os;
		try {
			os = response.getOutputStream();

			File file = new File(filePath);
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ file.getName());
			InputStream in = new FileInputStream(file.getAbsolutePath());

			byte[] outputByte = new byte[4096];
			// copy binary contect to output stream
			while (true) {
				int read = in.read(outputByte, 0, 4096);
				if (read == -1)
					break;
				os.write(outputByte, 0, read);
			}
			in.close();
			os.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static HashMap parseMultipartRequest(HttpServletRequest request) {

		System.out.println("Multipart Parser Start");

		HashMap param = new HashMap();
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		System.out.println(isMultipart);
		String inputFile = "", outputFile = "";
		if (!isMultipart) {
			System.out.println("File Not Uploaded");
		} else {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List items = null;
			String uid = "", desc = "";
			try {
				items = upload.parseRequest(request);
				System.out.println("items: " + items);
			} catch (FileUploadException e) {
				e.printStackTrace();
			}
			Iterator itr = items.iterator();
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				// textbox checkbox
				if (item.isFormField()) {
					String name = item.getFieldName();
					String value = StringHelper.n2s(item.getString());
					param.put(name, value);

				} else {
					// file
					String itemName = item.getName();
					param.put(item.getFieldName(), item.getName());
					try {
						param.put(item.getFieldName() + "_FILE",
								item.getInputStream());
						param.put(item.getFieldName() + "_ITEM",
								item);

						param.put(item.getFieldName() + "_FILE_CTYPE",
								item.getContentType());
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}
		System.out.println(param);
		return param;

	}

}
