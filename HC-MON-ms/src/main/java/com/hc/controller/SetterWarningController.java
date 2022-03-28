package com.hc.controller;

import com.hc.web.config.RedisTemplateUtil;
import com.hc.entity.Userright;
import com.hc.model.ClientInfoModel;
import com.hc.service.SetterWarningService;
import com.hc.utils.ApiResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/clientInfo", produces = {MediaType.APPLICATION_JSON_VALUE})
public class SetterWarningController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;
    @Autowired
    private SetterWarningService setterWarningService;
    @GetMapping("/setterUserWarningPage")
    public ApiResponse<ClientInfoModel>  selectUserInfoPage(@ApiParam(name = "fuzzy", value = "模糊查询参数", required = false)
                                                                    @RequestParam(value = "fuzzy", required = false) String fuzzy,
                                                                 @ApiParam(name = "pageSize", value = "每页显示条目数", required = true)
                                                                    @RequestParam(value = "pageSize", required = true) Integer pageSize,
                                                                 @ApiParam(name = "pageNumber", value = "当前页码数", required = true)
                                                                    @RequestParam(value = "pageNumber", required = true) Integer pageNumber,
                                                                 @ApiParam(name = "hospitalcode", value = "模糊查询参数", required = false)
                                                                    @RequestParam(value = "hospitalcode", required = false) String hospitalcode,
                                                                 HttpServletRequest httpRequest) throws Exception {
        String token = httpRequest.getHeader("token");
        ApiResponse<ClientInfoModel> warningUsers =
                setterWarningService.getWarningUsers(fuzzy, pageSize, pageNumber, hospitalcode, token);
        return warningUsers;
    }


    @PostMapping("/addWarningUser")
    public ApiResponse<Userright> addUser(@RequestBody @Valid Userright userright,HttpServletRequest httpRequest) throws Exception {
        return setterWarningService.addWarningUser(userright,httpRequest.getHeader("token"));
    }

    @PostMapping("/updateWarningUser")
    @ApiOperation(value = "修改用户信息")
    public ApiResponse<Userright> updateUser(@RequestBody @Valid Userright userright,HttpServletRequest httpRequest) throws Exception {
        return setterWarningService.updatedWarningUser(userright,httpRequest.getHeader("token"));

    }
}
