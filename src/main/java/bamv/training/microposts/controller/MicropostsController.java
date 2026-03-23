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
        List<MicropostDto> followsMicropostList = micropostService.searchUserMicropost(userId, page); // 自ユーザーのマイクロポスト
        int myFollowNumber = followService.findFollowNumber(userId); // 自ユーザーのフォロー数
        int myFollowerNumber = followService.findFollowerNumber(userId); // 自ユーザーのフォロワー数

        /* View ⇔ Controller */
        model.addAttribute("myUserId", user.getUserId());
        model.addAttribute("myUserName", user.getName());
        model.addAttribute("myFollowNumber", myFollowNumber);
        model.addAttribute("myFollowerNumber", myFollowerNumber);
        model.addAttribute("followsMicropostList", followsMicropostList);
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
    String userlist(Model model, HttpServletRequest httpServletRequest, @RequestParam(name = "page", defaultValue = "1") int page){
        /* ユーザー認証情報からユーザIDを取得 */
        String userId = httpServletRequest.getRemoteUser();

        List<UserDto> users = userService.findAllUsers(userId, page);
        model.addAttribute("users", users);
        model.addAttribute("page", page);
        return "userlist";
    }

    @PostMapping("/follow")
    String follow(HttpServletRequest httpServletRequest,
                  @RequestParam String followedUser,
                  @RequestParam(value = "returnTo", defaultValue = "/userlist") String returnTo){
        /* ユーザー認証情報からユーザIDを取得 */
        String userId = httpServletRequest.getRemoteUser();

        /* フォロー対象ユーザーのIDを取得 */
        String followedUserId = userService.findUser(followedUser).getUserId();

        followService.addFollow(userId, followedUserId);
        return "redirect:" + returnTo;
    }

    @PostMapping("/delete-follow")
    String deleteFollow(HttpServletRequest httpServletRequest,
                        @RequestParam String followedUser,
                        @RequestParam(value = "returnTo", defaultValue = "/userlist") String returnTo){
        /* ユーザー認証情報からユーザIDを取得 */
        String userId = httpServletRequest.getRemoteUser();

        /* フォロー解除対象ユーザーのIDを取得 */
        String followedUserId = userService.findUser(followedUser).getUserId();

        followService.deleteFollow(userId, followedUserId);
        return "redirect:" + returnTo;
    }

    @GetMapping("/userprofile")
    String userProfile(Model model,
                       HttpServletRequest httpServletRequest,
                       @RequestParam String userId,
                       @RequestParam(name = "page", defaultValue = "1") int page) {

        /* ユーザー認証情報からユーザIDを取得 */
        String myUserId = httpServletRequest.getRemoteUser();

        /* Model ⇔ Controller */
        UserDto user = userService.findUser(userId); // ユーザー情報
        List<MicropostDto> followsMicropostList = micropostService.searchUserMicropost(userId, page); // ユーザーのマイクロポスト
        int followNumber = followService.findFollowNumber(userId); // ユーザーのフォロー数
        int followerNumber = followService.findFollowerNumber(userId); // ユーザーのフォロワー数
        boolean isFollowing = followService.isFollowing(myUserId, userId); //フォローしているか

        /* View ⇔ Controller */
        model.addAttribute("userName", user.getName());
        model.addAttribute("userId", user.getUserId());
        model.addAttribute("followNumber", followNumber);
        model.addAttribute("followerNumber", followerNumber);
        model.addAttribute("followsMicropostList", followsMicropostList);
        model.addAttribute("isFollowing", isFollowing);
        model.addAttribute("page", page);

        return "userprofile";
    }

    @GetMapping("/followlist")
    String followList(Model model,
                      @RequestParam String userId,
                      @RequestParam(name = "page", defaultValue = "1") int page){

        List<UserDto> followingUsers = userService.findFollowingUser(userId, page);
        model.addAttribute("userId", userId);
        model.addAttribute("followingUsers", followingUsers);
        model.addAttribute("page", page);

        return "followlist";
    }

    @GetMapping("/followerlist")
    String followerList(Model model,
                        @RequestParam String userId,
                        @RequestParam(name = "page", defaultValue = "1") int page){

        List<UserDto> followedUsers = userService.findFollowedUser(userId, page);
        model.addAttribute("userId", userId);
        model.addAttribute("followedUsers", followedUsers);
        model.addAttribute("page", page);

        return "followerlist";
    }
}