package carrot.app.User;

import carrot.app.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.NullLiteral;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService{
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
    Date time = new Date();
    String localTime = format.format(time);

    @Autowired
    private final UserMapper userMapper;


    @Transactional
    public void joinUser(UserVo userVo){
        System.out.println(userVo);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userVo.setUpwd(passwordEncoder.encode(userVo.getPassword()));
        userVo.setAuth("USER");
        userMapper.saveUser(userVo);
        //System.out.println(userVo);
    }

    @Override
    //인자를 무조건 username으로 설절해야됨
    public UserVo loadUserByUsername(String username) throws UsernameNotFoundException {
        UserVo userVo = userMapper.getUserAccount(username);
        System.out.println(userVo);
        if (userVo == null){
            //System.out.println(userVo);
            throw new UsernameNotFoundException("User not authorized.");
        }
        return userVo;
    }

    public int countUserByUserNickname(String unick) {

        return userMapper.countUserByNickname(unick);
    }
    public int countUserByUserEmail(String uemail) {

        return userMapper.countUserByEmail(uemail);
    }
}