
package com.zp.mobile91q.category;

/**
 * 站点信息
 */
public class WebsiteInfo {

    // 站点id
    private int id;

    // 站点名
    private String name;

    // 站点logo
    private String logo;

    // 站点域名
    private String website;

    private String websiteCode;

    public WebsiteInfo(int id, String name, String logo, String webUrl,
            String websiteCode) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.website = webUrl;
        this.websiteCode = websiteCode;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogo() {
        return logo;
    }

    public String getWebsite() {
        return website;
    }

    public String getWebsiteCode() {
        return websiteCode;
    }

    public void setWebsiteCode(String websiteCode) {
        this.websiteCode = websiteCode;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String toString() {
        return "WebsiteInfo [name=" + name + ", logo=" + logo
                + ", websiteCode=" + websiteCode + "]";
    }

}
