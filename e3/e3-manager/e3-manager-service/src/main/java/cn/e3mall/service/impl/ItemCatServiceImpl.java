package cn.e3mall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemCatExample;
import cn.e3mall.pojo.TbItemCatExample.Criteria;
import cn.e3mall.service.ItemCatService;

@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	
	public TbItemCatMapper itemCatMapper;
	
	@Override
	public List<EasyUITreeNode> getCatList(long parentId) {
		
		//根据parentId查询节点列表
		TbItemCatExample example = new TbItemCatExample();
		//设置查询条件
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		//转换成EasyUITreeNode列表
		List<EasyUITreeNode> resultList = new ArrayList<>();
		for(TbItemCat tbItemCat : list){
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbItemCat.getId());
			node.setText(tbItemCat.getName());
			node.setState(tbItemCat.getIsParent()?"closed":"open");
			//添加到列表
			resultList.add(node);
		}
		
		//返回
		return resultList;
	}

	//查询所有商品类型节点
	@Override
	public EasyUIDataGridResult getCatList(int page, int rows) {
		//设置分页信息
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemCatExample example = new TbItemCatExample();
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		//创建一个返回值对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		//取分页结果
		PageInfo pageInfo = new PageInfo<>(list);
		//取总记录数
		long total = pageInfo.getTotal();
		//将总记录数和查询结果存入返回值对象
		result.setRows(list);
		result.setTotal(total);
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+total);
		return result;
	}

}
