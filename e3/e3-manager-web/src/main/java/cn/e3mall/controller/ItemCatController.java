package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.service.ItemCatService;

@Controller
public class ItemCatController {

	@Autowired
	private ItemCatService itemCatService;
	
	@RequestMapping("/item/cat/list")
	@ResponseBody
	public List<EasyUITreeNode> getItemCatList(@RequestParam(value="id" ,defaultValue="0") Long parentId){
		
		List<EasyUITreeNode> list = itemCatService.getCatList(parentId);
		return list;
	}
	
	@RequestMapping("/item/param/list")
	@ResponseBody
	public EasyUIDataGridResult getCatList(int page, int rows){
		
		EasyUIDataGridResult result = itemCatService.getCatList(page, rows);
		return result;
	}
	
	
}