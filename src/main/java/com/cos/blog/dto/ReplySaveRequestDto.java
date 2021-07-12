package com.cos.blog.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplySaveRequestDto {

	private int userId;
	private int boardId;
	private String content;

}
