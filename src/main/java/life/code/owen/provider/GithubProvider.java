package life.code.owen.provider;


import com.alibaba.fastjson.JSON;
import java.io.IOException;
import life.code.owen.dto.AccessTokenDTO;
import life.code.owen.dto.GithubUserDTO;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Component;

@Component
public class GithubProvider {

  public String getAccessToken(AccessTokenDTO accessTokenDTO) {
    MediaType mediaType
        = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();
    RequestBody body = RequestBody.create(JSON.toJSONString(accessTokenDTO), mediaType);
    Request request = new Request.Builder()
        .url("https://github.com/login/oauth/access_token")
        .post(body)
        .build();
    try (Response response = client.newCall(request).execute()) {
      String msg = response.body().string();
      String access_token = msg.split("&")[0].split("=")[1];
      System.out.println("msg : " + msg);
      System.out.println("access_token : " + access_token);
      return access_token;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }


  public GithubUserDTO getGithubUser(String token) {
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder()
        .url("https://api.github.com/user?access_token=" + token)
        .build();
    GithubUserDTO githubUserDTO = null;
    try {
      Response response = client.newCall(request).execute();
      String msg = response.body().string();
      System.out.println("getGithubUser msg: " + msg);
      githubUserDTO = JSON.parseObject(msg, GithubUserDTO.class);
      return githubUserDTO;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return githubUserDTO;
  }
}
