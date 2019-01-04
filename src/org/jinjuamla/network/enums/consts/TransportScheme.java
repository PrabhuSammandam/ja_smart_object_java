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
public final class TransportScheme {

    public static final int NONE = 0x0000;
    public static final int COAP = 0x0001;
    public static final int COAPS = 0x0002;
    public static final int COAP_TCP = 0x0004;
    public static final int COAPS_TCP = 0x0008;
    public static final int HTTP = 0x0010;
    public static final int HTTPS = 0x0020;
    public static final int COAP_RFCOMM = 0x0040;
    public static final int COAP_GATT = 0x0080;
    public static final int COAP_NFC = 0x0100;
    public static final int COAP_RA = 0x0200;
    public static final int ALL = 0xFFFF;

}
