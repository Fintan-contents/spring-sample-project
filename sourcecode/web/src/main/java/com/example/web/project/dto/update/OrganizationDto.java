package com.example.web.project.dto.update;

import org.springframework.beans.BeanUtils;

import com.example.common.generated.model.Organization;

/**
 * 組織を表すDto。
 *
 * @author sample
 */
public class OrganizationDto {

    /** 組織ID */
    private Integer organizationId;

    /** 組織名 */
    private String organizationName;

    /**
     * 組織IDを取得する。
     *
     * @return 組織ID
     */
    public Integer getOrganizationId() {
        return organizationId;
    }

    /**
     * 組織IDを設定する。
     *
     * @param id 組織ID
     */
    public void setOrganizationId(Integer id) {
        this.organizationId = id;
    }

    /**
     * 組織名を取得する。
     *
     * @return 組織名
     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     * 組織名を設定する。
     *
     * @param organizationName 組織名
     */
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    /**
     * Modelからインスタンスを生成する。
     * 
     * @param organization 変換元のModel
     * @return インスタンス
     */
    public static OrganizationDto fromOrganization(Organization organization) {
        OrganizationDto organizationDto = new OrganizationDto();
        BeanUtils.copyProperties(organization, organizationDto);
        return organizationDto;
    }
}
