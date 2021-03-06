package cn.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.common.utils.IDUtils;
import cn.e3mall.common.utils.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Autowired
	private JedisClient jedisClient;
	@Resource
	private Destination topicDestination;
	@Value("${REDIS_ITEM_PRE}")
	private String REDIS_ITEM_PRE;
	@Value("${ITEM_CACHE_EXPIRE}")
	private int ITEM_CACHE_EXPIRE;
	@Autowired
	private TbUserMapper tbUserMapper;
	
	@Override
	public TbItem getItemById(long itemId) {
		//查询缓存
		try {
			 String json = jedisClient.get(REDIS_ITEM_PRE+":"+itemId+":BASE");
			 if(StringUtils.isNoneBlank(json)){
				 TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
				 return tbItem;
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
		//缓存中没有，查询数据库
		//根据主键查询
		//TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andIdEqualTo(itemId);
		//执行查询
		List<TbItem> list = itemMapper.selectByExample(example);
		if(list != null & list.size() >0){
			//把结果添加到缓存
			try {
				jedisClient.set(REDIS_ITEM_PRE+":"+itemId+":BASE", JsonUtils.objectToJson(list.get(0)));
				//设置过期时间
				jedisClient.expire(REDIS_ITEM_PRE+":"+itemId+":BASE", ITEM_CACHE_EXPIRE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list.get(0);
		}
		return null;
		
	}

	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		// 设置分页信息
		PageHelper.startPage(page, rows);
		// 执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		/*TbItem tb = itemMapper.selectByPrimaryKey((long) 1351910);
		System.out.println(tb.getTitle());
		System.out.println(list.size());
		TbUser tbuser = tbUserMapper.selectByPrimaryKey((long) 1);*/
		//System.out.println(tbuser.getUsername());
		//创建一个返回值对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		//取分页结果
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		//取总记录数
		long total = pageInfo.getTotal();
		result.setTotal(total);
		return result;
	}

	@Override
	public E3Result addItem(TbItem item, String desc) {
		//生成商品id
		final long itemId = IDUtils.genItemId();
		//补全item的属性
		item.setId(itemId);
		//1-正常，2-下架，3-删除
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		//向商品表插入数据
		itemMapper.insert(item);
		//创建一个商品描述表对应的pojo对象。
		TbItemDesc itemDesc = new TbItemDesc();
		//补全属性
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		//向商品描述表插入数据
		itemDescMapper.insert(itemDesc);
		//发送商品添加消息
		jmsTemplate.send(topicDestination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(itemId + "");
				return textMessage;
			}
		});
		
		//返回成功
		return E3Result.ok();
	}

	


	@Override
	public void upItem(List<Long> itemId) {
		//通过商品id查询出TbItem对象
		//执行查询
		for(long id : itemId){
			TbItem tbItem = itemMapper.selectByPrimaryKey(id);
			//修改TbItem对象的status值为1
			tbItem.setStatus((byte) 1);
			//向商品表插入数据
			itemMapper.updateByPrimaryKey(tbItem);
		}
	}

	@Override
	public void downItem(List<Long> itemId) {
		//通过商品id查询出TbItem对象
		//执行查询
		for(long id : itemId){
			TbItem tbItem = itemMapper.selectByPrimaryKey(id);
			//修改TbItem对象的status值为2
			tbItem.setStatus((byte) 2);
			//向商品表插入数据
			itemMapper.updateByPrimaryKey(tbItem);
		}
		
	}

	@Override
	public void deleteItem(List<Long> itemId) {
		//通过商品id查询出TbItem对象
		//执行查询
		for(long id : itemId){
			itemMapper.deleteByPrimaryKey(id);
			
		}
	}

	@Override
	public TbItemDesc getItemDescById(long itemId) {
		//查询缓存
		try {
				System.out.println("redis-out");
				String json = jedisClient.get(REDIS_ITEM_PRE+":"+itemId+":DESC");
				if(StringUtils.isNoneBlank(json)){
				 TbItemDesc tbitemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				 return tbitemDesc;
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		//把结果添加到缓存
		try {
			System.out.println("redis-in");
			jedisClient.set(REDIS_ITEM_PRE+":"+itemId+":DESC", JsonUtils.objectToJson(itemDesc));
			//设置过期时间
			jedisClient.expire(REDIS_ITEM_PRE+":"+itemId+":DESC", ITEM_CACHE_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemDesc;
	}

}
