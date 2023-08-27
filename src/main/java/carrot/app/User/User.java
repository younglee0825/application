
package carrot.app.User;

import carrot.app.Profile.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.OneToOne;
import java.util.List;

@Getter
@EntityScan
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue
    @Column(name = "account")
    private Integer unum;
    private String uname;
    private String unick;
    private String uemail;
    private String uphone;
    private String upwd ;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Profile> profiles;
}
