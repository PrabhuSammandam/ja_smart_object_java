package org.jinjuamla.stack.consts;

/**
 * Created by psammand on 2/19/2018.
 */

public class CoapMsgConsts {
    public static final byte COAP_MSG_TYPE_CON = 0;
    public static final byte COAP_MSG_TYPE_NON = 1;
    public static final byte COAP_MSG_TYPE_ACK = 2;
    public static final byte COAP_MSG_TYPE_RST = 3;
    public static final byte COAP_MSG_TYPE_NONE = 4;

    public static final short MSG_CODE_EMPTY = 0;
    public static final short MSG_CODE_GET = 1;
    public static final short MSG_CODE_POST = 2;
    public static final short MSG_CODE_PUT = 3;
    public static final short MSG_CODE_DELETE = 4;
    public static final short MSG_CODE_CREATED_201 = 65;
    public static final short MSG_CODE_DELETED_202 = 66;
    public static final short MSG_CODE_VALID_203 = 67;
    public static final short MSG_CODE_CHANGED_204 = 68;
    public static final short MSG_CODE_CONTENT_205 = 69;
    public static final short MSG_CODE_CONTINUE_231 = 95;
    public static final short MSG_CODE_BAD_REQUEST_400 = 128;
    public static final short MSG_CODE_UNAUTHORIZED_401 = 129;
    public static final short MSG_CODE_BAD_OPTION_402 = 130;
    public static final short MSG_CODE_FORBIDDEN_403 = 131;
    public static final short MSG_CODE_NOT_FOUND_404 = 132;
    public static final short MSG_CODE_METHOD_NOT_ALLOWED_405 = 133;
    public static final short MSG_CODE_NOT_ACCEPTABLE_406 = 134;
    public static final short MSG_CODE_REQ_ENTITY_INCOMPLETE_408 = 136;
    public static final short MSG_CODE_PRECONDITION_FAILED_412 = 140;
    public static final short MSG_CODE_REQ_ENTITY_TOO_LARGE_413 = 141;
    public static final short MSG_CODE_UNSUPPORTED_CF_415 = 143;
    public static final short MSG_CODE_INTERNAL_SERVER_ERROR_500 = 160;
    public static final short MSG_CODE_NOT_IMPLEMENTED_501 = 161;
    public static final short MSG_CODE_BAD_GATEWAY_502 = 162;
    public static final short MSG_CODE_SERVICE_UNAVAILABLE_503 = 163;
    public static final short MSG_CODE_GATEWAY_TIMEOUT_504 = 164;
    public static final short MSG_CODE_PROXYING_NOT_SUPPORTED_505 = 165;

    public static final short PAYLOAD_FORMAT_NONE = (short) 0xFFFF;
    public static final short PAYLOAD_FORMAT_TEXT_PLAIN = 0;
    public static final short PAYLOAD_FORMAT_APP_LINK_FORMAT = 40;
    public static final short PAYLOAD_FORMAT_APP_XML = 41;
    public static final short PAYLOAD_FORMAT_APP_OCTET_STREAM = 42;
    public static final short PAYLOAD_FORMAT_APP_RDF_XML = 43;
    public static final short PAYLOAD_FORMAT_APP_EXI = 47;
    public static final short PAYLOAD_FORMAT_APP_JSON = 50;
    public static final short PAYLOAD_FORMAT_APP_CBOR = 60;
    public static final short PAYLOAD_FORMAT_APP_VND_OCF_CBOR = 10000;

    public static final short OPTION_IF_MATCH = 1;
    public static final short OPTION_URI_HOST = 3;
    public static final short OPTION_ETAG = 4;
    public static final short OPTION_IF_NONE_MATCH = 5;
    public static final short OPTION_OBSERVE = 6;
    public static final short OPTION_URI_PORT = 7;
    public static final short OPTION_LOCATION_PATH = 8;
    public static final short OPTION_URI_PATH = 11;
    public static final short OPTION_CONTENT_FORMAT = 12;
    public static final short OPTION_MAX_AGE = 14;
    public static final short OPTION_URI_QUERY = 15;
    public static final short OPTION_ACCEPT_FORMAT = 17;
    public static final short OPTION_LOCATION_QUERY = 20;
    public static final short OPTION_BLOCK_2 = 23;
    public static final short OPTION_BLOCK_1 = 27;
    public static final short OPTION_SIZE2 = 28;
    public static final short OPTION_PROXY_URI = 35;
    public static final short OPTION_PROXU_SCHEME = 39;
    public static final short OPTION_SIZE1 = 60;
    public static final short OPTION_ACCEPT_VERSION = 2049;
    public static final short OPTION_CONTENT_VERSION = 2053;

    public static boolean isRequest(int msgCode)
    {
        return msgCode == MSG_CODE_GET || msgCode == MSG_CODE_POST || msgCode == MSG_CODE_PUT || msgCode == MSG_CODE_DELETE;
    }

}
