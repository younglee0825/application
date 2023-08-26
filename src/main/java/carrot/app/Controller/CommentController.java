package carrot.app.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import carrot.app.User.UserVo;
import carrot.app.dto.CommentDTO;
import carrot.app.service.CommentService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

	@Autowired
	private CommentService commentService;

	@PostMapping("/commentList")
	@ResponseBody
	public List<CommentDTO> commentList(CommentDTO commentDto) {
		return commentService.getCommentList(commentDto.getPost_num());
	}


	@PostMapping("/addComment")
	@ResponseBody
	public Map<String, Integer> addComment(CommentDTO commentDto, Authentication authentication) {
		LOGGER.info("addComment");
		Map<String, Integer> result = new HashMap<>();

		UserVo userVo = (UserVo) authentication.getPrincipal();
		commentDto.setUser_num(userVo.getUnum());

		result.put("result", commentService.addComment(commentDto));

		return result;
	}
	

	@PostMapping("/selectComment")
	@ResponseBody
	public Map<String, String> selectComment(CommentDTO commentDto, Authentication authentication) {
		LOGGER.info("addComment");
		Map<String, String> result = new HashMap<>();

		UserVo userVo = (UserVo) authentication.getPrincipal();
		commentDto.setUser_num(userVo.getUnum());

		result.put("ctext", commentService.selectComment(commentDto));

		return result;
	}
	
	@PostMapping("/updateComment")
	@ResponseBody
	public Map<String, Integer> updateComment(CommentDTO commentDto, Authentication authentication) {
		LOGGER.info("addComment");
		Map<String, Integer> result = new HashMap<>();

		UserVo userVo = (UserVo) authentication.getPrincipal();
		commentDto.setUser_num(userVo.getUnum());

		result.put("result", commentService.updateComment(commentDto));

		return result;
	}
	
	@PostMapping("/deleteComment")
	@ResponseBody
	public Map<String, Integer> deleteComment(CommentDTO commentDto, Authentication authentication) {
		LOGGER.info("addComment");
		Map<String, Integer> result = new HashMap<>();

		UserVo userVo = (UserVo) authentication.getPrincipal();
		commentDto.setUser_num(userVo.getUnum());

		result.put("result", commentService.deleteComment(commentDto));

		return result;
	}
}
