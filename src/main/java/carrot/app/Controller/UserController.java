package carrot.app.Controller;

import carrot.app.User.UserService;
import carrot.app.User.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class UserController {

    @Autowired
    UserService userService;

 //local:8080/hello치면 이거 나옴
    @GetMapping("hello")
    public String hello(Model model){
        model.addAttribute("data", "hello!");
        return "test";
    }

    /**
     * 로그인 폼
     * @return
     */
    @RequestMapping("/login")
    public String login(Model model){
        return "login.html";
    }

    /**
    * 로그인 실패 폼
     * @return
     */
   @RequestMapping("/access_denied")
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
    public String userAccess(Model model, Authentication authentication) {
        //Authentication 객체를 통해 유저 정보를 가져올 수 있다.
        UserVo userVo = (UserVo) authentication.getPrincipal();  //userDetail 객체를 가져옴
        model.addAttribute("info", userVo.getUemail() +"의 "+ userVo.getUname()+ "님"); //유저 아이디
        return "user_access.html";
    }
    //내가 임시로 만들어 놓은 회원가입
    @GetMapping("/signUp")
    public String signUpForm() {
        return "signup";
    }

    @PostMapping("/signUp")
    public String signUp(UserVo userVo) {
        userService.joinUser(userVo);
        System.out.println(userVo);
        return "redirect:/login";
    }



}