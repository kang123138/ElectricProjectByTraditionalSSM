package com.atguigu.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.bean.T_MALL_SHOPPINGCAR;
import com.atguigu.bean.T_MALL_USER_ACCOUNT;
import com.atguigu.mapper.LoginMapper;
import com.atguigu.service.CartServiceInf;
import com.atguigu.util.MyJsonUtil;

@Controller
public class CartController {

	@Autowired
	CartServiceInf cartServiceInf;

	@Autowired
	private LoginMapper loginMapper;
	
	@RequestMapping("delete_cart")
	public String delete_cart(int car_id,int yh_id, HttpSession session, ModelMap map) {
		// 获取user对象
		T_MALL_USER_ACCOUNT user = loginMapper.select_userById(yh_id);
		int count = cartServiceInf.delete_cart(car_id);
		if(count >= 1) {
			// 向数据库查询现在user的cart对象
			List<T_MALL_SHOPPINGCAR> list_cart = cartServiceInf.get_list_cart_by_user(user);
			// 更新session
			 session.setAttribute("list_cart_session", list_cart);
			map.put("list_cart", list_cart);
		}
		return "cartListInner";
	}
	
	@RequestMapping("change_shfxz")
	public String change_shfxz(T_MALL_SHOPPINGCAR cart, ModelMap map, HttpSession session,
			@CookieValue(value = "list_cart_cookie", required = false) String list_cart_cookie, HttpServletResponse response) {
		// 从session中获取用户对象      
		T_MALL_USER_ACCOUNT user = (T_MALL_USER_ACCOUNT) session.getAttribute("user");
		// 创建购物车对象集合
		List<T_MALL_SHOPPINGCAR> list_cart = new ArrayList<T_MALL_SHOPPINGCAR>();

		// 购物车修改业务
		if (user == null) {
			// 用户没有登录，修改cookie
			list_cart = MyJsonUtil.json_to_list(list_cart_cookie, T_MALL_SHOPPINGCAR.class);
		} else {
			// 用户已经登录，修改db
			list_cart = (List<T_MALL_SHOPPINGCAR>) session.getAttribute("list_cart_session");
		}
		// 循环cookie或db中的每个cart对象
		for (int i = 0; i < list_cart.size(); i++) {
			//判断新改变的cart是否和cookie或db中的对象相同
			if (list_cart.get(i).getSku_id() == cart.getSku_id()) {
				// 如果重复的话，修改cart对象的shfxz状态
				list_cart.get(i).setShfxz(cart.getShfxz());
				// 再判断是修改cookie还是db
				if (user == null) {
					// 修改cookie，覆盖cookie
					Cookie cookie = new Cookie("list_cart_cookie", MyJsonUtil.list_to_json(list_cart));
					cookie.setMaxAge(60 * 60 * 24);
					response.addCookie(cookie);
				} else {
					// 否则就更新数据库
					cartServiceInf.update_cart(list_cart.get(i));
				}
			}

		}
		map.put("list_cart", list_cart);
		map.put("sum", get_sum(list_cart));
		return "cartListInner";
	}

	@RequestMapping("miniCart")
	public String miniCart(HttpSession session,
			@CookieValue(value = "list_cart_cookie", required = false) String list_cart_cookie, ModelMap map) {
		// 从session中获取用户对象
		T_MALL_USER_ACCOUNT user = (T_MALL_USER_ACCOUNT) session.getAttribute("user");
		// 创建购物车对象集合
		List<T_MALL_SHOPPINGCAR> list_cart = new ArrayList<T_MALL_SHOPPINGCAR>();
		// 如果User不存在
		if (user == null) {
			// 如果User不存在，没有登录，使用cookie获取购物车数据(需要把cookie字符串转换为集合)
			list_cart = MyJsonUtil.json_to_list(list_cart_cookie, T_MALL_SHOPPINGCAR.class);
		} else {
			// 如果User存在,使用session获取购物车数据
			list_cart = (List<T_MALL_SHOPPINGCAR>) session.getAttribute("list_cart_session");// 数据库
		}

		// 把得到的cookie或session中的数据传递给前台
		map.put("list_cart", list_cart);
		// 修改合计
		map.put("sum", get_sum(list_cart));
		return "miniCartList";
	}

	@RequestMapping("goto_cart_list")
	public String goto_cart_list(ModelMap map,
			@CookieValue(value = "list_cart_cookie", required = false) String list_cart_cookie, HttpSession session) {
		// 从session中获取用户对象
		T_MALL_USER_ACCOUNT user = (T_MALL_USER_ACCOUNT) session.getAttribute("user");
		// 创建购物车对象集合
		List<T_MALL_SHOPPINGCAR> list_cart = new ArrayList<T_MALL_SHOPPINGCAR>();
		// 如果User不存在
		if (user == null) {
			// 如果User不存在，没有登录，使用cookie获取购物车数据(需要把cookie字符串转换为集合)
			list_cart = MyJsonUtil.json_to_list(list_cart_cookie, T_MALL_SHOPPINGCAR.class);
		} else {
			// 如果User存在,使用session获取购物车数据
			list_cart = (List<T_MALL_SHOPPINGCAR>) session.getAttribute("list_cart_session");// 数据库
		}

		// 把得到的cookie或session中的数据传递给前台
		map.put("list_cart", list_cart);
		// 修改合计
		map.put("sum", get_sum(list_cart));
		return "cartList";
	}

