package carrot.app.Controller;

import carrot.app.User.User;
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

    @Autowired
    UserService userService;

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
    public UserVo signUp(@RequestBody UserVo userVo) {
        userService.joinUser(userVo);
        System.out.println(userVo);
        return userVo;
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