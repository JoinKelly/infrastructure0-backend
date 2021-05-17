package com.infrastructure.backend.model.common.request;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.util.Date;

public class ProjectCreateRequest {

    @NotBlank(message = "title is mandatory")
    private String title;

    private String description;

    @NotBlank(message = "StartDate is mandatory")
    private Date startDate;

    @NotBlank(message = "EndDate is mandatory")
    private Date endDate;

    private Integer leaderId;

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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Integer leaderId) {
        this.leaderId = leaderId;
    }
}
