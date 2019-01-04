/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.network.enums.consts;

/**
 *
 * @author psammand
 */
public final class NetworkFlag {
public static final int NONE              = 0x0000;
public static final int SCOPE_IF_LOCAL    = 0x0001;
public static final int SCOPE_LINK_LOCAL  = 0x0002;
public static final int SCOPE_REALM_LOCAL = 0x0003;
public static final int SCOPE_ADMIN_LOCAL = 0x0004;
public static final int SCOPE_SITE_LOCAL  = 0x0005;
public static final int SCOPE_ORG_LOCAL   = 0x0008;
public static final int SCOPE_GLOBAL      = 0x000E;
public static final int IPV4              = 0x0010;
public static final int IPV6              = 0x0020;
public static final int SECURE            = 0x0040;
public static final int MULTICAST         = 0x0080;
    
}
