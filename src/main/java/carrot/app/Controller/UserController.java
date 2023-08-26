package carrot.app.Controller;

import carrot.app.Exception.UserException;
import carrot.app.Profile.ProfileService;
import carrot.app.Profile.ProfileVo;
import carrot.app.User.User;
import carrot.app.User.UserService;
import carrot.app.User.UserVo;
import carrot.app.mapper.ProfileMapper;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final ProfileService profileService;

    @Autowired
    UserService userService;

    @Autowired
    private final ProfileMapper profileMapper;

 //local:8080/hello치면 이거 나옴
    @GetMapping("/hello")
    @ResponseBody
    public String hello(Model model){
        model.addAttribute("data", "hello!");
        return "test";
    }

    /**
     * 로그인 폼
     * @return
     */
    @GetMapping("/login")
    public String login(Model model){
        return "login.html";
    }

    /**
    * 로그인 실패 폼
     * @return
     */
   @GetMapping("/access_denied")
    public String accessDenied(@RequestParam(value = "error", required = false) String error,
                               @RequestParam(value = "exception", required = false) String exception,
                               Model model) {
       model.addAttribute("error", error);
       model.addAttribute("exception", exception);
       return "asset_denied";
   }

   /**
//     * 유저 페이지
//     * @param model
//     * @param authentication
//     * @return
//     */
   @GetMapping("/user_access")
   @ResponseBody
   public UserVo userAccess(Model model, Authentication authentication) {
       //Authentication 객체를 통해 유저 정보를 가져올 수 있다.
       UserVo userVo = (UserVo) authentication.getPrincipal();  //userDetail 객체를 가져옴
       System.out.println(userVo.getUnum());

       //ToDo: Profile num 적용 시키기 -> 만약에 ProfileVo 있으면 패스, 없으면 만들기
       ProfileVo profileVo = (profileService.getProfileByUserNum(userVo.getUnum()));
       if(profileVo.getUser_num()==null){
           profileVo.setUser_num(userVo.getUnum());
           profileVo.setUser_nick(userVo.getUnick());
           profileMapper.saveProfile(profileVo);
       }
       System.out.println(profileVo);

       model.addAttribute("info", userVo.getUemail() +"의 "+ userVo.getUname()+ "님"); //유저 아이디
       return userVo;
   }
    //내가 임시로 만들어 놓은 회원가입
    @GetMapping("/signUp")
    public String signUpForm() {
        return "signup";
    }

    @PostMapping("/signUp")
    @ResponseBody
    public UserVo signUp(UserVo userVo, @RequestParam("upwd") String upwd, @RequestParam("upwd2") String upwd2) throws Exception {
       //userPassword & userPassword2 check logic
       if(upwd.equals(upwd2)){
           userService.joinUser(userVo);
           System.out.println(userVo);
           return userVo;
       }else{
           throw new UserException("비밀번호가 일치하지 않습니다.");
       }
    }

    @GetMapping("/user-nickname-count")
    @ResponseBody
    public int countUserNickname(@RequestParam final String unick) {
        return userService.countUserByUserNickname(unick);
    }

    @GetMapping("/user-email-count")
    @ResponseBody
    public int countUserEmail(@RequestParam final String uemail) {
        return userService.countUserByUserEmail(uemail);
    }

}