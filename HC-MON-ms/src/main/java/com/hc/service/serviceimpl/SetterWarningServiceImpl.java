package com.hc.service.serviceimpl;

import com.github.pagehelper.PageInfo;
import com.hc.config.RedisTemplateUtil;
import com.hc.entity.Userright;
import com.hc.mapper.laboratoryFrom.UserInfoFromMapper;
import com.hc.model.ClientInfoModel;
import com.hc.service.SetterWarningService;
import com.hc.utils.ApiResponse;
import com.hc.utils.TokenHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class SetterWarningServiceImpl implements SetterWarningService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RedisTemplateUtil redisTemplateUtil;

    @Autowired
    private UserInfoFromMapper userInfoFromMapper;


    private Userright getUserrightByToken(String token) throws Exception {
        String userId = TokenHelper.getUserID(token);
        Userright userRight= userInfoFromMapper.selectUserById(userId);
        return userRight;
    }

    private String getUserToken(String token) throws Exception {
        Userright userrightByToken = getUserrightByToken(token);
        String username = userrightByToken.getUsername();
        String userid = username + username;
        String userToken = TokenHelper.createToken(userid);
        redisTemplateUtil.boundValueOps(userid).set(userToken,1, TimeUnit.HOURS);
        return userToken;
    }

    private HttpEntity setHttpEntity(String token) throws Exception {
        String userToken = getUserToken(token);
        HttpHeaders headers = new HttpHeaders();
        headers.add("token",userToken);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        return httpEntity;
    }

    private ApiResponse<ClientInfoModel> checkUser(Userright userRight){
        ApiResponse<ClientInfoModel> apiResponse = new ApiResponse<ClientInfoModel>();
        if(userRight == null){
            apiResponse.setMessage("用户信息错误");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
        String usertype = userRight.getUsertype();
        if(!"admin".equals(usertype)){
            apiResponse.setMessage("非管理员用户不能操作设置警报");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
        return apiResponse;
    }

    /**
     * 获取警报的用户列表
     *
     * @param fuzzy
     * @param pageSize
     * @param pageNumber
     * @param hospitalcode
     * @return
     */
    @Override
    public ApiResponse<ClientInfoModel> getWarningUsers(String fuzzy, Integer pageSize, Integer pageNumber, String hospitalcode,String token)
            throws Exception {
        Userright userRight = getUserrightByToken(token);
        ApiResponse<ClientInfoModel> clientInfoModelApiResponse = checkUser(userRight);
        if(clientInfoModelApiResponse.getCode() == ApiResponse.FAILED){
            return clientInfoModelApiResponse;
        }
        ApiResponse<ClientInfoModel> apiResponse = new ApiResponse<ClientInfoModel>();
        HttpEntity httpEntity = setHttpEntity(token);
        String username = userRight.getUsername();
        StringBuffer url = new StringBuffer(
                "http://localhost:8097/api/clientInfo/selectUserInfoPage?pagesize="+pageSize+"&pagenum="+pageNumber
                        +"&setterWarningUsername="+username);
        if(StringUtils.isNotEmpty(fuzzy)){
            url.append("&fuzzy="+fuzzy);
        }
        url.append("&hospitalcode="+userRight.getHospitalcode());
        ResponseEntity<Object> forObject
                = restTemplate.exchange(
                url.toString(),
                HttpMethod.GET,
                httpEntity,
                Object.class);
        LinkedHashMap body = (LinkedHashMap)forObject.getBody();
        Integer code = (Integer)body.get("code");
        String message = (String)body.get("message");
        LinkedHashMap pageInfoMap = (LinkedHashMap)body.get("page");
        PageInfo pageInfo = new PageInfo();
        pageInfo.setEndRow((int)pageInfoMap.get("endRow"));
        int firstPage = (int)pageInfoMap.get("firstPage");
        pageInfo.setIsFirstPage(firstPage == 1 ? true:false);
        pageInfo.setHasNextPage((boolean)pageInfoMap.get("hasNextPage"));
        pageInfo.setHasPreviousPage((boolean)pageInfoMap.get("hasPreviousPage"));
        pageInfo.setIsFirstPage((boolean)pageInfoMap.get("isFirstPage"));
        pageInfo.setIsLastPage((boolean)pageInfoMap.get("isLastPage"));
        pageInfo.setLastPage((int)pageInfoMap.get("lastPage"));
        pageInfo.setList((List)pageInfoMap.get("list"));
        pageInfo.setNavigateFirstPage((int)pageInfoMap.get("navigateFirstPage"));
        pageInfo.setNavigateLastPage((int)pageInfoMap.get("navigateLastPage"));
        pageInfo.setNavigatePages((int)pageInfoMap.get("navigatePages"));
        List<Integer> navigatepageNums = (List)pageInfoMap.get("navigatepageNums");
        int[] nums = new int[navigatepageNums.size()];
        for(int i=0;i<navigatepageNums.size();i++){
            nums[i] = navigatepageNums.get(i);
        }
        pageInfo.setNavigatepageNums(nums);
        pageInfo.setNextPage((int)pageInfoMap.get("nextPage"));
        pageInfo.setPageNum((int)pageInfoMap.get("pageNum"));
        pageInfo.setPageSize((int)pageInfoMap.get("pageSize"));
        pageInfo.setPages((int)pageInfoMap.get("pages"));
        pageInfo.setPrePage((int)pageInfoMap.get("prePage"));
        pageInfo.setSize((int)pageInfoMap.get("size"));
        pageInfo.setStartRow((int)pageInfoMap.get("startRow"));
        int total = (int)pageInfoMap.get("total");
        pageInfo.setTotal((long)total);
        apiResponse.setPage(pageInfo);
        apiResponse.setCode(code);
        apiResponse.setMessage(message);
        return apiResponse;
    }

    /**
     * admin用户新增警报用户
     *
     * @param userright
     * @return
     */
    @Override
    public ApiResponse<Userright> addWarningUser(Userright userright,String token) throws Exception {
        Userright userRight = getUserrightByToken(token);
        ApiResponse<Userright> apiResponse = new ApiResponse<Userright>();
        if(userRight == null){
            apiResponse.setMessage("用户信息错误");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
        String usertype = userRight.getUsertype();
        if(!"admin".equals(usertype)){
            apiResponse.setMessage("非管理员用户不能操作设置警报");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
        HttpEntity httpEntity = setHttpEntity(token);
        StringBuffer url = new StringBuffer(
                "http://localhost:8097/api/clientInfo/addUser");
//        url.append("&hospitalcode="+userRight.getHospitalcode());
        HttpEntity<Userright> requestHttpEntity = new HttpEntity<Userright>(userright,httpEntity.getHeaders());
        ResponseEntity<Object> forObject
                = restTemplate.exchange(
                url.toString(),
                HttpMethod.POST,
                requestHttpEntity,
                Object.class);
        LinkedHashMap body = (LinkedHashMap)forObject.getBody();
        apiResponse.setCode((int)body.get("code"));
        apiResponse.setMessage((String)body.get("message"));
        return apiResponse;
    }

    /**
     * admin更新警报用户
     *
     * @param userright
     * @return
     */
    @Override
    public ApiResponse<Userright> updatedWarningUser(Userright userright,String token) throws Exception {
        Userright userRight = getUserrightByToken(token);
        ApiResponse<Userright> apiResponse = new ApiResponse<Userright>();
        if(userRight == null){
            apiResponse.setMessage("用户信息错误");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
        String usertype = userRight.getUsertype();
        if(!"admin".equals(usertype)){
            apiResponse.setMessage("非管理员用户不能操作设置警报");
            apiResponse.setCode(ApiResponse.FAILED);
            return apiResponse;
        }
        HttpEntity httpEntity = setHttpEntity(token);
        StringBuffer url = new StringBuffer(
                "http://localhost:8097/api/clientInfo/updateUser");
//        url.append("&hospitalcode="+userRight.getHospitalcode());
        HttpEntity<Userright> requestHttpEntity = new HttpEntity<Userright>(userright,httpEntity.getHeaders());
        ResponseEntity<Object> forObject
                = restTemplate.exchange(
                url.toString(),
                HttpMethod.POST,
                requestHttpEntity,
                Object.class);
        LinkedHashMap body = (LinkedHashMap)forObject.getBody();
        apiResponse.setCode((int)body.get("code"));
        apiResponse.setMessage((String)body.get("message"));
        return apiResponse;
    }
}
