package carrot.app.Profile;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;



@Data
public class ProfileVo {

    @Id
    @GeneratedValue
    @Column(name = "account")
    private String user_nick;
    private Integer user_num;


    @Column(name = "myPage")
    private Long profile;
    private String goal;

//    public ProfileVo(MyPage mypage){
//        this.goal = myPage.getgoal();
//    }

}