package com.vmcomms.ptemagic.service.dto;


import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

import com.vmcomms.ptemagic.domain.enumeration.TestType;

/**
 * A DTO for the ExamType entity.
 */
public class QuestionCategoryDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @Column(name = "description")
    private String description;
    
    @Column(name = "type")
    private String type;
    
    @Column(name = "group_type")
    private String groupType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
    
    
}
