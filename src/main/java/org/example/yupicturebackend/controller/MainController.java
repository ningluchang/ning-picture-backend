package org.example.yupicturebackend.controller;

import org.example.yupicturebackend.common.BaseResponse;
import org.example.yupicturebackend.common.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class MainController {

	@GetMapping("/health")
	public BaseResponse<String> health(){
		return ResultUtils.success("success");
	}
}
