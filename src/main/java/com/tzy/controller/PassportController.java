package com.tzy.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tzy.domain.User;
import com.tzy.service.UserService;
import com.tzy.util.CMSException;

//用户登录注册模块
@RequestMapping("passport")
@Controller
public class PassportController {

	@Resource
	private UserService userService;

	// 去登录页面
	@GetMapping("login")
	public String login() {

		return "passport/login";

	}

	// 执行登录页面
	@PostMapping("login")
	public String login(Model model,User user,HttpSession session) {
		try {
			User u = userService.login(user);
			// 根据角色进入不同的页面
			if("0".equals(u.getRole())){//普通用户,进入个人中心
				//登录成功.存入session
				session.setAttribute("user", u);
				return "redirect:/my";
			}else {
				//登录成功.存入session
				session.setAttribute("admin", u);
				return "redirect:/admin";//管理员
			}
			
		} catch (CMSException e) {
			e.printStackTrace();
			model.addAttribute("error", e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("error", "系统异常,请于管理员联系");
		}
	
		
		return "passport/login";
	}

	// 去注册页面
	@GetMapping("reg")
	public String reg() {

		return "passport/reg";

	}

//执行注册
	@PostMapping("reg")
	public String reg(Model model, User user, RedirectAttributes redirectAttributes) {
		try {
			int i = userService.insertSelective(user);
			if (i > 0) {
				// 使用redirectAttributes 对象.用来重定向没携带参数
				redirectAttributes.addFlashAttribute("username", user.getUsername());
				return "redirect:/passport/login";// 注册成功则重定向到登录页面
			}
		} catch (CMSException e) {// catch自定义异常
			e.printStackTrace();// 给程序员找错用
			// 获取并封装消息.
			model.addAttribute("error", e.getMessage());// 给用户看的

		} catch (Exception e) {
			e.printStackTrace();
			// 获取并封装消息.
			model.addAttribute("error", "系统错误.请联系管理员");// 给用户看的
		}
		model.addAttribute("user", user);
		return "passport/reg";// 注册失败

	}
	
	
//注销
	@GetMapping("logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/passport/login";
		
	}
	

}
