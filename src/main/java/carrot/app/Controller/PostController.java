package carrot.app.Controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import carrot.app.User.UserVo;
import carrot.app.dto.CommentDTO;
import carrot.app.dto.PostDTO;
import carrot.app.service.CommentService;
import carrot.app.service.PostService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

	// Console 창에 해당 로그가 찍힌다. 따라서 프로그램이 오류 발생 시 어디서 어떤 이유로 오류가 발생하는지 알 수 있어 이슈 처리가 용이하다.
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

	@Autowired
	private PostService postService;

	@Autowired
	private CommentService commentService;

	@GetMapping("/postlist")
	public String postList(Model model, Authentication authentication) {
		if (authentication == null) {
			return "redirect:/login";
		}

		UserVo userVo = (UserVo) authentication.getPrincipal(); // 시큐리티의 인증정보에서 회원정보를 가져오자.
		model.addAttribute("userVo", userVo);

		List<PostDTO> postList = postService.getPostList(0, 4);
		model.addAttribute("postList", postList);

		return "post/postlist";
	}

	// 글쓰기 폼으로 이동
	@GetMapping("/postform")
	public String postForm(Model model, Authentication authentication) {
		if (authentication == null) {
			return "redirect:/login";
		}
		PostDTO postDto = new PostDTO();
		UserVo userVo = (UserVo) authentication.getPrincipal(); // 시큐리티의 인증정보에서 회원정보를 가져오자.
		model.addAttribute("username", userVo.getUsername()); // 회원정보에서 이름만 가져와서 Model에 추가한다.
		model.addAttribute("post", postDto);
		return "post/postform";
	}

	@PostMapping("/addpost")
	public String postInsert(PostDTO postDto, Authentication authentication) throws IOException {
		LOGGER.info("PostDTO >> {}", postDto);
		if (authentication == null) {
			return "redirect:/login";
		}
		UserVo userVo = (UserVo) authentication.getPrincipal(); // 회원정보
		postDto.setUser_num(userVo.getUnum());
		postDto.setFileContent(postDto.getImageFile().getBytes());
		postService.addPost(postDto);

		return "redirect:/post/postlist";
	}

	@GetMapping("/postcomment/{pnum}")
	public String postComment(@PathVariable("pnum") int pnum, Model model, Authentication authentication) {
		if (authentication == null) {
			return "redirect:/login";
		}

		UserVo userVo = (UserVo) authentication.getPrincipal(); // 회원정보
		model.addAttribute("userVo", userVo);

		PostDTO postDto = new PostDTO();
		postDto.setPnum(pnum);
		PostDTO dto = postService.getPost(postDto);
		model.addAttribute("post", dto);

		List<CommentDTO> commentList = commentService.getCommentList(pnum);
		model.addAttribute("commentList", commentList);

		return "/post/postcomment";
	}

	@GetMapping("/viewImage/{pnum}")
	public void imageView(@PathVariable("pnum") int pnum, HttpServletResponse res) {
		res.setContentType("image/jpeg");
		PostDTO postDto = new PostDTO();
		postDto.setPnum(pnum);
		PostDTO dto = postService.getPost(postDto);
		InputStream is = new ByteArrayInputStream(dto.getFileContent());
		try {
			IOUtils.copy(is, res.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@GetMapping("/profileImage/{pnum}")
	public void profileImage(@PathVariable("pnum") int pnum, HttpServletResponse res) {
		res.setContentType("image/jpeg");
		PostDTO postDto = new PostDTO();
		postDto.setPnum(pnum);
		PostDTO dto = postService.getPost(postDto);
		if (dto != null && dto.getProfileContent() != null) {
			InputStream is = new ByteArrayInputStream(dto.getProfileContent());
			try {
				IOUtils.copy(is, res.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@GetMapping("/commentProfile/{cnum}")
	public void commentProfile(@PathVariable("cnum") int cnum, HttpServletResponse res) {
		res.setContentType("image/jpeg");
		CommentDTO dto = commentService.getComment(cnum);
		if (dto != null && dto.getProfile() != null) {
			InputStream is = new ByteArrayInputStream(dto.getProfile());
			try {
				IOUtils.copy(is, res.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@PostMapping("/addlikes")
	@ResponseBody
	public Map<String, Integer> addLikes(PostDTO postDto, Authentication authentication) {
		Map<String, Integer> result = new HashMap<>();
		UserVo userVo = (UserVo) authentication.getPrincipal();
		postDto.setUser_num(userVo.getUnum());
		int count = postService.selectLikes(postDto);
		int num = 0;
		if (count == 0) {
			postService.addLikes(postDto);
			num = postService.countlikes(postDto);
		} else {
			num = -1;
		}
		result.put("result", num);
		return result;
	}
	
	@PostMapping("/ismypost")
	@ResponseBody
	public Map<String, Integer> isMyPost(PostDTO postDto, Authentication authentication) {
		Map<String, Integer> result = new HashMap<>();
		UserVo userVo = (UserVo) authentication.getPrincipal();
		postDto.setUser_num(userVo.getUnum());
		result.put("result", postService.isMyPost(postDto));
		return result;
	}
	
	@PostMapping("/postModify")
	public String postModify(PostDTO postDto, Model model, Authentication authentication) {
		if (authentication == null) {
			return "redirect:/login";
		}
		postDto = postService.getPost(postDto);
		UserVo userVo = (UserVo) authentication.getPrincipal(); // 시큐리티의 인증정보에서 회원정보를 가져오자.
		model.addAttribute("username", userVo.getUsername()); // 회원정보에서 이름만 가져와서 Model에 추가한다.
		model.addAttribute("post", postDto);
		LOGGER.info("POST DTO >> {}", postDto);
		return "post/postmodify";
	}
	
	@PostMapping("/postUpdate")
	public String postUpdate(PostDTO postDto, Model model, Authentication authentication) {
		if (authentication == null) {
			return "redirect:/login";
		}
		UserVo userVo = (UserVo) authentication.getPrincipal(); // 시큐리티의 인증정보에서 회원정보를 가져오자.
		postDto.setUser_num(userVo.getUnum());
		if (postDto.getImageFile() != null) {
			try {
				postDto.setFileContent(postDto.getImageFile().getBytes());
				postDto.setFilesize((int)postDto.getImageFile().getSize());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		LOGGER.info("UPDATE POST DTO >> {}", postDto);
		int result = postService.modifyPost(postDto);
		if (result > 0) {
			return "redirect:/post/postcomment/" + postDto.getPnum();
		}
		return "redirect:/post/postlist";
	}
	
	@PostMapping("/postDelete")
	public String postDelete(PostDTO postDto, Authentication authentication, RedirectAttributes rttr) {
		UserVo userVo = (UserVo) authentication.getPrincipal();
		postDto.setUser_num(userVo.getUnum());
		int result = postService.deletePost(postDto);
		rttr.addFlashAttribute("deleteResult", result);
		return "redirect:/post/postlist";
	}
}
