package kr.gaion.ceh.web.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import kr.gaion.ceh.common.Constants;
import kr.gaion.ceh.common.bean.settings.EsIndexInfo;
import kr.gaion.ceh.common.util.Utilities;
import kr.gaion.ceh.web.config.AppSettingUtil;
import kr.gaion.ceh.web.model.UploadedFile;

@Controller
public class UploadController {

	/**
	 * First line, it contains all fields
	 */
	// private static String firstLine = "init value";

	/**
	 * Upload page
	 * 
	 * @return
	 */
	@RequestMapping(value = "/uploadPage")
	public ModelAndView uploadPage(ModelMap model) {
		AppSettingUtil.loadCurrentConfigToModel(model, BaseController.webConfig);
		return new ModelAndView(Constants.UPLOAD_PAGE);
	}

	/**
	 * Upload single file using Spring Controller
	 */
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public @ResponseBody String uploadFileHandler(@RequestParam("name") String name,
			@RequestParam("file") MultipartFile file) {

		if (!file.isEmpty()) {
			// send file to Spark server
			try {
				byte[] bytes = file.getBytes();
				RestTemplate restClient = new RestTemplate();
				String json = new Gson().toJson(bytes);
				String uri = Utilities.getUri(BaseController.webConfig, Constants.API_UPLOAD);
				String response = restClient.postForObject(uri, json, String.class);

				return response;
			} catch (Exception e) {
				return "You failed to upload " + name + " => " + e.getMessage();
			}
		} else {
			return "You failed to upload " + name + " because the file was empty.";
		}
	}

	/**
	 * Upload multiple file using Spring Controller
	 * 
	 * @param files
	 * @return
	 */
	@RequestMapping(value = "/uploadMultipleFile", method = RequestMethod.POST)
	public @ResponseBody String uploadMultipleFileHandler(@RequestParam("file[]") MultipartFile[] files) {

		String message = "";
		for (MultipartFile file : files) {
			String name = file.getOriginalFilename();
			try {
				byte[] bytes = file.getBytes();

				// Creating the directory to store file
				String rootPath = System.getProperty("catalina.home");
				File dir = new File(rootPath + File.separator + "tmpFiles");
				if (!dir.exists())
					dir.mkdirs();

				// Create the file on server
				File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
				stream.write(bytes);
				stream.close();

				message = message + "You successfully uploaded file=" + name + "";
			} catch (Exception e) {
				return "You failed to upload " + name + " => " + e.getMessage();
			}
		}

		return message;
	}

	/**
	 * To request indexing data to ElasticSearch
	 * 
	 * @param indexEsInfo
	 * @param model
	 * @return
	 * @throws UnknownHostException
	 */
	@RequestMapping(value = "/indexDataToEs", method = RequestMethod.POST)
	public ModelAndView indexDataToEs(@ModelAttribute("indexEsInfo") EsIndexInfo indexEsInfo, ModelMap model)
			throws UnknownHostException {

		// delete old data
		/*
		 * if (indexEsInfo.getClearOldData()) {
		 * CommonController.deleteOldDataFromEs(indexEsInfo); } else { // continue }
		 */

		String json = indexEsInfo.toJson();
		RestTemplate restClient = new RestTemplate();
		String uri = Utilities.getUri(BaseController.webConfig, Constants.API_ES_INDEX);
		String response = restClient.postForObject(uri, json, String.class);

		model.addAttribute("msg", response);

		return new ModelAndView("subPage-responseViewer");
	}

	/**
	 * To test writing file
	 * 
	 * @param servletRequest
	 * @param uploadedFile
	 * @param bindingResult
	 * @param model
	 */
	@RequestMapping("/request-SendMultipleFileToRestServer")
	@ResponseBody
	public String sendMultipleFileToRestServer(HttpServletRequest servletRequest,
			@ModelAttribute UploadedFile uploadedFile, BindingResult bindingResult, ModelMap model) {

		MultipartFile multipartFile = uploadedFile.getMultipartFile();
		RestTemplate restClient = new RestTemplate();
		String fileNameFullPath = "";
		Gson gson = new Gson();

		try {
			byte[] bytes = multipartFile.getBytes();
			String uri = Utilities.getUri(BaseController.webConfig, Constants.API_UPLOAD);
			String response = restClient.postForObject(uri, bytes, String.class);
			@SuppressWarnings("unchecked")
			Map<String, String> mapResponse = gson.fromJson(response, HashMap.class);
			fileNameFullPath = mapResponse.get("fileName");

			System.out.println("Upload success with file ID:" + fileNameFullPath);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return fileNameFullPath;
	}

	/**
	 * to upload matching table (for FP-Growth)
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/uploadMatchingTable", method = RequestMethod.POST, produces = { "application/json" })
	public @ResponseBody void uploadMatchingTable(MultipartHttpServletRequest request, HttpServletResponse response)
			throws Exception {

		MultipartFile file = request.getFile("file");

		String name = file.getOriginalFilename();
		try {
			byte[] bytes = file.getBytes();

			// Creating the directory to store file
			String rootPath = Utilities.getPathInWorkingFolder(Constants.DATA_DIR);
			File dir = new File(rootPath);
			if (!dir.exists())
				dir.mkdirs();

			// Create the file on server
			File serverFile = new File(dir.getAbsolutePath() + File.separator + name);
			if (serverFile.exists()) {
				serverFile.delete();
				// continue
			} else {
				// continue
			}
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
			stream.write(bytes);
			stream.close();

			// save to global variable
			BaseController.webConfig.updateProperty(Constants.CONF_FPG_DATA_MATCHING_TABLE,
					serverFile.getAbsolutePath());
			BaseController.webConfig.saveProperties();

			response.setStatus(HttpServletResponse.SC_CREATED);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * to upload unlabeled file from web server to Spark server (Rest)
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "request-SendFileToRestServer", method = RequestMethod.POST, produces = {
			"application/json" })
	public @ResponseBody String sendFileToRestServer(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {

		MultipartFile multipartFile = request.getFile("file");
		RestTemplate restClient = new RestTemplate();
		// String fileNameFullPath = "";
		String jsonResponse = "";
		// Gson gson = new Gson();

		try {
			byte[] bytes = multipartFile.getBytes();
			String uri = Utilities.getUri(BaseController.webConfig, Constants.API_UPLOAD);
			jsonResponse = restClient.postForObject(uri, bytes, String.class);
			// Map<String, String> objResponse = gson.fromJson(jsonResponse, HashMap.class);
			// fileNameFullPath = objResponse.get("fileName");
			// String header = objResponse.get("header");
			// System.out.println(header);
			// System.out.println("Upload success with file ID:" + fileNameFullPath);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonResponse;
	}
}
