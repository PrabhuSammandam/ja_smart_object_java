/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jinjuamla.network.adapters.ip;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.MembershipKey;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jinjuamla.network.IpAdapterConfigMgr;
import org.jinjuamla.network.adapters.AdapterEvent;
import org.jinjuamla.network.adapters.IAdapter;
import org.jinjuamla.network.adapters.IAdapterEventHandler;
import org.jinjuamla.network.adapters.ip.networkinterfaces.IInterfaceMonitor;
import org.jinjuamla.network.datatypes.Endpoint;
import org.jinjuamla.network.datatypes.IpAdapterConfig;
import org.jinjuamla.network.datatypes.IpAddr;
import org.jinjuamla.network.enums.EAddressingMethod;
import org.jinjuamla.network.enums.EIpAddrFamily;
import org.jinjuamla.network.enums.ErrCode;
import org.jinjuamla.network.enums.consts.AdapterType;
import org.jinjuamla.network.enums.consts.NetworkFlag;
import org.jinjuamla.network.platform.INetworkPlatformFactory;
import org.jinjuamla.network.platform.NetworkPlatformFactoryMgr;

/**
 *
 * @author psammand
 */
public class IpAdapter implements IAdapter {
    private IAdapterEventHandler adapterEventHandler = null;
    private IInterfaceMonitor interfaceMonitor = null;
    private DatagramChannel ipv4McastDatagramChannel = null;
    private DatagramChannel ipv4UcastDatagramChannel = null;
    private DatagramChannel ipv6McastDatagramChannel = null;
    private DatagramChannel ipv6UcastDatagramChannel = null;
    private boolean isStarted = false;
    private ReceiverThread receiverTask = null;
    private SenderThread senderTask = null;
    private final BlockingQueue<IpAdapterSendMsg> senderTaskMsgQueue = new LinkedBlockingQueue<>();

    @Override
    public void setAdapterHandler(IAdapterEventHandler adapter_event_handler) {
        this.adapterEventHandler = adapter_event_handler;
    }

    @Override
    public int getType() {
        return AdapterType.IP;
    }

    @Override
    public ErrCode initialize() {
        return createSockets();
    }

    @Override
    public void readData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int sendMulticastData(Endpoint end_point, byte[] data, int data_length) {
        if (end_point == null || data == null || data_length == 0) {
            return -1;
        }

        int retValue = sendData(end_point, data, data_length, true);

        return retValue;
    }

    @Override
    public int sendUnicastData(Endpoint end_point, byte[] data, int data_length) {
        if (end_point == null || data == null || data_length == 0) {
            return -1;
        }

        int retValue = sendData(end_point, data, data_length, false);

        return retValue;
    }

    @Override
    public ErrCode startAdapter() {
        INetworkPlatformFactory networkPlatformFactory = NetworkPlatformFactoryMgr.Inst().getNetworkPlatformFactory();

        if (networkPlatformFactory == null) {
            return ErrCode.ERR;
        }

        interfaceMonitor = networkPlatformFactory.getInterfaceMonitor();

        if (interfaceMonitor == null) {
            return ErrCode.ERR;
        }

        ErrCode errCode;

        errCode = interfaceMonitor.startMonitor(AdapterType.IP);

        if (errCode == ErrCode.OK) {
            errCode = createAndStartSenderTask();
        }
        if (errCode == ErrCode.OK) {
            errCode = startServer();
        }

        if (errCode != ErrCode.OK) {
            stopAdapter();
        }

        return errCode;
    }

    @Override
    public ErrCode startListening() {
        ErrCode errCode = ErrCode.OK;

        if (isStarted) {
            return errCode;
        }

        if (interfaceMonitor == null) {
            return ErrCode.ERR;
        }

        IpAdapterConfig ipAdapterConfig = IpAdapterConfigMgr.Inst().getIpAdapterConfig();
        LinkedList<org.jinjuamla.network.adapters.ip.networkinterfaces.InterfaceAddress> interfaceAddrList = new LinkedList<>();

        interfaceMonitor.getInterfacesList(interfaceAddrList);

        for (org.jinjuamla.network.adapters.ip.networkinterfaces.InterfaceAddress interfaceAddress : interfaceAddrList) {
            if (interfaceAddress.getIpAddrFamily() == IpAddr.IPV4 && ipAdapterConfig.isIsIpv4McastEnabled()) {
                startIpv4Multicast(interfaceAddress);
                continue;
            }
            if (interfaceAddress.getIpAddrFamily() == IpAddr.IPV6 && ipAdapterConfig.isIsIpv6McastEnabled()) {
                startIpv6Multicast(interfaceAddress);
            }
        }

        return errCode;
    }

