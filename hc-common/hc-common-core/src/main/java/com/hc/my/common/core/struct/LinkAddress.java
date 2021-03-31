package com.hc.my.common.core.struct;

/**
 * @author LiuZhiHao
 * @date 2019/10/23 14:43
 * 描述:
 **/
import com.hc.my.common.core.constant.enums.NP;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

@Getter
@NoArgsConstructor
public class LinkAddress implements Serializable {

    private static final long serialVersionUID = 6765982216345874154L;

    public LinkAddress(String address) {
        try {
            URI uri = new URI(address);
            this.protocol = NP.from(uri.getScheme());
            this.schema = uri.getScheme() + "://";
            this.domain = schema + uri.getAuthority();
            this.ssl = (this.protocol.getSsl()).equals(uri.getScheme());
            this.host = uri.getHost();
            this.port = uri.getPort();
            if (null != uri.getUserInfo()) {
                String principal = uri.getUserInfo();
                int index = principal.indexOf(':');
                if (index >= 0) {
                    this.username = principal.substring(0, index);
                    this.password = principal.substring(index + 1);
                }
            }
            this.path = uri.getPath();
            this.query = uri.getQuery();
        } catch (URISyntaxException e) {

        }
    }

    public String translate(NP np) {
        return (this.ssl ? np.getSsl() : np.getSchema()) +
                "://" +
                (null == this.username || null == this.password ? "" : this.username + ":") +
                (null == this.username || null == this.password ? "" : this.password + "@") +
                (null == this.host ? "unknown" : this.host) +
                (null == this.port ? 80 : ":" + this.port) +
                (null == this.path ? "" : this.path) +
                (null == this.query ? "" : "?" + this.query);
    }

    @Override
    public String toString() {
        return translate(this.protocol);
    }

    private boolean ssl;
    private String domain;
    private NP protocol;
    private String schema;
    private String host;
    private Integer port;
    private String username;
    private String password;
    private String path;
    private String query;
}
