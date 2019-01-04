package org.jinjuamla.stack.coap;

import org.jinjuamla.stack.consts.CoapMsgConsts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by psammand on 2/19/2018.
 */
public class CoapOptionSet implements Cloneable {

    private int _uri_port = 0xFFFF;
    private int _content_format = 0xFFFF;
    private int _accept_format = 0xFFFF;
    private int _accept_version = 0xFFFF;
    private int _content_version = 0xFFFF;
    private int _size1 = 0;
    private int _size2 = 0;
    private List<CoapOption> _others;
    ArrayList<String> _uri_path_list = null;
    ArrayList<String> _uri_query_list = null;
    CoapBlockOption _block1 = null;
    CoapBlockOption _block2 = null;

    public CoapOptionSet() {
    }

    public void clear() {
        _uri_port = 0xFFFF;
        _content_format = 0xFFFF;
        _accept_format = 0xFFFF;
        _accept_version = 0xFFFF;
        _content_version = 0xFFFF;
        _size1 = 0;
        _size2 = 0;
        _uri_path_list = null;
        _uri_query_list = null;
        _block1 = null;
        _block2 = null;
    }

    /**
     * ********************************************************************************************
     */
    /**
     * ********************************URI PORT
     * OPTION*********************************************
     */
    /**
     * ********************************************************************************************
     */
    public int get_uri_port() {
        return _uri_port;
    }

    public boolean has_uri_port() {
        return _uri_port != 0xFFFF;
    }

    public CoapOptionSet set_uri_port(int port) {
        if (port < 0 || (1 << 16) - 1 < port) {
            throw new IllegalArgumentException("URI port option must be between 0 and " + ((1 << 16) - 1) + " (2 bytes) inclusive but was " + port);
        }
        _uri_port = port;
        return this;
    }

    public CoapOptionSet remove_uri_port() {
        _uri_port = 0xFFFF;
        return this;
    }

    /**
     * ********************************************************************************************
     */
    /**
     * ********************************URI QUERY
     * OPTION*********************************************
     */
    /**
     * ********************************************************************************************
     */
    public List<String> get_uri_query_list() {
        synchronized (this) {
            if (_uri_query_list == null) {
                _uri_query_list = new ArrayList<>();
            }
        }
        return _uri_query_list;
    }

    public String get_uri_query_string() {
        StringBuilder builder = new StringBuilder();
        for (String query : get_uri_path_list()) {
            builder.append(query).append("&");
        }

        if (builder.length() > 0) {
            builder.delete(builder.length() - 1, builder.length());
        }
        return builder.toString();
    }

    public CoapOptionSet set_uri_query_string(String query) {
        if (query == null) {
            return this;
        }
        while (query.startsWith("?")) {
            query = query.substring(1);
        }

        clear_uri_query();

        for (String segment : query.split("&")) {
            if (!segment.isEmpty()) {
                add_uri_query(segment);
            }
        }
        return this;
    }

    public CoapOptionSet add_uri_query(String argument) {
        if (argument != null) {
            get_uri_query_list().add(argument);
        }
        return this;
    }

    public CoapOptionSet remove_uri_query(String argument) {
        get_uri_query_list().remove(argument);
        return this;
    }

    public CoapOptionSet clear_uri_query() {
        get_uri_query_list().clear();
        return this;
    }

    /**
     * ********************************************************************************************
     */
    /**
     * ********************************URI PATH
     * OPTION*********************************************
     */
    /**
     * ********************************************************************************************
     */
    public List<String> get_uri_path_list() {
        synchronized (this) {
            if (_uri_path_list == null) {
                _uri_path_list = new ArrayList<>();
            }
        }
        return _uri_path_list;
    }

    public String get_uri_path_string() {
        StringBuilder buffer = new StringBuilder();

        for (String path : get_uri_path_list()) {
            buffer.append(path).append("/");
        }

        if (buffer.length() == 0) {
            return "";
        }
        else {
            return buffer.substring(0, buffer.length() - 1);
        }
    }

    public int get_uri_path_count() {
        return get_uri_path_list().size();
    }