    @Override
    public ErrCode startServer() {
        ErrCode errCode = ErrCode.OK;

        if (isStarted) {
            return errCode;
        }

        IpAdapterConfig ipAdapterConfig = IpAdapterConfigMgr.Inst().getIpAdapterConfig();

        if (!ipAdapterConfig.isIpv4Enabled() && !ipAdapterConfig.isIpv6Enabled()) {
            return ErrCode.ERR;
        }

        errCode = openSockets();

        if (errCode == ErrCode.OK) {
            errCode = startListening();
        }

        if (errCode == ErrCode.OK) {
            errCode = createAndStartReceivingThread();
        }

        if (errCode == ErrCode.OK) {
            isStarted = true;
        }

        return errCode;
    }

    @Override
    public ErrCode stopAdapter() {
        ErrCode errCode = ErrCode.OK;

        stopSenderThread();

        interfaceMonitor.stopMonitor(AdapterType.IP);

        stopServer();

        return errCode;
    }

    @Override
    public ErrCode stopListening() {
        return ErrCode.OK;
    }

    @Override
    public ErrCode stopServer() {
        ErrCode errCode = ErrCode.OK;

        if (!isStarted) {
            return errCode;
        }

        return errCode;
    }

    @Override
    public ErrCode terminate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private ErrCode createAndStartReceivingThread() {
        this.receiverTask = new ReceiverThread(this);
        this.receiverTask.start();

        return ErrCode.OK;
    }

    private ErrCode createAndStartSenderTask() {
        senderTask = new SenderThread(senderTaskMsgQueue, this);
        senderTask.start();

        return ErrCode.OK;
    }

    private ErrCode openIpv4Sockets() {
        ErrCode errCode = ErrCode.OK;

        IpAdapterConfig ipAdapterConfig = IpAdapterConfigMgr.Inst().getIpAdapterConfig();

        if (ipAdapterConfig.isIsIpv4UcastEnabled()) {
            int ipv4UcastPort = ipAdapterConfig.getIpv4UcastPort();

            SocketAddress socketAddr = new InetSocketAddress(ipv4UcastPort);

            DatagramSocket socket = ipv4UcastDatagramChannel.socket();

            try {
                socket.bind(socketAddr);
                ipAdapterConfig.setCurrentIpv4UcastPort(socket.getLocalPort());
                System.out.println("IPV4 binded port " + ipAdapterConfig.getCurrentIpv4UcastPort());
            }
            catch (SocketException ex) {
                Logger.getLogger(IpAdapter.class.getName()).log(Level.SEVERE, null, ex);
                errCode = ErrCode.ERR;
            }
        }

        if (ipAdapterConfig.isIsIpv4McastEnabled()) {
            int ipv4McastPort = ipAdapterConfig.getIpv4McastPort();

            try {
                ipv4McastDatagramChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
                DatagramSocket socket = ipv4McastDatagramChannel.socket();
                socket.bind(new InetSocketAddress(ipv4McastPort));
            }
            catch (SocketException ex) {
                Logger.getLogger(IpAdapter.class.getName()).log(Level.SEVERE, null, ex);
                errCode = ErrCode.ERR;
            }
            catch (IOException ex) {
                Logger.getLogger(IpAdapter.class.getName()).log(Level.SEVERE, null, ex);
                errCode = ErrCode.ERR;
            }
        }

        return errCode;
    }

