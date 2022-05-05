package com.hc.po;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "warningcontact")
public class Warningcontact implements Serializable {
    /**
     * ID
     */
    @Id
    private Integer id;

    /**
     * 联系人编码
     */
    private String contactcode;

    /**
     * 联系人姓名
     */
    private String contactname;

    /**
     * 联系人语音称呼
     */
    private String contactnamevoice;

    /**
     * 手机号
     */
    private String phonenumber;

    private static final long serialVersionUID = 1L;

    /**
     * 获取ID
     *
     * @return id - ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取联系人编码
     *
     * @return contactcode - 联系人编码
     */
    public String getContactcode() {
        return contactcode;
    }

    /**
     * 设置联系人编码
     *
     * @param contactcode 联系人编码
     */
    public void setContactcode(String contactcode) {
        this.contactcode = contactcode;
    }

    /**
     * 获取联系人姓名
     *
     * @return contactname - 联系人姓名
     */
    public String getContactname() {
        return contactname;
    }

    /**
     * 设置联系人姓名
     *
     * @param contactname 联系人姓名
     */
    public void setContactname(String contactname) {
        this.contactname = contactname;
    }

    /**
     * 获取联系人语音称呼
     *
     * @return contactnamevoice - 联系人语音称呼
     */
    public String getContactnamevoice() {
        return contactnamevoice;
    }

    /**
     * 设置联系人语音称呼
     *
     * @param contactnamevoice 联系人语音称呼
     */
    public void setContactnamevoice(String contactnamevoice) {
        this.contactnamevoice = contactnamevoice;
    }

    /**
     * 获取手机号
     *
     * @return phonenumber - 手机号
     */
    public String getPhonenumber() {
        return phonenumber;
    }

    /**
     * 设置手机号
     *
     * @param phonenumber 手机号
     */
    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public enum InnerColumn {
        id("id"),
        contactcode("contactcode"),
        contactname("contactname"),
        contactnamevoice("contactnamevoice"),
        phonenumber("phonenumber");

        private final String column;

        public String value() {
            return this.column;
        }

        public String getValue() {
            return this.column;
        }

        InnerColumn(String column) {
            this.column = column;
        }

        public String desc() {
            return this.column + " DESC";
        }

        public String asc() {
            return this.column + " ASC";
        }
    }
}