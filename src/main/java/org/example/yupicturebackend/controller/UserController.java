package org.example.yupicturebackend.controller;

import org.example.yupicturebackend.common.BaseResponse;
import org.example.yupicturebackend.common.ResultUtils;
import org.example.yupicturebackend.entity.dto.user.UserLoginDTO;
import org.example.yupicturebackend.entity.dto.user.UserRegisterDTO;
import org.example.yupicturebackend.entity.vo.LoginUserVO;
import org.example.yupicturebackend.excrption.ErrorCode;
import org.example.yupicturebackend.excrption.ThrowUtils;
import org.example.yupicturebackend.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

	@Resource
	private UserService userService;

	@PostMapping("/register")
	public BaseResponse<Long> userRegister(@RequestBody UserRegisterDTO dto) {
		ThrowUtils.throwIf(dto == null, ErrorCode.PARAMS_ERROR);
		Long result = userService.userRegister(dto);
		return ResultUtils.success(result);
	}

	@PostMapping("/login")
	public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginDTO dto, HttpServletRequest request){
		ThrowUtils.throwIf(dto == null, ErrorCode.PARAMS_ERROR);
		LoginUserVO result = userService.userLogin(dto, request);
		return ResultUtils.success(result);
	}
}
