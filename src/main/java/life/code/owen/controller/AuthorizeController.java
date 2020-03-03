package life.code.owen.controller;


import life.code.owen.dto.AccessTokenDTO;
import life.code.owen.dto.GithubUserDTO;
import life.code.owen.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {

  @Autowired
  private GithubProvider githubProvider;

  @Value("${github.client.id}")
  private String clientId;
  @Value("${github.client.secret}")
  private String clientSecret;
  @Value("${github.redirect.uri}")
  private String redirectUri;

  @GetMapping("/hello")
  public String hello(
      @RequestParam(name = "name", required = false, defaultValue = "ggm") String name,
      Model model) {
    model.addAttribute("name", name);
    return "greeting";
  }

  @GetMapping("/githubcallback")
  public String githubcallback(@RequestParam(name = "state") String state,
      @RequestParam(name = "code") String code) {
    AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
    accessTokenDTO.setClient_id(clientId);
    accessTokenDTO.setClient_secret(clientSecret);
    accessTokenDTO.setCode(code);
    accessTokenDTO.setState(state);
    accessTokenDTO.setRedirect_uri(redirectUri);
    String accessToken = githubProvider.getAccessToken(accessTokenDTO);
    GithubUserDTO githubUser = githubProvider.getGithubUser(accessToken);
    System.out.println("githubUser: "+githubUser.getLogin());
    return "index";
  }

}