    public CoapOptionSet setUriPath(String path) {
        if (path == null) {
            return this;
        }
        final String slash = "/";

        // remove leading slash
        if (path.startsWith(slash)) {
            path = path.substring(slash.length());
        }

        clear_uri_path();

        for (String segment : path.split(slash)) {
            // empty path segments are allowed (e.g., /test vs /test/)
            add_uri_path(segment);
        }
        return this;
    }

    public CoapOptionSet add_uri_path(String segment) {
        if (segment != null) {
            get_uri_path_list().add(segment);
        }
        return this;
    }

    public CoapOptionSet clear_uri_path() {
        get_uri_path_list().clear();
        return this;
    }

    /**
     * ********************************************************************************************
     */
    /**
     * ********************************ACCEPT FORMAT
     * OPTION****************************************
     */
    /**
     * ********************************************************************************************
     */
    public int get_accept_format() {
        return _accept_format;
    }

    public boolean has_accept_format() {
        return _accept_format != 0xFFFF;
    }

    public CoapOptionSet set_accept_format(int format) {
        if (format > -1) {
            _accept_format = format;
        }
        else {
            _accept_format = 0xFFFF;
        }
        return this;
    }

    public CoapOptionSet remove_accept_format() {
        _accept_format = 0xFFFF;
        return this;
    }

    /**
     * ********************************************************************************************
     */
    /**
     * ********************************CONTENT FORMAT
     * OPTION***************************************
     */
    /**
     * ********************************************************************************************
     */
    public int get_content_format() {
        return _content_format;
    }

    public boolean has_content_format() {
        return _content_format != 0xFFFF;
    }

    public CoapOptionSet set_content_format(int format) {
        if (format > -1) {
            _content_format = format;
        }
        else {
            _content_format = 0xFFFF;
        }
        return this;
    }

    public CoapOptionSet remove_content_format() {
        _content_format = 0xFFFF;
        return this;
    }

    /**
     * ********************************************************************************************
     */
    /**
     * ********************************CONTENT VERSION
     * OPTION***************************************
     */
    /**
     * ********************************************************************************************
     */
    public int get_content_version() {
        return _content_version;
    }

    public boolean has_content_version() {
        return _content_version != 0xFFFF;
    }

    public CoapOptionSet set_content_version(int format) {
        _content_version = (format > -1) ? format : null;
        return this;
    }

    public CoapOptionSet remove_content_version() {
        _content_version = 0xFFFF;
        return this;
    }

    /**
     * ********************************************************************************************
     */
    /**
     * ********************************ACCEPT VERSION
     * OPTION***************************************
     */
    /**
     * ********************************************************************************************
     */
    public int get_accept_version() {
        return _accept_version;
    }

    public boolean has_accept_version() {
        return _accept_version != 0xFFFF;
    }

    public CoapOptionSet set_accept_version(int format) {
        _accept_version = (format > -1) ? format : null;
        return this;
    }

    public CoapOptionSet remove_accept_version() {
        _accept_version = 0xFFFF;
        return this;
    }

    /**
     * ********************************************************************************************
     */
    /**
     * *************************************SIZE1
     * OPTION*******************************************
     */
    /**
     * ********************************************************************************************
     */
    public int get_size1() {
        return _size1;
    }

    public boolean has_size1() {
        return _size1 != 0;
    }

    public CoapOptionSet set_size1(int size1) {
        this._size1 = size1;
        return this;
    }

    public CoapOptionSet remove_size1() {
        this._size1 = 0;
        return this;
    }

    /**
     * ********************************************************************************************
     */
    /**
     * *************************************SIZE2
     * OPTION*******************************************
     */
    /**
     * ********************************************************************************************
     */
    public int get_size2() {
        return _size2;
    }

    public boolean has_size2() {
        return _size2 != 0;
    }

    public CoapOptionSet set_size2(int size2) {
        this._size2 = size2;
        return this;
    }

    public CoapOptionSet remove_size2() {
        this._size2 = 0;
        return this;
    }

    /**
     * ********************************************************************************************
     */
    /**
     * *************************************BLOCK1
     * OPTION*******************************************
     */
    /**
     * ********************************************************************************************
     */
    public boolean has_block1() {
        return _block1 != null;
    }

    public CoapBlockOption get_block1() {
        return _block1;
    }

