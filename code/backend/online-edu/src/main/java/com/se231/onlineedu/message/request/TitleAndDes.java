package com.se231.onlineedu.message.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Title And Description form class
 *
 * A form contains tile and description.
 *
 * @author Zhe Li
 *
 * @date 2019/7/3
 */
@ApiModel(value = "包含名称和简介的表单")
public class TitleAndDes {
    @ApiModelProperty(value = "名称",dataType = "String",allowableValues = "长度在3-30")
    @NotBlank
    @Size(min = 3,max = 30)
    private String title;

    @ApiModelProperty(value = "详情",allowableValues = "长度不超过1000，可为空")
    @Size(max=1000)
    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TitleAndDes() {
    }

    public TitleAndDes(@NotBlank @Size(min = 3, max = 30) String title, @Size(max = 1000) String description) {
        this.title = title;
        this.description = description;
    }
}
