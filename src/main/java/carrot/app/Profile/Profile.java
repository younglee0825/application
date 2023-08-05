package carrot.app.Profile;

import carrot.app.User.User;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.ibatis.annotations.One;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;

import javax.persistence.*;

@EntityScan
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Profile implements Serializable {
    private static final long serialVersionUID = 6039389327573333653L;

    @Id
    @GeneratedValue
    @Column(name = "account")
    private Integer user_num;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "unick", referencedColumnName = "unick") // 외래키 매핑
    private User user;
    private String user_nick;

    @Column(name="myPage")
    private Long profile;
    private String goal;
}

