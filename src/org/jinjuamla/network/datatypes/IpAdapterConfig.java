/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.network.datatypes;

/**
 *
 * @author psammand
 */
public class IpAdapterConfig {

    public boolean isIpv4Enabled()
    {
        return isIpv4McastEnabled || isIpv4UcastEnabled;
    }

    public boolean isIpv6Enabled()
    {
        return isIpv6McastEnabled || isIpv6UcastEnabled;
    }
    
    public int getIpv4UcastPort() {
        return ipv4UcastPort;
    }

    public void setIpv4UcastPort(int ipv4UcastPort) {
        this.ipv4UcastPort = ipv4UcastPort;
    }

    public int getIpv6UcastPort() {
        return ipv6UcastPort;
    }

    public void setIpv6UcastPort(int ipv6UcastPort) {
        this.ipv6UcastPort = ipv6UcastPort;
    }

    public int getCurrentIpv4UcastPort() {
        return currentIpv4UcastPort;
    }

    public void setCurrentIpv4UcastPort(int currentIpv4UcastPort) {
        this.currentIpv4UcastPort = currentIpv4UcastPort;
    }

    public int getCurrentIpv6UcastPort() {
        return currentIpv6UcastPort;
    }

    public void setCurrentIpv6UcastPort(int currentIpv6UcastPort) {
        this.currentIpv6UcastPort = currentIpv6UcastPort;
    }

    public int getIpv4McastPort() {
        return ipv4McastPort;
    }

    public void setIpv4McastPort(int ipv4McastPort) {
        this.ipv4McastPort = ipv4McastPort;
    }

    public int getIpv6McastPort() {
        return ipv6McastPort;
    }

    public void setIpv6McastPort(int ipv6McastPort) {
        this.ipv6McastPort = ipv6McastPort;
    }

    public boolean isIsIpv4UcastEnabled() {
        return isIpv4UcastEnabled;
    }

    public void setIsIpv4UcastEnabled(boolean isIpv4UcastEnabled) {
        this.isIpv4UcastEnabled = isIpv4UcastEnabled;
    }

    public boolean isIsIpv6UcastEnabled() {
        return isIpv6UcastEnabled;
    }

    public void setIsIpv6UcastEnabled(boolean isIpv6UcastEnabled) {
        this.isIpv6UcastEnabled = isIpv6UcastEnabled;
    }

    public boolean isIsIpv4McastEnabled() {
        return isIpv4McastEnabled;
    }

    public void setIsIpv4McastEnabled(boolean isIpv4McastEnabled) {
        this.isIpv4McastEnabled = isIpv4McastEnabled;
    }

    public boolean isIsIpv6McastEnabled() {
        return isIpv6McastEnabled;
    }

    public void setIsIpv6McastEnabled(boolean isIpv6McastEnabled) {
        this.isIpv6McastEnabled = isIpv6McastEnabled;
    }

    int ipv4UcastPort = 0;
    int ipv6UcastPort = 0;

    int currentIpv4UcastPort = 0;
    int currentIpv6UcastPort = 0;

    int ipv4McastPort = 5683;
    int ipv6McastPort = 5683;

    boolean isIpv4UcastEnabled = false;
    boolean isIpv6UcastEnabled = false;
    boolean isIpv4McastEnabled = false;
    boolean isIpv6McastEnabled = false;
}
