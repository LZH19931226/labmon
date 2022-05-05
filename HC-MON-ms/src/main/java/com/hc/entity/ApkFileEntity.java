package com.hc.po;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@ToString
@Table(name = "apkfileentity")
@Entity
public class ApkFileEntity {


    @Id
    @Column(name = "id")
    private  String id;

    @Column(name = "code")
    private  String code;

    @Column(name = "filename")
    private  String filename;

    @Column(name = "info")
    private String info;

    @Column(name = "forceUpdate")
    private String forceUpdate;

    @Column(name = "date")
    private Date date;

    @Column(name = "url")
    private String url;

    @Column(name = "versionname")
    private String versionname;

    @Column(name = "size")
    private String size;

}
