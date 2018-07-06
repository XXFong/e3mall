package cn.e3mall.controller;

import java.util.HashMap;
import java.util.Map;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.datetime.joda.MillisecondInstantPrinter;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.e3mall.common.utils.FastDFSClient;
import cn.e3mall.common.utils.JsonUtils;

@Controller
public class PictureController {

	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;
	
	@RequestMapping(value="/pic/upload", produces=MediaType.TEXT_PLAIN_VALUE + ";charset=utf-8")
	@ResponseBody
	public String fileUpload(MultipartFile uploadFile){
		
		try{
			//取文件的扩展名
			String originaFilename = uploadFile.getOriginalFilename();
			String extName = originaFilename.substring(originaFilename.lastIndexOf(".")+1);
			//创建一个FastDFS的客户端
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");
			//执行上传处理
			String path = fastDFSClient.uploadFile(uploadFile.getBytes(),extName);
			//凭借返回的url和ip地址，拼装成完整的url
			String url = IMAGE_SERVER_URL+path;
			//返回map
			Map result = new HashMap<>();
			result.put("error", 0);
			result.put("url", url);
			return JsonUtils.objectToJson(result);
		}catch(Exception e){
			e.printStackTrace();
			//返回map
			Map result = new HashMap<>();
			result.put("error", 1);
			result.put("message", "图片上传失败");
			return JsonUtils.objectToJson(result);
		}
	}
}
