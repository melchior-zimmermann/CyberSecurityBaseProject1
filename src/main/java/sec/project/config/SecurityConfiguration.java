package sec.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import sec.project.repository.SignupRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private SignupRepository signupRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // no real security at the moment
               
       http.authorizeRequests()
              .anyRequest().permitAll();
              
        /*
        http.authorizeRequests()
                .antMatchers("/h2-console/*").permitAll()
                .antMatchers("/loggedHome").authenticated().anyRequest().permitAll().and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/loggedHome")
                    .failureUrl("/badLogin")
                    .permitAll();
      http
      .logout()
          .logoutRequestMatcher(new AntPathRequestMatcher("/logout"));*/
    }

/*    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
*/
}