    public CoapOptionSet set_block1(short szx, boolean more, int number) {
        if (_block1 == null) {
            _block1 = new CoapBlockOption();
        }

        _block1.set_szx(szx);
        _block1.set_has_more(more);
        _block1.set_number(number);

        return this;
    }

    public CoapOptionSet set_block1(byte[] buffer) {
        if (_block1 == null) {
            _block1 = new CoapBlockOption();
        }

        _block1.decode(buffer);
        return this;
    }

    public CoapOptionSet set_block1(CoapBlockOption block) {
        if (_block1 == null) {
            _block1 = new CoapBlockOption();
        }

        _block1.set_szx(block.get_szx());
        _block1.set_has_more(block.is_has_more());
        _block1.set_number(block.get_number());

        return this;
    }

    public CoapOptionSet remove_block1() {
        _block1 = null;
        return this;
    }

    /**
     * ********************************************************************************************
     */
    /**
     * *************************************BLOCK2
     * OPTION*******************************************
     */
    /**
     * ********************************************************************************************
     */
    public boolean has_block2() {
        return _block2 != null;
    }

    public CoapBlockOption get_block2() {
        return _block2;
    }

    public CoapOptionSet set_block2(short szx, boolean more, int number) {
        if (_block2 == null) {
            _block2 = new CoapBlockOption(number, more, szx);
        }
        else {
            _block2.set_szx(szx);
            _block2.set_has_more(more);
            _block2.set_number(number);
        }

        return this;
    }

    public CoapOptionSet set_block2(byte[] buffer) {
        if (_block2 == null) {
            _block2 = new CoapBlockOption();
        }
        _block2.decode(buffer);
        return this;
    }

    public CoapOptionSet set_block2(CoapBlockOption block) {
        if (_block2 == null) {
            _block2 = new CoapBlockOption();
        }
        _block2.set_szx(block.get_szx());
        _block2.set_has_more(block.is_has_more());
        _block2.set_number(block.get_number());

        return this;
    }

    public CoapOptionSet remove_block2() {
        _block2 = null;
        return this;
    }

    public List<CoapOption> get_others() {
        List<CoapOption> otherOptions = this._others;

        if (otherOptions == null) {
            return Collections.emptyList();
        }
        else {
            return Collections.unmodifiableList(otherOptions);
        }
    }

    public boolean add_option(int optionNo, byte[] optionValue) {
        switch (optionNo) {
            case CoapMsgConsts.OPTION_URI_PORT:
                set_uri_port(getIntegerValue(optionValue));
                break;
            case CoapMsgConsts.OPTION_URI_PATH:
                add_uri_path(Arrays.toString(optionValue));
                break;
            case CoapMsgConsts.OPTION_URI_QUERY:
                add_uri_query(Arrays.toString(optionValue));
                break;
            case CoapMsgConsts.OPTION_ACCEPT_VERSION:
                set_accept_version(getIntegerValue(optionValue));
                break;
            case CoapMsgConsts.OPTION_CONTENT_VERSION:
                set_content_version(getIntegerValue(optionValue));
                break;
            case CoapMsgConsts.OPTION_CONTENT_FORMAT:
                set_content_format(getIntegerValue(optionValue));
                break;
            case CoapMsgConsts.OPTION_ACCEPT_FORMAT:
                set_accept_format(getIntegerValue(optionValue));
                break;
            case CoapMsgConsts.OPTION_SIZE1:
                set_size1(getIntegerValue(optionValue));
                break;
            case CoapMsgConsts.OPTION_SIZE2:
                set_size2(getIntegerValue(optionValue));
                break;
            case CoapMsgConsts.OPTION_BLOCK_1:
                set_block1(optionValue);
                break;
            case CoapMsgConsts.OPTION_BLOCK_2:
                set_block2(optionValue);
                break;
            default:
                return false;
        }
        return true;
    }

    public int getIntegerValue(byte[] value) {
        int ret = 0;

        for (int i = 0; i < value.length; i++) {
            ret += (value[value.length - i - 1] & 0xFF) << (i * 8);
        }
        return ret;
    }

