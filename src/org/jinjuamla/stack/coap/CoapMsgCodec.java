package org.jinjuamla.stack.coap;

import org.jinjuamla.network.enums.ErrCode;
import org.jinjuamla.stack.consts.CoapMsgConsts;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by psammand on 2/19/2018.
 */
public class CoapMsgCodec {

    public static CoapMsg decode_coap_msg(byte[] coap_pdu) throws CoapMsgCodecException {
        if (coap_pdu == null || coap_pdu.length == 0 || coap_pdu.length < 4) {
            throw new CoapMsgCodecException(ErrCode.INVALID_PARAMS);
        }

        int tempByte = coap_pdu[0] & 0xFF;

        if (tempByte >> 6 != 1) {
            throw new CoapMsgCodecException(ErrCode.INVALID_COAP_VERSION);
        }

        int type = (tempByte >> 4) & 0x03;
        int tokenLen = tempByte & 0x0F;

        if (tokenLen > 8) {
            throw new CoapMsgCodecException(ErrCode.MSG_FORMAT_ERROR);
        }

        int code = coap_pdu[1] & 0xFF;

        /*
         * Section 4.1: An Empty message has the Code field set to 0.00. The Token
		 * Length field MUST be set to 0 and bytes of data MUST NOT be present after the
		 * Message ID field. If there are any bytes, they MUST be processed as a message
		 * format error.
         */
        if ((code == CoapMsgConsts.MSG_CODE_EMPTY) && ((coap_pdu.length != 4) || (tokenLen != 0))) {
            throw new CoapMsgCodecException(ErrCode.MSG_FORMAT_ERROR);
        }

        int msgCodeClass = (code >> 5);

        if ((msgCodeClass == 1) || (msgCodeClass == 6) || (msgCodeClass == 7)) {
            throw new CoapMsgCodecException(ErrCode.MSG_FORMAT_ERROR);
        }

        tempByte = coap_pdu[2] & 0xFF;
        int msgId = tempByte << 8;
        tempByte = coap_pdu[3] & 0xFF;
        msgId |= tempByte;

        CoapMsg packet = new CoapMsg();

        packet.set_type(type);
        packet.set_code(code);
        packet.set_id(msgId);

        int curByteIndex = 4;

        if (curByteIndex == coap_pdu.length) {
            return packet;
        }

        if (tokenLen > 0) {
            byte[] token = Arrays.copyOfRange(coap_pdu, curByteIndex, curByteIndex + tokenLen);
            packet.get_token().set_token(token);

            curByteIndex += tokenLen;
        }

        if (curByteIndex == coap_pdu.length) {
            return packet;
        }

        tempByte = coap_pdu[curByteIndex] & 0xFF;

        if (tempByte == 0xFF) {
            throw new CoapMsgCodecException(ErrCode.MSG_FORMAT_ERROR);
        }

        int lastOptionNo = 0;

        while (curByteIndex < coap_pdu.length) {
            // check for payload marker, if it is there then mark the start of payload and
            // return
            if ((coap_pdu[curByteIndex] & 0xFF) == 0xFF) {
                // at least one byte payload must follow to the payload marker
                if ((coap_pdu.length - curByteIndex) < 2) {
                    throw new CoapMsgCodecException(ErrCode.MSG_FORMAT_ERROR);
                }
                else {
                    curByteIndex++; // skip payload marker

                    byte[] payload = Arrays.copyOfRange(coap_pdu, curByteIndex, coap_pdu.length);

                    packet.set_payload(payload);

                    return packet;
                }
            }
            else {
                int option_delta = (coap_pdu[curByteIndex] & 0xFF) >> 4; // initial delta in upper 4 bits
                int option_length = (coap_pdu[curByteIndex] & 0xFF) & 0x0F; // initial length in lower 4 bits
                
                curByteIndex++; // skip the initial delta & length byte

                switch (option_delta) {
                    case 13:
                        // An 8-bit unsigned integer follows the initial byte and indicates the Option
                        // Delta minus 13.
                        option_delta = (coap_pdu[curByteIndex] & 0xFF ) + 13;
                        curByteIndex++; // skip 1 byte big delta
                        break;
                    case 14:
                        // A 16-bit unsigned integer in network byte order follows the initial byte and
                        // indicates the Option Delta minus 269.
                        option_delta = (coap_pdu[curByteIndex] & 0xFF) << 8;
                        curByteIndex++;
                        option_delta |= (coap_pdu[curByteIndex] & 0xFF);
                        option_delta += 269;
                        curByteIndex++;
                        break;
                    case 15:
                        throw new CoapMsgCodecException(ErrCode.MSG_FORMAT_ERROR);
                    default:
                        break;
                }

                switch (option_length) {
                    case 13:
                        // An 8-bit unsigned integer precedes the Option Value and indicates the Option
                        // Length minus 13.
                        option_length = (coap_pdu[curByteIndex] & 0xFF) + 13;
                        curByteIndex++; // skip 1 byte big delta
                        break;
                    case 14:
                        // A 16-bit unsigned integer in network byte order precedes the Option Value and
                        // indicates the Option Length minus 269.
                        option_length = (coap_pdu[curByteIndex] & 0xFF) << 8;
                        curByteIndex++;
                        option_length |= (coap_pdu[curByteIndex] & 0xFF);
                        option_length += 269;
                        curByteIndex++;
                        break;
                    case 15:
                        throw new CoapMsgCodecException(ErrCode.MSG_FORMAT_ERROR);
                    default:
                        break;
                }

                // if current option length is greater than max option size or current option
                // length is greater than remaining payload
                if ((coap_pdu.length - curByteIndex) < option_length) {
                    throw new CoapMsgCodecException(ErrCode.MSG_FORMAT_ERROR);
                }

                lastOptionNo += option_delta;

                byte[] optionValue = Arrays.copyOfRange(coap_pdu, curByteIndex, curByteIndex + option_length);

                packet.get_option_set().add_option(lastOptionNo, optionValue);

                curByteIndex += option_length;
            }
        }

        return null;
    }

