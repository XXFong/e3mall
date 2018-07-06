package cn.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;

/**
 * 内容分类管理Service
 * @author Administrator
 *
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	@Override
	public List<EasyUITreeNode> getContentCatList(long parentId) {
		//取查询id，parenId
		//根据parentId查询tb_content_category,查询子节点列表
		TbContentCategoryExample example = new TbContentCategoryExample();
		//设置查询条件
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		//执行查询得到List<TbContentCategory>对象
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		//把列表转换为List<EasyUITreeNode>
		List<EasyUITreeNode> result = new ArrayList<>();
		//System.out.println("result.size():"+result.size());
		for(TbContentCategory resultList : list){
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(resultList.getId());
			node.setText(resultList.getName());
			node.setState(resultList.getIsParent()?"closed":"open");
			//添加到列表
			result.add(node);
		}
		
		
		return result;
	}
	
	
	@Override
	public E3Result addContentCategory(long parentId, String name) {
		// 1、接收两个参数：parentId、name
		// 2、向tb_content_category表中插入数据。
		// a)创建一个TbContentCategory对象
		TbContentCategory tbContentCategory = new TbContentCategory();
		// b)补全TbContentCategory对象的属性
		tbContentCategory.setIsParent(false);
		tbContentCategory.setName(name);
		tbContentCategory.setParentId(parentId);
		//排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数
		tbContentCategory.setSortOrder(1);
		//状态。可选值:1(正常),2(删除)
		tbContentCategory.setStatus(1);
		tbContentCategory.setCreated(new Date());
		tbContentCategory.setUpdated(new Date());
		// c)向tb_content_category表中插入数据
		contentCategoryMapper.insert(tbContentCategory);
		// 3、判断父节点的isparent是否为true，不是true需要改为true。
		TbContentCategory parentNode = contentCategoryMapper.selectByPrimaryKey(parentId);
		if(!parentNode.getIsParent()){
			parentNode.setIsParent(true);
			//更新父节点
			contentCategoryMapper.updateByPrimaryKey(parentNode);
		}
		// 4、需要主键返回。
		// 5、返回E3Result，其中包装TbContentCategory对象	
		return E3Result.ok(tbContentCategory);
	}


	@Override
	public E3Result renameContentCategory(long Id, String name) {
		//根据主键id查询出对应的TbContentCategory对象
		TbContentCategory tbContentCategory = contentCategoryMapper.selectByPrimaryKey(Id);
		//改变name值
		tbContentCategory.setName(name);
		//向tb_content_category表中更新数据
		contentCategoryMapper.updateByPrimaryKey(tbContentCategory);
		TbContentCategory result = contentCategoryMapper.selectByPrimaryKey(Id);
		return E3Result.ok(result);
	}


	@Override
	public void deleteContentCategory(long id) {
		//根据主键id查询出对应的TbContentCategory对象
		TbContentCategory tbContentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		//判断IsParent是否为1 ，1为父节点：不允许删除，0为子节点可以直接删除
		if(!tbContentCategory.getIsParent()){
			contentCategoryMapper.deleteByPrimaryKey(id);
		}
		//向tb_content_category表中更新数据
	}

}