    public ArrayList<CoapOption> get_sorted_options_list() {
        ArrayList<CoapOption> options = new ArrayList<>();

        if (has_uri_port()) {
            options.add(new CoapOption(CoapMsgConsts.OPTION_URI_PORT, get_uri_port()));
        }
        if (_uri_path_list != null) {
            for (String path : _uri_path_list) {
                options.add(new CoapOption(CoapMsgConsts.OPTION_URI_PATH, path));
            }
        }
        if (_uri_query_list != null) {
            _uri_query_list.forEach((query)
                    -> {
                options.add(new CoapOption(CoapMsgConsts.OPTION_URI_QUERY, query));
            });
        }
        if (has_content_format()) {
            options.add(new CoapOption(CoapMsgConsts.OPTION_CONTENT_FORMAT, get_content_format()));
        }
        if (has_content_version()) {
            options.add(new CoapOption(CoapMsgConsts.OPTION_CONTENT_VERSION, get_content_version()));
        }
        if (has_accept_format()) {
            options.add(new CoapOption(CoapMsgConsts.OPTION_ACCEPT_FORMAT, get_accept_format()));
        }
        if (has_accept_version()) {
            options.add(new CoapOption(CoapMsgConsts.OPTION_ACCEPT_VERSION, get_accept_version()));
        }
        if (has_size1()) {
            options.add(new CoapOption(CoapMsgConsts.OPTION_SIZE1, get_size1()));
        }
        if (has_size2()) {
            options.add(new CoapOption(CoapMsgConsts.OPTION_SIZE2, get_size2()));
        }
        if (has_block1()) {
            byte[] block = new byte[3];
            _block1.encode(block);
            options.add(new CoapOption(CoapMsgConsts.OPTION_BLOCK_1, block));
        }
        if (has_block2()) {
            byte[] block = new byte[3];
            _block2.encode(block);
            options.add(new CoapOption(CoapMsgConsts.OPTION_BLOCK_2, block));
        }

        if (_others != null) {
            options.addAll(_others);
        }

        Collections.sort(options);
        return options;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        StringBuilder sbv = new StringBuilder();
        int oldNr = -1;
        boolean list = false;

        sb.append('{');

        for (CoapOption opt : get_sorted_options_list()) {
            if (opt.getNumber() != oldNr) {
                if (oldNr != -1) {
                    if (list) {
                        sbv.append(']');
                    }
                    sb.append(sbv.toString());
                    sbv = new StringBuilder();
                    sb.append(", ");
                }
                else {
                }
                list = false;

                sb.append('"');
//        sb.append(OptionNumberRegistry.toString(opt.getNumber()));
                sb.append(opt.getNumber());
                sb.append('"');
                sb.append(':');
            }
            else {
                if (!list) {
                    sbv.insert(0, '[');
                }
                list = true;
                sbv.append(",");
            }
//      sbv.append(opt.toValueString());
            sbv.append(opt.getValue());

            oldNr = opt.getNumber();
        }
        if (list) {
            sbv.append(']');
        }
        sb.append(sbv.toString());
        sb.append('}');

        return sb.toString();
    }

    private List<CoapOption> getOthersInternal() {
        synchronized (this) {
            if (_others == null) {
                _others = new ArrayList<>();
            }
        }
        return _others;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        CoapOptionSet newOptionSet = (CoapOptionSet) super.clone();

        newOptionSet._uri_port = this._uri_port;
        newOptionSet._content_format = this._content_format;
        newOptionSet._accept_format = this._accept_format;
        newOptionSet._accept_version = this._accept_version;
        newOptionSet._content_version = this._content_version;
        newOptionSet._size1 = this._size1;
        newOptionSet._size2 = this._size2;

        if (_uri_path_list != null) {
            newOptionSet._uri_path_list = new ArrayList<>(_uri_path_list);
        }

        if (_uri_query_list != null) {
            newOptionSet._uri_query_list = new ArrayList<>(_uri_query_list);
        }

        if (_block1 != null) {
            newOptionSet._block1 = (CoapBlockOption) _block1.clone();
        }

        if (_block2 != null) {
            newOptionSet._block2 = (CoapBlockOption) _block2.clone();
        }

        return newOptionSet;
    }

    void add_option(CoapOption coapOption) {
        add_option(coapOption.getNumber(), coapOption.getValue());
    }

}
