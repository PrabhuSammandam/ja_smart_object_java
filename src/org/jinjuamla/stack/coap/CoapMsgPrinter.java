package org.jinjuamla.stack.coap;

import java.util.ArrayList;

import static org.jinjuamla.stack.consts.CoapMsgConsts.COAP_MSG_TYPE_ACK;
import static org.jinjuamla.stack.consts.CoapMsgConsts.COAP_MSG_TYPE_CON;
import static org.jinjuamla.stack.consts.CoapMsgConsts.COAP_MSG_TYPE_NON;
import static org.jinjuamla.stack.consts.CoapMsgConsts.COAP_MSG_TYPE_RST;
import static org.jinjuamla.stack.consts.CoapMsgConsts.*;

/**
 * Created by psammand on 3/19/2018.
 */
public class CoapMsgPrinter {

    public static final String COAP_MSG_TYPE_STRING_CON = "CON";
    public static final String COAP_MSG_TYPE_STRING_NON = "NON";
    public static final String COAP_MSG_TYPE_STRING_ACK = "ACK";
    public static final String COAP_MSG_TYPE_STRING_RST = "RST";

    public static final String MSG_CODE_STRING_EMPTY = "EMPTY:0";
    public static final String MSG_CODE_STRING_GET = "GET:1";
    public static final String MSG_CODE_STRING_POST = "POST:2";
    public static final String MSG_CODE_STRING_PUT = "PUT:3";
    public static final String MSG_CODE_STRING_DEL = "DEL:4";
    public static final String MSG_CODE_STRING_CREATED = "CREATED:2.01";
    public static final String MSG_CODE_STRING_DELETED = "DELETED:2.02";
    public static final String MSG_CODE_STRING_VALID = "VALID:2.03";
    public static final String MSG_CODE_STRING_CHANGED = "CHANGED:2.04";
    public static final String MSG_CODE_STRING_CONTENT = "CONTENT:2.05";
    public static final String MSG_CODE_STRING_CONTINUE = "CONTINUE:2.31";
    public static final String MSG_CODE_STRING_BAD_REQ = "BAD_REQ:4.00";
    public static final String MSG_CODE_STRING_UNAUTHORIZED = "UNAUTHORIZED:4.01";
    public static final String MSG_CODE_STRING_BAD_OPT = "BAD_OPT:4.02";
    public static final String MSG_CODE_STRING_FORBIDDEN = "FORBIDDEN:4.03";
    public static final String MSG_CODE_STRING_NOT_FOUND = "NOT_FOUND:4.04";
    public static final String MSG_CODE_STRING_METHOD_NOT_ALLOWED = "METHOD_NOT_ALLOWED:4.05";
    public static final String MSG_CODE_STRING_NOT_ACCEPTABLE = "NOT_ACCEPTABLE:4.06";
    public static final String MSG_CODE_STRING_REQ_ENTITY_INCOM = "REQ_ENTITY_INCOM:4.08";
    public static final String MSG_CODE_STRING_PRECOND_FAILED = "PRECOND_FAILED:4.12";
    public static final String MSG_CODE_STRING_REQ_ENTITY_LARGE = "REQ_ENTITY_LARGE:4.13";
    public static final String MSG_CODE_STRING_UNSUP_CONTENT_FORMAT = "UNSUP_CONTENT_FORMAT:4.15";
    public static final String MSG_CODE_STRING_SERVER_ERROR = "SERVER_ERROR:5.00";
    public static final String MSG_CODE_STRING_NOT_IMPL = "NOT_IMPL:5.01";
    public static final String MSG_CODE_STRING_BAD_GATEWAY = "BAD_GATEWAY:5.02";
    public static final String MSG_CODE_STRING_SERVICE_UNAVAIL = "SERVICE_UNAVAIL:5.03";
    public static final String MSG_CODE_STRING_GATEWAY_TIMEOUT = "GATEWAY_TIMEOUT:5.04";
    public static final String MSG_CODE_STRING_PROXY_NOT_SUPP = "PROXY_NOT_SUPP:5.05";

