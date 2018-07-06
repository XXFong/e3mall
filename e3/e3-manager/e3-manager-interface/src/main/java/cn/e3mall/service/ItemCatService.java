package cn.e3mall.service;

import java.util.List;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.pojo.EasyUITreeNode;

public interface ItemCatService {

	List<EasyUITreeNode> getCatList(long parentId);
	EasyUIDataGridResult getCatList(int page, int rows);
}