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
public final class AdapterType {

    public static final int DEFAULT = 0x0000;
    public static final int IP = 0x0001;
    public static final int TCP = 0x0002;
    public static final int NFC = 0x0004;
    public static final int REMOTE_ACCESS = 0x0010;
    public static final int BT_LE = 0x0020;
    public static final int BT_EDR = 0x0040;
    public static final int ALL = 0xFFFF;

    public static final int MAX = 6;

}