    public static final String OPTION_STRING_URI_PATH = "U_PATH";
    public static final String OPTION_STRING_URI_QUERY = "U_QUERY";
    public static final String OPTION_STRING_SIZE1 = "SZ1";
    public static final String OPTION_STRING_SIZE2 = "SZ2";
    public static final String OPTION_STRING_BLOCK1 = "BLK1";
    public static final String OPTION_STRING_BLOCK2 = "BLK2";
    public static final String OPTION_STRING_CONTENT_FORMAT = "CF";
    public static final String OPTION_STRING_ACCEPT_FORMAT = "AF";

    public static String get_msg_type_string(int msgType) {
        switch (msgType) {
            case COAP_MSG_TYPE_CON:
                return COAP_MSG_TYPE_STRING_CON;
            case COAP_MSG_TYPE_NON:
                return COAP_MSG_TYPE_STRING_NON;
            case COAP_MSG_TYPE_ACK:
                return COAP_MSG_TYPE_STRING_ACK;
            case COAP_MSG_TYPE_RST:
                return COAP_MSG_TYPE_STRING_RST;
        }
        return "";
    }

    public static String get_msg_code_string(int msgCode) {
        switch (msgCode) {
            case MSG_CODE_EMPTY:
                return (MSG_CODE_STRING_EMPTY);
            case MSG_CODE_GET:
                return (MSG_CODE_STRING_GET);
            case MSG_CODE_POST:
                return (MSG_CODE_STRING_POST);
            case MSG_CODE_PUT:
                return (MSG_CODE_STRING_PUT);
            case MSG_CODE_DELETE:
                return (MSG_CODE_STRING_DEL);
            case MSG_CODE_CREATED_201:
                return (MSG_CODE_STRING_CREATED);
            case MSG_CODE_DELETED_202:
                return (MSG_CODE_STRING_DELETED);
            case MSG_CODE_VALID_203:
                return (MSG_CODE_STRING_VALID);
            case MSG_CODE_CHANGED_204:
                return (MSG_CODE_STRING_CHANGED);
            case MSG_CODE_CONTENT_205:
                return (MSG_CODE_STRING_CONTENT);
            case MSG_CODE_CONTINUE_231:
                return (MSG_CODE_STRING_CONTINUE);
            case MSG_CODE_BAD_REQUEST_400:
                return (MSG_CODE_STRING_BAD_REQ);
            case MSG_CODE_UNAUTHORIZED_401:
                return (MSG_CODE_STRING_UNAUTHORIZED);
            case MSG_CODE_BAD_OPTION_402:
                return (MSG_CODE_STRING_BAD_OPT);
            case MSG_CODE_FORBIDDEN_403:
                return (MSG_CODE_STRING_FORBIDDEN);
            case MSG_CODE_NOT_FOUND_404:
                return (MSG_CODE_STRING_NOT_FOUND);
            case MSG_CODE_METHOD_NOT_ALLOWED_405:
                return (MSG_CODE_STRING_METHOD_NOT_ALLOWED);
            case MSG_CODE_NOT_ACCEPTABLE_406:
                return (MSG_CODE_STRING_NOT_ACCEPTABLE);
            case MSG_CODE_REQ_ENTITY_INCOMPLETE_408:
                return (MSG_CODE_STRING_REQ_ENTITY_INCOM);
            case MSG_CODE_PRECONDITION_FAILED_412:
                return (MSG_CODE_STRING_PRECOND_FAILED);
            case MSG_CODE_REQ_ENTITY_TOO_LARGE_413:
                return (MSG_CODE_STRING_REQ_ENTITY_LARGE);
            case MSG_CODE_UNSUPPORTED_CF_415:
                return (MSG_CODE_STRING_UNSUP_CONTENT_FORMAT);
            case MSG_CODE_INTERNAL_SERVER_ERROR_500:
                return (MSG_CODE_STRING_SERVER_ERROR);
            case MSG_CODE_NOT_IMPLEMENTED_501:
                return (MSG_CODE_STRING_NOT_IMPL);
            case MSG_CODE_BAD_GATEWAY_502:
                return (MSG_CODE_STRING_BAD_GATEWAY);
            case MSG_CODE_SERVICE_UNAVAILABLE_503:
                return (MSG_CODE_STRING_SERVICE_UNAVAIL);
            case MSG_CODE_GATEWAY_TIMEOUT_504:
                return (MSG_CODE_STRING_GATEWAY_TIMEOUT);
            case MSG_CODE_PROXYING_NOT_SUPPORTED_505:
                return (MSG_CODE_STRING_PROXY_NOT_SUPP);
        }

        return ("");
    }

