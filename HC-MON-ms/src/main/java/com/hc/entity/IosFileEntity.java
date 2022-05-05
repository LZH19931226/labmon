package com.hc.po;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@ToString
@Table(name = "iosfileentity")
@Entity
public class IosFileEntity {

    @Id
    @Column(name = "id")
    private  String id;

    @Column(name = "code")
    private  String code;

    @Column(name = "filename")
    private  String filename;

    @Column(name = "versionname")
    private String versionname;

    @Column(name = "info")
    private String info;

    @Column(name = "date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern ="yyyy-MM-dd" )
    private Date date;

}