    public static byte[] encodeEmptyMsg(int msgType, int msgId) {
        byte[] bytes = new byte[4];

        bytes[0] = 0x40;
        bytes[0] = (byte) ((msgType & 0x03) << 4);
        bytes[1] = (byte) CoapMsgConsts.MSG_CODE_EMPTY;
        bytes[2] = (byte) ((msgId >> 8) & 0xFF);
        bytes[3] = (byte) (msgId & 0xFF);

        return bytes;
    }

    public static byte[] encode_coap_msg(CoapMsg coap_msg) {
        if (coap_msg == null) {
            return null;
        }

        byte[] temp_out_bytes = new byte[1024];

        int msg_type = coap_msg.get_type();
        int msg_code = coap_msg.get_code();
        int msg_id = coap_msg.get_id();
        int token_len = coap_msg.get_token() != null ? coap_msg.get_token().get_length() : 0;

        temp_out_bytes[0] = 0x40;
        temp_out_bytes[0] |= ((msg_type & 0x03) << 4) & 0xFF;
        temp_out_bytes[0] |= token_len & 0x0F;

        temp_out_bytes[1] = (byte) msg_code;
        temp_out_bytes[2] = (byte) ((msg_id >> 8) & 0xFF);
        temp_out_bytes[3] = (byte) (msg_id & 0xFF);

        int current_byte_idx = 4;

        if (token_len > 0) {
            System.arraycopy(coap_msg.get_token().get_token(), 0, temp_out_bytes, current_byte_idx, token_len);
            current_byte_idx += token_len;
        }

        if (coap_msg.get_option_set() != null) {
            int last_option_no = 0;
            ArrayList<CoapOption> sorted_options_list = coap_msg.get_option_set().get_sorted_options_list();

            for (CoapOption current_option : sorted_options_list) {
                int current_delta = current_option.getNumber() - last_option_no;
                last_option_no = current_option.getNumber();
                int current_option_len = current_option.getLength();

                int current_option_start_idx = current_byte_idx;
                current_byte_idx++;
                temp_out_bytes[current_option_start_idx] = 0;

                // Delta Bytes
                if (current_delta < 13) {
                    temp_out_bytes[current_option_start_idx] |= (byte) ((current_delta & 0xFF) << 4);
                }
                else
                    if (current_delta < 269) {
                        temp_out_bytes[current_option_start_idx] |= (byte) ((13 & 0xFF) << 4);
                        temp_out_bytes[current_byte_idx] = (byte) ((current_delta & 0xFF) - 13);
                        current_byte_idx++;
                    }
                    else {
                        temp_out_bytes[current_option_start_idx] |= (byte) ((14 & 0xFF) << 4);

                        temp_out_bytes[current_byte_idx] = (byte) ((current_delta - 269) >> 8);
                        current_byte_idx++;
                        temp_out_bytes[current_byte_idx] = (byte) ((current_delta - 269) & 0xff);
                        current_byte_idx++;
                    }

                // Length Bytes
                if (current_option_len < 13) {
                    temp_out_bytes[current_option_start_idx] |= (byte) (current_option_len & 0x0F);
                }
                else
                    if (current_option_len < 269) {
                        temp_out_bytes[current_option_start_idx] |= (byte) (13);
                        temp_out_bytes[current_byte_idx] = (byte) (current_option_len - 13);
                        current_byte_idx++;
                    }
                    else {
                        temp_out_bytes[current_option_start_idx] |= (byte) (14);

                        temp_out_bytes[current_byte_idx] = (byte) ((current_option_len - 269) >> 8);
                        current_byte_idx++;
                        temp_out_bytes[current_byte_idx] = (byte) ((current_option_len - 269) & 0xff);
                        current_byte_idx++;
                    }

                if (current_option_len > 0) {
                    System.arraycopy(current_option.getValue(), 0, temp_out_bytes, current_byte_idx, current_option_len);
                    current_byte_idx += current_option_len;
                }
            }
        }
        if (coap_msg.get_payload() != null && coap_msg.get_payload().length > 0) {
            temp_out_bytes[current_byte_idx] = (byte) 0xFF;
            current_byte_idx++;

            System.arraycopy(coap_msg.get_payload(), 0, temp_out_bytes, current_byte_idx, coap_msg.get_payload().length);
            current_byte_idx += coap_msg.get_payload().length;
        }

        byte[] encodedBuffer = null;

        if (current_byte_idx > 0) {
            encodedBuffer = Arrays.copyOfRange(temp_out_bytes, 0, current_byte_idx);
        }

        return encodedBuffer;
    }

}
