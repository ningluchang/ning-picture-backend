package org.example.yupicturebackend.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.yupicturebackend.annotation.AuthCheck;
import org.example.yupicturebackend.entity.User;
import org.example.yupicturebackend.entity.enums.UserRoleEnum;
import org.example.yupicturebackend.excrption.BusinessException;
import org.example.yupicturebackend.excrption.ErrorCode;
import org.example.yupicturebackend.service.UserService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
	@Around("@annotation(authCheck)")
	public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
		// 从注解中取出设定的必须角色
		String mustRole = authCheck.mustRole();
		// 获取当前 HTTP 请求的上下文，从中取出 HttpServletRequest
		// 这样就可以从请求中读取登录信息，比如 Cookie、Header 中的 Token 等
		RequestAttributes requestAttribute = RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = ((ServletRequestAttributes) requestAttribute).getRequest();

		// 当前登录用户
		User loginUser = userService.getLoginUser(request);
		// 把字符串形式的 mustRole（如 "admin") 转成枚举对象，方便后续比较
		UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(mustRole);
		// 不需要权限,放行
		// 如果注解没有设定角色（为空），说明该接口对所有登录用户开放
		if (mustRoleEnum == null){
			return joinPoint.proceed();
		}
		// 以下为:必须有该权限才通过
		// 获取当前用户具有的权限
		UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(loginUser.getUserRole());
		// 没有权限,拒绝
		// 如果登录用户角色是无效的，说明是非法用户，直接抛出异常拒绝
		if (userRoleEnum == null){
			throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
		}
		// 要求必须有管理员权限,但用户没有管理器权限,拒绝
		// 如果接口要求“必须是管理员”，但你不是管理员，抛出无权限异常
		if (UserRoleEnum.ADMIN.equals(mustRoleEnum) && !UserRoleEnum.ADMIN.equals(userRoleEnum)){
			throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
		}
		// 通过权限校验,放行
		return joinPoint.proceed();
	}
}
