package cn.itcast.store.web.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.store.domain.Category;
import cn.itcast.store.service.CategoryService;
import cn.itcast.store.service.ServiceImp.CategoryServiceImp;
import cn.itcast.store.utils.JedisUtils;
import cn.itcast.store.web.base.BaseServlet;
import net.sf.json.JSONArray;
import redis.clients.jedis.Jedis;

public class CategoryServlet extends BaseServlet {
	
	//findAllCats
	public String findAllCats(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		//��redis�л�ȡȫ��������Ϣ
		Jedis jedis=JedisUtils.getJedis();
		String jsonStr=jedis.get("allCats");
		if(null==jsonStr||"".equals(jsonStr)){
			//����ҵ����ȡȫ������
			CategoryService CategoryService=new CategoryServiceImp();
			List<Category> list = CategoryService.getAllCats();
			//��ȫ������ת��ΪJSON��ʽ������
			jsonStr=JSONArray.fromObject(list).toString();
			System.out.println(jsonStr);
			//����ȡ����JSON��ʽ�����ݴ���redis
			jedis.set("allCats", jsonStr);
			System.out.println("redis������û������");
			//��ȫ��������Ϣ��Ӧ���ͻ���
			//���������������Ӧ��������JSON��ʽ���ַ���
			resp.setContentType("application/json;charset=utf-8");
			resp.getWriter().print(jsonStr);			
		}else{
			System.out.println("redis������������");
			
			//��ȫ��������Ϣ��Ӧ���ͻ���
			//���������������Ӧ��������JSON��ʽ���ַ���
			resp.setContentType("application/json;charset=utf-8");
			resp.getWriter().print(jsonStr);
		}
		
		JedisUtils.closeJedis(jedis);

		return null;
	}
}
