package com.se231.onlineedu.controller;


import com.se231.onlineedu.model.CoursePrototype;
import com.se231.onlineedu.model.Resource;
import com.se231.onlineedu.model.ResourceType;
import com.se231.onlineedu.service.CoursePrototypeService;
import com.se231.onlineedu.util.SaveFileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author liu
 * @date 2019/07/11
 */
@Api(tags = "与课程中的资源有关的控制类")
@RestController
@RequestMapping("/api/coursePrototypes/{courseProtoTypeId}")
public class ResourceController {

    @Autowired
    public CoursePrototypeService coursePrototypeService;


    @ApiOperation("上传资源")
    @PostMapping("/{resourceType}")
    public String uploadFiles(@RequestParam("resource")MultipartFile multipartFile, @PathVariable Long courseProtoTypeId, @PathVariable String resourceType) throws IOException {
        String suffix = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        String url = SaveFileUtil.saveFile(multipartFile, suffix);
        Resource resource = new Resource(url, ResourceType.valueOf(resourceType.toUpperCase()), multipartFile.getOriginalFilename());
        coursePrototypeService.saveResource(courseProtoTypeId, resource);
        return url;
    }

    @ApiOperation("删除资源")
    @DeleteMapping("/{name}")
    public String deleteResource(@PathVariable String name,@PathVariable Long courseProtoTypeId){
        coursePrototypeService.deleteResources(courseProtoTypeId,name);
        return name;
    }

    @ApiOperation("获取资源")
    @GetMapping("/resources")
    public List<Resource> getResources(@PathVariable Long courseProtoTypeId){
        return coursePrototypeService.getCoursePrototypeInfo(courseProtoTypeId).getResources();
    }

}