    private static String getOptionNoAsString(int optionNo) {
        switch (optionNo) {
            case OPTION_URI_PATH:
                return OPTION_STRING_URI_PATH;
            case OPTION_URI_QUERY:
                return OPTION_STRING_URI_QUERY;
            case OPTION_BLOCK_1:
                return OPTION_STRING_BLOCK1;
            case OPTION_BLOCK_2:
                return OPTION_STRING_BLOCK2;
            case OPTION_SIZE1:
                return OPTION_STRING_SIZE1;
            case OPTION_SIZE2:
                return OPTION_STRING_SIZE2;
            case OPTION_CONTENT_FORMAT:
                return OPTION_STRING_CONTENT_FORMAT;
            case OPTION_ACCEPT_FORMAT:
                return OPTION_STRING_ACCEPT_FORMAT;
        }
        return "";
    }

    private static String getOptionValueAsString(CoapOptionSet optionSet, CoapOption coapOption) {
        switch (coapOption.getNumber()) {
            case OPTION_URI_PATH:
                return coapOption.getStringValue();
            case OPTION_URI_QUERY:
                return coapOption.getStringValue();
            case OPTION_BLOCK_1: {
                CoapBlockOption block1 = optionSet.get_block1();
                return String.format("[%d,%d,%d]", block1.get_number(), block1.is_has_more() ? 0 : 1, block1.get_size());
            }
            case OPTION_BLOCK_2: {
                CoapBlockOption block2 = optionSet.get_block2();
                return String.format("[%d,%d,%d]", block2.get_number(), block2.is_has_more() ? 0 : 1, block2.get_size());
            }
            case OPTION_SIZE1:
            case OPTION_SIZE2:
            case OPTION_CONTENT_FORMAT:
            case OPTION_CONTENT_VERSION:
            case OPTION_ACCEPT_FORMAT:
            case OPTION_ACCEPT_VERSION:
                return String.format("%d", coapOption.getIntegerValue());
        }
        return "";
    }

    public static void print(CoapMsg coapMsg, boolean isInMessage) {
        StringBuilder sb = new StringBuilder();

        sb.append(isInMessage ? "[INP]" : "[OUT]");
        sb.append(String.format("t:%s ", get_msg_type_string(coapMsg.get_type())));
        sb.append(String.format("c:%s ", get_msg_code_string(coapMsg.get_code())));
        sb.append(String.format("i:0x%04X t:", coapMsg.get_id()));

        byte[] token = coapMsg.get_token().get_token();

        if (token != null) {
            for (int i = 0; i < token.length; i++) {
                sb.append(String.format("%02X", token[i]));
            }
        }
        
        sb.append(" ");

        CoapOptionSet option_set = coapMsg.get_option_set();
        ArrayList<CoapOption> sorted_options_list = option_set.get_sorted_options_list();

        sorted_options_list.forEach((option) -> {
            sb.append(getOptionNoAsString(option.getNumber()));
            sb.append(":");
            sb.append(getOptionValueAsString(option_set, option));
            sb.append(" ");
        });

        System.out.println(sb.toString());
    }

}
