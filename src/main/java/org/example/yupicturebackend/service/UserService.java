package org.example.yupicturebackend.service;

import org.example.yupicturebackend.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.yupicturebackend.entity.dto.user.UserLoginDTO;
import org.example.yupicturebackend.entity.dto.user.UserRegisterDTO;
import org.example.yupicturebackend.entity.vo.LoginUserVO;

import javax.servlet.http.HttpServletRequest;

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

	LoginUserVO getLoginUserVO(User user);
}
