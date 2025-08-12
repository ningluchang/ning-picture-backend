package org.example.yupicturebackend.entity.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.yupicturebackend.common.PageRequest;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserQueryDTO extends PageRequest implements Serializable {
	/**
	 * 用户id
	 */
	private Long id;
	/**
	 * 用户名称
	 */
	private String userName;
	/**
	 * 账号
	 */
	private String userAccount;
	/**
	 * 头像
	 */
	private String userAvatar;
	/**
	 * 个人简介
	 */
	private String userProfile;
	/**
	 * 角色：user/admin/ban
	 */
	private String userRole;
	private static final long serialVersionUID = 1L;
}
