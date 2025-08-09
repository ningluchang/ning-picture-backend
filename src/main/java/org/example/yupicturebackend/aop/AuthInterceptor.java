package org.example.yupicturebackend.aop;

import org.aspectj.lang.annotation.Aspect;
import org.example.yupicturebackend.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Aspect
@Component
public class AuthInterceptor {

	@Resource
	private UserService userService;

	/**
	 * 执行拦截
	 * @param joinPoint 切入点
	 * @param authCheck 权限校验注解
	 */
}