	private BigDecimal get_sum(List<T_MALL_SHOPPINGCAR> list_cart) {
		BigDecimal sum = new BigDecimal("0");
		// 遍历购物车集合
		for (int i = 0; i < list_cart.size(); i++) {
			// 判断是否选中
			if (list_cart.get(i).getShfxz().equals("1")) {
				// 如果选中
				sum = sum.add(new BigDecimal(list_cart.get(i).getHj() + ""));
			}
		}
		return sum;
	}

	@RequestMapping("add_cart")
	public String add_cart(HttpSession session, HttpServletResponse response,
			@CookieValue(value = "list_cart_cookie", required = false) String list_cart_cookie, T_MALL_SHOPPINGCAR cart,
			ModelMap map) {
		int yh_id = cart.getYh_id();
		// 创建cart集合
		List<T_MALL_SHOPPINGCAR> list_cart = new ArrayList<T_MALL_SHOPPINGCAR>();

		// 添加购物车操作
		if (yh_id == 0) {
			// 用户未登陆，操作cookie
			// 如果cookie为空，调用工具类判断
			if (StringUtils.isBlank(list_cart_cookie)) {
				// 把前台页面传递过来的cart对象放入集合中
				list_cart.add(cart);
			} else {
				// 如果cookie不为空，要判断购物车对象是否重复
				// 把本地的cookie转换为java对象，即list
				list_cart = MyJsonUtil.json_to_list(list_cart_cookie, T_MALL_SHOPPINGCAR.class);
				// 判断购物车对象是否重复
				boolean b = if_new_cart(list_cart, cart);
				if (b) {
					// 新的购物车，本地cookie中没有该cookie对象，添加到本地cookie中
					list_cart.add(cart);
				} else {
					// 老车，更新
					// 遍历购物车集合
					for (int i = 0; i < list_cart.size(); i++) {
						// 判断新添加的购物车对象中sku_id是否和本地的cart对象中的一样
						if (list_cart.get(i).getSku_id() == cart.getSku_id()) {
							// 如果相同，表示购物车对象重复了，更新本地的cookie中的该对象
							list_cart.get(i).setTjshl(list_cart.get(i).getTjshl() + cart.getTjshl());
							list_cart.get(i).setHj(list_cart.get(i).getTjshl() * list_cart.get(i).getSku_jg());
						}
					}
				}

			}
			// 下面是覆盖cookie
			// 创建cookie-把购物车集合转换为json字符串放入cookie
			Cookie cookie = new Cookie("list_cart_cookie", MyJsonUtil.list_to_json(list_cart));
			// 设置cookie的过期时间
			cookie.setMaxAge(60 * 60 * 24);
			// 把cookie添加到本地浏览器
			response.addCookie(cookie);
		} else {
			// 用户已登陆，操作db
			// 从session中取得原来已有的购物车列表
			list_cart = (List<T_MALL_SHOPPINGCAR>) session.getAttribute("list_cart_session");// 数据库
			// db如果为空，返回false，db不为空，但是不重复，也是返回false
			boolean b = cartServiceInf.if_cart_exists(cart);
			if (!b) {
				// db为空，添加新的购物车--通过特殊的sql语句
				cartServiceInf.add_cart(cart);
				// 同步session，判断数据库中的list_cart是否为空
				if (list_cart == null || list_cart.isEmpty()) {
					list_cart = new ArrayList<T_MALL_SHOPPINGCAR>();
					list_cart.add(cart);
					session.setAttribute("list_cart_session", list_cart);
				} else {
					list_cart.add(cart);
				}
			} else {
				// db不为空，更新购物车
				for (int i = 0; i < list_cart.size(); i++) {
					if (list_cart.get(i).getSku_id() == cart.getSku_id()) {
						list_cart.get(i).setTjshl(list_cart.get(i).getTjshl() + cart.getTjshl());
						list_cart.get(i).setHj(list_cart.get(i).getTjshl() * list_cart.get(i).getSku_jg());
						// 老车，更新
						cartServiceInf.update_cart(list_cart.get(i));
					}
				}
			}
			/*
			 * if (list_cart == null || list_cart.isEmpty()) { // db中没有数据
			 * cartServiceInf.add_cart(cart); // 同步session list_cart.add(cart);
			 * session.setAttribute("list_cart_session", list_cart); } else { // 判断购物车对象是否重复
			 * boolean b = if_new_cart(list_cart, cart); if (b) { // 新的购物车，db中没有数据
			 * cartServiceInf.add_cart(cart); // 同步session list_cart.add(cart); } else { //
			 * 老车，更新db cartServiceInf.update_cart(cart); //
			 * 同步session，将前台的cart更新session中即数据库中的list_cart for (int i = 0; i <
			 * list_cart.size(); i++) { if (list_cart.get(i).getSku_id() ==
			 * cart.getSku_id()) { list_cart.get(i).setTjshl(list_cart.get(i).getTjshl() +
			 * cart.getTjshl()); list_cart.get(i).setHj(list_cart.get(i).getTjshl() *
			 * list_cart.get(i).getSku_jg()); // 老车，更新
			 * cartServiceInf.update_cart(list_cart.get(i)); } } } }
			 */
		}
		return "redirect:/cart_success.do";
	}

	// 判断购物车对象是否重复
	private boolean if_new_cart(List<T_MALL_SHOPPINGCAR> list_cart, T_MALL_SHOPPINGCAR cart) {
		boolean flag = true;
		for (int i = 0; i < list_cart.size(); i++) {
			if (list_cart.get(i).getSku_id() == cart.getSku_id()) {
				flag = false;
			}
		}
		return flag;
	}

	@RequestMapping("cart_success")
	public String cart_success(ModelMap map) {
		return "cartSuccess";
	}

}