    private ErrCode openIpv6Sockets() {
        ErrCode errCode = ErrCode.OK;

        IpAdapterConfig ipAdapterConfig = IpAdapterConfigMgr.Inst().getIpAdapterConfig();

        if (ipAdapterConfig.isIsIpv6UcastEnabled()) {
            try {
                DatagramSocket socket = ipv6UcastDatagramChannel.socket();
                socket.bind(new InetSocketAddress(ipAdapterConfig.getIpv6UcastPort()));
                ipAdapterConfig.setCurrentIpv6UcastPort(socket.getLocalPort());

                System.out.println("IPV6 binded port " + ipAdapterConfig.getCurrentIpv6UcastPort());
            }
            catch (SocketException ex) {
                Logger.getLogger(IpAdapter.class.getName()).log(Level.SEVERE, null, ex);
                errCode = ErrCode.ERR;
            }
        }

        if (ipAdapterConfig.isIsIpv6McastEnabled()) {
            try {
                ipv6McastDatagramChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
                DatagramSocket socket = ipv6McastDatagramChannel.socket();
                socket.bind(new InetSocketAddress(ipAdapterConfig.getIpv6McastPort()));
            }
            catch (SocketException ex) {
                Logger.getLogger(IpAdapter.class.getName()).log(Level.SEVERE, null, ex);
                errCode = ErrCode.ERR;
            }
            catch (IOException ex) {
                Logger.getLogger(IpAdapter.class.getName()).log(Level.SEVERE, null, ex);
                errCode = ErrCode.ERR;
            }
        }

        return errCode;
    }

    private ErrCode openSockets() {
        ErrCode errCode = openIpv4Sockets();

        if (errCode != ErrCode.OK) {
            return errCode;
        }

        errCode = openIpv6Sockets();

        if (errCode != ErrCode.OK) {
            return errCode;
        }

        return errCode;
    }

