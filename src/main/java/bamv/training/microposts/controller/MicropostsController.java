package bamv.training.microposts.controller;

import bamv.training.microposts.dto.MicropostDto;
import bamv.training.microposts.dto.UserDto;
import bamv.training.microposts.form.MicropostForm;
import bamv.training.microposts.form.UserForm;
import bamv.training.microposts.service.FollowService;
import bamv.training.microposts.service.MicropostService;
import bamv.training.microposts.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MicropostsController {
    @Autowired
    private UserService userService;

    @Autowired
    private MicropostService micropostService;

    @Autowired
    private FollowService followService;

    @GetMapping("/micropostshome")
    String micropostshome(Model model, @ModelAttribute MicropostForm micropostForm, BindingResult bindingResult, HttpServletRequest httpServletRequest, @RequestParam(name = "page", defaultValue = "1") int page) {
        /* ユーザー認証情報からユーザIDを取得 */
        String userId = httpServletRequest.getRemoteUser();

        /* Model ⇔ Controller */
        UserDto user = userService.findUser(userId); // 自ユーザー情報
        int myMicropostNumber = micropostService.countMicropostNumber(userId); // 自ユーザーマイクロポスト数
        List<MicropostDto> followsMicropostList = micropostService.searchFollowMicropost(userId, page); // 自ユーザーおよびフォローのマイクロポスト
        int myFollowNumber = followService.findFollowNumber(userId); // 自ユーザーのフォロー数
        int myFollowerNumber = followService.findFollowerNumber(userId); // 自ユーザーのフォロワー数

        /* View ⇔ Controller */
        model.addAttribute("myUserId", user.getUserId());
        model.addAttribute("myUserName", user.getName());
        model.addAttribute("myMicropostsNumber", myMicropostNumber);
        model.addAttribute("myFollowNumber", myFollowNumber);
        model.addAttribute("myFollowerNumber", myFollowerNumber);
        model.addAttribute("followsMicropostList", followsMicropostList);
        model.addAttribute("page", page);

        return "micropostshome";
    }

    @PostMapping("/postnewmicropost")
    String postnewmicropost(Model model, HttpServletRequest httpServletRequest, @ModelAttribute @Valid MicropostForm micropostForm, BindingResult bindingResult) {
        /* ユーザー認証情報からユーザIDを取得 */
        String userId = httpServletRequest.getRemoteUser();

        if (bindingResult.hasErrors())
            return micropostshome(model, micropostForm, bindingResult, httpServletRequest, 1);

        micropostService.createNewMicropost(userId, micropostForm.getContent());

        return "redirect:/micropostshome";
    }

    @GetMapping("/login")
    String login(Model model) {
        return "login";
    }

    @GetMapping("/myprofile")
    String myprofile(Model model, HttpServletRequest httpServletRequest, @RequestParam(name = "page", defaultValue = "1") int page) {
        /* ユーザー認証情報からユーザIDを取得 */
        String userId = httpServletRequest.getRemoteUser();

        /* Model ⇔ Controller */
        UserDto user = userService.findUser(userId); // 自ユーザー情報
        List<MicropostDto> micropostList = micropostService.searchUserMicropost(userId, page); // 自ユーザーのマイクロポスト
        int myFollowNumber = followService.findFollowNumber(userId); // 自ユーザーのフォロー数
        int myFollowerNumber = followService.findFollowerNumber(userId); // 自ユーザーのフォロワー数

        /* View ⇔ Controller */
        model.addAttribute("myUserId", user.getUserId()); //フォローリスト画面に渡したいためIDを追加で付加
        model.addAttribute("myUserName", user.getName());
        model.addAttribute("myFollowNumber", myFollowNumber);
        model.addAttribute("myFollowerNumber", myFollowerNumber);
        model.addAttribute("micropostList", micropostList);
        model.addAttribute("page", page);

        return "myprofile";
    }

    @GetMapping("/signup")
    String signup(Model model, @ModelAttribute UserForm userForm, BindingResult bindingResult) {
        return "signup";
    }

    @PostMapping("/signup")
    String postsignup(Model model, @ModelAttribute @Valid UserForm userForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return signup(model, userForm, bindingResult);

        userService.createNewUser(userForm.getUserId(), userForm.getUserName(), userForm.getPassword());

        return "redirect:/login";
    }

    @GetMapping("/userlist")
    String userlist(Model model,
                    HttpServletRequest httpServletRequest,
                    @RequestParam(name = "page", defaultValue = "1") int page){
        /* ログインユーザのIDを取得 */
        String myUserId = httpServletRequest.getRemoteUser();

        /* Model ⇔ Controller */
        List<UserDto> users = userService.findAllUsers(myUserId, page); //自身を除くユーザのリスト

        /* View ⇔ Controller */
        model.addAttribute("myUserId", myUserId);
        model.addAttribute("users", users);
        model.addAttribute("page", page);

        return "userlist";
    }

    @PostMapping("/follow")
    String follow(HttpServletRequest httpServletRequest,
                  @RequestParam String followedUser,
                  /* リダイレクト先 */
                  @RequestParam(value = "returnTo", defaultValue = "/userlist") String returnTo){
        /* ユーザー認証情報からユーザIDを取得 */
        String myUserId = httpServletRequest.getRemoteUser();

        /* フォロー対象ユーザーのIDを取得 */
        String followedUserId = userService.findUser(followedUser).getUserId();

        followService.addFollow(myUserId, followedUserId);

        return "redirect:" + returnTo;
    }

    @PostMapping("/delete-follow")
    String deleteFollow(HttpServletRequest httpServletRequest,
                        @RequestParam String followedUser,
                        /* リダイレクト先 */
                        @RequestParam(value = "returnTo", defaultValue = "/userlist") String returnTo){
        /* ユーザー認証情報からユーザIDを取得 */
        String myUserId = httpServletRequest.getRemoteUser();

        /* フォロー解除対象ユーザのIDを取得 */
        String followedUserId = userService.findUser(followedUser).getUserId();

        followService.deleteFollow(myUserId, followedUserId);

        return "redirect:" + returnTo;
    }

    @GetMapping("/userprofile")
    String userprofile(Model model,
                       HttpServletRequest httpServletRequest,
                       @RequestParam String userId,
                       @RequestParam(name = "page", defaultValue = "1") int page) {

        /* ログインユーザのIDを取得 フォロー関係のチェックのため */
        String myUserId = httpServletRequest.getRemoteUser();

        /* Model ⇔ Controller */
        UserDto user = userService.findUser(userId); // ユーザー情報
        List<MicropostDto> micropostList = micropostService.searchUserMicropost(userId, page); // ユーザーのマイクロポスト
        int followNumber = followService.findFollowNumber(userId); // ユーザーのフォロー数
        int followerNumber = followService.findFollowerNumber(userId); // ユーザーのフォロワー数

        /* View ⇔ Controller */
        model.addAttribute("myUserId", myUserId);
        model.addAttribute("user", user);
        model.addAttribute("followNumber", followNumber);
        model.addAttribute("followerNumber", followerNumber);
        model.addAttribute("micropostList", micropostList);
        model.addAttribute("page", page);

        return "userprofile";
    }

    @GetMapping("/followlist")
    String followlist(Model model,
                      HttpServletRequest httpServletRequest,
                      @RequestParam String userId,
                      @RequestParam(name = "page", defaultValue = "1") int page){

        /* ログインユーザーのIDを取得 */
        String myUserId = httpServletRequest.getRemoteUser();

        /* Model ⇔ Controller */
        String userName = userService.findUser(userId).getName(); //ユーザー名
        List<UserDto> followingUsers = userService.findFollowingUsers(myUserId, userId, page); //フォローユーザーのリスト

        /* View ⇔ Controller */
        model.addAttribute("myUserId", myUserId); //自身のフォローボタンを表示させない処理のため使用
        model.addAttribute("userId", userId);
        model.addAttribute("userName", userName);
        model.addAttribute("followingUsers", followingUsers);
        model.addAttribute("page", page);

        return "followlist";
    }

    @GetMapping("/followerlist")
    String followerlist(Model model,
                        HttpServletRequest httpServletRequest,
                        @RequestParam String userId,
                        @RequestParam(name = "page", defaultValue = "1") int page){

        /* ログインユーザーのIDを取得 */
        String myUserId = httpServletRequest.getRemoteUser();

        /* Model ⇔ Controller */
        String userName = userService.findUser(userId).getName(); //ユーザー名
        List<UserDto> followedUsers = userService.findFollowedUsers(myUserId, userId, page); //フォロワーのリスト

        /* View ⇔ Controller */
        model.addAttribute("myUserId", myUserId); //自身のフォローボタンを表示させない処理のため使用
        model.addAttribute("userId", userId);
        model.addAttribute("userName", userName);
        model.addAttribute("followedUsers", followedUsers);
        model.addAttribute("page", page);

        return "followerlist";
    }
}