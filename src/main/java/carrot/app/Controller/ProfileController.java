package carrot.app.Controller;

import carrot.app.Profile.ProfileService;
import carrot.app.Profile.ProfileVo;
import carrot.app.User.UserService;
import carrot.app.User.UserVo;
import carrot.app.dto.PostDTO;
import carrot.app.dto.RecruitDTO;
import carrot.app.mapper.MapMapper;
import carrot.app.mapper.UserMapper;
import carrot.app.service.PostService;
import carrot.app.service.RecruitService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    @Autowired
    private ProfileService profileService;
    @Autowired
    private RecruitService recruitService;
    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;

    @Autowired
    private final UserMapper userMapper;
    @Autowired
    private MapMapper mapMapper;



    @GetMapping("/mypage")
//   @ResponseBody
    public String myPage(Model model) {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserVo) {
            UserVo userVo = (UserVo) authentication.getPrincipal();
            ProfileVo profileVo = (profileService.getProfileByUserNum(userVo.getUnum()));
            System.out.println(profileVo);

            String profileImageUrl = profileService.getProfileImageUrl(profileVo.getUser_num());
            System.out.println(profileImageUrl);


            model.addAttribute("nickname", userVo.getUnick() + "님"); //유저 아이디
            model.addAttribute("goal", profileVo.getGoal());
            model.addAttribute("profileImageUrl", profileImageUrl);
            if(profileVo.getImage()==null){
                model.addAttribute("accountNumber","default-profile");
            }else{
                model.addAttribute("accountNumber",profileVo.getUser_num());
            }
            return "/mypage";
        } else {
            return "redirect:/login";
        }
    }


    @GetMapping("/mypage/editPro")
    public String editProfile(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserVo userVo = (UserVo) authentication.getPrincipal();
        ProfileVo profileVo = (profileService.getProfileByUserNum(userVo.getUnum()));

        model.addAttribute("nickname", userVo.getUnick());
        model.addAttribute("goal", profileVo.getGoal());
        if(profileVo.getImage()==null){
            model.addAttribute("accountNumber","default-profile");
        }else{
            model.addAttribute("accountNumber",profileVo.getUser_num());
        }

        return "/mypage/editPro";
    }


    @PostMapping("/mypage/editPro")
    public String editPro(
            @RequestParam(value="newNick", required = false) String nickname,
            @RequestParam(value = "inputGoal", required = false) String goal,
            @RequestPart(value="file",required = false) MultipartFile file,
            Authentication authentication
    ) {
        UserVo userVo = (UserVo) authentication.getPrincipal();
        userMapper.updateProfileNickname(userVo.getUnick(),nickname);
        profileService.saveUserGoal(goal, nickname); // 목표 저장 또는 업데이트
        MediaType contentType = MediaType.parseMediaType(file.getContentType());
        System.out.println(contentType);
        if (file != null) {
            profileService.saveImage(file); // 이미지 업로드
        }
        System.out.println(nickname);
        System.out.println(goal);
        System.out.println(file);

        return "redirect:/mypage";
        // UserVo 객체를 JSON 형태로 반환하지 않음
    }
    /**
     *
     * 사용자가 작성한 게시물
     *
     * */
    @GetMapping("/mypage/content")
    public String postListAuthentication (Model model, Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }

        UserVo userVo = (UserVo) authentication.getPrincipal();
        model.addAttribute("userVo", userVo);

        List<PostDTO> postListAuthentication = postService.getPostListAuthentication(0, 4, userVo.getUnum());
        model.addAttribute("postList", postListAuthentication);
        model.addAttribute("accountNumber",userVo.getUnum());

        return "post/postlist";
    }
    /**
     * 내가 작성한 모집글
     * */
    @GetMapping("/mypage/recruit")
    public String recruitListAuthentication (Model model, Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }

        UserVo userVo = (UserVo) authentication.getPrincipal();
        model.addAttribute("userVo", userVo);

        List<RecruitDTO> recruitList = recruitService.getRecruitAuthentication(0, 4, userVo.getUnum());
        model.addAttribute("recruitList", recruitList);
        model.addAttribute("accountNumber",userVo.getUnum());


        return "recruit/recruitlist";
    }

    @GetMapping("/mypage/part")
    public String recruitpart (Model model, Authentication authentication){
        if (authentication == null){
            return "redirect:/login";
        }

        UserVo userVo = (UserVo) authentication.getPrincipal();
        model.addAttribute("userVo", userVo);

        List<RecruitDTO> recruitListForUser = recruitService.getRecruitListForUser(userVo.getUnum());
        model.addAttribute("recruitList", recruitListForUser);
        model.addAttribute("accountNumber", userVo.getUnum());

        return "recruit/recruitlist";
    }


}