    private int sendData(Endpoint end_point, byte[] data, int data_length, boolean isMulticast) {
        byte[] dataCopy = Arrays.copyOf(data, data_length);
        IpAdapterSendMsg ipAdapterSendMsg = new IpAdapterSendMsg(end_point, dataCopy, data_length, isMulticast);

        try {
            senderTaskMsgQueue.put(ipAdapterSendMsg);
        }
        catch (InterruptedException ex) {
            Logger.getLogger(IpAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return data_length;
    }

    private void startIpv4Multicast(org.jinjuamla.network.adapters.ip.networkinterfaces.InterfaceAddress interfaceAddress) {
        String multicastAddrString = "224.0.1.187";

        try {
            NetworkInterface nwkIf = NetworkInterface.getByName(interfaceAddress.getName());
            InetAddress mcastGroup = InetAddress.getByName(multicastAddrString);

            ipv4McastDatagramChannel.setOption(StandardSocketOptions.IP_MULTICAST_IF, nwkIf);
            MembershipKey mcastGroupMembershipKey = ipv4McastDatagramChannel.join(mcastGroup, nwkIf);

        }
        catch (SocketException ex) {
            Logger.getLogger(IpAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(IpAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void startIpv6Multicast(org.jinjuamla.network.adapters.ip.networkinterfaces.InterfaceAddress interfaceAddress) {
        String multicastAddrStringLink = "ff02::158";
        String multicastAddrStringRealm = "ff03::158";
        String multicastAddrStringSite = "ff05::158";

        try {
            NetworkInterface nwkIf = NetworkInterface.getByName(interfaceAddress.getName());

            ipv6McastDatagramChannel.setOption(StandardSocketOptions.IP_MULTICAST_IF, nwkIf);
            ipv6McastDatagramChannel.join(InetAddress.getByName(multicastAddrStringLink), nwkIf);
            ipv6McastDatagramChannel.join(InetAddress.getByName(multicastAddrStringRealm), nwkIf);
            ipv6McastDatagramChannel.join(InetAddress.getByName(multicastAddrStringSite), nwkIf);

        }
        catch (SocketException ex) {
            Logger.getLogger(IpAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(IpAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void stopReceiverThread() {
        try {
            receiverTask.interrupt();
            receiverTask.join();
        }
        catch (InterruptedException ex) {
            Logger.getLogger(IpAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void stopSenderThread() {
        try {
            senderTask.interrupt();
            senderTask.join();
        }
        catch (InterruptedException ex) {
            Logger.getLogger(IpAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    ErrCode createSockets() {
        ErrCode errCode = ErrCode.OK;
        IpAdapterConfig ipAdapterConfig = IpAdapterConfigMgr.Inst().getIpAdapterConfig();

        try {
            if (errCode == ErrCode.OK && ipAdapterConfig.isIsIpv4UcastEnabled()) {
                this.ipv4UcastDatagramChannel = DatagramChannel.open(StandardProtocolFamily.INET);

                if (this.ipv4UcastDatagramChannel != null) {
                    this.ipv4UcastDatagramChannel.configureBlocking(false);
                }
                else {
                    errCode = ErrCode.ERR;
                }
            }
            if (errCode == ErrCode.OK && ipAdapterConfig.isIsIpv4McastEnabled()) {
                this.ipv4McastDatagramChannel = DatagramChannel.open(StandardProtocolFamily.INET);
                if (this.ipv4McastDatagramChannel != null) {
                    this.ipv4McastDatagramChannel.configureBlocking(false);
                }
                else {
                    errCode = ErrCode.ERR;
                }
            }
            if (errCode == ErrCode.OK && ipAdapterConfig.isIsIpv6UcastEnabled()) {
                this.ipv6UcastDatagramChannel = DatagramChannel.open(StandardProtocolFamily.INET6);
                if (this.ipv6UcastDatagramChannel != null) {
                    this.ipv6UcastDatagramChannel.configureBlocking(false);
                }
                else {
                    errCode = ErrCode.ERR;
                }
            }
            if (errCode == ErrCode.OK && ipAdapterConfig.isIsIpv6McastEnabled()) {
                this.ipv6McastDatagramChannel = DatagramChannel.open(StandardProtocolFamily.INET6);
                if (this.ipv6McastDatagramChannel != null) {
                    this.ipv6McastDatagramChannel.configureBlocking(false);
                }
                else {
                    errCode = ErrCode.ERR;
                }
            }
        }
        catch (IOException ex) {
            Logger.getLogger(IpAdapter.class.getName()).log(Level.SEVERE, null, ex);
            errCode = ErrCode.ERR;
        }

        return errCode;
    }

    class IpAdapterSendMsg {

        private IpAdapterSendMsg(Endpoint end_point, byte[] data, int data_length, boolean isMulticast) {
            this.endpoint = end_point;
            this.dataBuf = data;
            this.dataBufLen = data_length;
            this.isMulticast = isMulticast;
        }

        public Endpoint getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(Endpoint endpoint) {
            this.endpoint = endpoint;
        }

        public byte[] getDataBuf() {
            return dataBuf;
        }

        public void setDataBuf(byte[] dataBuf) {
            this.dataBuf = dataBuf;
        }

        public int getDataBufLen() {
            return dataBufLen;
        }

        public void setDataBufLen(int dataBufLen) {
            this.dataBufLen = dataBufLen;
        }

        public boolean isIsMulticast() {
            return isMulticast;
        }

        public void setIsMulticast(boolean isMulticast) {
            this.isMulticast = isMulticast;
        }

        Endpoint endpoint = null;
        byte[] dataBuf = null;
        int dataBufLen = 0;
        boolean isMulticast = false;
    }

    class ReceiverThread extends Thread {

        int RECEIVE_BUFFER_SIZE = 1024;

        
        Selector selector = null;
        SelectionKey ipv4UcastSelectionKey = null;
        SelectionKey ipv4McastSelectionKey = null;
        SelectionKey ipv6UcastSelectionKey = null;
        SelectionKey ipv6McastSelectionKey = null;

        IpAdapter ipAdapter = null;

        public ReceiverThread(IpAdapter ipAdapter) {
            this.ipAdapter = ipAdapter;
        }

        /*
            ipv4 + port 		= IPV4 + UNICAST
            ipv4 + 5683 		= IPV4 + MULTICAST
            224.0.1.187 + 5683	= IPV4 + MULTICAST
            224.0.1.187 + port	= none

            ipv6 + port 		= IPV6 + UNICAST
            ipv6 + 5683 		= IPV6 + MULTICAST
            FF02::158 + 5683	= IPV6 + MULTICAST
            FF03::158 + 5683	= IPV6 + MULTICAST
            FF05::158 + 5683	= IPV6 + MULTICAST
            FF02::158 + port	= none	
            FF03::158 + port	= none
            FF05::158 + port	= none
         */
        private void readData(SelectionKey key) {
            DatagramChannel datagramChannel = (DatagramChannel) key.channel();
            UserData userData = (UserData) key.attachment();

            try {
                ByteBuffer receiveBuffer = ByteBuffer.allocate(RECEIVE_BUFFER_SIZE);
                InetSocketAddress socketAddress = (InetSocketAddress) datagramChannel.receive(receiveBuffer);

                receiveBuffer.flip();
                int receivedBytesLen = receiveBuffer.remaining();

                byte[] dataBuf = new byte[receivedBytesLen];

                receiveBuffer.get(dataBuf);

                InetAddress outboundAddress = getOutboundAddress(socketAddress);
                NetworkInterface outboundNetworkInterface = NetworkInterface.getByInetAddress(outboundAddress);

                int networkFlag = userData.getNetworkFlag(socketAddress);
                int adapterType = AdapterType.IP;
                int port = socketAddress.getPort();
                int index = outboundNetworkInterface != null ? outboundNetworkInterface.getIndex() : 0;
                IpAddr ipAddr = getIpAddr(socketAddress);

                Endpoint endpoint = new Endpoint((short) adapterType, (short) networkFlag, port, index, ipAddr);

                if (ipAdapter.adapterEventHandler != null) {
                    AdapterEvent adapterEvent = new AdapterEvent(AdapterEvent.TYPE_PACKET_RECEIVED, endpoint, dataBuf, receivedBytesLen);
                    ipAdapter.adapterEventHandler.handleAdapterEvent(adapterEvent);
                }

            }
            catch (IOException ex) {
                Logger.getLogger(IpAdapter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        IpAddr getIpAddr(InetSocketAddress socketAddress) {
            if (socketAddress.getAddress() instanceof Inet4Address) {
                Inet4Address ipv4Address = (Inet4Address) socketAddress.getAddress();

                byte[] address = ipv4Address.getAddress();
                byte[] addressCopy = Arrays.copyOf(address, address.length);

                IpAddr ipAddr = new IpAddr(IpAddr.IPV4);

                ipAddr.setAddr(addressCopy);

                return ipAddr;
            }
            else
                if (socketAddress.getAddress() instanceof Inet6Address) {
                    Inet6Address ipv6Address = (Inet6Address) socketAddress.getAddress();

                    byte[] address = ipv6Address.getAddress();
                    byte[] addressCopy = Arrays.copyOf(address, address.length);

                    IpAddr ipAddr = new IpAddr(IpAddr.IPV6);

                    ipAddr.setAddr(addressCopy);

                    return ipAddr;
                }
            return null;
        }

        void setupChannels() {
            try {
                selector = Selector.open();

                if (ipAdapter.ipv4UcastDatagramChannel != null) {
                    ipAdapter.ipv4UcastDatagramChannel.register(selector, SelectionKey.OP_READ, new UserData(EIpAddrFamily.IPV4, EAddressingMethod.UNICAST));
                }

                if (ipAdapter.ipv4McastDatagramChannel != null) {
                    ipAdapter.ipv4McastDatagramChannel.register(selector, SelectionKey.OP_READ, new UserData(EIpAddrFamily.IPV4, EAddressingMethod.MULTICAST));
                }

                if (ipAdapter.ipv6UcastDatagramChannel != null) {
                    ipAdapter.ipv6UcastDatagramChannel.register(selector, SelectionKey.OP_READ, new UserData(EIpAddrFamily.IPV6, EAddressingMethod.UNICAST));
                }

                if (ipAdapter.ipv6McastDatagramChannel != null) {
                    ipAdapter.ipv6McastDatagramChannel.register(selector, SelectionKey.OP_READ, new UserData(EIpAddrFamily.IPV6, EAddressingMethod.MULTICAST));
                }
            }
            catch (IOException ex) {
                Logger.getLogger(IpAdapter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void run() {
            setupChannels();

            while (true) {
                if (interrupted()) {
                    break;
                }

                try {
                    selector.select();

                    Iterator selectedKeys = selector.selectedKeys().iterator();

                    while (selectedKeys.hasNext()) {
                        SelectionKey key = (SelectionKey) selectedKeys.next();
                        selectedKeys.remove();

                        if (!key.isValid()) {
                            continue;
                        }

                        if (key.isReadable()) {
                            readData(key);
                        }
                    }

                }
                catch (IOException ex) {
                    Logger.getLogger(IpAdapter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        private InetAddress getOutboundAddress(SocketAddress remoteAddress) throws SocketException {
            final InetAddress localAddress;
            // connect is needed to bind the socket and retrieve the local address
            // later (it would return 0.0.0.0 otherwise)
            try ( // https://stackoverflow.com/questions/22045165/java-datagrampacket-receive-how-to-determine-local-ip-interface
                    DatagramSocket sock = new DatagramSocket()) {
                // connect is needed to bind the socket and retrieve the local address
                // later (it would return 0.0.0.0 otherwise)
                sock.connect(remoteAddress);
                localAddress = sock.getLocalAddress();
                sock.disconnect();
            }

            return localAddress;
        }

        class UserData {

            public UserData(EIpAddrFamily ipAddrFamily, EAddressingMethod addressingMethod) {
                this.ipAddrFamily = ipAddrFamily;
                this.addressingMethod = addressingMethod;
            }

            public EIpAddrFamily getIpAddrFamily() {
                return ipAddrFamily;
            }

            public void setIpAddrFamily(EIpAddrFamily ipAddrFamily) {
                this.ipAddrFamily = ipAddrFamily;
            }

            public EAddressingMethod getAddressingMethod() {
                return addressingMethod;
            }

            public void setAddressingMethod(EAddressingMethod addressingMethod) {
                this.addressingMethod = addressingMethod;
            }

            public ByteBuffer getDataBuffer() {
                return dataBuffer;
            }

            public int getNetworkFlag(InetSocketAddress socketAddr) {
                if (ipAddrFamily == EIpAddrFamily.IPV4) {
                    if (addressingMethod == EAddressingMethod.UNICAST) {
                        return (NetworkFlag.IPV4);
                    }

                    return (NetworkFlag.IPV4 | NetworkFlag.MULTICAST);
                }

                if (ipAddrFamily == EIpAddrFamily.IPV6) {
                    if (addressingMethod == EAddressingMethod.UNICAST) {
                        return (NetworkFlag.IPV6);
                    }

                    InetAddress inetAddress = socketAddr.getAddress();

                    if (inetAddress instanceof Inet6Address) {
                        int networkFlag = NetworkFlag.IPV6 | NetworkFlag.MULTICAST;

                        Inet6Address inet6Address = (Inet6Address) inetAddress;

                        if (inet6Address.isMCLinkLocal()) {
                            networkFlag |= NetworkFlag.SCOPE_LINK_LOCAL;
                        }

                        if (inet6Address.isMCSiteLocal()) {
                            networkFlag |= NetworkFlag.SCOPE_SITE_LOCAL;
                        }

                        return networkFlag;
                    }
                }

                return NetworkFlag.NONE;
            }

            EIpAddrFamily ipAddrFamily;
            EAddressingMethod addressingMethod;
            ByteBuffer dataBuffer = ByteBuffer.allocate(RECEIVE_BUFFER_SIZE);
        }
    }

    class SenderThread extends Thread {

        IpAdapter ipAdapter = null;
        BlockingQueue<IpAdapterSendMsg> blockingQueue;

        public SenderThread(BlockingQueue<IpAdapterSendMsg> blockingQueue, IpAdapter ipAdapter) {
            this.blockingQueue = blockingQueue;
            this.ipAdapter = ipAdapter;
        }

        @Override
        public void run() {
            while (true) {
                if (interrupted()) {
                    break;
                }

                try {
                    IpAdapterSendMsg newMessage = blockingQueue.take();
                    sendMessage(newMessage);
                }
                catch (InterruptedException ex) {
                    Logger.getLogger(IpAdapter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        private void sendMessage(IpAdapterSendMsg newMessage) {
            if (newMessage == null) {
                return;
            }

            Endpoint endpoint = newMessage.getEndpoint();
            byte[] dataBuf = newMessage.getDataBuf();
            boolean isIpv4Transfer = (endpoint.getNetworkFlag() & NetworkFlag.IPV4) != 0;
            boolean isIpv6Transfer = (endpoint.getNetworkFlag() & NetworkFlag.IPV6) != 0;

            System.out.println("Sending message");

            if (newMessage.isIsMulticast()) {
                endpoint.setPort((short) 5685);

                DatagramChannel ipv4Channel = (isIpv4Transfer) ? ipAdapter.ipv4UcastDatagramChannel : null;
                DatagramChannel ipv6Channel = (isIpv6Transfer) ? ipAdapter.ipv6UcastDatagramChannel : null;

                LinkedList<org.jinjuamla.network.adapters.ip.networkinterfaces.InterfaceAddress> interfaceAddrList = new LinkedList<>();

                interfaceMonitor.getInterfacesList(interfaceAddrList);

                for (org.jinjuamla.network.adapters.ip.networkinterfaces.InterfaceAddress ifAddr : interfaceAddrList) {
                    if (ipv4Channel != null && ifAddr.isIPV4()) {
                        try {
                            NetworkInterface nwkInterface = NetworkInterface.getByIndex(ifAddr.getIndex());
                            ipv4Channel.setOption(StandardSocketOptions.IP_MULTICAST_IF, nwkInterface);

                            ByteBuffer buf = ByteBuffer.allocate(dataBuf.length);
                            buf.clear();
                            buf.put(dataBuf);
                            buf.flip();

                            InetAddress address = Inet4Address.getByAddress(endpoint.getIpAddr().getAddr());
                            int bytesSent = ipv4Channel.send(buf, new InetSocketAddress(address, endpoint.getPort()));
                        }
                        catch (IOException ex) {
                            Logger.getLogger(IpAdapter.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    if (ipv6Channel != null && ifAddr.isIPV6()) {
                        try {
                            NetworkInterface nwkInterface = NetworkInterface.getByIndex(ifAddr.getIndex());
                            ipv4Channel.setOption(StandardSocketOptions.IP_MULTICAST_IF, nwkInterface);

                            ByteBuffer buf = ByteBuffer.allocate(dataBuf.length);
                            buf.clear();
                            buf.put(dataBuf);
                            buf.flip();

                            InetAddress address = Inet4Address.getByAddress(endpoint.getIpAddr().getAddr());
                            int bytesSent = ipv6Channel.send(buf, new InetSocketAddress(address, endpoint.getPort()));
                        }
                        catch (IOException ex) {
                            Logger.getLogger(IpAdapter.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
            else {
                if (endpoint.getPort() == 0) {
                    endpoint.setPort((short) 5685);
                }

                DatagramChannel channel = (isIpv4Transfer) ? ipAdapter.ipv4UcastDatagramChannel : ipAdapter.ipv6UcastDatagramChannel;

                ByteBuffer buf = ByteBuffer.allocate(dataBuf.length);
                buf.clear();
                buf.put(dataBuf);
                buf.flip();

                try {
                    InetAddress address = Inet4Address.getByAddress(endpoint.getIpAddr().getAddr());

                    if (address != null) {
                        InetSocketAddress inetSocketAddress = new InetSocketAddress(address, (int) endpoint.getPort());

                        int bytesSent = channel.send(buf, inetSocketAddress);
                    }
                }
                catch (UnknownHostException ex) {
                    Logger.getLogger(IpAdapter.class.getName()).log(Level.SEVERE, null, ex);
                }
                catch (IOException ex) {
                    Logger.getLogger(IpAdapter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

}
