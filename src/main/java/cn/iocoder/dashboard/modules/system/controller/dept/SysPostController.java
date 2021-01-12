package cn.iocoder.dashboard.modules.system.controller.dept;

import cn.iocoder.dashboard.common.enums.CommonStatusEnum;
import cn.iocoder.dashboard.common.pojo.CommonResult;
import cn.iocoder.dashboard.common.pojo.PageResult;
import cn.iocoder.dashboard.modules.system.controller.dept.vo.post.*;
import cn.iocoder.dashboard.modules.system.convert.dept.SysPostConvert;
import cn.iocoder.dashboard.modules.system.dal.mysql.dataobject.dept.SysPostDO;
import cn.iocoder.dashboard.modules.system.service.dept.SysPostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static cn.iocoder.dashboard.common.pojo.CommonResult.success;

@Api(tags = "岗位 API")
@RestController
@RequestMapping("/system/post")
public class SysPostController {

    @Resource
    private SysPostService postService;

    @ApiOperation(value = "获取岗位精简信息列表", notes = "只包含被开启的岗位，主要用于前端的下拉选项")
    @GetMapping("/list-all-simple")
    public CommonResult<List<SysPostSimpleRespVO>> listSimplePosts() {
        // 获得岗位列表，只要开启状态的
        List<SysPostDO> list = postService.listPosts(null, Collections.singleton(CommonStatusEnum.ENABLE.getStatus()));
        // 排序后，返回给前端
        list.sort(Comparator.comparing(SysPostDO::getSort));
        return success(SysPostConvert.INSTANCE.convertList02(list));
    }

    @ApiOperation("获得岗位分页列表")
    @GetMapping("/page")
//    @PreAuthorize("@ss.hasPermi('system:post:list')")
    public CommonResult<PageResult<SysPostRespVO>> pagePosts(@Validated SysPostPageReqVO reqVO) {
        return success(SysPostConvert.INSTANCE.convertPage(postService.pagePosts(reqVO)));
    }

    @ApiOperation("新增岗位")
    @PostMapping("/create")
//    @PreAuthorize("@ss.hasPermi('system:post:add')")
//    @Log(title = "岗位管理", businessType = BusinessType.INSERT)
    public CommonResult<Long> createPost(@Validated @RequestBody SysPostCreateReqVO reqVO) {
        Long postId = postService.createPost(reqVO);
        return success(postId);
    }

    @ApiOperation("修改岗位")
    @PostMapping("/update")
//    @PreAuthorize("@ss.hasPermi('system:post:edit')")
//    @Log(title = "岗位管理", businessType = BusinessType.UPDATE)
    public CommonResult<Boolean> updatePost(@Validated @RequestBody SysPostUpdateReqVO reqVO) {
        postService.updatePost(reqVO);
        return success(true);
    }

    @ApiOperation("删除岗位")
    @PostMapping("/delete")
//    @PreAuthorize("@ss.hasPermi('system:post:remove')")
//    @Log(title = "岗位管理", businessType = BusinessType.DELETE)
    public CommonResult<Boolean> deletePost(@RequestParam("id") Long id) {
        postService.deletePost(id);
        return success(true);
    }

    @ApiOperation("获得岗位信息")
    @ApiImplicitParam(name = "id", value = "岗位编号", readOnly = true, example = "1024")
//    @PreAuthorize("@ss.hasPermi('system:post:query')")
    @GetMapping(value = "/get")
    public CommonResult<SysPostRespVO> getPost(@RequestParam("id") Long id) {
        return success(SysPostConvert.INSTANCE.convert(postService.getPost(id)));
    }

//    @Log(title = "岗位管理", businessType = BusinessType.EXPORT)
//    @PreAuthorize("@ss.hasPermi('system:post:export')")
//    @GetMapping("/export")
//    public AjaxResult export(SysPost post) {
//        List<SysPost> list = postService.selectPostList(post);
//        ExcelUtil<SysPost> util = new ExcelUtil<SysPost>(SysPost.class);
//        return util.exportExcel(list, "岗位数据");
//    }
}