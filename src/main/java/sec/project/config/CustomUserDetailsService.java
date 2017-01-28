package sec.project.config;

import java.util.Arrays;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private SignupRepository signupRepository;
    //@Autowired
    //private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        // this data would typically be retrieved from a database
       //String pass = passwordEncoder.encode("secret");
       Signup test = new Signup("ted", "here", "secret");
       test.setThing("Something crazy!!!");
       test.setShareThing(true);
       signupRepository.save(test);
               
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Signup account  = signupRepository.findByName(name);
        if (account == null) {
            throw new UsernameNotFoundException("No such user: " + name);
        }

        return new org.springframework.security.core.userdetails.User(
                account.getName(),
                account.getPassword(),
                true,
                true,
                true,
                true,
                Arrays.asList(new SimpleGrantedAuthority("USER")));
    }
}
