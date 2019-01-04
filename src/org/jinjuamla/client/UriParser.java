/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.client;

import org.jinjuamla.network.datatypes.Endpoint;
import org.jinjuamla.network.datatypes.IpAddr;
import org.jinjuamla.network.enums.consts.NetworkFlag;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

/**
 *
 * @author psammand
 */
public class UriParser {
    private static final Pattern IP_PATTERN = Pattern.compile("(\\[[0-9a-f:]+\\]|[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})");
    String _uri;
    String _uriPath;
    String _uriQuery;
    int port;
    IpAddr ipAddr = null;

    public UriParser(String uri) {
        this._uri = uri;
    }

    public boolean parse() {
        String coapUri = _uri;
        if (!_uri.contains("://")) {
            coapUri = "coap://" + _uri;
        }

        try {
            URI uri = new URI(coapUri);
            String host = uri.getHost() == null ? "localhost" : uri.getHost();
            InetAddress destAddress = InetAddress.getByName(host);

            ipAddr = new IpAddr(destAddress.getAddress(), IpAddr.IPV4);
            port = uri.getPort();

            String path = uri.getPath();
            if (path != null && path.length() > 1) {
                _uriPath = path;
            }

            String query = uri.getQuery();
            if (query != null) {
                _uriQuery = query;
            }
        }
        catch (URISyntaxException | UnknownHostException ex) {
            return false;
        }
        
        return true;
    }

    public String getUriPath() {
        return _uriPath;
    }

    public String getUriQuery() {
        return _uriQuery;
    }

    public Endpoint getEndpoint() {
        Endpoint endpoint = new Endpoint();

        endpoint.setPort(port);
        endpoint.setIpAddr(ipAddr);
        endpoint.setNetworkFlag(NetworkFlag.IPV4);

        return endpoint;
    }

}
