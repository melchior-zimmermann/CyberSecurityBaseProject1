package sec.project.controller;
import java.util.List;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;
import org.springframework.security.core.Authentication;

@Controller
public class SignupController {

    @Autowired
    private SignupRepository signupRepository;
    
   // @Autowired
    //private PasswordEncoder passwordEncoder;

    
    @RequestMapping("*")
    public String defaultMapping() {
        return "redirect:/welcome";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm() {
        return "form";
    }

    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String loadWelcome() {
        return "welcome";
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loadLogin() {
        return "login";
    }
    
    @RequestMapping(value = "/badLogin", method = RequestMethod.GET)
    public String loadBadLogin(Model model, String name) {
        model.addAttribute("name", name);
        return "badLogin";
    }
    
    @RequestMapping(value = "/manageThing", method = RequestMethod.GET)
    public String loadManageThing(Model model, String name) {
        model.addAttribute("name", name);
        return "manageThing";
    }
    
    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public String loadAccount(Model model, String name) {
        model.addAttribute("name", name);
        return "account";
    }
    
    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(@RequestParam String name, @RequestParam String address, @RequestParam String password) {
        if(signupRepository.findByName(name)==null){
            //String pass = passwordEncoder.encode(password);
            signupRepository.save(new Signup(name, address, password));
            return "done";
        }else{
            return "nameTaken";
        }
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String submitForm(@RequestParam String name, @RequestParam String password) {
        Signup signup = signupRepository.findByName(name);
        if(signup==null || (!signup.getPassword().equals(password))){
            //String pass = passwordEncoder.encode(password);
            return "redirect:badLogin?name="+name;
        }else{
            return "redirect:/loggedHome?name="+name+"&password="+password;
        }
    }
    
    @RequestMapping(value = "/manageThing", method = RequestMethod.POST)
    public String submitFormThing(@RequestParam String name, @RequestParam String thing, @RequestParam String share) {
        Signup signup = signupRepository.findByName(name);
        if(signup==null){
            //String pass = passwordEncoder.encode(password);
            return "redirect:badLogin?name="+name;
        }else{
            signup.setThing(thing);
            if(share.equals("yes")){
                signup.setShareThing(true);
            }else{
                signup.setShareThing(false);
            }
            String password = signup.getPassword();
            signupRepository.save(signup);
            return "redirect:/loggedHome?name="+name+"&password="+password;
        }
    }

    @RequestMapping(value = "/loggedHome", method = RequestMethod.GET)
    public String loadLoggedHome(Model model, String name, String password) {
        Signup user;
        user = signupRepository.findByName(name);
        if(user==null || !user.getPassword().equals(password)){
            return "redirect:/badLogin?name="+name;
        }

        model.addAttribute("thing", user.getThing());
        model.addAttribute("name", name);

        List<Signup> allUsers = signupRepository.findAll();
        List<Signup> sharingUsers = new ArrayList<Signup>();
        for(Signup thisOne : allUsers){
            if(thisOne.getShareThing()){
                sharingUsers.add(thisOne);
                
            }
        }
        model.addAttribute("users", sharingUsers);
        return "loggedHome";
    }

    
    @RequestMapping(value = "/loggedHome", method = RequestMethod.POST)
    public String submitFormHome(@RequestParam String toDo, @RequestParam String name) {
        Signup user;
        user = signupRepository.findByName(name);
        if(user==null){
            return "redirect:/badLogin?name="+name;
        }

        if(toDo.equals("manage")){
            return "redirect:/manageThing?name="+name;
        }else if(toDo.equals("account")){
            return "redirect:/account?name="+name;
        }else if(toDo.equals("logout")){
            return "/logout";
        }
        
        return "/logout";
        
    }
    
    @RequestMapping(value = "/account", method = RequestMethod.POST)
    public String submitFormAccount(@RequestParam String address, @RequestParam String oldPass, @RequestParam String newPass, @RequestParam String name) {
        Signup user;
        user = signupRepository.findByName(name);
        if(user==null || !user.getPassword().equals(oldPass)){
            return "redirect:/badLogin?name="+name;
        }
        String pass = oldPass;

        if(!newPass.equals("")){
            user.setPassword(newPass);
            pass = newPass;
        }
        if(!address.equals("")){
            user.setAddress(address);
        }
        
        signupRepository.save(user);
        
        return "redirect:/loggedHome?name="+name+"&password="+pass;
        
    }
}
