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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import carrot.app.User.UserVo;
import carrot.app.dto.RecruitDTO;
import carrot.app.service.RecruitService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recruit")
public class RecruitController {

	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

	@Autowired
	private RecruitService recruitService;

	// 목록
	@RequestMapping("/recruitlist")
	public String recruitList(@RequestParam Map<String, Object> paramMap, Model model, Authentication authentication) {
		if (authentication == null) {
			return "redirect:/login";
		}

		int pageNo = 0;
		int pageSize = 4;

		UserVo userVo = (UserVo) authentication.getPrincipal(); // 시큐리티의 인증정보에서 회원정보를 가져오자.

		paramMap.put("userNum", userVo.getUnum());
		paramMap.put("pageNo", pageNo);
		paramMap.put("pageSize", pageSize);
		

		List<RecruitDTO> recruitList = recruitService.getRecruitList(paramMap);
		model.addAttribute("recruitList", recruitList);

		return "recruit/recruitlist";
	}

	// 프로필 이미지
	@GetMapping("/profileImage/{rnum}")
	public void profileImage(@PathVariable("rnum") int rnum, HttpServletResponse res) {

		res.setContentType("image/jpeg");

		RecruitDTO recruitDto = new RecruitDTO();
		recruitDto.setRnum(rnum);
		recruitDto = recruitService.getRecruit(recruitDto);

		if (recruitDto.getProfile() != null) {
			InputStream is = new ByteArrayInputStream(recruitDto.getProfile());
			try {
				IOUtils.copy(is, res.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 글쓰기
	@GetMapping("/recruitform")
	public String recruitForm(Model model) {
		return "recruit/recruitform";
	}

	@PostMapping("/recruitwrite")
	public String recruitWrite(RecruitDTO recruitDto, Authentication authentication, RedirectAttributes attr) {

		if (authentication == null) {
			return "redirect:/login";
		}

		UserVo userVo = (UserVo) authentication.getPrincipal(); // 시큐리티의 인증정보에서 회원정보를 가져오자.
		recruitDto.setUserNum(userVo.getUnum());

		try {
			recruitDto.setRphotos(recruitDto.getRphoto().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

		int result = recruitService.addRecruit(recruitDto);
		if (result == 0) {
			attr.addFlashAttribute("msg", "모집 게시판 글쓰기에 실패했습니다.");
		}

		return "redirect:/recruit/recruitlist";
	}

	// 상세보기
	@GetMapping("/recruitdetail/{rnum}")
	public String recruitDetail(@PathVariable("rnum") int rnum, Model model, Authentication authentication) {
		if (authentication == null) {
			return "redirect:/login";
		}
		
		UserVo userVo = (UserVo) authentication.getPrincipal(); // 시큐리티의 인증정보에서 회원정보를 가져오자.
		model.addAttribute("userVo", userVo);

		RecruitDTO recruitDto = new RecruitDTO();
		recruitDto.setRnum(rnum);
		recruitDto = recruitService.getRecruit(recruitDto);
		
		LOGGER.info("참여하기 카운트 : {}", recruitDto.getJoinCount());

		model.addAttribute("recruit", recruitDto);
		return "recruit/recruitdetail";
	}

	// 수정폼
	@PostMapping("/recruitmodify")
	public String modifyRecruit(RecruitDTO recruitDto, Model model, Authentication authentication) {
		if (authentication == null) {
			return "redirect:/login";
		}

		UserVo userVo = (UserVo) authentication.getPrincipal(); // 시큐리티의 인증정보에서 회원정보를 가져오자.

		recruitDto = recruitService.getRecruit(recruitDto);
		recruitDto.setUserNum(userVo.getUnum());

		model.addAttribute("recruit", recruitDto);

		return "recruit/recruitmodify";
	}

	// 수정
	@PostMapping("/recruitupdate")
	public String updateRecruit(RecruitDTO recruitDto, Authentication authentication) {
		if (authentication == null) {
			return "redirect:/login";
		}

		UserVo userVo = (UserVo) authentication.getPrincipal(); // 시큐리티의 인증정보에서 회원정보를 가져오자.
		recruitDto.setUserNum(userVo.getUnum());

		try {
			recruitDto.setRphotos(recruitDto.getRphoto().getBytes());
			recruitDto.setFilesize((int) recruitDto.getRphoto().getSize());
		} catch (IOException e) {
			e.printStackTrace();
		}

		recruitService.updateRecruit(recruitDto);

		return "redirect:/recruit/recruitlist";
	}

	// 삭제
	@PostMapping("/recruitdelete")
	public String deleteRecruit(RecruitDTO recruitDto, Authentication authentication) {
		if (authentication == null) {
			return "redirect:/login";
		}

		UserVo userVo = (UserVo) authentication.getPrincipal(); // 시큐리티의 인증정보에서 회원정보를 가져오자.
		recruitDto.setUserNum(userVo.getUnum());

		recruitService.deleteRecruit(recruitDto);

		return "redirect:/recruit/recruitlist";
	}

	// 참여하기
	@PostMapping("/recruitjoin")
	@ResponseBody
	public Map<String, Integer> joinRecruit(RecruitDTO recruitDto, Authentication authentication) {

		UserVo userVo = (UserVo) authentication.getPrincipal(); // 시큐리티의 인증정보에서 회원정보를 가져오자.
		recruitDto.setUserNum(userVo.getUnum());

		int check = recruitService.countJoin(recruitDto);

		Map<String, Integer> resultMap = new HashMap<>();

		int result = -1;
		if (check == 0) {
			result = recruitService.insertJoin(recruitDto);
		}

		resultMap.put("result", result);
		resultMap.put("count", recruitService.countRecruitJoin(recruitDto));

		return resultMap;
	}

	// 스크랩
	@PostMapping("/recruitscrap")
	@ResponseBody
	public Map<String, Integer> recruitlistScrap(RecruitDTO recruitDto, Authentication authentication) {

		UserVo userVo = (UserVo) authentication.getPrincipal(); // 시큐리티의 인증정보에서 회원정보를 가져오자.
		recruitDto.setUserNum(userVo.getUnum());

		int count = recruitService.countScrap(recruitDto);

		Map<String, Integer> resultMap = new HashMap<>();

		int result = -1;
		if (count == 0) {
			result = recruitService.insertScrap(recruitDto);
		}

		resultMap.put("result", result);

		return resultMap;
	}
}
