package cn.e3mall.controller;

import java.util.List;

import org.aspectj.lang.annotation.RequiredTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCategoryService;

/**
 * 内容分类管理Controller
 * @author Administrator
 *
 */
@Controller
public class ContentCatController {
	
	@Autowired
	private ContentCategoryService contentCategoryService;

	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCatList(
			@RequestParam(name="id",defaultValue="0")Long parentId){
		//System.out.println(parentId);
		List<EasyUITreeNode> list = contentCategoryService.getContentCatList(parentId);
		//System.out.println(list.size());
		return list;
		
	}
	
	@RequestMapping(value="/content/category/create", method=RequestMethod.POST)
	@ResponseBody
	public E3Result annContentCategory(Long parentId, String name){
		E3Result result = contentCategoryService.addContentCategory(parentId, name);
		return result;
	}
	
	@RequestMapping(value="/content/category/update", method=RequestMethod.POST)
	@ResponseBody
	public E3Result renameContentCategory(long id, String name) {
		E3Result result = contentCategoryService.renameContentCategory(id, name);
		return result;
	}
	
	@RequestMapping(value="/content/category/delete", method=RequestMethod.POST)
	@ResponseBody
	public void deleteContentCatefory(long id){
		contentCategoryService.deleteContentCategory(id);
	}
	
	
}
