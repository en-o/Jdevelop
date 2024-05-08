package cn.jdevelops.util.http;

import junit.framework.TestCase;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class IpUtilTest extends TestCase {

    public void testGetRealIp() throws SocketException, UnknownHostException {
        System.out.println(IpUtil.getRealIp());
        System.out.println(InetAddress.getLocalHost().getHostAddress());
    }
}
