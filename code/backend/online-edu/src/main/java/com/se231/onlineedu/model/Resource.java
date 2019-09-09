package com.se231.onlineedu.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * Resource Class
 *
 * Resource class is used to map the resource file contained in a specific course.
 *
 * @author zhe li
 * @date 2019/7/1
 */
@Entity
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String url;

    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public Resource(@NotBlank String url, ResourceType resourceType, String title) {
        this.url = url;
        this.resourceType = resourceType;
        this.title = title;
    }

    public Resource() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return id.equals(resource.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
