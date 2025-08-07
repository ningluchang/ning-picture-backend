package org.example.yupicturebackend.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.yupicturebackend.entity.User;
import org.example.yupicturebackend.entity.dto.user.UserLoginDTO;
import org.example.yupicturebackend.entity.dto.user.UserRegisterDTO;
import org.example.yupicturebackend.entity.enums.UserRoleEnum;
import org.example.yupicturebackend.entity.vo.LoginUserVO;
import org.example.yupicturebackend.excrption.BusinessException;
import org.example.yupicturebackend.excrption.ErrorCode;
import org.example.yupicturebackend.service.UserService;
import org.example.yupicturebackend.mapper.UserMapper;
import org.example.yupicturebackend.utils.PasswordEncryptor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import static org.example.yupicturebackend.constant.UserConstant.USER_LOGIN_STATE;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
		implements UserService {

	@Override
	public long userRegister(UserRegisterDTO dto) {
		// 1.校验
		if (StrUtil.hasBlank(dto.getUserAccount(), dto.getUserPassword(), dto.getCheckPassword())) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
		}
		if (dto.getUserAccount().length() < 4) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
		}
		if (dto.getUserPassword().length() < 8 || dto.getCheckPassword().length() < 8) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
		}
		if (!dto.getUserPassword().equals(dto.getCheckPassword())) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
		}

		// 2.检查是否重复
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("userAccount", dto.getUserAccount());
		long count = this.baseMapper.selectCount(queryWrapper);
		if (count > 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
		}

		// 3.加密
		String salt = PasswordEncryptor.generateSalt();
		String encryptedPassword = PasswordEncryptor.encryptPassword(
				dto.getUserPassword(),
				salt);
		User user = new User();
		user.setUserAccount(dto.getUserAccount());
		user.setUserPassword(encryptedPassword);
		user.setUserName("无名");
		user.setUserRole(UserRoleEnum.USER.getValue());
		user.setSalt(salt);
		boolean save = this.save(user);
		if (!save) {
			throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败,数据库错误");
		}

		return user.getId();
	}

	@Override
	public LoginUserVO userLogin(UserLoginDTO dto, HttpServletRequest request) {
		// 1.校验
		if (StrUtil.hasBlank(dto.getUserAccount(), dto.getUserPassword())) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
		}
		if (dto.getUserAccount().length() < 4){
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
		}
		if (dto.getUserPassword().length() < 8) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
		}

		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("userAccount", dto.getUserAccount());
        User user = this.baseMapper.selectOne(queryWrapper);
		if (user == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
		}

		String password = dto.getUserPassword();
		String encryptedPassword = PasswordEncryptor.encryptPassword(password,user.getSalt());
		queryWrapper.eq("userPassword", encryptedPassword);
		user = this.baseMapper.selectOne(queryWrapper);
		if (user == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
		}
		LoginUserVO loginUserVO = getLoginUserVO(user);
		request.getSession().setAttribute(USER_LOGIN_STATE, loginUserVO);
		return loginUserVO;
	}

	@Override
	public LoginUserVO getLoginUserVO(User user) {
		if (user != null) {
			LoginUserVO loginUserVO = new LoginUserVO();
			BeanUtils.copyProperties(user, loginUserVO);
			return loginUserVO;
		}
		return null;
	}
}




