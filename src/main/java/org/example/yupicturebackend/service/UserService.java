package org.example.yupicturebackend.service;

import org.example.yupicturebackend.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.yupicturebackend.entity.dto.user.UserLoginDTO;
import org.example.yupicturebackend.entity.dto.user.UserRegisterDTO;
import org.example.yupicturebackend.entity.vo.LoginUserVO;
import org.example.yupicturebackend.entity.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService extends IService<User> {
	/**
	 * 用户注册
	 * @param dto 注册信息
	 * @return 注册成功后的用户ID
	 */
	long userRegister(UserRegisterDTO dto);

	/**
	 * 用户登录
	 * @param dto 登录信息
	 * @return 登录成功后的用户信息
	 */
	LoginUserVO userLogin(UserLoginDTO dto, HttpServletRequest request);

	/**
	 * 获取脱敏后的用户信息
	 * @param user 用户信息
	 * @return 脱敏后的用户信息
	 */
	LoginUserVO getLoginUserVO(User user);

	/**
	 * 获取当前登录用户
	 * @param request 请求
	 * @return 当前登录用户
	 */
	User getLoginUser(HttpServletRequest request);

	/**
	 * 用户注销
	 * @param request 请求
	 * @return 是否注销成功
	 */
	boolean userLogout(HttpServletRequest request);

	/**
	 * 获取脱敏后的用户信息
	 * @param user 用户信息
	 * @return 脱敏后的用户信息
	 */
	UserVO getUserVO(User user);

	/**
	 * 获取脱敏后的用户列表
	 * @param userList 用户列表
	 * @return 脱敏后的用户列表
	 */
	List<UserVO> getUserVOList(List<User> userList);
}
