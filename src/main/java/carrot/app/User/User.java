
package carrot.app.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer unum;
    private String uname;
    private String unick;
    private String uemail;
    private String uphone;
    private String upwd ;
}
