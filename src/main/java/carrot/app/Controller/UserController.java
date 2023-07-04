package carrot.app.Controller;

import carrot.app.User.UserService;
import carrot.app.User.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserController {

    //@Autowired - @RequiredArgsConstructor가 있으면 @Autowired를 선언하지 않아도 됨.
    // 대신 멤버를 private final로 선언할 것.
    private final UserService userService;

 //local:8080/hello치면 이거 나옴
    @GetMapping("hello")
    @ResponseBody
    public String hello(Model model){
        model.addAttribute("data", "hello!");
        return "test";
    }

    /**
     * 로그인 폼
     * @return
     */
    @RequestMapping("/login")
    @ResponseBody
    public String login(Model model){
        return "login.html";
    }

    /**
    * 로그인 실패 폼
     * @return
     */
   @RequestMapping("/access_denied")
    @ResponseBody
    public String accessDenied() {
        return "asset_denied.html";
   }

   /**
//     * 유저 페이지
//     * @param model
//     * @param authentication
//     * @return
//     */
    @RequestMapping("/user_access")
    @ResponseBody
    public String userAccess(Model model, Authentication authentication) {
        //Authentication 객체를 통해 유저 정보를 가져올 수 있다.
        UserVo userVo = (UserVo) authentication.getPrincipal();  //userDetail 객체를 가져옴
        model.addAttribute("info", userVo.getUemail() +"의 "+ userVo.getUname()+ "님"); //유저 아이디
        return "user_access.html";
    }
    //내가 임시로 만들어 놓은 회원가입
    @GetMapping("/signUp")
    @ResponseBody
    public String signUpForm() {
        return "signup";
    }

    @PostMapping("/signUp")
    @ResponseBody
    public String signUp(@RequestBody UserVo userVo) {
        userService.joinUser(userVo);
        return userVo.getUemail();
        //System.out.println(userVo);
        //return "redirect:/login";
    }
    /**
     * 아이디 중복 체크
     */
    @GetMapping("/user-email-count")
    @ResponseBody
    public int countUserEmail(@RequestParam final String uemail) {
        return userService.countUserByUserEmail(uemail);
    }
    /**
     * 닉네임 중복 체크
     */
    @GetMapping("/user-nickname-count")
    @ResponseBody
    public int countUserNickname(@RequestParam final String unick) {
        return userService.countUserByUserNickname(unick);
    }



}
